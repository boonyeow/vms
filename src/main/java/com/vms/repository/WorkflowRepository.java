package com.vms.repository;

import com.vms.model.Workflow;
import com.vms.model.enums.AccountType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends CrudRepository<Workflow, Long> {
}