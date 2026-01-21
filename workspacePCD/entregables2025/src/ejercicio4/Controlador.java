package ejercicio4;

import messagepassing.*;

import messagepassing.MailBox;

public class Controlador extends Thread {
	
	public static final int FIN_FOR = Main.NUM_COMPRAS_POR_CLIENTE * Main.NUM_SENDS_POR_CLIENTE * Main.NUM_CLIENTES;
	public static final int VALOR_COLA_RAPIDA_MAX = 5;
	public static final String CADENA_VACIA = "";
	
	// Array con los buzones de todos los clientes
	private MailBox[] respuestasClientes;
	
	// Buzones para realizar las diferentes acciones de los clientes
	private MailBox seleccionarCola, cogerColaRapida, cogerColaLenta, liberarColaRapida, liberarColaLenta, cogerPantalla,
			liberarPantalla;
	
	// Booleanos para las guardas que sirven para entrar en las diferentes colas y en la pantalla
	// por medio del selector
	private boolean libreColaRapida = true;
	private boolean libreColaLenta = true;
	private boolean librePantalla = true;

	private Selector s;

	public Controlador(MailBox[] respuestasClientes, MailBox seleccionarCola, MailBox cogerColaRapida, MailBox cogerColaLenta,
			MailBox liberarColaRapida, MailBox liberarColaLenta, MailBox cogerPantalla, MailBox liberarPantalla) {
		
		this.respuestasClientes = respuestasClientes;
		this.seleccionarCola = seleccionarCola;
		this.cogerColaRapida = cogerColaRapida;
		this.cogerColaLenta = cogerColaLenta;
		this.liberarColaRapida = liberarColaRapida;
		this.liberarColaLenta = liberarColaLenta;
		this.cogerPantalla = cogerPantalla;
		this.liberarPantalla = liberarPantalla;

		s = new Selector();

		s.addSelectable(seleccionarCola, false);
		s.addSelectable(cogerColaRapida, false);
		s.addSelectable(cogerColaLenta, false);
		s.addSelectable(liberarColaRapida, false);
		s.addSelectable(liberarColaLenta, false);
		s.addSelectable(cogerPantalla, false);
		s.addSelectable(liberarPantalla, false);

		cogerColaRapida.setGuardValue(libreColaRapida == true);
		cogerColaLenta.setGuardValue(libreColaLenta == true);
		cogerPantalla.setGuardValue(librePantalla == true);

	}

	@Override
	public void run() {

		for (int i = 0; i < FIN_FOR; i++) {
			switch (s.selectOrBlock()) {
			case 1: // Coger una cola, para ello se obtine id del cliente y su tiempo para mandarle a que cola debe ir 
				String idClienteYTiempoString = (String) seleccionarCola.receive();
				String idClienteYTiempo[] = idClienteYTiempoString.split(Main.SEPARADOR);
				
				int idCliente = Integer.parseInt(idClienteYTiempo[0]);
				int tiempo = Integer.parseInt(idClienteYTiempo[1]);
				String colaElegida = ((tiempo < VALOR_COLA_RAPIDA_MAX)? Main.COLA_RAPIDA : Main.COLA_LENTA);
				
				respuestasClientes[idCliente].send(colaElegida);
				break;
			case 2: // Coger cola rápida, el cliente irá a esta cola si su tiempo es <=5
				idCliente = Integer.parseInt((String)cogerColaRapida.receive());
				libreColaRapida = false;
				
				respuestasClientes[idCliente].send(CADENA_VACIA);
				break;
			case 3: // Coger cola lenta, el cliente irá a esta cola si su tiepo es >5
				idCliente = Integer.parseInt((String)cogerColaLenta.receive());
				libreColaLenta = false;
				
				respuestasClientes[idCliente].send(CADENA_VACIA);
				break;
			case 4: // Dejar cola rápida, esta obción es para abandonar la cola rápida
				liberarColaRapida.receive();
				libreColaRapida = true;
				break;
			case 5: // Dejar cola lenta, esta obción es para abandonar la cola lenta
				liberarColaLenta.receive();
				libreColaLenta = true;
				break;
			case 6: // Pedir pantalla, esta obción permite poseer la pantalla sin que nadie interfiera
				idCliente = Integer.parseInt((String)cogerPantalla.receive());
				librePantalla = false;
				
				respuestasClientes[idCliente].send(CADENA_VACIA);
				break;
			case 7: // Dejar pantalla, esta obción deja libre la pantalla 
				liberarPantalla.receive();
				librePantalla = true;
				break;
			}
		}
	}

}
