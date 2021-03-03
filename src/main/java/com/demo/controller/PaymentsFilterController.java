package com.demo.controller;

import com.demo.model.form.FilterForm;
import com.demo.model.xml.FilterPaymentsRequest;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/view/filters")
public class PaymentsFilterController {


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
    @RequestMapping(value = "/request", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> createFilter(HttpSession session){
        FilterPaymentsRequest filter = (FilterPaymentsRequest) session.getAttribute("filter");
        session.removeAttribute("filter");

        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<FilterPaymentsRequest> requestEntity = new HttpEntity<>(filter, requestHeaders);

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Object> responseEntity = template.exchange("http://localhost:9966//rest/v1/journal/json", HttpMethod.POST, requestEntity, Object.class);
        return responseEntity;
    }
}
