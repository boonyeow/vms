package com.vms.repository;

import com.vms.model.FormSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormSectionRepository extends CrudRepository<FormSection, Long> {
    @Query("SELECT fs FROM FormSection fs LEFT JOIN FETCH fs.authorizedAccounts a WHERE fs.form.id = :formId")
    List<FormSection> findAllByFormIdWithAccounts(Long formId);
}