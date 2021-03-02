package com.demo.service;

import com.demo.model.Client;
import com.demo.repository.ClientRepository;
import com.demo.response_entity.AccountsContainerResponse;
import com.demo.response_entity.CreatedClientResponse;
import com.demo.response_entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientService {

    private ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    /**
     * @param id client identifier
     * @return all client account in database
     */
    public ResponseEntity<?> getClientAccountsJson(Integer id){
        Client client = repository.findById(id).orElse(null);
        if (client == null){
            log.error("Client with id {} not found. LogName \"{}\"", id, log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Client not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client.getAccounts(), HttpStatus.OK);
    }

    /**
     * @param id client identifier
     * @see AccountsContainerResponse - class-wrapper for list of XML-like accounts
     * @return all client account in database
     */
    public ResponseEntity<?> getClientAccountsXml(Integer id){
        Client client = repository.findById(id).orElse(null);
        if (client == null){
            log.error("Client with id {} not found. LogName \"{}\"", id, log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Client not found"), HttpStatus.NOT_FOUND);
        }

        AccountsContainerResponse response = new AccountsContainerResponse();
        response.setAccounts(client.getAccounts());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param client persist client in database
     * @see CreatedClientResponse - response class
     * @return Return persisting result
     */
    public ResponseEntity<?> createClient(Client client){
        client.getAccounts().forEach(acc -> acc.setClient(client));

        Client persistedClient = repository.save(client);
        return new ResponseEntity<>(new CreatedClientResponse(persistedClient.getClientId()), HttpStatus.CREATED);
    }

    /**
     * @param client persist client in database
     * @param responseType asked MediaType for response
     * @see CreatedClientResponse - response class
     * @return Return persisting result
     */
    public ResponseEntity<?> createClient(Client client, String responseType){
        client.getAccounts().forEach(acc -> acc.setClient(client));

        HttpHeaders headers = new HttpHeaders();
        if (responseType.equals("json")){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        if (responseType.equals("xml")){
            headers.setContentType(MediaType.APPLICATION_XML);
        }

        Client persistedClient = repository.save(client);
        return new ResponseEntity<>(new CreatedClientResponse(persistedClient.getClientId()), headers, HttpStatus.CREATED);
    }
}
