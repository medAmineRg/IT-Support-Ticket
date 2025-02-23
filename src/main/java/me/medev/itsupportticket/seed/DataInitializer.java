package me.medev.itsupportticket.seed;

import me.medev.itsupportticket.entity.User;
import me.medev.itsupportticket.entity.UserRole;
import me.medev.itsupportticket.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(new User("employee1@example.com", "password", UserRole.EMPLOYEE, "employee1",
                        LocalDateTime.now()));
                userRepository.save(new User("itsupport1@example.com", "password", UserRole.ITSUPPORT, "itsupport1",
                        LocalDateTime.now()));
            }
        };
    }
}