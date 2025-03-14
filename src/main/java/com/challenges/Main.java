package com.challenges;

import com.challenges.hotel.models.*;
import com.challenges.hotel.exceptions.*;
import com.challenges.tasks.models.*;
import com.challenges.shared.MongoDBConnection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();
        TaskManager taskManager = new TaskManager();

        try {
            // =============================
            // GESTION DE HABITACIONES
            // =============================
            System.out.println("Gestion de Habitaciones");

            // Anadir habitaciones manualmente
            while (true) {
                int roomNumber;
                while (true) {
                    System.out.print("Ingrese numero de habitacion (o 0 para terminar): ");
                    if (scanner.hasNextInt()) {
                        roomNumber = scanner.nextInt();
                        scanner.nextLine();
                        if (roomNumber >= 0) {
                            break;
                        } else {
                            System.out.println("Error: El numero de habitacion no puede ser negativo.");
                        }
                    } else {
                        System.out.println("Error: Entrada invalida. Debe ingresar un numero.");
                        scanner.next(); 
                    }
                }

                if (roomNumber == 0) break;

                int roomTypeOption;
                while (true) {
                    System.out.println("Seleccione tipo de habitacion: 1. SINGLE, 2. DOUBLE, 3. SUITE");
                    System.out.print("Opcion: ");
                    if (scanner.hasNextInt()) {
                        roomTypeOption = scanner.nextInt();
                        scanner.nextLine();
                        if (roomTypeOption >= 1 && roomTypeOption <= 3) {
                            break;
                        } else {
                            System.out.println("Error: Opcion fuera de rango. Ingrese un numero entre 1 y 3.");
                        }
                    } else {
                        System.out.println("Error: Entrada invalida. Debe ingresar un numero.");
                        scanner.next();
                    }
                }

                Room.RoomType roomType = switch (roomTypeOption) {
                    case 1 -> Room.RoomType.SINGLE;
                    case 2 -> Room.RoomType.DOUBLE;
                    case 3 -> Room.RoomType.SUITE;
                    default -> throw new IllegalArgumentException("Tipo invalido");
                };

                hotel.addRoom(new Room(roomNumber, roomType));
                System.out.println("Habitacion anadida con exito.");
            }

            // Reservar habitacion
            System.out.println("\nReservar una habitacion");
            int roomTypeOption;
            while (true) {
                System.out.println("Seleccione tipo de habitacion: 1. SINGLE, 2. DOUBLE, 3. SUITE");
                System.out.print("Opcion: ");
                if (scanner.hasNextInt()) {
                    roomTypeOption = scanner.nextInt();
                    scanner.nextLine();
                    if (roomTypeOption >= 1 && roomTypeOption <= 3) {
                        break;
                    } else {
                        System.out.println("Error: Opcion fuera de rango. Ingrese un numero entre 1 y 3.");
                    }
                } else {
                    System.out.println("Error: Entrada invalida. Debe ingresar un numero.");
                    scanner.next();
                }
            }

            Room.RoomType roomType = switch (roomTypeOption) {
                case 1 -> Room.RoomType.SINGLE;
                case 2 -> Room.RoomType.DOUBLE;
                case 3 -> Room.RoomType.SUITE;
                default -> throw new IllegalArgumentException("Tipo invalido");
            };

            try {
                Room reserved = hotel.reserveRoom(roomType);
                System.out.println("Reservaste la habitacion: " + reserved.getNumber());
            } catch (NoRoomAvailableException e) {
                System.out.println("Error: " + e.getMessage());
            }

            // =============================
            // GESTION DE TAREAS
            // =============================
            System.out.println("\nGestion de Tareas");

            // Agregar tareas manualmente
            while (true) {
                System.out.print("Ingrese la descripcion de la tarea (o 'salir' para terminar): ");
                String description = scanner.nextLine();
                if (description.equalsIgnoreCase("salir")) break;

                System.out.println("Seleccione prioridad: 1. ALTA, 2. MEDIA, 3. BAJA");
                System.out.print("Opcion: ");
                int priorityOption = scanner.nextInt();
                scanner.nextLine();

                Task.Priority priority = switch (priorityOption) {
                    case 1 -> Task.Priority.HIGH;
                    case 2 -> Task.Priority.MEDIUM;
                    case 3 -> Task.Priority.LOW;
                    default -> throw new IllegalArgumentException("Prioridad invalida");
                };

                Task task = new Task(description, priority);
                taskManager.addTask(task);
                System.out.println("Tarea anadida con exito.");
            }

            // Marcar tareas como completadas
            System.out.println("\nTareas actuales:");
            for (Task task : taskManager.getTasksByPriority()) {
                System.out.printf("ID: %s | Descripcion: %s | Prioridad: %s | Estado: %s%n",
                        task.getId(), task.getDescription(), task.getPriority(), task.getStatus());
            }

            System.out.print("Ingrese ID de la tarea a completar (o 'salir' para terminar): ");
            while (scanner.hasNext()) {
                String taskId = scanner.next();
                scanner.nextLine();

                if (taskId.equalsIgnoreCase("salir")) {
                    break;
                }

                Task task = taskManager.getTaskById(taskId);
                if (task == null) {
                    System.out.println("Error: ID de tarea no encontrado. Intente nuevamente:");
                    continue;
                }

                if (task.getStatus() == Task.Status.COMPLETED) {
                    System.out.println("La tarea ya esta completada. Seleccione otra (o 'salir' para terminar):");
                    continue;
                }

                taskManager.markTaskAsCompleted(taskId);
                System.out.println("Tarea marcada como completada... Ingrese ID otra tarea a completar (o 'salir' para terminar)");
            }

            // Preguntar al usuario si desea eliminar las tareas completadas
            System.out.print("\nDesea eliminar todas las tareas completadas? (si/no): ");
            String deleteOption = scanner.nextLine().trim().toLowerCase();
            if (deleteOption.equals("si")) {
                taskManager.deleteCompletedTasks();
                System.out.println("Tareas completadas eliminadas.");
            } else {
                System.out.println("Las tareas completadas no fueron eliminadas.");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            MongoDBConnection.close();
        }
    }
}
