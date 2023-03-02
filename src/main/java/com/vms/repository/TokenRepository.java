package com.vms.repository;
import java.util.List;
import java.util.Optional;

import com.vms.model.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {

    @Query(value = """
      select t from Token t inner join Account a\s
      on t.account.id = a.id\s
      where a.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}