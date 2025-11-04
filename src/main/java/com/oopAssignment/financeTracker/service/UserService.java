package com.oopAssignment.financeTracker.service;

import java.util.List;
import java.util.Random;

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
    private final Random random = new Random();

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createOrUpdateUserByContact(String contactNumber) {
        User existingUser = userRepository.findByContactNumber(contactNumber);

        String otp = String.format("%06d", random.nextInt(999999));

        if (existingUser != null) {
            existingUser.setOtp(otp);
            return userRepository.save(existingUser);
        } else {
            User newUser = new User();
            newUser.setContactNumber(contactNumber);
            newUser.setOtp(otp);
            return userRepository.save(newUser);
        }
    }

    public String verifyOtp(String contactNumber, String otp) {
        User user = userRepository.findByContactNumber(contactNumber);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found!");
        }

        String userOtp = user.getOtp();
        if (userOtp == null || !userOtp.equals(otp)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "The OTP you entered is incorrect.");
        }

        user.setOtp(null);
        userRepository.save(user);

        return jwtService.generateToken(user.getId(), contactNumber);
    }
}
