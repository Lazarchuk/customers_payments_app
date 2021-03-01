package com.demo.controller;

import com.demo.model.Payment;
import com.demo.model.form.PaymentForm;
import com.demo.service.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/view/payments")
public class PaymentController {

    private PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

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
    public ResponseEntity<?> paymentsTransact(HttpSession session){
        List<Payment> payments = (List<Payment>) session.getAttribute("payments");
        session.removeAttribute("payments");
        return service.createPayments(payments, "");
    }

}
