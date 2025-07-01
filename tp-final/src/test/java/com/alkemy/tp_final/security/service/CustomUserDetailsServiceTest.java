package com.alkemy.tp_final.security.service;

import com.alkemy.tp_final.model.UsuarioModel;
import com.alkemy.tp_final.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_ReturnsUserDetails_WhenUserExists() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setEmail("test@mail.com");
        usuario.setPassword("pass");
        usuario.setRoles(List.of("USER"));

        when(usuarioRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@mail.com");

        assertEquals("test@mail.com", userDetails.getUsername());
        assertEquals("pass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_Throws_WhenNotFound() {
        when(usuarioRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("notfound@mail.com"));
    }
}

