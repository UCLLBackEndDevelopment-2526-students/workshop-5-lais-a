package be.ucll.unit.model;

import be.ucll.model.Profile;
import be.ucll.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidValues_whenCreatingUser_thenUserIsCreatedWithThoseValues() {
        User user = new User("John Doe", 56, "john.doe@ucll.be", "john1234");

        assertEquals("John Doe", user.getName());
        assertEquals(56, user.getAge());
        assertEquals("john.doe@ucll.be", user.getEmail());
        assertEquals("john1234", user.getPassword());
    }

    @Test
    public void givenValidValuesWithProfile_whenCreatingUser_thenUserIsCreatedWithProfile() {
        Profile profile = new Profile("Teacher at UCLL", "Leuven", "Science, reading, cooking, movies");
        User user = new User("John Doe", 56, "john.doe@ucll.be", "john1234", profile);

        assertEquals(profile, user.getProfile());
    }

    @Test
    public void givenMinorUserWithProfile_whenCreatingUser_thenRuntimeExceptionIsThrown() {
        Profile profile = new Profile("Teacher at UCLL", "Leuven", "Science, reading, cooking, movies");

        Exception ex = Assertions.assertThrows(RuntimeException.class,
                () -> new User("John Doe", 17, "john.doe@ucll.be", "john1234", profile));

        Assertions.assertEquals("User must be at least 18 years old to have a profile.", ex.getMessage());
    }

    @Test
    public void givenEmptyName_whenSettingName_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("", 56, "john.doe@ucll.be", "john1234"));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Name is required", violation.getMessage());
    }

    @Test
    public void givenNegativeAge_whenSettingAge_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", -56, "john.doe@ucll.be", "john1234"));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Age must be a positive integer between 0 and 101", violation.getMessage());
    }

    @Test
    public void givenAgeLargerThan101_whenSettingAge_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", 120, "john.doe@ucll.be", "john1234"));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Age must be a positive integer between 0 and 101", violation.getMessage());
    }

    @Test
    public void givenInvalidEmailNull_whenSettingEmail_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", 12, null, "john1234"));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("E-mail must be a valid email format", violation.getMessage());
    }

    @Test
    public void givenInvalidEmailNoAt_whenSettingEmail_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", 12, "john.doe.ucll.be", "john1234"));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("E-mail must be a valid email format", violation.getMessage());
    }

    @Test
    public void givenInvalidEmailNoDot_whenSettingEmail_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", 12, "john@doe@ucll@be", "john1234"));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("E-mail must be a valid email format", violation.getMessage());
    }

    @Test
    public void givenInvalidPasswordToShort_whenSettingPassword_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", 12, "john.doe@ucll.be", "234"));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Password must be at least 8 characters long", violation.getMessage());
    }

    @Test
    public void givenInvalidPasswordEmptyString_whenSettingPassword_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", 12, "john.doe@ucll.be", ""));
        assertEquals(2, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Password must be at least 8 characters long", violation.getMessage());
        violation = violations.iterator().next();
        assertEquals("Password must be at least 8 characters long", violation.getMessage());
    }

    @Test
    public void givenInvalidPasswordNull_whenSettingPassword_thenRuntimeExceptionIsThrown() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("John Doe", 12, "john.doe@ucll.be", null));
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
    }

    @Test
    public void givenExistingUser_whenChangingEmail_thenRuntimeExceptionIsThrown() {
        User user = new User("John Doe", 30, "john.doe@ucll.be", "password123");

        Exception ex = Assertions.assertThrows(RuntimeException.class,
                () -> user.setEmail("new.email@ucll.be"));

        Assertions.assertEquals("E-mail address cannot be changed.", ex.getMessage());
    }

}
