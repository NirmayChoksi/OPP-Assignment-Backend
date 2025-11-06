package com.oopAssignment.financeTracker.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oopAssignment.financeTracker.model.User;
import com.oopAssignment.financeTracker.repository.UserRepository;
import com.oopAssignment.financeTracker.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private static final SecureRandom secureRandom = new SecureRandom();

    // ✅ these could be moved to application.yml later
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int OTP_COOLDOWN_SECONDS = 30;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ===========================
    // ✅ PUBLIC METHODS
    // ===========================

    public User generateOtpForContact(String contactNumber) {
        User user = getOrCreateUser(contactNumber);
        enforceOtpCooldown(user);

        assignNewOtp(user);
        return userRepository.save(user);
    }

    public void resendOtp(String contactNumber) {
        User user = findUserOrThrow(contactNumber);
        enforceOtpCooldown(user);

        assignNewOtp(user);
        userRepository.save(user);
    }

    public String verifyOtp(String contactNumber, String otp) {
        User user = findUserOrThrow(contactNumber);

        validateOtpExists(user);
        validateOtpNotExpired(user);
        validateOtpMatch(user, otp);

        clearOtp(user);
        userRepository.save(user);

        return jwtService.generateToken(user.getId(), contactNumber);
    }

    // ===========================
    // ✅ PRIVATE HELPER METHODS
    // ===========================

    private User getOrCreateUser(String contactNumber) {
        User user = userRepository.findByContactNumber(contactNumber);
        if (user == null) {
            user = new User();
            user.setContactNumber(contactNumber);
        }
        return user;
    }

    private User findUserOrThrow(String contactNumber) {
        User user = userRepository.findByContactNumber(contactNumber);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        return user;
    }

    private void enforceOtpCooldown(User user) {
        LocalDateTime generatedAt = user.getOtpGeneratedAt();
        LocalDateTime now = LocalDateTime.now();

        if (generatedAt != null && generatedAt.isAfter(now.minusSeconds(OTP_COOLDOWN_SECONDS))) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Please wait before requesting another OTP.");
        }
    }

    private void assignNewOtp(User user) {
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
    }

    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private void validateOtpExists(User user) {
        if (user.getOtp() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No OTP was requested.");
        }
    }

    private void validateOtpNotExpired(User user) {
        LocalDateTime generatedAt = user.getOtpGeneratedAt();

        if (generatedAt == null ||
                generatedAt.isBefore(LocalDateTime.now().minusMinutes(OTP_EXPIRY_MINUTES))) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "OTP has expired. Please request a new one.");
        }
    }

    private void validateOtpMatch(User user, String otp) {
        if (!user.getOtp().equals(otp)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid OTP.");
        }
    }

    private void clearOtp(User user) {
        user.setOtp(null);
        user.setOtpGeneratedAt(null);
    }
}
