package com.libreriaSanSebastian.userService.service;

import com.libreriaSanSebastian.userService.model.Usuario;
import com.libreriaSanSebastian.userService.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorNombre(String nombre) {
        return Optional.ofNullable(usuarioRepository.findByNombre(nombre));
    }

    public Optional<Usuario> buscarPorRut(String rut) {
        return Optional.ofNullable(usuarioRepository.findByRut(rut));
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
