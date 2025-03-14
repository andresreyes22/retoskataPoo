package com.challenges.shared;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Clase para gestionar la conexion con la base de datos MongoDB.
 */
public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    /**
     * Obtiene la instancia de la base de datos. Si la conexion no existe, la crea.
     *
     * @return Instancia de {@link MongoDatabase}.
     */
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("mongodb+srv://andres1701512948:Q2MtS3mQei9x1vra@cluster0.sfbq7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
            database = mongoClient.getDatabase("challenges");
        }
        return database;
    }

    /**
     * Cierra la conexion con la base de datos.
     */
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}
