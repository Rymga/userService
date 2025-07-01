package com.libreriaSanSebastian.userService.assemblers;

import com.libreriaSanSebastian.userService.controller.UsuarioController;
import com.libreriaSanSebastian.userService.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        EntityModel<Usuario> usuarioModel = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("usuarios"));

        // Enlace para actualizar usuario
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).actualizar(usuario.getId(), usuario)).withRel("actualizar"));

        // Enlace para eliminar usuario
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).eliminar(usuario.getId())).withRel("eliminar"));

        return usuarioModel;
    }
}
