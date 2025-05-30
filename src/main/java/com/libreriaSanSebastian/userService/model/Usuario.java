package com.libreriaSanSebastian.userService.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String rut;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
}