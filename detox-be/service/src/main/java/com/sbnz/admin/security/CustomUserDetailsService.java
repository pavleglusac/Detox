package com.sbnz.admin.security;

import com.sbnz.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.sbnz.admin.exception.UserNotFoundException;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	public UserDetails loadUserById(UUID id) {
		return userRepository.findById(id).orElse(null);
	}
}
