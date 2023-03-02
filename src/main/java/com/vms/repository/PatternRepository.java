package com.vms.repository;
import com.vms.model.Pattern;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatternRepository extends CrudRepository<Pattern, Long> {

    Optional<Pattern> findByPattern(String pattern);
}