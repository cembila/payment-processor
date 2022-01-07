package com.technicaltest.paymentprocessor.jpatest;

import com.technicaltest.paymentprocessor.entity.Account;
import com.technicaltest.paymentprocessor.entity.Payment;
import com.technicaltest.paymentprocessor.repository.AccountRepository;
import com.technicaltest.paymentprocessor.repository.PaymentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JPARepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void testIfPaymentRepositoryIsEmpty() {
        Iterable<Payment> payments = paymentRepository.findAll();

        assertThat(payments).isEmpty();
    }

    @Test
    public void testSavePayment()
    {
        Payment payment = new Payment();
        payment.setPayment_id("7701637b-a127-4e18-acbc-fd257c5acc6e");
        Account account = new Account();
        account.setAccount_id(1);
        payment.setAccount_id(account);
        payment.setPayment_type("online");
        payment.setCredit_card("3541509543881320");
        payment.setAmount(43);

        Payment paymentSaved = paymentRepository.save(payment);

        assertNotNull(paymentSaved.getPayment_id());
        assertThat(paymentSaved).hasFieldOrPropertyWithValue("payment_id", "7701637b-a127-4e18-acbc-fd257c5acc6e");
        assertThat(paymentSaved).hasFieldOrPropertyWithValue("account", account);
        assertThat(paymentSaved).hasFieldOrPropertyWithValue("payment_type", "online");
        assertThat(paymentSaved).hasFieldOrPropertyWithValue("credit_card", "3541509543881320");
        assertThat(paymentSaved).hasFieldOrPropertyWithValue("amount", 43);
    }

    @Test
    public void testFindPaymentById(){
        Payment payment1 = new Payment();
        payment1.setPayment_id("8da743c7-0f88-49ed-9e0a-f1c70ed8a52d");
        Account account1 = new Account();
        account1.setAccount_id(150);
        payment1.setAccount_id(account1);
        payment1.setPayment_type("online");
        payment1.setCredit_card("6540616765574519");
        payment1.setAmount(71);
        Payment paymentSaved1 = paymentRepository.save(payment1);

        Payment payment2 = new Payment();
        payment2.setPayment_id("9ffc62f4-94a6-4809-9192-071419321933");
        Account account2 = new Account();
        account2.setAccount_id(644);
        payment2.setAccount_id(account2);
        payment2.setPayment_type("online");
        payment2.setCredit_card("4711041418546625636");
        payment2.setAmount(39);
        Payment paymentSaved2 = paymentRepository.save(payment2);

        Payment payment3 = new Payment();
        payment3.setPayment_id("cd03a516-2869-4e84-878a-9a194301cd66");
        Account account3 = new Account();
        account3.setAccount_id(1190);
        payment3.setAccount_id(account3);
        payment3.setPayment_type("online");
        payment3.setCredit_card("2280185404821254");
        payment3.setAmount(58);
        Payment paymentSaved3 = paymentRepository.save(payment3);

        Payment paymentFromDB1 = paymentRepository.findById(payment1.getPayment_id()).get();
        assertThat(paymentFromDB1).isEqualTo(payment1);
    }

    @Test
    public void testFindAllPayments(){
        Payment payment1 = new Payment();
        payment1.setPayment_id("14f654f3-0653-4589-bbd8-dc80c2ffc218");
        Account account1 = new Account();
        account1.setAccount_id(1667);
        payment1.setAccount_id(account1);
        payment1.setPayment_type("online");
        payment1.setCredit_card("503887285545");
        payment1.setAmount(54);

        Payment payment2 = new Payment();
        payment2.setPayment_id("9b8b8107-e85e-4aee-ade6-8cc51568ddb5");
        Account account2 = new Account();
        account2.setAccount_id(672);
        payment2.setAccount_id(account2);
        payment2.setPayment_type("online");
        payment2.setCredit_card("3533774765256797");
        payment2.setAmount(9);

        Payment payment3 = new Payment();
        payment3.setPayment_id("d8b7c2dd-06a0-4ce0-a531-d413288f27e5");
        Account account3 = new Account();
        account3.setAccount_id(1594);
        payment3.setAccount_id(account3);
        payment3.setPayment_type("online");
        payment3.setCredit_card("6553762131924782");
        payment3.setAmount(10);

        Iterable<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(3).contains(payment1, payment2, payment3);
    }

    @Test
    public void testSaveAccount() throws ParseException {
        Account account = new Account();
        account.setAccount_id(10);
        account.setName("Name Surname");
        account.setEmail("namesurname@gmail.com");
        String dateString = "2000-1-1";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
        Date birthdate = formatter.parse(dateString);
        account.setBirthdate(birthdate);
        String dateString2 = "2021-5-5";
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-dd-MM");
        Date lastPaymentDate = formatter2.parse(dateString2);
        account.setLast_payment_date((Timestamp) lastPaymentDate);

        Account accountSaved = accountRepository.save(account);

        assertNotNull(accountSaved.getAccount_id());
        assertThat(accountSaved).hasFieldOrPropertyWithValue("account_id", 1);
        assertThat(accountSaved).hasFieldOrPropertyWithValue("name", "Name Surname");
        assertThat(accountSaved).hasFieldOrPropertyWithValue("email", "namesurname@gmail.com");
        assertThat(accountSaved).hasFieldOrPropertyWithValue("birthdate", birthdate);
        assertThat(accountSaved).hasFieldOrPropertyWithValue("last_payment_date", lastPaymentDate);
    }



}
