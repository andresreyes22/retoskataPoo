package com.challenges.hotel.models;

import org.bson.Document;

/**
 * Representa una habitacion en el hotel con un numero, tipo y estado.
 */
public class Room {
    private String id;
    private int number;
    private RoomType type;
    private RoomStatus status;

    /**
     * Constructor que inicializa una habitacion con un numero y tipo especifico.
     * El estado inicial de la habitacion es "AVAILABLE".
     *
     * @param number Numero de la habitacion.
     * @param type Tipo de habitacion (SINGLE, DOUBLE, SUITE).
     */
    public Room(int number, RoomType type) {
        this.number = number;
        this.type = type;
        this.status = RoomStatus.AVAILABLE;
    }

    /**
     * Obtiene el ID de la habitacion.
     *
     * @return ID de la habitacion.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID de la habitacion.
     *
     * @param id ID unico de la habitacion.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el numero de la habitacion.
     *
     * @return Numero de la habitacion.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Obtiene el tipo de la habitacion.
     *
     * @return Tipo de habitacion (SINGLE, DOUBLE, SUITE).
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Obtiene el estado actual de la habitacion.
     *
     * @return Estado de la habitacion (AVAILABLE u OCCUPIED).
     */
    public RoomStatus getStatus() {
        return status;
    }

    /**
     * Establece el estado de la habitacion.
     *
     * @param status Nuevo estado de la habitacion.
     */
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    /**
     * Convierte la habitacion en un documento BSON para su almacenamiento en MongoDB.
     *
     * @return Documento BSON con los datos de la habitacion.
     */
    public Document toDocument() {
        return new Document()
                .append("number", number)
                .append("type", type.toString())
                .append("status", status.toString());
    }

    /**
     * Crea una instancia de Room a partir de un documento BSON.
     *
     * @param doc Documento BSON que contiene los datos de la habitacion.
     * @return Objeto Room creado a partir del documento.
     */
    public static Room fromDocument(Document doc) {
        Room room = new Room(
            doc.getInteger("number"),
            RoomType.valueOf(doc.getString("type"))
        );
        room.setId(doc.getObjectId("_id").toString());
        room.setStatus(RoomStatus.valueOf(doc.getString("status")));
        return room;
    }

    /**
     * Enumeracion que define los tipos de habitacion disponibles.
     */
    public enum RoomType {
        SINGLE, DOUBLE, SUITE
    }

    /**
     * Enumeracion que define los posibles estados de una habitacion.
     */
    public enum RoomStatus {
        AVAILABLE, OCCUPIED
    }
}
