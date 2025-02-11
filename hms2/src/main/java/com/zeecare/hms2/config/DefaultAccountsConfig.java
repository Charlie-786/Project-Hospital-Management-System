package com.zeecare.hms2.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zeecare.hms2.entity.Admin;
import com.zeecare.hms2.entity.Doctor;
import com.zeecare.hms2.repository.AdminRepository;
import com.zeecare.hms2.repository.DoctorRepository;

@Configuration
public class DefaultAccountsConfig implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default admin account
        if (!adminRepository.findByEmail("admin@zeecare.com").isPresent()) {
            Admin admin = new Admin();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@zeecare.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // Encrypt password
            admin.setPhone("1234567890");
            admin.setDob(new Date()); // Set a default date of birth
            admin.setGender("Male");
            admin.setRole("ADMIN");
            admin.setCreatedAt(new Date());
            adminRepository.save(admin);
            System.out.println("Default admin account created.");
        }

        // Create default doctor account
        if (!doctorRepository.findByEmail("doctor@zeecare.com").isPresent()) {
            Doctor doctor = new Doctor();
            doctor.setFirstName("John");
            doctor.setLastName("Doe");
            doctor.setEmail("doctor@zeecare.com");
            doctor.setPassword(passwordEncoder.encode("doctor123")); // Encrypt password
            doctor.setPhone("9876543210");
            doctor.setDob(new Date()); // Set a default date of birth
            doctor.setGender("Male");
            doctor.setRole("DOCTOR");
            doctor.setExperience(5);
            doctor.setDoctorDepartment("Cardiology");
            doctor.setCreatedAt(new Date());
            doctorRepository.save(doctor);
            System.out.println("Default doctor account created.");
        }
    }
}