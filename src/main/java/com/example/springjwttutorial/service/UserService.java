package com.example.springjwttutorial.service;

import com.example.springjwttutorial.auth.domain.Account;
import com.example.springjwttutorial.auth.domain.Authority;
import com.example.springjwttutorial.auth.repository.AccountRepository;
import com.example.springjwttutorial.dto.UserDto;
import com.example.springjwttutorial.util.SecurityUtil;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 사용자 회원 가입
	 *
	 * @param userDto
	 * @return
	 */
	@Transactional
	public Account signup(UserDto userDto) {

		if (accountRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
			throw new RuntimeException("이미 가입되어 있는 유저입니다.");
		}

		Authority authority = Authority.builder()
				.authorityName("ROLE_USER")
				.build();

		Account account = Account.builder()
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.authorities(Collections.singleton(authority))
				.activated(true)
				.build();

		return accountRepository.save(account);
	}

	/**
	 * username 으로 사용자 및 권한 정보를 가져온다.
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<Account> getUserWithAuthorities(String username) {
		return accountRepository.findOneWithAuthoritiesByUsername(username);
	}

	/**
	 * Security context 에 저장된 사용자 및 권한 정보를 가져온다.
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<Account> getMyUserWithAuthorities() {
		return SecurityUtil.getCurrentUsername().flatMap(accountRepository::findOneWithAuthoritiesByUsername);
	}

}
