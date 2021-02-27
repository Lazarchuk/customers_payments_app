package com.demo.repository;

import com.demo.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PaymentSystemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCustomer(Customer customer){
        entityManager.persist(customer);
        entityManager.merge(customer);
    }

    @Transactional
    public Customer findCustomerById(Integer id){
        return entityManager.find(Customer.class, id);
    }
}
