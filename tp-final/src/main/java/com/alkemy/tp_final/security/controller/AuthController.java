package com.alkemy.tp_final.security.controller;

import com.alkemy.tp_final.dto.UsuarioDTO;
import com.alkemy.tp_final.mapper.UsuarioMapper;
import com.alkemy.tp_final.model.UsuarioModel;
import com.alkemy.tp_final.repository.UsuarioRepository;
import com.alkemy.tp_final.security.dto.AuthRequest;
import com.alkemy.tp_final.security.dto.AuthResponse;
import com.alkemy.tp_final.security.JwtUtil;
import com.alkemy.tp_final.security.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Autenticar usuario",
            description = "Autentica un usuario y devuelve un token JWT si las credenciales son válidas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, retorna el token JWT"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(r -> r.replace("ROLE_", ""))
                    .toList();

            String token = jwtUtil.generateToken(userDetails.getUsername(), roles);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Registra un usuario en el sistema. Devuelve un mensaje de éxito o error si el email ya está registrado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado con éxito"),
            @ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UsuarioDTO request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email ya registrado");
        }

        UsuarioModel newUser = usuarioMapper.toModel(request);

        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(List.of("USER", "ADMIN"));

        usuarioRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }

}
