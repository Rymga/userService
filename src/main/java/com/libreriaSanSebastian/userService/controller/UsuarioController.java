package com.libreriaSanSebastian.userService.controller;

import com.libreriaSanSebastian.userService.model.Usuario;
import com.libreriaSanSebastian.userService.service.UsuarioService;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "Listar todos los usuarios",
        description = "Obtiene una lista completa de todos los usuarios registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de usuarios obtenida exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
    )
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Busca y retorna un usuario específico por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener usuario por nombre",
        description = "Busca y retorna un usuario específico por su nombre"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content
        )
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Usuario> obtenerPorNombre(
            @Parameter(description = "Nombre del usuario", required = true, example = "Juan Pérez")
            @PathVariable String nombre) {
        return usuarioService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener usuario por RUT",
        description = "Busca y retorna un usuario específico por su RUT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content
        )
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Usuario> obtenerPorRut(
            @Parameter(description = "RUT del usuario", required = true, example = "12345678-9")
            @PathVariable String rut) {
        return usuarioService.buscarPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo usuario",
        description = "Registra un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @RequestBody Usuario usuario) {
        try {
            if (usuario.getNombre() == null || usuario.getNombre().isEmpty() ||
                usuario.getEmail() == null || usuario.getEmail().isEmpty() ||
                usuario.getRut() == null || usuario.getRut().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Nombre, email y RUT son campos requeridos"));
            }
            
            Usuario usuarioCreado = usuarioService.guardar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario", required = true)
            @RequestBody Usuario usuario) {
        try {
            if (usuario.getNombre() == null || usuario.getNombre().isEmpty() ||
                usuario.getEmail() == null || usuario.getEmail().isEmpty() ||
                usuario.getRut() == null || usuario.getRut().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Nombre, email y RUT son campos requeridos"));
            }
            
            return usuarioService.buscarPorId(id)
                    .map(existente -> {
                        usuario.setId(id);
                        Usuario actualizado = usuarioService.guardar(usuario);
                        return ResponseEntity.ok(actualizado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina permanentemente un usuario del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuario eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        if (usuarioService.buscarPorId(id).isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}