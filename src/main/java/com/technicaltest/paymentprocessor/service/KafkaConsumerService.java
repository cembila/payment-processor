package com.technicaltest.paymentprocessor.service;

import com.technicaltest.paymentprocessor.entity.Account;
import com.technicaltest.paymentprocessor.entity.Error;
import com.technicaltest.paymentprocessor.entity.Payment;
import com.technicaltest.paymentprocessor.repository.AccountRepository;
import com.technicaltest.paymentprocessor.repository.AccountService;
import com.technicaltest.paymentprocessor.repository.PaymentRepository;
import com.technicaltest.paymentprocessor.repository.PaymentService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;


@Service
public class KafkaConsumerService implements PaymentService, AccountService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private CountDownLatch latch = new CountDownLatch(1);
    private String payload = null;

    @Value("${paymentURI}")
    private String paymentURI;

    @Value("${logURI}")
    private String logURI;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate restTemplate;


    @KafkaListener(topics = {"offline", "online"}, groupId = "consume_payments", containerFactory = "kafkaListenerContainerFactory")
    public void consumePayments(String payment) throws JSONException {

        logger.info("Received message: {}", payment);

        JSONObject paymentAsJson = new JSONObject(payment);
        paymentAsJson.remove("delay");

        String paymentType = String.valueOf(paymentAsJson.get("payment_type"));

        // Process payment messages
        if (Objects.equals(paymentType, "online")) {

            Payment paymentRequestAsObj = createPaymentAsObj(paymentAsJson);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Content-Type", "application/json");
            HttpEntity<Payment> entity = new HttpEntity<Payment>(paymentRequestAsObj, headers);

            // Validate the payment
            try {
                ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity(paymentURI, entity, Payment.class);
                System.out.println("Valid online payment");
                savePayment(paymentAsJson);
            }
            catch (HttpStatusCodeException ex) {
                System.out.println("Invalid online payment");
                logError(paymentAsJson, ex);
            }

        } else if (Objects.equals(paymentType, "offline")) {
            System.out.println("Offline payment");
            savePayment(paymentAsJson);
        }
    }


    @Override
    public Payment savePayment(JSONObject paymentAsJson) throws JSONException {
        Payment paymentToDB = createPaymentAsObj(paymentAsJson);

        int account_id = (int) paymentAsJson.get("account_id");
        Account account = getAccountById(account_id);
        Date date = new Date();
        Timestamp lastPaymentDate = new Timestamp(date.getTime());
        account.setLast_payment_date(lastPaymentDate);
        updateAccount(account);

        paymentToDB.setAccount_id(account);

        return paymentRepository.save(paymentToDB);
    }


    @Override
    public Payment getPaymentById(String payment_id){
        Optional<Payment> payment = paymentRepository.findById(payment_id);
        return payment.get();
    }


    public void logError(JSONObject paymentAsJson, HttpStatusCodeException ex) throws JSONException {

        // Create error object
        Error error = new Error();
        String paymentId = (String) paymentAsJson.get("payment_id");
        String errorType = ex.getStatusText();
        String errorDescription = "Error description: " + errorType;
        error.setPayment_id(paymentId);
        error.setError_type(errorType);
        error.setError_description(errorDescription);

        // Store error logs
        HttpHeaders headersError = new HttpHeaders();
        headersError.setContentType(MediaType.APPLICATION_JSON);
        headersError.set("Content-Type", "application/json");
        HttpEntity<Error> errorEntity = new HttpEntity<Error>(error, headersError);
        try {
            restTemplate.postForEntity(paymentURI, errorEntity, Error.class);
        } catch (HttpStatusCodeException exLog) {
            System.out.println(exLog.getStatusCode().toString());
        }

    }


    @Override
    public Account getAccountById(int account_id) {
        Optional<Account> account = null;
        try {
            account = accountRepository.findById(account_id);
        } catch (EntityNotFoundException e) {
        }

        return account.get();
    }


    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }


    public Payment createPaymentAsObj(JSONObject json) throws JSONException {
        Payment payment = new Payment();

        Account account = new Account();
        account.setAccount_id((Integer) json.get("account_id"));

        payment.setPayment_id((String) json.get("payment_id"));
        payment.setAccount_id(account);
        payment.setPayment_type((String) json.get("payment_type"));
        payment.setCredit_card((String) json.get("credit_card"));
        payment.setAmount((Integer) json.get("amount"));

        return payment;
    }
}
