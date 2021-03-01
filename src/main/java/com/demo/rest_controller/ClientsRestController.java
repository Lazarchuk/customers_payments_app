package com.demo.rest_controller;

import com.demo.model.Client;
import com.demo.response_entity.ErrorResponse;
import com.demo.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rest/v1/clients")
public class ClientsRestController {
    private ClientService service;

    public ClientsRestController(ClientService service) {
        this.service = service;
    }

    @RequestMapping(value = "{id}/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClientsAccountsJson(@PathVariable("id") Integer id){
        if (id == null){
            log.error("Input client identifier is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input client identifier is null"), HttpStatus.BAD_REQUEST);
        }

        return service.getClientAccountsJson(id);
    }

    @RequestMapping(value = "{id}/xml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getClientsAccountsXml(@PathVariable("id") Integer id){
        if (id == null){
            log.error("Input client identifier is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input client identifier is null"), HttpStatus.BAD_REQUEST);
        }

        return service.getClientAccountsXml(id);
    }

    @RequestMapping(value = "create/json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> saveClientJson(@RequestBody Client client){

        if (client == null){
            log.error("Input client is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input client is null"), HttpStatus.BAD_REQUEST);
        }

        return service.createClient(client);
    }

    @RequestMapping(value = "create/xml", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> saveClientXml(@RequestBody Client client){
        if (client == null){
            log.error("Input client is null. LogName \"{}\"", log.getName());
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Input client is null"), HttpStatus.BAD_REQUEST);
        }

        return service.createClient(client);
    }

}
