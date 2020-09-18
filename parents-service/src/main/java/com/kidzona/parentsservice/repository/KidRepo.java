package com.kidzona.parentsservice.repository;

import com.kidzona.parentsservice.entity.Kid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KidRepo extends JpaRepository<Kid, Integer> {
}
