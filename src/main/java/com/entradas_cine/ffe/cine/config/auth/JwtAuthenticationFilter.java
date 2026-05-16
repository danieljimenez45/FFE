package com.entradas_cine.ffe.cine.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Si la petición trae un Bearer JWT válido, deja al usuario autenticado en el contexto.
 * Si el token es inválido, caducado o el usuario no existe, se ignora la cabecera y la petición
 * continúa como anónima (las rutas públicas siguen respondiendo; las protegidas devuelven 401).
 * Va solo en la cadena de seguridad de la API, no en la web con formulario y sesión.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthUsersService authUsersService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("Procesando request: {} {}", request.getMethod(), request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader)
                || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            log.debug("Sin cabecera Authorization válida, continuando sin autenticar");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7).trim();
        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userName;
        try {
            userName = jwtService.extractUserName(jwt);
        } catch (Exception e) {
            log.debug("Bearer ignorado (token no parseable): {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.hasText(userName)
                || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final UserDetails userDetails;
        try {
            userDetails = authUsersService.loadUserByUsername(userName);
        } catch (UsernameNotFoundException e) {
            log.debug("Bearer ignorado (usuario no encontrado): {}", userName);
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtService.isTokenValid(jwt, userDetails)) {
            log.debug("Token válido, autenticando usuario: {}", userName);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        } else {
            log.debug("Bearer ignorado (token no válido para el usuario): {}", userName);
        }

        filterChain.doFilter(request, response);
    }
}
