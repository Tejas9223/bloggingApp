package com.codewithdurgesh;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codewithdurgesh.config.AppConstants;
import com.codewithdurgesh.entities.Role;
import com.codewithdurgesh.entities.User;
import com.codewithdurgesh.repositories.RoleRepo;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Application implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private com.codewithdurgesh.repositories.UserRepo userRepo;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) {
        System.out.println("Encoded password for testing: " + this.passwordEncoder.encode("abc"));

        try {
            // ✅ Step 1: Seed roles only once
            if (roleRepo.count() == 0) {
                Role admin = new Role();
                admin.setId(AppConstants.ADMIN_USER); // usually 501
                admin.setName("ROLE_ADMIN");

                Role user = new Role();
                user.setId(AppConstants.NORMAL_USER); // usually 502
                user.setName("ROLE_USER");

                List<Role> roles = List.of(admin, user);
                roleRepo.saveAll(roles);
                System.out.println("Default roles created.");
            } else {
                System.out.println("Roles already exist.");
            }

            // ✅ Step 2: Seed admin user only once
            System.out.println("Admin role present: " + roleRepo.findById(AppConstants.ADMIN_USER).isPresent());
            System.out.println("Existing admin user: " + userRepo.findByEmail("admin@example.com"));
            if (userRepo.findByEmail("admin@example.com") == null) {
                User adminUser = new User();
                adminUser.setName("Admin User");
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setAbout("System administrator");
                adminUser.getRoles().add(roleRepo.findById(AppConstants.ADMIN_USER).get());
                userRepo.save(adminUser);
                System.out.println("Admin user created.");
            } else {
                System.out.println("Admin user already exists.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
