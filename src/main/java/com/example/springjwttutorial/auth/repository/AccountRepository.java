package com.example.springjwttutorial.auth.repository;

import com.example.springjwttutorial.auth.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, RevisionRepository<Account, Long, Integer> {

	@EntityGraph(attributePaths = "authorities")
	Optional<Account> findOneWithAuthoritiesByUsername(String username);
}
