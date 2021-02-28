package com.demo.rest_controller;

import com.demo.model.Account;
import com.demo.model.xml.FilterPaymentsRequest;
import com.demo.model.Payment;
import com.demo.repository.AccountRepository;
import com.demo.repository.PaymentRepository;
import com.demo.response_entity.ErrorResponse;
import com.demo.response_entity.JournalContainerResponse;
import com.demo.response_entity.JournalResponse;
import com.demo.service.JournalService;
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
@RequestMapping("/rest/v1/journal")
public class PaymentsJournalRestController {
    private PaymentRepository paymentRepository;
    private JournalService service;

    public PaymentsJournalRestController(PaymentRepository paymentRepository, JournalService service) {
        this.paymentRepository = paymentRepository;
        this.service = service;
    }

    @RequestMapping(value = "json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> filterPaymentsJson(@RequestBody FilterPaymentsRequest request){
        List<Payment> payments = paymentRepository.findAllBySourceAccountAndDestinationAccount(request.getSourceAccount(), request.getDestinationAccount());
        if (payments.isEmpty()){
            log.error("Input request is empty. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input request is empty"), HttpStatus.BAD_REQUEST);
        }

        return service.filterPaymentsJson(payments, request);
    }

    @RequestMapping(value = "xml", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> filterPaymentsXml(@RequestBody FilterPaymentsRequest request){
        List<Payment> payments = paymentRepository.findAllBySourceAccountAndDestinationAccount(request.getSourceAccount(), request.getDestinationAccount());
        if (payments.isEmpty()){
            log.error("Input request is empty. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input request is empty"), HttpStatus.BAD_REQUEST);
        }

        return service.filterPaymentsxml(payments, request);
    }
}
