package com.demo.controller;

import com.demo.model.form.FilterForm;
import com.demo.model.xml.FilterPaymentsRequest;
import com.demo.service.JournalService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/view/filters")
public class PaymentsFilterController {

    private JournalService service;

    public PaymentsFilterController(JournalService service) {
        this.service = service;
    }

    //Load form to create filter query
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getFilterForm(FilterForm filterForm){
        return "create_filter";
    }


    /**
     *
     * @param filterForm model object from thymeleaf HTML form
     * @return Return this form if filter is invalid or redirect to controller that make a request to database
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createFilter(@Valid FilterForm filterForm, BindingResult bindingResult, HttpSession session){

        if (bindingResult.hasErrors()){
            return "create_filter";
        }

        FilterPaymentsRequest filter = new FilterPaymentsRequest();
        filter.setPayerId(filterForm.getPayerId());
        filter.setRecipientId(filterForm.getRecipientId());
        filter.setSourceAccount(filterForm.getSourceAccount());
        filter.setDestinationAccount(filterForm.getDestinationAccount());
        session.setAttribute("filter", filter);
        return "redirect:/view/filters/request";
    }

    /**
     * @return All payments matches in database according to request
     */
    @RequestMapping(value = "/request", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createFilter(HttpSession session){
        FilterPaymentsRequest filter = (FilterPaymentsRequest) session.getAttribute("filter");
        session.removeAttribute("filter");
        return service.filterPaymentsJson(filter);
    }
}
