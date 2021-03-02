package com.demo.response_entity;

import com.demo.model.Account;
import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for XML response of client accounts list
 */
@XmlRootElement(name = "accounts")
@Getter
public class AccountsContainerResponse {

    private List<Account> accounts;

    public AccountsContainerResponse() {
        accounts = new ArrayList<>();
    }

    @XmlElements(@XmlElement(name = "account", type = Account.class))
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
