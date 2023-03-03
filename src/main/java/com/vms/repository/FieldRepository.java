package com.vms.repository;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.vms.model.Field;

@Repository
public interface FieldRepository extends CrudRepository<Field, Long> {

}
