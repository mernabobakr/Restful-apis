package com.kidzona.parentsservice.repository;

import com.kidzona.parentsservice.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepo extends JpaRepository<Parent,Integer> {

    List<Parent> findByEmail(String email);
    List<Parent> findByUserId(int userId);
}
