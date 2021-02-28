package com.demo.controller;

import com.demo.model.Account;
import com.demo.model.Client;
import com.demo.rest_controller.ClientsRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/view/clients")
public class ClientController {
    private ClientsRestController clientsRestController;

    public ClientController(ClientsRestController clientsRestController) {
        this.clientsRestController = clientsRestController;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView initForm(){
        return new ModelAndView("startpage");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "customerName")
    @ResponseBody
    public ResponseEntity<List<Account>> createClient(@RequestParam("customerName") String name){
        System.out.println(name);
        Client client = new Client();
        client.setFirstName(name);
        client.setLastName("Yohanson");

        Account account = new Account();
        account.setAccountNumber("654984263");
        account.setAccountType("card/credit");
        account.setBalance(new BigDecimal(9900.00));
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        client.setAccounts(accounts);

        ResponseEntity responseEntity = clientsRestController.saveClient(client);
        return responseEntity;
    }
}
