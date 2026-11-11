package org.eclipse.jakarta.hello.controller;

import org.eclipse.jakarta.hello.entity.Tarea;
import org.eclipse.jakarta.hello.service.TareaService;
import org.eclipse.jakarta.hello.dto.TareaRequestDTO;
import org.eclipse.jakarta.hello.dto.ErrorDTO;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/tareas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TareaController {

    @Inject
    private TareaService tareaService;

    @GET
    public List<Tarea> listar() {
        return tareaService.listar();
    }

    @POST
    public Response crear(TareaRequestDTO datos) {

        if (datos == null) {
            return error(Response.Status.BAD_REQUEST, "VALIDATION_ERROR", "El cuerpo de la solicitud es obligatorio");
        }

        try {
            Tarea nueva = tareaService.crear(datos.getTitulo());
            return Response.status(Response.Status.CREATED).entity(nueva).build();

        } catch (IllegalArgumentException e) {
            return error(Response.Status.BAD_REQUEST, "VALIDATION_ERROR", e.getMessage());
        }
    }

    @PATCH
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, TareaRequestDTO datos) {

        if (datos == null || datos.getCompletada() == null) {
            return error(Response.Status.BAD_REQUEST, "VALIDATION_ERROR", "El campo completada es obligatorio y debe ser booleano");
        }

        try {
            Tarea actualizada = tareaService.actualizar(id, datos.getCompletada());

            if (actualizada == null) {
                return error(Response.Status.NOT_FOUND, "NOT_FOUND", "Tarea no encontrada");
            }

            return Response.ok(actualizada).build();

        } catch (IllegalArgumentException e) {
            return error(Response.Status.BAD_REQUEST, "VALIDATION_ERROR", e.getMessage());
        }
    }

    private Response error(Response.Status estado, String codigo, String mensaje) {

        ErrorDTO error = new ErrorDTO(codigo, mensaje);
        return Response.status(estado).entity(error).type(MediaType.APPLICATION_JSON).build();
    }
}
