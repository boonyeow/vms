package com.vms.repository;

import com.vms.model.Form;
import com.vms.model.keys.FormCompositeKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FormRepository extends CrudRepository<Form, FormCompositeKey> {
    @Query("SELECT f FROM Form f WHERE f.id.id = :id")
    List<Form> findByFormId_Id(Long id);
}