package com.libreriaSanSebastian.userService.repository;

import com.libreriaSanSebastian.userService.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByNombre(String nombre);
    Usuario findByRut(String rut);
}
