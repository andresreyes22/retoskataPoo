package com.challenges.tasks.models;

import com.challenges.shared.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar las tareas en la base de datos MongoDB.
 */
public class TaskManager {
    private final MongoCollection<Document> tasksCollection;

    /**
     * Constructor que inicializa la coleccion de tareas en MongoDB.
     */
    public TaskManager() {
        this.tasksCollection = MongoDBConnection.getDatabase().getCollection("tasks");
    }

    /**
     * Agrega una nueva tarea a la base de datos.
     *
     * @param task Objeto Task que se desea almacenar.
     */
    public void addTask(Task task) {
        Document doc = task.toDocument();
        tasksCollection.insertOne(doc);
        task.setId(doc.getObjectId("_id").toString());
    }

    /**
     * Marca una tarea como completada en la base de datos.
     *
     * @param taskId ID de la tarea que se desea marcar como completada.
     */
    public void markTaskAsCompleted(String taskId) {
        tasksCollection.updateOne(
            Filters.eq("_id", new ObjectId(taskId)),
            new Document("$set", new Document("status", Task.Status.COMPLETED.toString()))
        );
    }

    /**
     * Elimina todas las tareas que tengan el estado "COMPLETED" de la base de datos.
     */
    public void deleteCompletedTasks() {
        tasksCollection.deleteMany(
            Filters.eq("status", Task.Status.COMPLETED.toString())
        );
    }

    /**
     * Obtiene una lista de tareas ordenadas por prioridad.
     *
     * @return Lista de tareas ordenadas por prioridad (de mayor a menor).
     */
    public List<Task> getTasksByPriority() {
        List<Task> tasks = new ArrayList<>();
        tasksCollection.find()
            .sort(new Document("priority", 1))
            .forEach(doc -> tasks.add(Task.fromDocument(doc)));
        return tasks;
    }
    
    /**
     * Obtiene una tarea especifica por su ID.
     *
     * @param taskId ID de la tarea a buscar.
     * @return Objeto Task si se encuentra, o null si no existe.
     */
    public Task getTaskById(String taskId) {
        try {
            Document doc = tasksCollection.find(Filters.eq("_id", new ObjectId(taskId))).first();
            return (doc != null) ? Task.fromDocument(doc) : null;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: ID de tarea invalido.");
            return null;
        }
    }
}
