package com.vms.service;

import org.springframework.beans.factory.annotation.Autowired;

public class FormService {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private AccountService accountService;

}
