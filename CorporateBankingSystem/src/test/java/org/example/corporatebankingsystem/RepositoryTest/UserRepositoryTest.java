package org.example.corporatebankingsystem.RepositoryTest;

import org.example.corporatebankingsystem.Model.Role;
import org.example.corporatebankingsystem.Model.User;
import org.example.corporatebankingsystem.Repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void findByUsername_shouldReturnUser_whenExists() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@test.com");
        user.setRole(Role.RM);

        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("john");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@test.com");
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenNotExists() {
        Optional<User> result = userRepository.findByUsername("unknown");

        assertThat(result).isEmpty();
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenExists() {
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@test.com");
        user.setRole(Role.ADMIN);

        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("alice");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenNotExists() {
        boolean exists = userRepository.existsByUsername("missing");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenExists() {
        User user = new User();
        user.setUsername("bob");
        user.setEmail("bob@test.com");
        user.setRole(Role.ANALYST);

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("bob@test.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenNotExists() {
        boolean exists = userRepository.existsByEmail("noemail@test.com");

        assertThat(exists).isFalse();
    }
}
