package com.kidzona.smsauth.repos;

import com.kidzona.smsauth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    public List<User> findByPhone(String phone);
}
