package ejercicio4;

import messagepassing.*;

public class Main {
	
	public static final int NUMERO_CLIENTES = 30;
	
	public static final String A = "A";
	public static final String B = "B";
	
	public static void main(String[] args) {
		MailBox[] respuestasClientes = new MailBox[NUMERO_CLIENTES];
		
		MailBox caja, cogerCajaA, cogerCajaB ,liberarCajaA, liberarCajaB, pedirPantalla, liberarPantalla;
		
		for(int i = 0; i < NUMERO_CLIENTES; i++) {
			respuestasClientes[i] = new MailBox();
		}
		
		caja = new MailBox();
		cogerCajaA = new MailBox();
		cogerCajaB = new MailBox();
		liberarCajaA = new MailBox();
		liberarCajaB = new MailBox();
		pedirPantalla = new MailBox();
		liberarPantalla = new MailBox();
		
		Cliente[] clientes = new Cliente[NUMERO_CLIENTES];
		for(int i = 0; i < NUMERO_CLIENTES; i++) {
			clientes[i] = new Cliente(i, caja, cogerCajaA, cogerCajaB ,liberarCajaA, liberarCajaB, pedirPantalla, liberarPantalla, respuestasClientes[i]);
		}
		
		Controlador controlador = new Controlador(respuestasClientes, caja, cogerCajaA, cogerCajaB ,liberarCajaA, liberarCajaB, pedirPantalla, liberarPantalla);
		
		controlador.start();
		
		for(int i = 0; i < NUMERO_CLIENTES; i++) {
			clientes[i].start();
		}
		
		try {
			controlador.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < NUMERO_CLIENTES; i++) {
			try {
				clientes[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("El hilo principal ha terminado");
		
	}
	
}
