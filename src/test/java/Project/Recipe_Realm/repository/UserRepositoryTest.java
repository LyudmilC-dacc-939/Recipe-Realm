package Project.Recipe_Realm.repository;


import Project.Recipe_Realm.enums.Role;
import Project.Recipe_Realm.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        //Action
        User user = new User();
        user.setEmail("Chincho@abv.bg");
        user.setUsername("chinchoo00");
        user.setPassword("chinchoMishoka");
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Test
    @DisplayName("Test 1: Find By Email")
    @Order(1)
    void findByEmail_returnsUser() {

        User existingUser = userRepository.getByEmail("Chincho@abv.bg");
        Assertions.assertEquals(existingUser.getUsername(), "chinchoo00");
    }

    @Test
    @DisplayName("Test 2: Find By Email: Fail")
    @Order(2)
    void findByEmail_returnsNull() {
        User existingUser = userRepository.getByEmail("john.doe@gmail.com");
        Assert.state(existingUser == null, "The user is empty");
    }

    @Test
    @DisplayName("Test 3: Find By Id")
    @Order(3)
    void findById_returnsUser() {
        User user = new User();
        user.setEmail("Chincho@abv.bg");
        user.setUsername("chinchoo00");
        user.setPassword("chinchoMishoka");
        userRepository.save(user);
        User existingUser = userRepository.findById(user.getId()).get();
        Assertions.assertEquals(existingUser.getEmail(), "Chincho@abv.bg");
    }

    @Test
    @DisplayName("Test 4: Find By Id: Fail")
    @Order(4)
    void findById_returnsNull() {
        Optional<User> existingUser = userRepository.findById(1L);
        Assert.state(existingUser.isEmpty(), "is Null");
    }

    @Test
    @DisplayName("Test 5: Get by Id")
    @Order(5)
    void getReferenceById() {
        User user = new User();
        user.setEmail("Chincho@abv.bg");
        user.setUsername("chinchoo00");
        user.setPassword("chinchoMishoka");
        userRepository.save(user);
        User existingUser = userRepository.getReferenceById(user.getId());
        Assertions.assertEquals(existingUser.getEmail(), "Chincho@abv.bg");
    }

    @Test
    void findByUsername() {
        Optional<User> existingUser = userRepository.findByUsername("chinchoo00");
        Assert.state(existingUser.isPresent(), "User is Null");
        //Message should be if test fails so...opposite logic
    }

    @Test
    void getByEmail() {
        User existingUser = userRepository.getByEmail("Chincho@abv.bg");
        Assert.state(existingUser != null, "User is Null");
    }

    @Test
    void getByEmail_returnsNull() {
        User existingUser = userRepository.getByEmail("test.john.doe@abv.bg");
        Assert.state(existingUser == null, "User is not null");
    }
}