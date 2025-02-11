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
import com.zeecare.hms2.dto.PrescriptionRequest;
import com.zeecare.hms2.entity.Appointment;
import com.zeecare.hms2.entity.Doctor;
import com.zeecare.hms2.entity.Message;
import com.zeecare.hms2.entity.Prescription;
import com.zeecare.hms2.entity.User;
import com.zeecare.hms2.repository.DoctorRepository;
import com.zeecare.hms2.service.AppointmentService;
import com.zeecare.hms2.service.DoctorService;
import com.zeecare.hms2.service.MessageService;
import com.zeecare.hms2.service.PrescriptionService;
import com.zeecare.hms2.service.UserService;
import com.zeecare.hms2.util.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/user/doctor")
public class DoctorController {
	
	@Autowired
    private DoctorService doctorService;
	
	@Autowired
    private AppointmentService appointmentService;
	
	@Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PrescriptionService prescriptionService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
    @Autowired
    private DoctorRepository doctorRepository; // Use DoctorRepository for doctor-specific queries

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @GetMapping("/doctors/{department}")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(doctorRepository.findByRoleAndDoctorDepartment("Doctor", department));
    }
    @PostMapping("/addnew")
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
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    	Doctor doctor = doctorService.login(request.getEmail(), request.getPassword(), request.getRole());
        UserDetails userDetails = doctorService.loadUserByUsername(doctor.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/me")
    public ResponseEntity<Doctor> getCurrentUser(@RequestParam String email) {
        return ResponseEntity.ok(doctorService.getCurrentUser(email));
    }
    
    @GetMapping("/appointment/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
    
    @GetMapping("/appointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(id));
    }
    
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
    @GetMapping("/message/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {
        userService.logout(email);
        return ResponseEntity.ok("Logged out successfully");
    }
    @PostMapping("/prescribe/{appointmentId}")
    public ResponseEntity<Prescription> prescribeMedication(
            @PathVariable Long appointmentId,
            @RequestBody @Valid PrescriptionRequest request) {
        // Create the prescription using the service
        Prescription createdPrescription = prescriptionService.prescribeMedication(
                appointmentId,
                request.getMedication(),
                request.getDosage(),
                request.getInstructions()
        );

        return ResponseEntity.ok(createdPrescription);
    }
}