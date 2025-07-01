package com.libreriaSanSebastian.userService.assemblers;

import com.libreriaSanSebastian.userService.controller.RolController;
import com.libreriaSanSebastian.userService.model.Rol;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<Rol>> {

    @Override
    public EntityModel<Rol> toModel(Rol rol) {
        EntityModel<Rol> rolModel = EntityModel.of(rol,
                linkTo(methodOn(RolController.class).obtenerPorId(rol.getId())).withSelfRel(),
                linkTo(methodOn(RolController.class).listarTodos()).withRel("roles"));

        // Enlace para actualizar el rol
        rolModel.add(linkTo(methodOn(RolController.class).actualizar(rol.getId(), rol)).withRel("actualizar"));

        // Enlace para eliminar el rol
        rolModel.add(linkTo(methodOn(RolController.class).eliminar(rol.getId())).withRel("eliminar"));

        return rolModel;
    }
}
