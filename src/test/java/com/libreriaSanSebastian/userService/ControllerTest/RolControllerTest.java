package com.libreriaSanSebastian.userService.ControllerTest;

import com.libreriaSanSebastian.userService.controller.RolController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libreriaSanSebastian.userService.model.Rol;
import com.libreriaSanSebastian.userService.service.RolService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    private Rol rol1;
    private Rol rol2;

    @BeforeEach
    void setUp() {
        rol1 = new Rol();
        rol1.setId(1L);
        rol1.setNombre("ADMIN");
        rol1.setDescripcion("Administrador");

        rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("USER");
        rol2.setDescripcion("Usuario");
    }

    @Test
    void testListarTodos() throws Exception {
        // Arrange
        List<Rol> roles = Arrays.asList(rol1, rol2);
        when(rolService.listarTodos()).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("ADMIN"))
                .andExpect(jsonPath("$[1].nombre").value("USER"));

        verify(rolService, times(1)).listarTodos();
    }

    @Test
    void testObtenerPorId_Existente() throws Exception {
        // Arrange
        when(rolService.buscarPorId(1L)).thenReturn(Optional.of(rol1));

        // Act & Assert
        mockMvc.perform(get("/api/v1/roles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ADMIN"))
                .andExpect(jsonPath("$.descripcion").value("Administrador"));

        verify(rolService, times(1)).buscarPorId(1L);
    }

    @Test
    void testObtenerPorId_NoExistente() throws Exception {
        // Arrange
        when(rolService.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/roles/99"))
                .andExpect(status().isNotFound());

        verify(rolService, times(1)).buscarPorId(99L);
    }

    @Test
    void testCrear() throws Exception {
        // Arrange
        Rol nuevoRol = new Rol();
        nuevoRol.setNombre("MODERADOR");
        nuevoRol.setDescripcion("Moderador del sistema");

        Rol rolGuardado = new Rol();
        rolGuardado.setId(3L);
        rolGuardado.setNombre("MODERADOR");
        rolGuardado.setDescripcion("Moderador del sistema");

        when(rolService.guardar(any(Rol.class))).thenReturn(rolGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoRol)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("MODERADOR"));

        verify(rolService, times(1)).guardar(any(Rol.class));
    }

    @Test
    void testActualizar_Existente() throws Exception {
        // Arrange
        Rol rolActualizado = new Rol();
        rolActualizado.setId(1L);
        rolActualizado.setNombre("ADMIN_UPDATED");
        rolActualizado.setDescripcion("Administrador actualizado");

        when(rolService.buscarPorId(1L)).thenReturn(Optional.of(rol1));
        when(rolService.guardar(any(Rol.class))).thenReturn(rolActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/v1/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rolActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("ADMIN_UPDATED"));

        verify(rolService, times(1)).buscarPorId(1L);
        verify(rolService, times(1)).guardar(any(Rol.class));
    }

    @Test
    void testEliminar_Existente() throws Exception {
        // Arrange
        when(rolService.buscarPorId(1L)).thenReturn(Optional.of(rol1));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/roles/1"))
                .andExpect(status().isNoContent());

        verify(rolService, times(1)).buscarPorId(1L);
        verify(rolService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminar_NoExistente() throws Exception {
        // Arrange
        when(rolService.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/roles/99"))
                .andExpect(status().isNotFound());

        verify(rolService, times(1)).buscarPorId(99L);
        verify(rolService, times(0)).eliminar(anyLong());
    }
}