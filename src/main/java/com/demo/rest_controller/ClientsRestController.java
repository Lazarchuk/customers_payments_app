package com.demo.rest_controller;

import com.demo.model.Client;
import com.demo.repository.ClientRepository;
import com.demo.response_entity.CreatedClientResponse;
import com.demo.response_entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rest/v1/clients")
public class ClientsRestController {
    private ClientRepository repository;

    public ClientsRestController(ClientRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClientsAccounts(@PathVariable("id") Integer id){
        if (id == null){
            log.error("Input client identifier is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input client identifier is null"), HttpStatus.BAD_REQUEST);
        }

        Client client = repository.findById(id).orElse(null);
        if (client == null){
            log.error("Client with id {} not found. LogName \"{}\"", id, log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Client not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client.getAccounts(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveClient(@RequestBody Client client){
        if (client == null){
            log.error("Input client is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input client is null"), HttpStatus.BAD_REQUEST);
        }

        client.getAccounts().forEach(acc -> acc.setClient(client));

        Client persistedClient = repository.save(client);
        return new ResponseEntity<>(new CreatedClientResponse(persistedClient.getClientId()), HttpStatus.CREATED);
    }

}
