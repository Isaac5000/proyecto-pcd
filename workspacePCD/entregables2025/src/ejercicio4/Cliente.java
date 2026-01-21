package ejercicio4;

import java.util.Random;

import messagepassing.MailBox;

public class Cliente extends Thread {
	
	private static final int EMPEZAR_EN_UNO = 1;
	private static final int TIEMPO_PAGO_MIN = 1;
	private static final int TIEMPO_PAGO_MAX = 11; // Número aleatorio entre 0 y 10

	private static final int PASAR_A_SEGUNDOS = 1000;
	
	// Semaforos para realizar las difirentes acciones disponibles a partir de los mensajes recividos
	// del controlador.
	private MailBox seleccionarCola, cogerColaRapida, cogerColaLenta, liberarColaRapida, liberarColaLenta, cogerPantalla,
			liberarPantalla;
	
	// Este buzón es donde recivira las respuestas el cliente por parte del controlador
	private MailBox buzonCliente; 

	private int idCliente;

	public Cliente(int idCliente, MailBox seleccionarCola, MailBox cogerColaRapida, MailBox cogerColaLenta,
			MailBox liberarColaRapida, MailBox liberarColaLenta, MailBox cogerPantalla, MailBox liberarPantalla,
			MailBox buzonCliente) {

		this.idCliente = idCliente;
		this.seleccionarCola = seleccionarCola;
		this.cogerColaRapida = cogerColaRapida;
		this.cogerColaLenta = cogerColaLenta;
		this.liberarColaRapida = liberarColaRapida;
		this.liberarColaLenta = liberarColaLenta;
		this.cogerPantalla = cogerPantalla;
		this.liberarPantalla = liberarPantalla;
		this.buzonCliente = buzonCliente;

	}

	@Override
	public void run() {
		for (int i = 0; i < Main.NUM_COMPRAS_POR_CLIENTE; i++) {
			// Este es el tiempo aleatorio entre 1-10
			int tiempoDePagoT = TIEMPO_PAGO_MIN + new Random().nextInt(TIEMPO_PAGO_MAX - TIEMPO_PAGO_MIN);
			
			// Este es el sleep de tiempoDePagoT en segundos
			try {
				Thread.sleep(tiempoDePagoT * PASAR_A_SEGUNDOS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Se pide una cola al controlador
			String idClienteYTiempo = new String(this.idCliente + Main.SEPARADOR + tiempoDePagoT);
			seleccionarCola.send(idClienteYTiempo);
			
			String colaRecivida = (String) buzonCliente.receive();
			
			// Se selecciona la cola por medio del controlador
			String idClienteString = new String(this.idCliente + "");
			if (colaRecivida.equals(Main.COLA_RAPIDA)) {
				cogerColaRapida.send(idClienteString);
				buzonCliente.receive();

				liberarColaRapida.send(idClienteString);
			} else {
				cogerColaLenta.send(idClienteString);
				buzonCliente.receive();

				liberarColaLenta.send(idClienteString);
			}
			
			// Se pide la pantalla
			cogerPantalla.send(idClienteString);
			buzonCliente.receive();
			
			int idClienteMas = idCliente + EMPEZAR_EN_UNO;
			System.out.println(
					"--------------------------------------------------------------\n" 
					+ "“Persona  " + idClienteMas + " ha usado la cola " + colaRecivida + "\n" 
					+ "Tiempo de pago= " + tiempoDePagoT + "\n"
					+ "Thread.sleep(" + (tiempoDePagoT * PASAR_A_SEGUNDOS) + ")\n" 
					+ "Persona " + idClienteMas + " liberando la cola " + colaRecivida + "” \n"
					+ "--------------------------------------------------------------");
			
			// Se deja libre la pantalla
			liberarPantalla.send(idClienteString);
		}
	}

}
