package com.vms.repository;

import com.vms.model.Form;
import com.vms.model.FormSection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FormRepository extends CrudRepository<Form, Long> {
    Optional<Form> findByName(String name);
    List<FormSection> findAllSectionsById(Long id);
}