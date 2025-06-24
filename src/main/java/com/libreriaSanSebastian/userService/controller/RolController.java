package com.libreriaSanSebastian.userService.controller;

import com.libreriaSanSebastian.userService.model.Rol;
import com.libreriaSanSebastian.userService.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Operaciones relacionadas con la gestión de roles de usuarios")
public class RolController {

    @Autowired
    private RolService rolService;

    @Operation(
        summary = "Listar todos los roles",
        description = "Obtiene una lista completa de todos los roles registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de roles obtenida exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))
    )
    @GetMapping
    public List<Rol> listarTodos() {
        return rolService.listarTodos();
    }

    @Operation(
        summary = "Obtener rol por ID",
        description = "Busca y retorna un rol específico por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rol encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Rol no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerPorId(
            @Parameter(description = "ID único del rol", required = true, example = "1")
            @PathVariable Long id) {
        return rolService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener rol por nombre",
        description = "Busca y retorna un rol específico por su nombre"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rol encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Rol no encontrado",
            content = @Content
        )
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Rol> obtenerPorNombre(
            @Parameter(description = "Nombre del rol", required = true, example = "ADMIN")
            @PathVariable String nombre) {
        return rolService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo rol",
        description = "Registra un nuevo rol en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Rol creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Datos del rol a crear", required = true)
            @RequestBody Rol rol) {
        try {
            if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El nombre del rol es requerido"));
            }
            
            Rol rolCreado = rolService.guardar(rol);
            return ResponseEntity.status(HttpStatus.CREATED).body(rolCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Actualizar rol",
        description = "Actualiza los datos de un rol existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rol actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Rol no encontrado",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID único del rol", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del rol", required = true)
            @RequestBody Rol rol) {
        try {
            if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El nombre del rol es requerido"));
            }
            
            return rolService.buscarPorId(id)
                    .map(existente -> {
                        rol.setId(id);
                        Rol actualizado = rolService.guardar(rol);
                        return ResponseEntity.ok(actualizado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Eliminar rol",
        description = "Elimina permanentemente un rol del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Rol eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Rol no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del rol", required = true, example = "1")
            @PathVariable Long id) {
        if (rolService.buscarPorId(id).isPresent()) {
            rolService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}