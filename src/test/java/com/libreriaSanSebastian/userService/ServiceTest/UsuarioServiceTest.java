package com.libreriaSanSebastian.userService.ServiceTest;

import com.libreriaSanSebastian.userService.service.UsuarioService;
import com.libreriaSanSebastian.userService.model.Rol;
import com.libreriaSanSebastian.userService.model.Usuario;
import com.libreriaSanSebastian.userService.repository.UsuarioRepository;
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

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        rolAdmin = new Rol();
        rolAdmin.setId(1L);
        rolAdmin.setNombre("ADMIN");
        rolAdmin.setDescripcion("Administrador");

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
    void testListarTodos() {
        // Arrange
        List<Usuario> usuariosEsperados = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuariosEsperados);

        // Act
        List<Usuario> usuariosObtenidos = usuarioService.listarTodos();

        // Assert
        assertNotNull(usuariosObtenidos);
        assertEquals(2, usuariosObtenidos.size());
        assertEquals("Juan Pérez", usuariosObtenidos.get(0).getNombre());
        assertEquals("María González", usuariosObtenidos.get(1).getNombre());
        
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_Existente() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarPorId(1L);

        // Assert
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Juan Pérez", usuarioEncontrado.get().getNombre());
        assertEquals("juan@email.com", usuarioEncontrado.get().getEmail());
        
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorNombre_Existente() {
        // Arrange
        when(usuarioRepository.findByNombre("Juan Pérez")).thenReturn(usuario1);

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarPorNombre("Juan Pérez");

        // Assert
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Juan Pérez", usuarioEncontrado.get().getNombre());
        
        verify(usuarioRepository, times(1)).findByNombre("Juan Pérez");
    }

    @Test
    void testBuscarPorRut_Existente() {
        // Arrange
        when(usuarioRepository.findByRut("12345678-9")).thenReturn(usuario1);

        // Act
        Optional<Usuario> usuarioEncontrado = usuarioService.buscarPorRut("12345678-9");

        // Assert
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("12345678-9", usuarioEncontrado.get().getRut());
        
        verify(usuarioRepository, times(1)).findByRut("12345678-9");
    }

    @Test
    void testGuardar() {
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

        when(usuarioRepository.save(nuevoUsuario)).thenReturn(usuarioGuardado);

        // Act
        Usuario resultado = usuarioService.guardar(nuevoUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Pedro Silva", resultado.getNombre());
        
        verify(usuarioRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    void testEliminar() {
        // Arrange
        Long idAEliminar = 1L;

        // Act
        usuarioService.eliminar(idAEliminar);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(idAEliminar);
    }
}