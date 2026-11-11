package org.eclipse.jakarta.hello.service;

import org.eclipse.jakarta.hello.entity.Tarea;
import org.eclipse.jakarta.hello.repository.TareaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TareaService {

    @Inject
    private TareaRepository tareaRepository;

    public List<Tarea> listar() {
        return tareaRepository.listar();
    }

    public Tarea buscarPorId(Long id) {
        return tareaRepository.buscarPorId(id);
    }

    @Transactional
    public Tarea crear(String titulo) {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }

        String tituloLimpio = titulo.trim();

        if (tituloLimpio.length() > 150) {
            throw new IllegalArgumentException("El título no puede superar los 150 caracteres");
        }

        Tarea tarea = new Tarea(tituloLimpio);
        return tareaRepository.guardar(tarea);
    }

    @Transactional
    public Tarea actualizar(Long id, Boolean completada) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }

        if (completada == null) {
            throw new IllegalArgumentException(
                    "El campo completada es obligatorio y debe ser booleano");
        }

        Tarea tarea = tareaRepository.buscarPorId(id);

        if (tarea == null) {
            return null;
        }

        tarea.setCompletada(completada);
        return tareaRepository.actualizar(tarea);
    }
}
