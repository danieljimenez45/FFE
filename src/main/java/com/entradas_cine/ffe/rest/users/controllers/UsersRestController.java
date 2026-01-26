package com.entradas_cine.ffe.rest.users.controllers;


import com.entradas_cine.ffe.rest.users.dto.UserInfoResponse;
import com.entradas_cine.ffe.rest.users.dto.UserRequest;
import com.entradas_cine.ffe.rest.users.dto.UserResponse;
import com.entradas_cine.ffe.rest.users.models.User;
import com.entradas_cine.ffe.rest.users.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/${api.version}/users") // Es la ruta del controlador
@PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
public class UsersRestController {
    private final UsersService usersService;
    private final PaginationLinksUtils paginationLinksUtils;
    private final EntradasService entradasService;

    /**
     * Obtiene todos los usuarios
     *
     * @param username  username del usuario
     * @param email     email del usuario
     * @param isDeleted si está borrado o no
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @param request   petición
     * @return Respuesta con la página de usuarios
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("findAll: username: {}, email: {}, isDeleted: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                username, email, isDeleted, page, size, sortBy, direction);
        // Creamos el objeto de ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Creamos cómo va a ser la paginación
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un usuario por su id
     *
     * @param id del usuario, se pasa como parámetro de la URL /{id}
     * @return Usuario si existe
     * @throws UserNotFound si no existe el usuario (404)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) {
        log.info("findById: id: {}", id);
        return ResponseEntity.ok(usersService.findById(id));
    }

    /**
     * Crea un nuevo usuario
     *
     * @param userRequest usuario a crear
     * @return Usuario creado
     * @throws UserNameOrEmailExists               si el nombre de usuario o el email ya existen
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("save: userRequest: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    /**
     * Actualiza un usuario
     *
     * @param id          id del usuario
     * @param userRequest usuario a actualizar
     * @return Usuario actualizado
     * @throws UserNotFound                        si no existe el usuario (404)
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * @throws UserNameOrEmailExists               si el nombre de usuario o el email ya existen (400)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        log.info("update: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    /**
     * Borra un usuario
     *
     * @param id id del usuario
     * @return Respuesta vacía
     * @throws UserNotFound si no existe el usuario (404)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("delete: id: {}", id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el usuario actual
     *
     * @param user usuario autenticado
     * @return Datos del usuario
     */
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los user pueden acceder
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        // Está autenticado, por lo que devolvemos sus datos ya sabemos su id
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }

    /**
     * Actualiza el usuario actual
     *
     * @param user        usuario autenticado
     * @param userRequest usuario a actualizar
     * @return Usuario actualizado
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     */
    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal User user,
                                                 @Valid @RequestBody UserRequest userRequest) {
        log.info("updateMe: user: {}, userRequest: {}", user, userRequest);
        return ResponseEntity.ok(usersService.update(user.getId(), userRequest));
    }

    /**
     * Borra el usuario actual
     *
     * @param user usuario autenticado
     * @return Respuesta vacía
     */
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user) {
        log.info("deleteMe: user: {}", user);
        usersService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene las entradas del usuario actual
     *
     * @param user      usuario autenticado
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @return Respuesta con la página de entradas
     */
    @GetMapping("/me/entradas")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<PageResponse<EntradaResponseDto>> getEntradasByUsuario(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idEntrada") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Obteniendo entradas del usuario con id: {}", user.getId());
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(entradasService.findByUsuarioId(user.getId(), pageable), sortBy, direction));
    }

    /**
     * Obtiene una entrada del usuario actual
     *
     * @param user     usuario autenticado
     * @param idEntrada id de la entrada
     * @return Entrada
     * @throws EntradaNotFoundException si no existe la entrada
     */
    @GetMapping("/me/entradas/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<EntradaResponseDto> getEntrada(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long idEntrada
    ) {
        log.info("Obteniendo entrada con id: {}", idEntrada);
        return ResponseEntity.ok(entradasService.findByUsuarioId(user.getId(), idEntrada));
    }

    /**
     * Crea una entrada para el usuario actual
     *
     * @param user   usuario autenticado
     * @param entrada entrada a crear
     * @return Entrada creada
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     */
    @PostMapping("/me/entradas")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<EntradaResponseDto> saveEntrada(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody EntradaCreateDto entrada
    ) {
        log.info("Creando entrada: {}", entrada);
        return ResponseEntity.status(HttpStatus.CREATED).body(entradasService.save(entrada, user.getId()));
    }

    /**
     * Actualiza una entrada del usuario actual
     *
     * @param user     usuario autenticado
     * @param idEntrada id de la entrada
     * @param  entrada a actualizar
     * @return Entrada actualizada
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * @throws EntradaNotFoundException                      si no existe la entrada (404)
     */
    @PutMapping("/me/entradas/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<EntradaResponseDto> updateEntrada(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long idEntrada,
            @Valid @RequestBody EntradaUpdateDto entrada) {
        log.info("Actualizando entrada con id: {}", idEntrada);
        return ResponseEntity.ok(entradasService.update(idEntrada, entrada, user.getId()));
    }

    /**
     * Borra una entrada del usuario actual
     *
     * @param user     usuario autenticado
     * @param idEntrada id de la entrada
     * @return Entrada borrada
     * @throws EntradaNotFoundException  si no existe la entrada (404)
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     */
    @DeleteMapping("/me/entradas/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Void> deleteEntrada(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long idEntrada
    ) {
        log.info("Borrando entrada con id: {}", idEntrada);
        entradasService.deleteById(idEntrada, user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     *
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        BindingResult result = ex.getBindingResult();
        problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName()
                + "'. " + "Núm. errores: " + result.getErrorCount());

        Map<String, String> errores = new HashMap<>();
        result.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("errores", errores);
        return problemDetail;
    }
}