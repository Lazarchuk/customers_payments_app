package com.demo.rest_controller;

import com.demo.model.*;
import com.demo.model.xml.PaymentsXmlContainer;
import com.demo.repository.PaymentRepository;
import com.demo.response_entity.ErrorResponse;
import com.demo.response_entity.PaymentContainerResponse;
import com.demo.response_entity.PaymentResponse;
import com.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value = "create/json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createPaymentJson(@RequestBody Payment payment){
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


    @RequestMapping(value = "create/xml", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createPaymentXml(@RequestBody Payment payment){
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


    @RequestMapping(value = "createmany/json", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPaymentsJson(@RequestBody List<Payment> payments,
                                                @RequestParam(name = "responseType", required = false) String responseType){
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

        HttpHeaders headers = new HttpHeaders();
        if (responseType != null && responseType.equals("xml")){
            PaymentContainerResponse xmlResponse = new PaymentContainerResponse();
            xmlResponse.setPayments(paymentResponses);
            headers.setContentType(MediaType.APPLICATION_XML);
            return new ResponseEntity<>(xmlResponse, headers, HttpStatus.OK);
        }
        if(responseType != null && responseType.equals("json")){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        return new ResponseEntity<>(paymentResponses, HttpStatus.OK);
    }


    @RequestMapping(value = "createmany/xml", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createPaymentsXml(@RequestBody PaymentsXmlContainer container,
                                               @RequestParam(name = "responseType", required = false) String responseType){

        List<PaymentResponse> paymentResponses = new ArrayList<>();
        if (container == null || container.getPayments().isEmpty()){
            log.error("Input payment is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input payment is null or empty"), HttpStatus.BAD_REQUEST);
        }

        List<Payment> persistedPayments = paymentRepository.saveAll(container.getPayments());

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

        HttpHeaders headers = new HttpHeaders();
        if (responseType != null && responseType.equals("xml")){
            PaymentContainerResponse xmlResponse = new PaymentContainerResponse();
            xmlResponse.setPayments(paymentResponses);
            headers.setContentType(MediaType.APPLICATION_XML);
            return new ResponseEntity<>(xmlResponse, headers, HttpStatus.OK);
        }
        if(responseType != null && responseType.equals("json")){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        return new ResponseEntity<>(paymentResponses, HttpStatus.OK);
    }
}
