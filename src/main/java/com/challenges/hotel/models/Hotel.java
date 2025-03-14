package com.challenges.hotel.models;

import com.challenges.hotel.exceptions.NoRoomAvailableException;
import com.challenges.hotel.exceptions.RoomNotFoundException;
import com.challenges.shared.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 * La clase Hotel gestiona las habitaciones y sus estados en la base de datos.
 */
public class Hotel {
    private final MongoCollection<Document> roomsCollection;

    /**
     * Constructor de la clase Hotel.
     * Inicializa la conexión con la colección "rooms" en la base de datos.
     */
    public Hotel() {
        this.roomsCollection = MongoDBConnection.getDatabase().getCollection("rooms");
    }

    /**
     * Agrega una nueva habitación a la base de datos.
     *
     * @param room La habitación a agregar.
     */
    public void addRoom(Room room) {
        roomsCollection.insertOne(room.toDocument());
    }

    /**
     * Reserva una habitación del tipo especificado, marcándola como ocupada.
     *
     * @param type El tipo de habitación a reservar.
     * @return La habitación reservada.
     * @throws NoRoomAvailableException Si no hay habitaciones disponibles del tipo solicitado.
     */
    public Room reserveRoom(Room.RoomType type) throws NoRoomAvailableException {
        Document roomDoc = roomsCollection.findOneAndUpdate(
            Filters.and(
                Filters.eq("type", type.toString()),
                Filters.eq("status", Room.RoomStatus.AVAILABLE.toString())
            ),
            Updates.set("status", Room.RoomStatus.OCCUPIED.toString())
        );

        if (roomDoc == null) {
            throw new NoRoomAvailableException("No " + type + " rooms available");
        }

        return Room.fromDocument(roomDoc);
    }

    /**
     * Libera una habitación específica, marcándola como disponible.
     *
     * @param roomNumber El número de la habitación a liberar.
     * @throws RoomNotFoundException Si la habitación no existe o no está ocupada.
     */
    public void releaseRoom(int roomNumber) throws RoomNotFoundException {
        Document result = roomsCollection.findOneAndUpdate(
            Filters.and(
                Filters.eq("number", roomNumber),
                Filters.eq("status", Room.RoomStatus.OCCUPIED.toString())
            ),
            Updates.set("status", Room.RoomStatus.AVAILABLE.toString())
        );

        if (result == null) {
            throw new RoomNotFoundException("Room " + roomNumber + " not found or not occupied");
        }
    }
    /**
     * Obtiene una lista de todas las habitaciones del hotel.
     * @return Lista de habitaciones.
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        for (Document doc : roomsCollection.find()) {
            rooms.add(Room.fromDocument(doc));
        }
        return rooms;
    }

    /**
     * Obtiene una lista de todas las habitaciones ocupadas.F
     * @return Lista de habitaciones ocupadas.
     */
    public List<Room> getOccupiedRooms() {
        List<Room> occupiedRooms = new ArrayList<>();
        for (Document doc : roomsCollection.find(Filters.eq("status", Room.RoomStatus.OCCUPIED.toString()))) {
            occupiedRooms.add(Room.fromDocument(doc));
        }
        return occupiedRooms;
    }
}
