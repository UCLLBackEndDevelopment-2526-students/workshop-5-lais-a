package be.ucll.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 0, message = "Age must be a positive integer between 0 and 101")
    @Max(value = 101, message = "Age must be a positive integer between 0 and 101")
    private int age;

    @NotBlank(message = "E-mail must be a valid email format")
    @Email(message = "E-mail must be a valid email format")
    private String email;

    @NotBlank(message = "Password must be at least 8 characters long")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Valid
    @OneToOne
    @JoinColumn(name = "profile_id", unique = true)
    private Profile profile;

    public User(String name, int age, String email, String password) {
        setName(name);
        setAge(age);
        setEmail(email);
        setPassword(password);
    }

    public User(String name, int age, String email, String password, Profile profile) {
        this(name, age, email, password);
        setProfile(profile);
    }

    protected User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (this.email != null && !this.email.equals(email))
            throw new RuntimeException("E-mail address cannot be changed.");

        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        if (profile != null && getAge() < 18) {
            throw new RuntimeException("User must be at least 18 years old to have a profile.");
        }

        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profile=" + profile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(profile, user.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email, password, profile);
    }
}
