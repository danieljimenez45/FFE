package com.entradas_cine.ffe.cine.config.SecurityConfig;

import com.entradas_cine.ffe.cine.config.ApiRoutes;
import com.entradas_cine.ffe.cine.config.auth.AuthUsersService;
import com.entradas_cine.ffe.cine.config.auth.JwtAuthenticationFilter;
import com.entradas_cine.ffe.cine.config.auth.JwtService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración de seguridad de la aplicación.
 * Se definen varias cadenas de filtros en orden de prioridad:
 * la API REST usa JWT sin sesión, la web usa formulario de login con sesión y CSRF,
 * y en desarrollo se habilitan Swagger, GraphiQL y la consola H2.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // -------------------------------------------------------------------------
    // Beans que comparten la API y la parte web
    // -------------------------------------------------------------------------

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService, AuthUsersService authUsersService) {
        return new JwtAuthenticationFilter(jwtService, authUsersService);
    }

    /**
     * Spring intentaría colgar el filtro JWT en toda la aplicación; lo desactivamos aquí
     * y solo lo enganchamos a mano en la cadena de la API, que es donde tiene sentido.
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * Repositorio de contexto de seguridad que combina sesión HTTP y atributo de request.
     * Se expone como bean para poder inyectarlo en RegisterController y hacer el auto-login
     * después del registro. En Spring Security 6 hay que declararlo explícitamente.
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ApiRoutes.API_PREFIX + "/**", configuration);
        return source;
    }

    // -------------------------------------------------------------------------
    // Primero: solo rutas /api/v1/** (la API que habla JSON y lleva JWT)
    //
    // Sin sesión de servidor ni CSRF: el token va en la cabecera. Si alguien intenta
    // entrar sin estar identificado cuando hace falta, mejor un 401 claro que un 403 raro.
    // Abajo se listan qué URLs van a pie, cuáles piden usuario logueado y cuáles solo admin.
    // -------------------------------------------------------------------------
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .securityMatcher(ApiRoutes.API_PREFIX + "/**")
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                // 403 si hay sesión/token pero sin permiso.
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        response.sendError(HttpStatus.FORBIDDEN.value(), "Acceso denegado")))
            .authorizeHttpRequests(auth -> auth

                // Registro e inicio de sesión: abiertos
                .requestMatchers(HttpMethod.POST,
                    ApiRoutes.AUTH + "/signup",
                    ApiRoutes.AUTH + "/signin").permitAll()

                // Ver cartelera y sesiones sin cuenta
                .requestMatchers(HttpMethod.GET,
                    ApiRoutes.PELICULAS + "/**").permitAll()
                .requestMatchers(HttpMethod.GET,
                    ApiRoutes.SESIONES + "/**").permitAll()

                // Ocupación de butacas: hace falta antes de loguearse al elegir sitio
                .requestMatchers(HttpMethod.GET,
                    ApiRoutes.ENTRADAS + "/sesion/*/butacas-ocupadas").permitAll()

                // Comprar y ver facturas: hace falta token de usuario
                .requestMatchers(HttpMethod.GET,
                    ApiRoutes.ENTRADAS + "/**").authenticated()
                .requestMatchers(HttpMethod.POST,
                    ApiRoutes.ENTRADAS + "/**").authenticated()
                .requestMatchers(HttpMethod.GET,
                    ApiRoutes.FACTURAS + "/**").authenticated()
                .requestMatchers(HttpMethod.POST,
                    ApiRoutes.FACTURAS + "/**").authenticated()

                // Crear o tocar catálogo / borrar cosas sensibles: solo administración
                .requestMatchers(HttpMethod.POST,
                    ApiRoutes.PELICULAS + "/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,
                    ApiRoutes.PELICULAS + "/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,
                    ApiRoutes.ENTRADAS + "/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,
                    ApiRoutes.FACTURAS + "/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,
                    ApiRoutes.SESIONES + "/**").hasRole("ADMIN")

                // CRUD de usuarios en API: solo administración
                .requestMatchers(
                    ApiRoutes.USUARIOS + "/**").hasRole("ADMIN")

                // Cualquier otra ruta de la API que no encaje arriba: cerrada
                .anyRequest().denyAll()
            );

        return http.build();
    }

    /**
     * Sin perfil {@code dev}: bloquea documentación OpenAPI, Swagger UI, GraphQL y H2
     * en una cadena propia (sin formLogin). Así se devuelve 403 y no un 302 al login MVC.
     * Con {@code dev} activo no existe este bean; entra la cadena {@code swaggerFilterChain}.
     */
    @Bean
    @Order(2)
    @Profile("!dev")
    public SecurityFilterChain herramientasDocBloqueadasSinPerfilDev(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                    "/swagger-ui/**",
                    "/v3/**",
                    "/swagger-ui.html",
                    "/graphiql",
                    "/graphiql/**",
                    "/graphql",
                    "/graphql/**",
                    "/h2-console/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().denyAll());
        return http.build();
    }

    // -------------------------------------------------------------------------
    // En modo desarrollo: dejar entrar a Swagger y al playground de GraphQL
    // (si no existiera esta cadena, la parte web las taparía con la regla de “cerrado”).
    // -------------------------------------------------------------------------
    @Bean
    @Order(2)
    @Profile("dev")
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/swagger-ui/**",
                "/v3/**",
                "/swagger-ui.html",
                "/graphiql",
                "/graphiql/**",
                "/graphql",
                "/graphql/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    // -------------------------------------------------------------------------
    // En modo desarrollo: consola web de H2 (necesita quitar CSRF aquí y permitir iframes).
    // -------------------------------------------------------------------------
    @Bean
    @Order(3)
    @Profile("dev")
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public SecurityFilterChain h2FilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(PathRequest.toH2Console())
            .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    // -------------------------------------------------------------------------
    // Por último: el resto de la web (HTML, estáticos, login con formulario)
    //
    // Aquí sí hay sesión y CSRF: en los formularios Pebble hay que meter el token oculto
    // que Spring mete en el modelo (_csrf).
    //
    // Se añaden cabeceras razonables (tipo de contenido, iframe solo mismo sitio, HSTS).
    //
    // Ojo: sin perfil dev, Swagger/OpenAPI/H2 van en la cadena dedicada
    // {@code herramientasDocBloqueadasSinPerfilDev} (403, sin redirección al login).
    // -------------------------------------------------------------------------
    @Bean
    @Order(4)
    public SecurityFilterChain mvcFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            // Mismo repositorio que usa RegisterController para que el auto-login persista bien
            .securityContext(sc -> sc.securityContextRepository(securityContextRepository()))
            // Con Pebble hay que usar CsrfTokenRequestAttributeHandler para que el token CSRF
            // se resuelva antes del renderizado de la vista y no falle al crear la sesión
            .csrf(csrf -> csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
            .headers(headers -> headers
                .contentTypeOptions(Customizer.withDefaults())
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .httpStrictTransportSecurity(hsts -> hsts
                    .maxAgeInSeconds(31_536_000)
                    .includeSubDomains(true)))
            .authorizeHttpRequests(auth -> auth

                // Home, login, registro y recursos estáticos sin sesión
                .requestMatchers(
                    "/",
                    "/sesiones/*/butacas",
                    "/auth/login",
                    "/auth/register",
                    "/error",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/video/**",
                    "/webjars/**",
                    "/favicon.ico").permitAll()

                // Panel de administración: solo ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Cualquier otra página: hay que haberse logueado por el formulario
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login-post")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error=true")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());

        return http.build();
    }
}
