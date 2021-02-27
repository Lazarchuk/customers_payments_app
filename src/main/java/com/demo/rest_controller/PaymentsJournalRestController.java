package com.demo.rest_controller;

import org.hibernate.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.Map;

@RestController
@RequestMapping("/rest/v1/journal")
public class PaymentsJournalRestController {

    @PersistenceContext
    private EntityManager entityManager;


    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filterPayments(@RequestBody Map<String, Integer> request){
        System.out.println(request.get("payer_id"));
        System.out.println(request.get("recipient_id"));
        System.out.println(request.get("source_acc_id"));
        System.out.println(request.get("dest_acc_id"));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
