package com.demo.rest_controller;

import com.demo.model.Account;
import com.demo.model.Payment;
import com.demo.repository.AccountRepository;
import com.demo.repository.PaymentRepository;
import com.demo.response_entity.CreatedPaymentResponse;
import com.demo.response_entity.MultiplePaymentsResponse;
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
    private PaymentRepository repository;
    private AccountRepository accRepository;
    private PaymentService service;

    public PaymentsRestController(PaymentRepository repository, AccountRepository accRepository, PaymentService service) {
        this.repository = repository;
        this.accRepository = accRepository;
        this.service = service;
    }

    /**
    * @param requestPayment
    */

    @RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedPaymentResponse> createPayment(@RequestBody Payment requestPayment){
        if (requestPayment == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account = accRepository.findById(requestPayment.getSourceAccount()).orElse(null);
        if (account == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        requestPayment.setAccount(account);
        Payment persistedPayment = repository.save(requestPayment);
        CreatedPaymentResponse paymentResponse = new CreatedPaymentResponse();
        paymentResponse.setPaymentId(persistedPayment.getPaymentId());

        try {
            service.processPayment(persistedPayment);
            log.info("Transaction number {} success", persistedPayment.getPaymentId());
        } catch (RuntimeException e){
            log.error("Transaction number {} failed. RuntimeException was thrown during execution. LogName \"{}\"", persistedPayment.getPaymentId(), log.getName());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    /**
     *
     * @param requestPayments
     * @return
     */
    @RequestMapping(value = "createmany", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MultiplePaymentsResponse>> createPayments(@RequestBody List<Payment> requestPayments){
        if (requestPayments == null || requestPayments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<MultiplePaymentsResponse> paymentsResponses = new ArrayList<>();

        List<Payment> persistedPayments = repository.saveAll(requestPayments);

        for (Payment payment: persistedPayments) {
            try {
                service.processPayment(payment);
                paymentsResponses.add(new MultiplePaymentsResponse(payment.getPaymentId(), "ok"));
                log.info("Transaction number {} success", payment.getPaymentId());
            } catch (RuntimeException e){
                paymentsResponses.add(new MultiplePaymentsResponse(payment.getPaymentId(), "error"));
                log.error("Transaction number {} failed. RuntimeException was thrown during execution. LogName \"{}\"", payment.getPaymentId(), log.getName());
            }
        }

        return new ResponseEntity<>(paymentsResponses, HttpStatus.OK);
    }
}
