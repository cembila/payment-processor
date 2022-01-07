package com.technicaltest.paymentprocessor.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {

    @Id
    @Column(name = "payment_id", length = 100)
    private String payment_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account_id;

    //@Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = 150)
    public String payment_type;

    @Column(name = "credit_card", length = 100)
    private String credit_card;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "created_on")
    @CreationTimestamp
    private Timestamp created_on;

}
