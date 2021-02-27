package com.demo.controller;

import com.demo.model.Account;
import com.demo.model.Customer;
import com.demo.rest_controller.CustomersRestController;
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
@RequestMapping("/view/customers")
public class CustomerController {
    private CustomersRestController customersRestController;

    public CustomerController(CustomersRestController customersRestController) {
        this.customersRestController = customersRestController;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView initForm(){
        return new ModelAndView("startpage");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "customerName")
    @ResponseBody
    public ResponseEntity<List<Account>> createCustomer(@RequestParam("customerName") String name){
        System.out.println(name);
        Customer customer = new Customer();
        customer.setFirstName(name);
        customer.setLastName("Yohanson");

        Account account = new Account();
        account.setAccountNumber("654984263");
        account.setAccountType("card/credit");
        account.setBalance(new BigDecimal(9900.00));
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        customer.setAccounts(accounts);

        ResponseEntity responseEntity = customersRestController.saveCustomer(customer);
        return responseEntity;
    }
}
