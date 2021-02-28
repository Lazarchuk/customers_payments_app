package com.demo.rest_controller;

import com.demo.model.Account;
import com.demo.model.Payment;
import com.demo.repository.AccountRepository;
import com.demo.repository.PaymentRepository;
import com.demo.response_entity.ErrorResponse;
import com.demo.response_entity.PaymentsJournalResponse;
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
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rest/v1/journal")
public class PaymentsJournalRestController {
    private PaymentRepository paymentRepository;
    private AccountRepository accountRepository;

    public PaymentsJournalRestController(PaymentRepository paymentRepository, AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filterPayments(@RequestBody Map<String, Integer> request){
        List<Payment> payments = paymentRepository.findAllBySourceAccountAndDestinationAccount(request.get("source_acc_id"), request.get("dest_acc_id"));
        if (payments.isEmpty()){
            log.error("Input request is empty. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input request is empty"), HttpStatus.BAD_REQUEST);
        }

        Account sourceAccount = accountRepository.findById(request.get("source_acc_id")).orElse(null);
        Account destinationAccount = accountRepository.findById(request.get("dest_acc_id")).orElse(null);

        if (sourceAccount == null){
            log.error("Source account with id \"{}\" not found. LogName \"{}\"", request.get("source_acc_id"), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Source account not found"), HttpStatus.NOT_FOUND);
        }
        if (destinationAccount == null){
            log.error("Destination account with id \"{}\" not found. LogName \"{}\"", request.get("dest_acc_id"), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Destination account not found"), HttpStatus.NOT_FOUND);
        }

        if(!sourceAccount.getClient().getClientId().equals(request.get("payer_id"))){
            log.error("Payer with id \"{}\" don't have the account with id \"{}\". LogName \"{}\"", request.get("payer_id"), request.get("source_acc_id"), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Payer identifier and source account don't matches"), HttpStatus.NOT_FOUND);
        }
        if(!destinationAccount.getClient().getClientId().equals(request.get("recipient_id"))){
            log.error("Recipient with id \"{}\" don't have the account with id \"{}\". LogName \"{}\"", request.get("recipient_id"), request.get("dest_acc_id"), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Recipient identifier and destination account don't matches"), HttpStatus.NOT_FOUND);
        }

        List<PaymentsJournalResponse> response = new ArrayList<>();
        payments.forEach(payment -> {
            PaymentsJournalResponse journalResponse = new PaymentsJournalResponse();
            journalResponse.setPaymentId(payment.getPaymentId());
            journalResponse.setSourceAccount(sourceAccount.getAccountNumber());
            journalResponse.setDestinationAccount(destinationAccount.getAccountNumber());
            journalResponse.setAmount(payment.getAmount());
            journalResponse.setPayer(sourceAccount.getClient());
            journalResponse.setRecipient(destinationAccount.getClient());
            response.add(journalResponse);
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
