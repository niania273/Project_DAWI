package com.project.project.dawi.repository;

import com.project.project.dawi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    User findByEmail(String email);
}
