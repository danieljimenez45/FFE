package com.entradas_cine.ffe.cine.config;

/**
 * Constantes centralizadas de rutas REST.
 * Cambiar API_PREFIX aquí propaga el cambio a todos los controladores.
 */
public final class ApiRoutes {

    private ApiRoutes() {}

    public static final String API_PREFIX = "/api/v1";

    public static final String PELICULAS  = API_PREFIX + "/peliculas";
    public static final String SESIONES   = API_PREFIX + "/sesiones";
    public static final String ENTRADAS   = API_PREFIX + "/entradas";
    public static final String FACTURAS   = API_PREFIX + "/facturas";
    public static final String USUARIOS   = API_PREFIX + "/usuarios";
    public static final String AUTH       = API_PREFIX + "/auth";
}
