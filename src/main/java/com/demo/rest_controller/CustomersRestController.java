package com.demo.rest_controller;

import com.demo.model.Account;
import com.demo.model.Customer;
import com.demo.repository.AccountRepository;
import com.demo.repository.CustomerRepository;
import com.demo.repository.PaymentSystemRepository;
import com.demo.response_entity.CreatedCustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping("/rest/v1/customers")
public class CustomersRestController {
    private CustomerRepository repository;
    private AccountRepository accountRepository;
    //private PaymentSystemRepository repository;

    public CustomersRestController(CustomerRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getCustomerAccount(@PathVariable("id") Integer id){
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Customer customer = repository.findById(id).orElse(null);
        if (customer == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customer.getAccounts(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedCustomerResponse> saveCustomer(@RequestBody Customer customer){
        if (customer == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        customer.getAccounts().forEach(acc -> acc.setCustomer(customer));

        Customer persistedCustomer = repository.save(customer);

        CreatedCustomerResponse customerResponse = new CreatedCustomerResponse();
        customerResponse.setClientId(persistedCustomer.getClientId());
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

}
