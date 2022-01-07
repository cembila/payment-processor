package com.technicaltest.paymentprocessor.resttemplatetest;

import com.technicaltest.paymentprocessor.entity.Account;
import com.technicaltest.paymentprocessor.entity.Payment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentServiceForTest paymentServiceForTest = new PaymentServiceForTest();

    @Value("${paymentURI}")
    private String paymentURI;

    @Test
    public void testIfReturnsMockedObject() {
        Payment payment = new Payment();
        payment.setPayment_id("fdf50f69-a23a-4924-9276-9468a815443a");
        Account account = new Account();
        account.setAccount_id(1);
        payment.setAccount_id(account);
        payment.setPayment_type("online");
        payment.setCredit_card("12345");
        payment.setAmount(12);

        Mockito.when(restTemplate.getForEntity(paymentURI, Payment.class))
                .thenReturn(new ResponseEntity(payment, HttpStatus.OK));

        Payment payment2 = paymentServiceForTest.validatePayment();
        Assert.assertEquals(payment, payment2);
    }

}
