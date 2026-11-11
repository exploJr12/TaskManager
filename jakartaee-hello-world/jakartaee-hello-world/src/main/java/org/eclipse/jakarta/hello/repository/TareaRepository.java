package org.eclipse.jakarta.hello.repository;

import org.eclipse.jakarta.hello.entity.Tarea;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TareaRepository {

    @PersistenceContext(unitName = "TareasPU")
    private EntityManager entityManager;

    // Obtener todas las tareas
    public List<Tarea> listar() {
        return entityManager
                .createQuery("SELECT t FROM Tarea t ORDER BY t.id", Tarea.class)
                .getResultList();
    }

    // Buscar una tarea por ID
    public Tarea buscarPorId(Long id) {
        return entityManager.find(Tarea.class, id);
    }

    // Guardar una tarea
    @Transactional
    public Tarea guardar(Tarea tarea) {
        entityManager.persist(tarea);
        return tarea;
    }

    // Actualizar una tarea
    @Transactional
    public Tarea actualizar(Tarea tarea) {
        return entityManager.merge(tarea);
    }
}
