package com.technicaltest.paymentprocessor.repository;

import com.technicaltest.paymentprocessor.entity.Payment;
import org.json.JSONException;
import org.json.JSONObject;

public interface PaymentService {

    public Payment savePayment(JSONObject paymentAsJson) throws JSONException;

    public Payment getPaymentById(String payment_id);

}
