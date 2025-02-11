package com.zeecare.hms2.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zeecare.hms2.dto.LoginRequest;
import com.zeecare.hms2.entity.Admin;
import com.zeecare.hms2.entity.Appointment;
import com.zeecare.hms2.entity.Doctor;
import com.zeecare.hms2.entity.Message;
import com.zeecare.hms2.entity.User;
import com.zeecare.hms2.service.AdminService;
import com.zeecare.hms2.service.AppointmentService;
import com.zeecare.hms2.service.DoctorService;
import com.zeecare.hms2.service.MessageService;
import com.zeecare.hms2.service.UserService;
import com.zeecare.hms2.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/user/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;

    @PostMapping("/addnew")
    public ResponseEntity<Admin> addNewAdmin(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam Integer age,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob, // Format added
            @RequestParam String gender) {
        
        Admin admin = adminService.addNewAdmin(firstName, lastName, email, phone, password, age, dob, gender);
        return ResponseEntity.ok(admin);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    	Admin admin = adminService.login(request.getEmail(), request.getPassword(), request.getRole());
        UserDetails userDetails = adminService.loadUserByUsername(admin.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/me")
    public ResponseEntity<Admin> getCurrentUser(@RequestParam String email) {
        return ResponseEntity.ok(adminService.getCurrentUser(email));
    }
    @GetMapping("/appointment/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
 // In your controller
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalAppointments", appointmentService.countAppointments());
        stats.put("totalDoctors", doctorService.countDoctors());
        return ResponseEntity.ok(stats);
    }
    
    @PutMapping("/updateStatus/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long appointmentId, // This should be Long
            @RequestParam String status) {
        
        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, status);
        return ResponseEntity.ok(updatedAppointment);
    }
    @PostMapping("/doctor/addnew")
    public ResponseEntity<User> addNewDoctor(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam Integer experience,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,  // Format the date
            @RequestParam String gender,
            @RequestParam String doctorDepartment,
            @RequestParam MultipartFile docAvatar) throws IOException {
        
        System.out.println("dob: " + dob); // Debugging output

        Doctor doctor = doctorService.addNewDoctor(firstName, lastName, email, phone, password, experience, dob, gender, doctorDepartment, docAvatar);
        return ResponseEntity.ok(doctor);
    }
    
    @GetMapping("/message/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {
        userService.logout(email);
        return ResponseEntity.ok("Logged out successfully");
    }
}