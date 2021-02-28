package com.demo.rest_controller;

import com.demo.model.*;
import com.demo.model.xml.PaymentsXmlContainer;
import com.demo.repository.PaymentRepository;
import com.demo.response_entity.ErrorResponse;
import com.demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        return service.createPayment(payment);
    }


    @RequestMapping(value = "create/xml", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createPaymentXml(@RequestBody Payment payment){
        if (payment == null){
            log.error("Input payment is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input payment is null"), HttpStatus.BAD_REQUEST);
        }

        return service.createPayment(payment);
    }


    @RequestMapping(value = "createmany/json", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPaymentsJson(@RequestBody List<Payment> payments,
                                                @RequestParam(name = "responseType", required = false) String responseType){

        if (payments == null || payments.isEmpty()){
            log.error("Input payment is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input payment is null or empty"), HttpStatus.BAD_REQUEST);
        }

        List<Payment> persistedPayments = paymentRepository.saveAll(payments);

        return service.createPayments(persistedPayments, responseType);
    }


    @RequestMapping(value = "createmany/xml", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createPaymentsXml(@RequestBody PaymentsXmlContainer container,
                                               @RequestParam(name = "responseType", required = false) String responseType){

        if (container == null || container.getPayments().isEmpty()){
            log.error("Input payment is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input payment is null or empty"), HttpStatus.BAD_REQUEST);
        }

        List<Payment> persistedPayments = paymentRepository.saveAll(container.getPayments());

        return service.createPayments(persistedPayments, responseType);
    }
}
