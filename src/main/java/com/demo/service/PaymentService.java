package com.demo.service;

import com.demo.model.Account;
import com.demo.model.Payment;
import com.demo.repository.AccountRepository;
import com.demo.repository.PaymentRepository;
import com.demo.response_entity.ErrorResponse;
import com.demo.response_entity.PaymentContainerResponse;
import com.demo.response_entity.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentService {


    private AccountRepository accountRepository;
    private PaymentRepository paymentRepository;

    public PaymentService(AccountRepository accountRepository, PaymentRepository paymentRepository) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Transact payment from source account to destination account necessary sum
     * @param payment - persisted payment that need to be transacted
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void processPayment(Payment payment) {
        Account source = accountRepository.findById(payment.getSourceAccount()).orElse(null);
        Account destination = accountRepository.findById(payment.getDestinationAccount()).orElse(null);

        if (source == null){
            log.error("Payment source account with id {} not found. LogName \"{}\"", payment.getSourceAccount(), log.getName());
            throw new RuntimeException("Source account not found");
        }
        if (destination == null){
            log.error("Payment destination account with id {} not found. LogName \"{}\"", payment.getDestinationAccount(), log.getName());
            throw new RuntimeException("Destination account not found");
        }
        if (source.getBalance().compareTo(payment.getAmount()) == -1){
            log.error("Unable to transact payment with payment_id={}. Balance less then payment amount. LogName \"{}\"", payment.getPaymentId(), log.getName());
            throw new RuntimeException("Low account balance");
        }

        Double paymentAmount = payment.getAmount();
        source.setBalance(source.getBalance() - paymentAmount);
        destination.setBalance(destination.getBalance() + paymentAmount);

        accountRepository.save(source);
        accountRepository.save(destination);
    }

    /**
     * Persist payment in database, generate transaction result
     * @param payment - new request payment from controller
     * @return Return transaction result (id, status)
     */
    public ResponseEntity<?> createPayment(Payment payment){
        Payment persistedPayment = paymentRepository.save(payment);
        try {
            processPayment(persistedPayment);
            log.info("Transaction number {} success", persistedPayment.getPaymentId());
        } catch (RuntimeException e){
            log.error("Transaction number {} failed. RuntimeException was thrown during execution. LogName \"{}\"", persistedPayment.getPaymentId(), log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new PaymentResponse(persistedPayment.getPaymentId(), "ok"), HttpStatus.CREATED);
    }

    /**
     * Persist payments in database, generate transactions result
     * @param payments - new request payments list from controller
     * @return Return transaction result (id, status)
     */
    public ResponseEntity<?> createPayments(List<Payment> payments, String responseType){
        if (payments == null || payments.isEmpty()){
            log.error("Input payment is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input payment is null or empty"), HttpStatus.OK);
        }

        List<Payment> persistedPayments = paymentRepository.saveAll(payments);

        List<PaymentResponse> paymentResponses = new ArrayList<>();
        persistedPayments.forEach(payment -> {
            try {
                processPayment(payment);
                paymentResponses.add(new PaymentResponse(payment.getPaymentId(), "ok"));
                log.info("Transaction number {} success", payment.getPaymentId());
            } catch (RuntimeException e){
                System.out.println("111113153135");
                paymentResponses.add(new PaymentResponse(payment.getPaymentId(), e.getMessage()));
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
            headers.setContentType(MediaType.APPLICATION_JSON);;
        }

        return new ResponseEntity<>(paymentResponses, HttpStatus.OK);
    }

}
