package com.helpdeskpro.helpdesk_pro.controlador;

import com.helpdeskpro.helpdesk_pro.dto.CatalogoResponseDTO;
import com.helpdeskpro.helpdesk_pro.repositorio.CategoriasRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.DepartamentosRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.EstadosRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.PrioridadesRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.RolesRepositorio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoControlador {
    private final RolesRepositorio rolesRepositorio;
    private final DepartamentosRepositorio departamentosRepositorio;
    private final CategoriasRepositorio categoriasRepositorio;
    private final PrioridadesRepositorio prioridadesRepositorio;
    private final EstadosRepositorio estadosRepositorio;

    public CatalogoControlador(
            RolesRepositorio rolesRepositorio,
            DepartamentosRepositorio departamentosRepositorio,
            CategoriasRepositorio categoriasRepositorio,
            PrioridadesRepositorio prioridadesRepositorio,
            EstadosRepositorio estadosRepositorio) {
        this.rolesRepositorio = rolesRepositorio;
        this.departamentosRepositorio = departamentosRepositorio;
        this.categoriasRepositorio = categoriasRepositorio;
        this.prioridadesRepositorio = prioridadesRepositorio;
        this.estadosRepositorio = estadosRepositorio;
    }

    @GetMapping("/roles")
    public List<CatalogoResponseDTO> roles() {
        return rolesRepositorio.findAll().stream()
                .map(item -> new CatalogoResponseDTO(item.getId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/departamentos")
    public List<CatalogoResponseDTO> departamentos() {
        return departamentosRepositorio.findAll().stream()
                .map(item -> new CatalogoResponseDTO(item.getId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/categorias")
    public List<CatalogoResponseDTO> categorias() {
        return categoriasRepositorio.findAll().stream()
                .map(item -> new CatalogoResponseDTO(item.getId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/prioridades")
    public List<CatalogoResponseDTO> prioridades() {
        return prioridadesRepositorio.findAll().stream()
                .map(item -> new CatalogoResponseDTO(item.getId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/estados")
    public List<CatalogoResponseDTO> estados() {
        return estadosRepositorio.findAll().stream()
                .map(item -> new CatalogoResponseDTO(item.getId(), item.getNombre()))
                .toList();
    }
}
