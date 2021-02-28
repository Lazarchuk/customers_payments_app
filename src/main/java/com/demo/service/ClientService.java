package com.demo.service;

import com.demo.model.Client;
import com.demo.repository.ClientRepository;
import com.demo.response_entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientService {

    private ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> getClientAccounts(Integer id){
        Client client = repository.findById(id).orElse(null);
        if (client == null){
            log.error("Client with id {} not found. LogName \"{}\"", id, log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Client not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client.getAccounts(), HttpStatus.OK);
    }
}
