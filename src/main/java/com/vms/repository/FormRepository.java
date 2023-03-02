package com.vms.repository;

import com.vms.model.Form;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FormRepository extends CrudRepository<Form, Long> {
    Optional<Form> findByName(String name);
}