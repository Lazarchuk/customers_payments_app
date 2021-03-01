package com.demo.rest_controller;

import com.demo.model.xml.FilterPaymentsRequest;
import com.demo.service.JournalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/rest/v1/journal")
public class PaymentsJournalRestController {
    private JournalService service;

    public PaymentsJournalRestController(JournalService service) {
        this.service = service;
    }

    @RequestMapping(value = "json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
                                                        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> filterPaymentsJson(@RequestBody FilterPaymentsRequest request){

        return service.filterPaymentsJson(request);
    }

    @RequestMapping(value = "xml", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE,
                                                        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> filterPaymentsXml(@RequestBody FilterPaymentsRequest request){

        return service.filterPaymentsxml(request);
    }
}
