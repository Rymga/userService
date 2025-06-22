package com.libreriaSanSebastian.userService.ServiceTest;

import com.libreriaSanSebastian.userService.service.RolService;
import com.libreriaSanSebastian.userService.model.Rol;
import com.libreriaSanSebastian.userService.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Rol rol1;
    private Rol rol2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rol1 = new Rol();
        rol1.setId(1L);
        rol1.setNombre("ADMIN");
        rol1.setDescripcion("Administrador del sistema");

        rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("USER");
        rol2.setDescripcion("Usuario estándar");
    }

    @Test
    void testListarTodos() {
        // Arrange
        List<Rol> rolesEsperados = Arrays.asList(rol1, rol2);
        when(rolRepository.findAll()).thenReturn(rolesEsperados);

        // Act
        List<Rol> rolesObtenidos = rolService.listarTodos();

        // Assert
        assertNotNull(rolesObtenidos);
        assertEquals(2, rolesObtenidos.size());
        assertEquals("ADMIN", rolesObtenidos.get(0).getNombre());
        assertEquals("USER", rolesObtenidos.get(1).getNombre());

        verify(rolRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_Existente() {
        // Arrange
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol1));

        // Act
        Optional<Rol> rolEncontrado = rolService.buscarPorId(1L);

        // Assert
        assertTrue(rolEncontrado.isPresent());
        assertEquals("ADMIN", rolEncontrado.get().getNombre());
        assertEquals("Administrador del sistema", rolEncontrado.get().getDescripcion());

        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_NoExistente() {
        // Arrange
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Rol> rolEncontrado = rolService.buscarPorId(99L);

        // Assert
        assertFalse(rolEncontrado.isPresent());

        verify(rolRepository, times(1)).findById(99L);
    }

    @Test
    void testBuscarPorNombre_Existente() {
        // Arrange
        when(rolRepository.findByNombre("ADMIN")).thenReturn(rol1);

        // Act
        Optional<Rol> rolEncontrado = rolService.buscarPorNombre("ADMIN");

        // Assert
        assertTrue(rolEncontrado.isPresent());
        assertEquals("ADMIN", rolEncontrado.get().getNombre());

        verify(rolRepository, times(1)).findByNombre("ADMIN");
    }

    @Test
    void testBuscarPorNombre_NoExistente() {
        // Arrange
        when(rolRepository.findByNombre("NOEXISTE")).thenReturn(null);

        // Act
        Optional<Rol> rolEncontrado = rolService.buscarPorNombre("NOEXISTE");

        // Assert
        assertFalse(rolEncontrado.isPresent());

        verify(rolRepository, times(1)).findByNombre("NOEXISTE");
    }

    @Test
    void testGuardar() {
        // Arrange
        Rol nuevoRol = new Rol();
        nuevoRol.setNombre("MODERADOR");
        nuevoRol.setDescripcion("Moderador del sistema");

        Rol rolGuardado = new Rol();
        rolGuardado.setId(3L);
        rolGuardado.setNombre("MODERADOR");
        rolGuardado.setDescripcion("Moderador del sistema");

        when(rolRepository.save(nuevoRol)).thenReturn(rolGuardado);

        // Act
        Rol resultado = rolService.guardar(nuevoRol);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("MODERADOR", resultado.getNombre());

        verify(rolRepository, times(1)).save(nuevoRol);
    }

    @Test
    void testEliminar() {
        // Arrange
        Long idAEliminar = 1L;

        // Act
        rolService.eliminar(idAEliminar);

        // Assert
        verify(rolRepository, times(1)).deleteById(idAEliminar);
    }
}