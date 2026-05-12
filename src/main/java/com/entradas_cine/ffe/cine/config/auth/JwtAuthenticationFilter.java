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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Si la petición trae un Bearer JWT válido, deja al usuario autenticado en el contexto.
 * Si el token falla o no hay usuario, responde 401; si no hay token, deja pasar
 * y Spring aplica después las rutas públicas o protegidas.
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

        log.info("Procesando request: {} {}", request.getMethod(), request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        // Sin cabecera o sin "Bearer ": dejar pasar (rutas permitAll)
        if (!StringUtils.hasText(authHeader)
                || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            log.info("Sin cabecera Authorization válida, continuando sin autenticar");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Cabecera Authorization encontrada, procesando token");
        final String jwt = authHeader.substring(7);

        // Extraer username del token; si falla → 401
        final String userName;
        try {
            userName = jwtService.extractUserName(jwt);
        } catch (Exception e) {
            log.warn("Token no válido: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Token no autorizado o no válido");
            return;
        }

        log.info("Username extraído del token: {}", userName);

        if (StringUtils.hasText(userName)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargar usuario desde BD; si no existe → 401
            final UserDetails userDetails;
            try {
                userDetails = authUsersService.loadUserByUsername(userName);
            } catch (Exception e) {
                log.warn("Usuario no encontrado: {}", userName);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Usuario no autorizado");
                return;
            }

            // Validar token y establecer contexto de seguridad
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("Token válido, autenticando usuario: {}", userName);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);
    }
}
