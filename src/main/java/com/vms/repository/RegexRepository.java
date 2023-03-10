package com.vms.repository;

import com.vms.model.Regex;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegexRepository extends CrudRepository<Regex, Long> {
    Boolean existsByPattern(String pattern);
}