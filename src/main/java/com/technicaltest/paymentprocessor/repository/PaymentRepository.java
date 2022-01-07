package com.technicaltest.paymentprocessor.repository;

import com.technicaltest.paymentprocessor.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

}
