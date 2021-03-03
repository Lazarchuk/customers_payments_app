package com.demo.controller;

import com.demo.model.Payment;
import com.demo.model.form.PaymentForm;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/view/payments")
public class PaymentController {


    // Load form to create payment
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getPaymentForm(PaymentForm paymentForm){
        return "create_payment";
    }

    /**
     * Create payment
     * @param paymentForm   model object from thymeleaf HTML form
     * @return Return this form if payment is invalid or return new form to create new payment
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String getCretePayment(@Valid PaymentForm paymentForm, BindingResult bindingResult,
                                  HttpSession session, ModelMap model){

        if (bindingResult.hasErrors()){
            return "create_payment";
        }

        List<Payment> payments = (List<Payment>) session.getAttribute("payments");
        if (payments == null){
            payments = new ArrayList<>();
        }
        Payment payment = new Payment();
        payment.setSourceAccount(paymentForm.getSourceAccount());
        payment.setDestinationAccount(paymentForm.getDestinationAccount());
        payment.setAmount(paymentForm.getAmount());
        payment.setReason(paymentForm.getReason());
        payments.add(payment);

        session.setAttribute("payments", payments);
        model.addAttribute("paymentForm", new PaymentForm());
        return "create_payment";
    }

    /**
     * Transact payment, save in database
     * @return Transaction result
     */
    @RequestMapping(value = "/transact", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> paymentsTransact(HttpSession session){
        List<Payment> payments = (List<Payment>) session.getAttribute("payments");
        session.removeAttribute("payments");

        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<List<Payment>> requestEntity = new HttpEntity<>(payments, requestHeaders);

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Object> responseEntity = template.exchange("http://localhost:9966/rest/v1/payments/createmany/fromjson", HttpMethod.POST, requestEntity, Object.class);
        return responseEntity;
    }

}
