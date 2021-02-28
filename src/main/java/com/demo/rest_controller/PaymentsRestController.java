package com.demo.rest_controller;

import com.demo.model.Payment;
import com.demo.repository.PaymentRepository;
import com.demo.response_entity.ErrorResponse;
import com.demo.response_entity.PaymentResponse;
import com.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/v1/payments")
public class PaymentsRestController {
    private PaymentRepository paymentRepository;
    private PaymentService service;

    public PaymentsRestController(PaymentRepository paymentRepository, PaymentService service) {
        this.paymentRepository = paymentRepository;
        this.service = service;
    }

    /**
    * @param payment
    */

    @RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPayment(@RequestBody Payment payment){
        if (payment == null){
            log.error("Input payment is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input payment is null"), HttpStatus.BAD_REQUEST);
        }

        Payment persistedPayment = paymentRepository.save(payment);
        try {
            service.processPayment(persistedPayment);
            log.info("Transaction number {} success", persistedPayment.getPaymentId());
        } catch (RuntimeException e){
            log.error("Transaction number {} failed. RuntimeException was thrown during execution. LogName \"{}\"", persistedPayment.getPaymentId(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Payment transaction processing error"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new PaymentResponse(persistedPayment.getPaymentId(), "ok"), HttpStatus.CREATED);
    }

    /**
     *
     * @param payments
     * @return
     */
    @RequestMapping(value = "createmany", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPayments(@RequestBody List<Payment> payments){
        List<PaymentResponse> paymentResponses = new ArrayList<>();
        if (payments == null || payments.isEmpty()){
            log.error("Input payment is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input payment is null or empty"), HttpStatus.BAD_REQUEST);
        }

        List<Payment> persistedPayments = paymentRepository.saveAll(payments);

        persistedPayments.forEach(payment -> {
            try {
                service.processPayment(payment);
                paymentResponses.add(new PaymentResponse(payment.getPaymentId(), "ok"));
                log.info("Transaction number {} success", payment.getPaymentId());
            } catch (RuntimeException e){
                paymentResponses.add(new PaymentResponse(payment.getPaymentId(), "error"));
                log.error("Transaction number {} failed. RuntimeException was thrown during execution. LogName \"{}\"", payment.getPaymentId(), log.getName());
            }
        });

        return new ResponseEntity<>(paymentResponses, HttpStatus.OK);
    }
}
