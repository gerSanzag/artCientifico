package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Vista para interactuar con el usuario para la creación de artículos científicos
 */
public class ArtCientificoView {
    
    private final Scanner scanner;
    
    public ArtCientificoView() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Método para mostrar un mensaje al usuario
     * @param mensaje mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
    
    /**
     * Método para solicitar un dato al usuario
     * @param mensaje mensaje de solicitud
     * @param obligatorio si el campo es obligatorio
     * @return valor ingresado por el usuario o null si no es obligatorio y no ingresó nada
     */
    public String solicitarDato(String mensaje, boolean obligatorio) {
        String valor = null;
        boolean valido = false;
        
        while (!valido) {
            System.out.println(mensaje + (obligatorio ? " (obligatorio)" : " (opcional, presione Enter para omitir)"));
            valor = scanner.nextLine().trim();
            
            if (valor.isEmpty() && obligatorio) {
                System.out.println("Este campo es obligatorio, por favor ingrese un valor.");
            } else {
                valido = true;
                if (valor.isEmpty() && !obligatorio) {
                    valor = null;
                }
            }
        }
        
        return valor;
    }
    
    /**
     * Método para solicitar un entero al usuario
     * @param mensaje mensaje de solicitud
     * @param obligatorio si el campo es obligatorio
     * @return valor ingresado por el usuario o null si no es obligatorio y no ingresó nada
     */
    public Integer solicitarEntero(String mensaje, boolean obligatorio) {
        while (true) {
            String entrada = solicitarDato(mensaje, obligatorio);
            
            if (entrada == null && !obligatorio) {
                return null;
            }
            
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
    }
    
    /**
     * Método para solicitar una lista de palabras clave al usuario
     * @return lista de palabras clave
     */
    public List<String> solicitarPalabrasClaves() {
        List<String> palabrasClaves = new ArrayList<>();
        
        while (true) {
            String palabra = solicitarDato("Ingrese una palabra clave (o presione Enter para terminar)", false);
            if (palabra == null) {
                break;
            }
            palabrasClaves.add(palabra);
        }
        
        return palabrasClaves;
    }
    
    /**
     * Método para preguntar si se desea continuar
     * @return true si se desea continuar, false en caso contrario
     */
    public boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = solicitarDato(mensaje + " (s/n)", true);
            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            } else if (respuesta.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Por favor ingrese 's' para sí o 'n' para no.");
            }
        }
    }
    
    /**
     * Cerrar el scanner
     */
    public void cerrar() {
        scanner.close();
    }
} 