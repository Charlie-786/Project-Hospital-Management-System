package com.zeecare.hms2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zeecare.hms2.dto.LoginRequest;
import com.zeecare.hms2.entity.Appointment;
import com.zeecare.hms2.entity.Doctor;
import com.zeecare.hms2.entity.Prescription;
import com.zeecare.hms2.entity.User;
import com.zeecare.hms2.repository.DoctorRepository;
import com.zeecare.hms2.service.AppointmentService;
import com.zeecare.hms2.service.PrescriptionService;
import com.zeecare.hms2.service.UserService;
import com.zeecare.hms2.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private DoctorRepository doctorRepository; 	

    @PostMapping("/patient/register")
    public ResponseEntity<User> registerPatient(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerPatient(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword(), request.getRole());
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/patient/me")
    public ResponseEntity<User> getCurrentUser(@RequestParam String email) {
        return ResponseEntity.ok(userService.getCurrentUser(email));
    }

    @GetMapping("/patient/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {
        userService.logout(email);
        return ResponseEntity.ok("Logged out successfully");
    }
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }
    @GetMapping("/doctors/{department}")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(doctorRepository.findByRoleAndDoctorDepartment("Doctor", department));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByUser(userId));
    }
    @GetMapping("/user/{id}/all")
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@PathVariable Long id) {
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(id);
        return ResponseEntity.ok(appointments);
    }
}