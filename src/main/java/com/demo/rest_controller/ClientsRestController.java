package com.demo.rest_controller;

import com.demo.model.Account;
import com.demo.model.Client;
import com.demo.repository.ClientRepository;
import com.demo.response_entity.CreatedClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/v1/clients")
public class ClientsRestController {
    private ClientRepository repository;

    public ClientsRestController(ClientRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getClientsAccounts(@PathVariable("id") Integer id){
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Client client = repository.findById(id).orElse(null);
        if (client == null){
            log.error("Client with id {} not found. LogName \"{}\"", id, log.getName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client.getAccounts(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedClientResponse> saveClient(@RequestBody Client client){
        if (client == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        client.getAccounts().forEach(acc -> acc.setClient(client));

        Client persistedClient = repository.save(client);

        CreatedClientResponse clientResponse = new CreatedClientResponse();
        clientResponse.setClientId(persistedClient.getClientId());
        return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
    }

}
