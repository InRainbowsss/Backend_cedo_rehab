package com.upc.cedorehab.security.controllers;

import com.upc.cedorehab.security.dtos.AuthRequestDTO;
import com.upc.cedorehab.security.dtos.AuthResponseDTO;
import com.upc.cedorehab.security.services.CustomUserDetailsService;
import com.upc.cedorehab.security.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> createAuthenticationToken(@RequestBody AuthRequestDTO authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setRoles(roles);
        return ResponseEntity.ok().headers(responseHeaders).body(authResponseDTO);
    }

    @PostMapping("/auth/pin")
    public ResponseEntity<?> authenticateByPin(@RequestBody Map<String, String> request) {
        String pin = request != null ? request.get("pin") : "";
        if ("2399".equals(pin)) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
            final String token = jwtUtil.generateToken(userDetails);
            Set<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Authorization", token);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("roles", roles);
            response.put("username", "admin");
            response.put("success", true);
            return ResponseEntity.ok().headers(responseHeaders).body(response);
        }
        return ResponseEntity.status(401).body(Map.of("error", "Clave incorrecta", "success", false));
    }

}
