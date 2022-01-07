package com.technicaltest.paymentprocessor.resttemplatetest;

import com.technicaltest.paymentprocessor.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceForTest {

    @Value("${paymentURI}")
    private String paymentURI;

    @Autowired
    private RestTemplate restTemplate;

    public Payment validatePayment() {
        ResponseEntity resp = restTemplate.getForEntity(paymentURI, Payment.class);

        return resp.getStatusCode() == HttpStatus.OK ? (Payment) resp.getBody() : null;
    }

}
