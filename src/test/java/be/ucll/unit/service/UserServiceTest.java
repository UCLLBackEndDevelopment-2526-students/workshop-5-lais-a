package be.ucll.unit.service;

import be.ucll.model.User;
import be.ucll.repository.LoanRepository;
import be.ucll.service.UserService;
import be.ucll.unit.repository.UserRepositoryStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepositoryStub userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepositoryStub();
        userService = new UserService(userRepository, new LoanRepository());
    }

    @Test
    public void givenUserRepositoryWithUsers_whenCallingAllUsers_allUsersFromRepositoryArePresent() {
        // when
        List<User> result = userService.getAllUsers();

        // then
        assertEquals(userRepository.getUsers().size(), result.size());
        assertTrue(userRepository.getUsers().containsAll(result));
    }

    @Test
    public void givenUserRepositoryWithAdultUsers_whenGetAdults_thenReturnAdultUsers() {
        // given
        // userRepository with default users

        // when
        List<User> users = userService.getAllAdultUsers();

        // then
        assertEquals(3, users.size());
        assertTrue(users.contains(new User("John Doe", 25, "john.doe@ucll.be", "john1234")));
        assertTrue(users.contains(new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234")));
        assertFalse(users.contains(new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234")));
        assertFalse(users.contains(new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234")));
    }

    @Test
    public void givenUserRepositoryWithNoAdultUsers_whenGetAdults_thenReturnEmptyList() {
        // given
        userRepository.setUsers(List.of(
                    new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
                    new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234")));

        // when
        List<User> users = userService.getAllAdultUsers();

        // then
        assertEquals(0, users.size());
        assertFalse(users.contains(new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234")));
        assertFalse(users.contains(new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234")));
    }

    @Test
    public void givenUserRepositoryWithNoUsers_whenGetAdults_thenReturnEmptyList() {
        // given
        userRepository.setUsers(List.of());

        // when
        List<User> users = userService.getAllAdultUsers();

        // then
        assertEquals(0, users.size());
    }

    @Test
    public void givenUserRepositoryWithUsers_whenGetUsersBetweenAge5And25_thenReturnUsersBetweenAge() {
        // given

        // when
        List<User> users = userService.getUsersBetweenAge(5, 25);

        // then
        assertEquals(3, users.size());
        assertTrue(users.contains(new User("John Doe", 25, "john.doe@ucll.be", "john1234")));
        assertFalse(users.contains(new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234")));
        assertTrue(users.contains(new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234")));
        assertFalse(users.contains(new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234")));

    }

    @Test
    public void givenUserRepositoryWithNoUsers_whenGetUsersBetweenAge5And25_thenReturnEmptyList() {
        // given
        userRepository.setUsers(List.of());

        // when
        List<User> users = userService.getUsersBetweenAge(5, 25);

        // then
        assertEquals(0, users.size());
    }

    @Test
    public void givenUserRepositoryWithNoUsersBetweenAge5And25_whenGetUsersBetweenAge5And25_thenReturnEmptyList() {
        // given
        userRepository.setUsers(List.of(
                new User("Jack Doe", 55, "jack.doe@ucll.be", "jack1234"),
                new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234")));

        // when
        List<User> users = userService.getUsersBetweenAge(5, 25);

        // then
        assertEquals(0, users.size());
    }

    @Test
    public void givenUserRepositoryWithUsers_whenGetUsersBetweenAge25And5_thenThrowException() {
        // given

        // when, then
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.getUsersBetweenAge(25, 5));

        assertEquals("Minimum age cannot be greater than maximum age", ex.getMessage());
    }

    @Test
    public void givenUserRepositoryWithUsers_whenGetUsersBetweenAge5And500_thenThrowException() {
        // given

        // when, then
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.getUsersBetweenAge(5, 500));

        assertEquals("Invalid age range. Age must be between 0 and 150.", ex.getMessage());
    }

    @Test
    public void givenUserRepositoryWithUsers_whenGetUsersWithNameDoe_thenReturnUsersWithName() {
        // given

        // when
        List<User> users = userService.getUsersByName("Doe");

        // then
        assertEquals(4, users.size());
        assertTrue(users.contains(new User("John Doe", 25, "john.doe@ucll.be", "john1234")));
        assertFalse(users.contains(new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234")));
        assertTrue(users.contains(new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234")));
        assertTrue(users.contains(new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234")));
    }

    @Test
    public void givenUserRepositoryWithUsers_whenGetUsersWithNameDodge_thenServiceExceptionIsThrown() {
        // given

        // when
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.getUsersByName("Dodge"));

        assertEquals("No users found with the specified name", ex.getMessage());
    }

    @Test
    public void givenUserRepositoryWithUsers_whenGetUsersWithEmptyName_thenAlleUsersAreReturned() {
        // given

        // when
        List<User> users = userService.getUsersByName("");

        // then
        assertEquals(5, users.size());
    }

    @Test
    public void givenNewUser_whenAddUser_thenUserIsAdded() {
        // given
        User newUser = new User("New User", 28, "new.user@ucll.be", "newpass123");

        // when
        User result = userService.addUser(newUser);

        // then
        assertEquals(newUser.getEmail(), result.getEmail());
        assertEquals(newUser.getName(), result.getName());
        assertTrue(userService.getAllUsers().contains(newUser));
    }

    @Test
    public void givenExistingUserEmail_whenAddUser_thenExceptionIsThrown() {
        // given
        User existingUser = new User("John Doe", 25, "john.doe@ucll.be", "john1234");

        // when, then
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.addUser(existingUser));

        assertEquals("User already exists.", ex.getMessage());
    }

    @Test
    public void givenExistingUser_whenUpdateWithSameEmail_thenUserIsUpdated() {
        // given
        String email = "john.doe@ucll.be";
        User updated = new User("John Updated", 26, email, "newpass123");

        // when
        User result = userService.updateUser(email, updated);

        // then
        assertEquals(updated.getName(), result.getName());
        assertEquals(updated.getAge(), result.getAge());
        assertEquals(updated.getPassword(), result.getPassword());
        assertEquals(updated.getEmail(), result.getEmail());
        assertTrue(userService.getAllUsers().contains(updated));
    }

    @Test
    public void givenNonExistingUser_whenUpdate_thenThrowsUserDoesNotExist() {
        // given
        String missingEmail = "missing@ucll.be";
        User updated = new User("Missing", 40, missingEmail, "password123");

        // when, then
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.updateUser(missingEmail, updated));

        assertEquals("User does not exist.", ex.getMessage());
    }

    @Test
    public void givenExistingUser_whenChangingEmail_thenThrowsEmailCannotBeChanged() {
        // given
        String email = "john.doe@ucll.be";
        User updatedWithDifferentEmail = new User("John Doe", 25, "new.email@ucll.be", "john1234");

        // when, then
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.updateUser(email, updatedWithDifferentEmail));

        assertEquals("E-mail address cannot be changed.", ex.getMessage());
    }

    @Test
    public void givenUserWithoutActiveLoans_whenDeleteUser_thenUserIsDeleted() {
        // given
        String existingEmail = "john.doe@ucll.be";

        // when
        userService.deleteUser(existingEmail);

        // then
        assertFalse(userRepository.userExists(existingEmail));
        assertEquals(4, userRepository.getUsers().size());
    }

    @Test
    public void givenNonExistingUser_whenDeleteUser_thenThrowException() {
        // given
        String nonExistingEmail = "nobody@ucll.be";

        // when, then
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(nonExistingEmail));

        assertEquals("User does not exist.", ex.getMessage());
    }

    @Test
    public void givenUserWithActiveLoans_whenDeleteUser_thenThrowException() {
        // given
        String email = "jane.toe@ucll.be";

        // when, then
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(email));

        assertEquals("User has active loans.", ex.getMessage());
    }

}
