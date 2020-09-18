package com.kidzona.smsauth.repos;

import com.kidzona.smsauth.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepo extends JpaRepository<Token, String> {

    List<Token> findByToken(String token);
}
