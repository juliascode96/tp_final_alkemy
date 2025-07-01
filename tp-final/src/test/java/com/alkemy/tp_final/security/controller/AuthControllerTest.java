package com.alkemy.tp_final.security.controller;

import com.alkemy.tp_final.dto.UsuarioDTO;
import com.alkemy.tp_final.mapper.UsuarioMapper;
import com.alkemy.tp_final.model.UsuarioModel;
import com.alkemy.tp_final.repository.UsuarioRepository;
import com.alkemy.tp_final.security.JwtUtil;
import com.alkemy.tp_final.security.dto.AuthRequest;
import com.alkemy.tp_final.security.dto.AuthResponse;
import com.alkemy.tp_final.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock private CustomUserDetailsService userDetailsService;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private UsuarioMapper usuarioMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("login - autenticación exitosa")
    void login_ReturnsToken_WhenCredentialsValid() {
        AuthRequest request = new AuthRequest("test@email.com", "password");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                request.getEmail(), "encodedPassword",
                List.of(() -> "ROLE_USER")
        );

        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(request.getEmail())).thenReturn(userDetails);
        when(jwtUtil.generateToken(eq(request.getEmail()), anyList())).thenReturn("token123");

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token123", response.getBody().getToken());
    }

    @Test
    @DisplayName("login - credenciales inválidas")
    void login_ReturnsUnauthorized_WhenAuthFails() {
        AuthRequest request = new AuthRequest("user", "pass");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("signup - usuario nuevo creado")
    void signup_ReturnsCreated_WhenUserIsNew() {
        UsuarioDTO dto = new UsuarioDTO(null, "new@email.com", "123456", List.of(), true);
        UsuarioModel model = new UsuarioModel();
        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(usuarioMapper.toModel(dto)).thenReturn(model);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded123");

        ResponseEntity<String> response = authController.signup(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Usuario registrado con éxito", response.getBody());
        verify(usuarioRepository).save(any(UsuarioModel.class));
    }

    @Test
    @DisplayName("signup - email duplicado")
    void signup_ReturnsConflict_WhenEmailExists() {
        UsuarioDTO dto = new UsuarioDTO(null, "existing@email.com", "123456", List.of(), true);
        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new UsuarioModel()));

        ResponseEntity<String> response = authController.signup(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email ya registrado", response.getBody());
    }
}
