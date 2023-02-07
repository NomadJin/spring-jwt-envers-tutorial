package com.example.springjwttutorial.util;

import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

	/**
	 * Security Context 의 Authentication 객체를 이용해 username 을 리턴
	 * @return
	 */
	public static Optional<String> getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			log.info("Security Context에 인증 정보가 없습니다.");
			return Optional.empty();
		}

		String username = null;

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
			username = springSecurityUser.getUsername();
		} else if (authentication.getPrincipal() instanceof String) {
			username = (String) authentication.getPrincipal();
		}

		return Optional.ofNullable(username);
	}
}
