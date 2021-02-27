package com.demo.service;

import com.demo.model.Account;
import com.demo.model.Payment;
import com.demo.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentService {

    private AccountRepository repository;

    public PaymentService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void processPayment(Payment payment) {
        Account source = repository.findById(payment.getSourceAccount()).orElse(null);
        Account destination = repository.findById(payment.getDestinationAccount()).orElse(null);

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

        BigDecimal paymentAmount = payment.getAmount();
        source.setBalance(source.getBalance().subtract(paymentAmount));
        destination.setBalance(destination.getBalance().add(paymentAmount));

        repository.save(source);
        repository.save(destination);
    }
}
