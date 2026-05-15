package be.ucll.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Bio is required.")
    private String bio;

    @NotBlank(message = "Location is required.")
    private String location;

    @NotBlank(message = "Interests are required.")
    private String interests;

    public Profile(String bio, String location, String interests) {
        setBio(bio);
        setLocation(location);
        setInterests(interests);
    }

    protected Profile() {
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "bio='" + bio + '\'' +
                ", location='" + location + '\'' +
                ", interests='" + interests + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(bio, profile.bio) && Objects.equals(location, profile.location) && Objects.equals(interests, profile.interests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bio, location, interests);
    }
}
