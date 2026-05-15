package be.ucll.unit.model;

import be.ucll.model.Profile;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileTest {

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
    public void givenValidValues_whenCreatingProfile_thenProfileIsCreatedWithThoseValues() {
        Profile profile = new Profile("Teacher at UCLL", "Leuven", "Science, reading, cooking, movies");

        assertEquals("Teacher at UCLL", profile.getBio());
        assertEquals("Leuven", profile.getLocation());
        assertEquals("Science, reading, cooking, movies", profile.getInterests());
    }

    @Test
    public void givenInvalidBio_whenCreatingProfile_thenErrorIsThrown() {
        Set<ConstraintViolation<Profile>> violations = validator.validate(new Profile("", "Leuven", "Science"));

        assertEquals(1, violations.size());
        ConstraintViolation<Profile> violation = violations.iterator().next();
        assertEquals("Bio is required.", violation.getMessage());
    }

    @Test
    public void givenInvalidLocation_whenCreatingProfile_thenErrorIsThrown() {
        Set<ConstraintViolation<Profile>> violations = validator.validate(new Profile("Teacher at UCLL", "", "Science"));

        assertEquals(1, violations.size());
        ConstraintViolation<Profile> violation = violations.iterator().next();
        assertEquals("Location is required.", violation.getMessage());
    }

    @Test
    public void givenInvalidInterests_whenCreatingProfile_thenErrorIsThrown() {
        Set<ConstraintViolation<Profile>> violations = validator.validate(new Profile("Teacher at UCLL", "Leuven", ""));

        assertEquals(1, violations.size());
        ConstraintViolation<Profile> violation = violations.iterator().next();
        assertEquals("Interests are required.", violation.getMessage());
    }
}
