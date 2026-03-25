package com.crm.service;

import com.crm.dto.AuthResponse;
import com.crm.dto.LoginRequest;
import com.crm.dto.RegisterRequest;

public interface UserService {
	
    AuthResponse register(RegisterRequest request);
    
    AuthResponse login(LoginRequest request);
}