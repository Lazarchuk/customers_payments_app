package com.demo.repository;

import com.demo.model.Client;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PaymentSystemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCustomer(Client customer){
        entityManager.persist(customer);
        entityManager.merge(customer);
    }

    @Transactional
    public Client findCustomerById(Integer id){
        return entityManager.find(Client.class, id);
    }
}
