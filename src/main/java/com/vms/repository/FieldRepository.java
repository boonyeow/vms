package com.vms.repository;
import com.vms.model.Form;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.vms.model.Field;

import java.util.List;
import java.util.Map;

@Repository
public interface FieldRepository extends CrudRepository<Field, Long> {

}
