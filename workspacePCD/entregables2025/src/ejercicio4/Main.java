package ejercicio4;

import messagepassing.*;

public class Main {
	
	public static final int NUM_CLIENTES = 50;
	public static final int NUM_COMPRAS_POR_CLIENTE = 5;
	public static final int NUM_SENDS_POR_CLIENTE = 5;
	
	public static final String SEPARADOR = "\n";
	public static final String COLA_RAPIDA = "R";
	public static final String COLA_LENTA = "L";
	
	public static Cliente[] clientes = new Cliente[NUM_CLIENTES];
	public static Controlador controlador;
	
	public static void main(String[] args) {
		MailBox[] respuestasClientes = new MailBox[NUM_CLIENTES]; // Hace falta un buzón para cada cliente, para dar una respuesta del controlador
		
		MailBox seleccionarCola, cogerColaRapida, cogerColaLenta, liberarColaRapida, liberarColaLenta, cogerPantalla,
		liberarPantalla; // Es necesario un buzón distinto para indicar la acción que se quiere realizar
		
		seleccionarCola = new MailBox();
		cogerColaRapida = new MailBox();
		cogerColaLenta = new MailBox();
		liberarColaRapida = new MailBox();
		liberarColaLenta = new MailBox();
		cogerPantalla = new MailBox();
		liberarPantalla = new MailBox();
		
		// Se inicializan los buzones de los clientes y los clientes
		for(int i = 0; i < NUM_CLIENTES; i++) {
			respuestasClientes[i] = new MailBox();
			clientes[i] = new Cliente(i, seleccionarCola, cogerColaRapida, cogerColaLenta, liberarColaRapida, liberarColaLenta, cogerPantalla,
					liberarPantalla, respuestasClientes[i]);
		}
		
		controlador = new Controlador(respuestasClientes, seleccionarCola, cogerColaRapida, cogerColaLenta, liberarColaRapida, liberarColaLenta, cogerPantalla,
				liberarPantalla);
		
		controlador.start();
		
		for(int i = 0; i < NUM_CLIENTES; i++) {
			clientes[i].start();
		}
		
		for(int i = 0; i < NUM_CLIENTES; i++) {
			try {
				clientes[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			controlador.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
