package com.entradas_cine.ffe.cine.rest.notificaciones.models;

import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NOTIFICACIONES_USUARIO")
public class NotificacionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoNotificacion tipo;

    @Column(nullable = false, length = 120)
    private String peliculaTitulo;

    @Column(nullable = false)
    private LocalDate fechaSesion;

    @Column(nullable = false, length = 40)
    private String horario;

    @Column(nullable = false, length = 40)
    private String sala;

    @Column(nullable = false)
    private Float importe;

    @Column(nullable = false)
    private boolean leida;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}
