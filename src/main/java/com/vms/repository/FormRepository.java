package com.vms.repository;

import com.vms.model.Form;
import org.springframework.data.repository.CrudRepository;

public interface FormRepository extends CrudRepository<Form, Long> {
    public Form findByFormName(String formName);
}