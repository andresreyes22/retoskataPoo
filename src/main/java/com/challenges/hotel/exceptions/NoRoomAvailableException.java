package com.challenges.hotel.exceptions;

/**
 * Excepcion lanzada cuando no hay habitaciones disponibles para reservar.
 */
public class NoRoomAvailableException extends Exception {
    
    /**
     * Constructor de la excepcion con un mensaje especifico.
     *
     * @param message Mensaje descriptivo del error.
     */
    public NoRoomAvailableException(String message) {
        super(message);
    }
}
