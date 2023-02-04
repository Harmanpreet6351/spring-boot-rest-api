package com.todorest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todorest.model.User;
import com.todorest.repository.UserRepository;
import com.todorest.request.CredentialRequest;
import com.todorest.service.JwtUtils; 

@RestController
public class AuthController {

	@Autowired
	JwtUtils jwtUtil;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	PasswordEncoder passEncoder;
	
	@Autowired
	UserRepository userRepo;
	
	@GetMapping("/auth/token")
	public ResponseEntity<Map<String, String>> getToken(@RequestBody CredentialRequest creds) {
		Optional<User> user = userRepo.findByUsername(creds.getUsername());
		if(user.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		try {
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword())
				);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		Map<String, String> result = new HashMap<>();
		result.put("token", jwtUtil.getToken(user.get().getUsername()));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/auth/register")
	public ResponseEntity<User> registerUser(@RequestBody CredentialRequest creds) {
		User user = new User();
		user.setUsername(creds.getUsername());
		user.setPassword(passEncoder.encode(creds.getPassword()));	
		userRepo.save(user);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
}
