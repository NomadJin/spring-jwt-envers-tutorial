package com.example.springjwttutorial.auth.repository;


import com.example.springjwttutorial.auth.domain.Account;
import com.example.springjwttutorial.auth.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String>, RevisionRepository<Authority, String, Integer> {

}
