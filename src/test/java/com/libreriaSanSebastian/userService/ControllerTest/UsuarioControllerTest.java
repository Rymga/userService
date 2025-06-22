package com.libreriaSanSebastian.userService.ControllerTest;

import com.libreriaSanSebastian.userService.controller.UsuarioController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libreriaSanSebastian.userService.model.Rol;
import com.libreriaSanSebastian.userService.model.Usuario;
import com.libreriaSanSebastian.userService.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario1;
    private Usuario usuario2;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol();
        rolAdmin.setId(1L);
        rolAdmin.setNombre("ADMIN");

        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@email.com");
        usuario1.setRut("12345678-9");
        usuario1.setRol(rolAdmin);

        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("María González");
        usuario2.setEmail("maria@email.com");
        usuario2.setRut("98765432-1");
        usuario2.setRol(rolAdmin);
    }

    
    @Test
    void testListarTodos() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioService.listarTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].nombre").value("María González"));

        verify(usuarioService, times(1)).listarTodos();
    }


    @Test
    void testObtenerPorRut_Existente() throws Exception {
        // Arrange
        when(usuarioService.buscarPorRut("12345678-9")).thenReturn(Optional.of(usuario1));

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios/rut/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));

        verify(usuarioService, times(1)).buscarPorRut("12345678-9");
    }


    @Test
    void testObtenerPorRut_NoExistente() throws Exception {
        // Arrange
        when(usuarioService.buscarPorRut("99999999-9")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios/rut/99999999-9"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).buscarPorRut("99999999-9");
    }


    @Test
    void testObtenerPorId_Existente() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(usuario1));

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));

        verify(usuarioService, times(1)).buscarPorId(1L);
    }


    @Test
    void testObtenerPorId_NoExistente() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).buscarPorId(99L);
    }


    @Test
    void testCrear() throws Exception {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Pedro Silva");
        nuevoUsuario.setEmail("pedro@email.com");
        nuevoUsuario.setRut("11111111-1");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(3L);
        usuarioGuardado.setNombre("Pedro Silva");
        usuarioGuardado.setEmail("pedro@email.com");
        usuarioGuardado.setRut("11111111-1");

        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuarioGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Pedro Silva"));

        verify(usuarioService, times(1)).guardar(any(Usuario.class));
    }


    @Test
    void testActualizar_Existente() throws Exception {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Juan Pérez Actualizado");
        usuarioActualizado.setEmail("juan_actualizado@email.com");
        usuarioActualizado.setRut("12345678-9");

        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(usuario1));
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez Actualizado"));

        verify(usuarioService, times(1)).buscarPorId(1L);
        verify(usuarioService, times(1)).guardar(any(Usuario.class));
    }


    @Test
    void testActualizar_NoExistente() throws Exception {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(99L);
        usuarioActualizado.setNombre("Usuario No Existente");

        when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/v1/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).buscarPorId(99L);
    }


    @Test
    void testEliminar_Existente() throws Exception {    
        // Arrange
        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(usuario1));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).buscarPorId(1L);
        verify(usuarioService, times(1)).eliminar(1L);
    }


    @Test
    void testEliminar_NoExistente() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).buscarPorId(99L);
    }
}