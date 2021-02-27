package com.demo.rest_controller;

import com.demo.model.Account;
import com.demo.model.Client;
import com.demo.repository.AccountRepository;
import com.demo.repository.ClientRepository;
import com.demo.response_entity.CreatedClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/v1/customers")
public class ClientsRestController {
    private ClientRepository repository;
    private AccountRepository accountRepository;
    //private PaymentSystemRepository repository;

    public ClientsRestController(ClientRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getCustomerAccount(@PathVariable("id") Integer id){
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Client customer = repository.findById(id).orElse(null);
        if (customer == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customer.getAccounts(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedClientResponse> saveCustomer(@RequestBody Client customer){
        if (customer == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        customer.getAccounts().forEach(acc -> acc.setClient(customer));

        Client persistedCustomer = repository.save(customer);

        CreatedClientResponse customerResponse = new CreatedClientResponse();
        customerResponse.setClientId(persistedCustomer.getClientId());
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

}
