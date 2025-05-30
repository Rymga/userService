package com.libreriaSanSebastian.userService.controller;


import com.libreriaSanSebastian.userService.model.Rol;
import com.libreriaSanSebastian.userService.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public List<Rol> listarTodos() {
        return rolService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerPorId(@PathVariable Long id) {
        return rolService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Rol> obtenerPorNombre(@PathVariable String nombre) {
        return rolService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rol> crear(@RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.guardar(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizar(@PathVariable Long id, @RequestBody Rol rol) {
        return rolService.buscarPorId(id)
                .map(existente -> {
                    rol.setId(id);
                    return ResponseEntity.ok(rolService.guardar(rol));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (rolService.buscarPorId(id).isPresent()) {
            rolService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}