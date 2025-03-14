package com.challenges.tasks.models;

import org.bson.Document;

/**
 * Representa una tarea con una descripcion, prioridad y estado.
 */
public class Task {
    private String id;
    private String description;
    private Priority priority;
    private Status status;

    /**
     * Constructor que inicializa una tarea con una descripcion y prioridad especifica.
     * El estado inicial de la tarea es "PENDING".
     *
     * @param description Descripcion de la tarea.
     * @param priority Prioridad de la tarea (HIGH, MEDIUM, LOW).
     */
    public Task(String description, Priority priority) {
        this.description = description;
        this.priority = priority;
        this.status = Status.PENDING;
    }

    /**
     * Obtiene el ID de la tarea.
     *
     * @return ID de la tarea.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID de la tarea.
     *
     * @param id ID unico de la tarea.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene la descripcion de la tarea.
     *
     * @return Descripcion de la tarea.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtiene la prioridad de la tarea.
     *
     * @return Prioridad de la tarea (HIGH, MEDIUM, LOW).
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Obtiene el estado actual de la tarea.
     *
     * @return Estado de la tarea (PENDING o COMPLETED).
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Establece el estado de la tarea.
     *
     * @param status Nuevo estado de la tarea.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Convierte la tarea en un documento BSON para su almacenamiento en MongoDB.
     *
     * @return Documento BSON con los datos de la tarea.
     */
    public Document toDocument() {
        return new Document()
                .append("description", description)
                .append("priority", priority.toString())
                .append("status", status.toString());
    }

    /**
     * Crea una instancia de Task a partir de un documento BSON.
     *
     * @param doc Documento BSON que contiene los datos de la tarea.
     * @return Objeto Task creado a partir del documento.
     */
    public static Task fromDocument(Document doc) {
        Task task = new Task(
            doc.getString("description"),
            Priority.valueOf(doc.getString("priority"))
        );
        task.setId(doc.getObjectId("_id").toString());
        task.setStatus(Status.valueOf(doc.getString("status")));
        return task;
    }

    /**
     * Enumeracion que define los niveles de prioridad de una tarea.
     */
    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    /**
     * Enumeracion que define los posibles estados de una tarea.
     */
    public enum Status {
        PENDING, COMPLETED
    }
}
