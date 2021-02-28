package com.demo.service;

import com.demo.model.Account;
import com.demo.model.Payment;
import com.demo.model.xml.FilterPaymentsRequest;
import com.demo.repository.AccountRepository;
import com.demo.response_entity.ErrorResponse;
import com.demo.response_entity.JournalContainerResponse;
import com.demo.response_entity.JournalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JournalService {

    private AccountRepository repository;

    public JournalService(AccountRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> filterPaymentsJson(List<Payment> payments, FilterPaymentsRequest request){
        Account sourceAccount = repository.findById(request.getSourceAccount()).orElse(null);
        Account destinationAccount = repository.findById(request.getDestinationAccount()).orElse(null);

        if (sourceAccount == null){
            log.error("Source account with id \"{}\" not found. LogName \"{}\"", request.getSourceAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Source account not found"), HttpStatus.NOT_FOUND);
        }
        if (destinationAccount == null){
            log.error("Destination account with id \"{}\" not found. LogName \"{}\"", request.getDestinationAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Destination account not found"), HttpStatus.NOT_FOUND);
        }

        if(!sourceAccount.getClient().getClientId().equals(request.getPayerId())){
            log.error("Payer with id \"{}\" don't have the account with id \"{}\". LogName \"{}\"", request.getPayerId(), request.getSourceAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Payer identifier and source account don't matches"), HttpStatus.NOT_FOUND);
        }
        if(!destinationAccount.getClient().getClientId().equals(request.getRecipientId())){
            log.error("Recipient with id \"{}\" don't have the account with id \"{}\". LogName \"{}\"", request.getRecipientId(), request.getDestinationAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Recipient identifier and destination account don't matches"), HttpStatus.NOT_FOUND);
        }

        List<JournalResponse> response = new ArrayList<>();
        payments.forEach(payment -> {
            JournalResponse journalResponse = new JournalResponse();
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


    public ResponseEntity<?> filterPaymentsxml(List<Payment> payments, FilterPaymentsRequest request){
        Account sourceAccount = repository.findById(request.getSourceAccount()).orElse(null);
        Account destinationAccount = repository.findById(request.getDestinationAccount()).orElse(null);

        if (sourceAccount == null){
            log.error("Source account with id \"{}\" not found. LogName \"{}\"", request.getSourceAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Source account not found"), HttpStatus.NOT_FOUND);
        }
        if (destinationAccount == null){
            log.error("Destination account with id \"{}\" not found. LogName \"{}\"", request.getDestinationAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Destination account not found"), HttpStatus.NOT_FOUND);
        }

        if(!sourceAccount.getClient().getClientId().equals(request.getPayerId())){
            log.error("Payer with id \"{}\" don't have the account with id \"{}\". LogName \"{}\"", request.getPayerId(), request.getSourceAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Payer identifier and source account don't matches"), HttpStatus.NOT_FOUND);
        }
        if(!destinationAccount.getClient().getClientId().equals(request.getRecipientId())){
            log.error("Recipient with id \"{}\" don't have the account with id \"{}\". LogName \"{}\"", request.getRecipientId(), request.getDestinationAccount(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Recipient identifier and destination account don't matches"), HttpStatus.NOT_FOUND);
        }

        List<JournalResponse> paymentsList = new ArrayList<>();
        JournalContainerResponse response = new JournalContainerResponse();

        payments.forEach(payment -> {
            JournalResponse journalResponse = new JournalResponse();
            journalResponse.setPaymentId(payment.getPaymentId());
            journalResponse.setSourceAccount(sourceAccount.getAccountNumber());
            journalResponse.setDestinationAccount(destinationAccount.getAccountNumber());
            journalResponse.setAmount(payment.getAmount());
            journalResponse.setPayer(sourceAccount.getClient());
            journalResponse.setRecipient(destinationAccount.getClient());
            paymentsList.add(journalResponse);
        });
        response.setPayments(paymentsList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
