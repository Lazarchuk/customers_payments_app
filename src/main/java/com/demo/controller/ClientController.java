package com.demo.controller;

import com.demo.model.Account;
import com.demo.model.Client;
import com.demo.model.form.AccountForm;
import com.demo.model.form.ClientForm;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
@RequestMapping("/view/clients")
public class ClientController {


    // Load form to get clients accounts
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getClientAccounts(){
        return "get_accounts";
    }


    /**
     *  Get accounts by client id
     * @param id  client_id
     * @param responseType chosen content type to response list of accounts. Only JSON and XML available
     * @return redirect to rest controller which returns ResponseBody on accounts list
     */
    @RequestMapping(value = "get/by_id", method = RequestMethod.GET)
    public String getClientAccounts(@RequestParam("client_id") Integer id,
                        @RequestParam(name = "responseType" , required = false, defaultValue = "json") String responseType){

        if (responseType.equals("xml")){
            return String.format("redirect:/rest/v1/clients/%d/xml", id);
        }
        return String.format("redirect:/rest/v1/clients/%d/json", id);
    }


    // Load form to create client
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getClientForm(ClientForm clientForm){
        return "create_client";
    }

    /**
     * Creates client from UI
     * @param clientForm   model object from thymeleaf HTML form
     * @return  Return this form if client is invalid or redirect to creation account form
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createClient(@Valid ClientForm clientForm, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "create_client";
        }

        Client client = new Client();
        client.setFirstName(clientForm.getFirstName());
        client.setLastName(clientForm.getLastName());
        session.setAttribute("client", client);
        return "redirect:/view/clients/account";
    }


    // Load form to create account
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountForm(AccountForm accountForm){
        return "create_account";
    }

    /**
     * Creates account, add it to created client
     * @param accountForm   model object from thymeleaf HTML form
     * @return  Return this form if account is invalid of return new form to create new account
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public String createAccount(@Valid AccountForm accountForm, BindingResult bindingResult,
                                HttpSession session, ModelMap model) {

        if (bindingResult.hasErrors()) {
            return "create_account";
        }

        Client client = (Client) session.getAttribute("client");
        Account account = new Account();
        account.setAccountNumber(accountForm.getAccountNumber());
        account.setAccountType(accountForm.getAccountType());
        account.setBalance(accountForm.getBalance());

        client.getAccounts().add(account);
        session.setAttribute("client", client);
        model.addAttribute("accountForm", new AccountForm());
        return "create_account";
    }


    /**
     * Persist client in database
     * @return Return result of saving client in DB
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> saveClient(HttpSession session){

        Client client = (Client)  session.getAttribute("client");
        session.removeAttribute("client");

        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<Client> requestEntity = new HttpEntity<>(client, requestHeaders);

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Object> responseEntity = template.exchange("http://localhost:9966/rest/v1/clients/create/json", HttpMethod.POST, requestEntity, Object.class);
        return responseEntity;
    }


}
