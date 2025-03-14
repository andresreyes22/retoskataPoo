package com.challenges.hotel.exceptions;

/**
 * Excepcion lanzada cuando no se encuentra una habitacion especifica en el sistema.
 */
public class RoomNotFoundException extends Exception {

    /**
     * Constructor de la excepcion con un mensaje especifico.
     *
     * @param message Mensaje descriptivo del error.
     */
    public RoomNotFoundException(String message) {
        super(message);
    }
}
