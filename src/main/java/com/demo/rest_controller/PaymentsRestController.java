package com.demo.rest_controller;

import com.demo.model.*;
import com.demo.model.xml.PaymentsXmlContainer;
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
    private PaymentService service;

    public PaymentsRestController(PaymentService service) {
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


    @RequestMapping(value = "createmany/fromjson", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPaymentsJson(@RequestBody List<Payment> payments,
                                                @RequestParam(name = "responseType", required = false) String responseType){

        return service.createPayments(payments, responseType);
    }


    @RequestMapping(value = "createmany/fromxml", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createPaymentsXml(@RequestBody PaymentsXmlContainer container,
                                               @RequestParam(name = "responseType", required = false) String responseType){

        return service.createPayments(container.getPayments(), responseType);
    }
}
