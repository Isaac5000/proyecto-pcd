package ejercicio4;

import java.util.Random;

import messagepassing.MailBox;

public class Cliente extends Thread {
	
	public static final int NUMERO_SENDS = 5;
	
	public static final int NUMERO_PAGOS = 5;
	
	private static final int TIEMPO_PAGO_MIN = 1;
	private static final int TIEMPO_PAGO_MAX = 11; // Número aleatorio entre 0 y 10
	
	private static final int A_MILISEGUNDOS = 1000;
	
	private MailBox caja, cogerCajaA, cogerCajaB ,liberarCajaA, liberarCajaB, pedirPantalla, liberarPantalla, buzonCliente;
	
	private int idc;
	
	public Cliente(int idc, MailBox caja, MailBox cogerCajaA, MailBox cogerCajaB , MailBox liberarCajaA, MailBox liberarCajaB, MailBox pedirPantalla, MailBox liberarPantalla, MailBox buzonCliente) {
		this.idc = idc; // id para el cliente
		this.caja = caja;
		this.cogerCajaA = cogerCajaA;
		this.cogerCajaB = cogerCajaB;
		this.liberarCajaA = liberarCajaA;
		this.liberarCajaB = liberarCajaB;
		this.pedirPantalla = pedirPantalla;
		this.liberarPantalla = liberarPantalla;
		this.buzonCliente = buzonCliente;
		
	}
	
	
	@Override
	public void run() {
		
		for(int i = 0; i < NUMERO_PAGOS; i++) {
			int tiempo = TIEMPO_PAGO_MIN + new Random().nextInt(TIEMPO_PAGO_MAX - TIEMPO_PAGO_MIN);
			try {
				Thread.sleep(tiempo * A_MILISEGUNDOS);
				// Enviar buzón con la id y el tiempo
				String enviar = new String(idc + "," + tiempo);
				
				caja.send(enviar);
				
				// Esperar la respuesta
				String cajaTocada = (String) buzonCliente.receive();
				
				if(cajaTocada.equals(Main.A)) {
					// Enviar un buzón para colocarnos en la caja A
					cogerCajaA.send(new String(idc + ""));
					buzonCliente.receive();
					
					liberarCajaA.send(new String(""));
				}
				else {
					cogerCajaB.send(new String(idc + ""));
					buzonCliente.receive();
					
					liberarCajaB.send(new String(""));
				}
				
				pedirPantalla.send(new String(idc + ""));
				buzonCliente.receive();
				
				System.out.println("--------------------------------------------------------------\n"
						+ "Persona " + idc + " ha usado la caja " + cajaTocada
						+ "\nTiempo de pago=" + tiempo + "\nThread.sleep("+tiempo+")"+
						"\nPersona " + idc + " liberando la caja " + cajaTocada 
						+ "\n--------------------------------------------------------------");
				
				liberarPantalla.send(new String(""));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
