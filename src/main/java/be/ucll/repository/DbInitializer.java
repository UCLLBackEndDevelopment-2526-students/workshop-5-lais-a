package be.ucll.repository;

import be.ucll.model.Profile;
import be.ucll.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;

    public DbInitializer(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @PostConstruct
    public void initialize() {
        Profile profileJohn = profileRepository.save(new Profile("Student", "Antwerp", "Amazing science"));
        Profile profileJane = profileRepository.save(new Profile("Architect", "Leuven", "Sleeping"));
        Profile profileBirgit = profileRepository.save(new Profile("Java Programmer", "Hasselt", "Walking in the forest, Science"));

        User userJohn = new User("John Doe", 25, "john.doe@ucll.be", "john1234", profileJohn);
        userRepository.save(userJohn);

        User userJane = new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234", profileJane);
        userRepository.save(userJane);

        User user = new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234", profileBirgit);
        userRepository.save(user);

        user = new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234");
        userRepository.save(user);

        user = new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234");
        userRepository.save(user);
    }
}
