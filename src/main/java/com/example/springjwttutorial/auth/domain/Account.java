package com.example.springjwttutorial.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "account")
@Setter
@Getter
@NoArgsConstructor
public class Account {

	@Id
	@Column(name = "account_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, unique = true)
	private String username;

	@JsonIgnore
	@Column(length = 100)
	private String password;

	@Column(length = 50)
	private String nickname;

	@JsonIgnore
	private boolean activated;

	@ManyToMany
	@JoinTable( // JoinTable은 테이블과 테이블 사이에 별도의 조인 테이블을 만들어 양 테이블간의 연관관계를 설정 하는 방법
			name = "account_authority",
			joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "account_id")},
			inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
	private Set<Authority> authorities;


	@Builder
	public Account(String username, String password, String nickname, boolean activated,
			Set<Authority> authorities) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.authorities = authorities;
		this.activated = activated;
	}
}
