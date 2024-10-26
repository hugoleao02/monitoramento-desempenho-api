package com.projeto.monitoramento_desempenho_api.infra.security;

import com.projeto.monitoramento_desempenho_api.domain.entities.User;
import com.projeto.monitoramento_desempenho_api.infra.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserRepository userRepository;

    @Autowired
    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = this.recoverToken(request);
        System.out.println("Token recuperado: " + token); // Log do token

        if (nonNull(token)) {
            try {
                String email = tokenService.getEmailFromToken(token);
                System.out.println("Email recuperado do token: " + email); // Log do email

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

                UserDetails userDetails = new UserAuthenticated(user);

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
