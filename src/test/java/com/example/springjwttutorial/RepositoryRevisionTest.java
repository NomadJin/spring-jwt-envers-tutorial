package com.example.springjwttutorial;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springjwttutorial.auth.domain.Account;
import com.example.springjwttutorial.auth.domain.Authority;
import com.example.springjwttutorial.auth.repository.AccountRepository;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

@SpringBootTest
public class RepositoryRevisionTest {

	@Autowired
	private AccountRepository accountRepository;

	private Account account;

	@BeforeEach
	public void save() {
		accountRepository.deleteAll();

		Authority authority = Authority.builder()
				.authorityName("ROLE_USER")
				.build();

		account = accountRepository.save(Account.builder().username("test1").password("test1").nickname("test1").activated(true)
				.authorities(Collections.singleton(authority)).build());
	}

	@Test
	void initialRevision() {

		Revisions<Integer, Account> revisions = accountRepository.findRevisions(account.getId());

		System.out.println("revisions" + revisions.getLatestRevision().toString());

		assertThat(revisions).isNotEmpty().allSatisfy(revision -> assertThat(revision.getEntity())
				.extracting(Account::getId, Account::getUsername, Account::getNickname, Account::isActivated)
				.containsExactly(account.getId(), account.getUsername(), account.getNickname(), account.isActivated()));
	}

	@Test
	void updateRevisionNumber() {
		account.setUsername("updateName");

		accountRepository.save(account);
		Optional<Revision<Integer, Account>> lastChangeRevision = accountRepository.findLastChangeRevision(account.getId());

		assertThat(lastChangeRevision).isPresent().hasValueSatisfying(revision -> assertThat(revision.getRevisionNumber()).hasValue(3))
				.hasValueSatisfying(revision -> assertThat(revision.getEntity()).extracting(Account::getUsername).isEqualTo("updateName"));
	}

	@Test
	void deleteRevisionNumber() {

		accountRepository.delete(account);

		Revisions<Integer, Account> revisions = accountRepository.findRevisions(account.getId());

		assertThat(revisions).hasSize(2);

		Iterator<Revision<Integer, Account>> iterator = revisions.iterator();
		Revision<Integer, Account> initialRevision = iterator.next();
		Revision<Integer, Account> finalRevision = iterator.next();

		assertThat(initialRevision).satisfies(
				rev -> assertThat(rev.getEntity()).extracting(Account::getId, Account::getUsername, Account::getPassword)
						.containsExactly(account.getId(), account.getUsername(), account.getPassword()));

		assertThat(finalRevision).satisfies(rev -> assertThat(rev.getEntity()).extracting(Account::getId, Account::getUsername, Account::getPassword)
				.containsExactly(account.getId(), null, null));

	}
}
