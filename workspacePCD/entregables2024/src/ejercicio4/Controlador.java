package ejercicio4;

import messagepassing.*;

public class Controlador extends Thread{
	
	private static final int FIN_FOR = Cliente.NUMERO_SENDS * Cliente.NUMERO_PAGOS * Main.NUMERO_CLIENTES;
	
	private MailBox[] respuestasClientes;
	
	private MailBox caja, cogerCajaA, cogerCajaB ,liberarCajaA, liberarCajaB, pedirPantalla, liberarPantalla;
	
	private boolean libreCajaA = true;
	private boolean libreCajaB = true;
	private boolean librePantalla = true;
	
	private Selector s;
	
	public Controlador(MailBox[] respuestasClientes, MailBox caja, MailBox cogerCajaA, MailBox cogerCajaB , MailBox liberarCajaA, MailBox liberarCajaB, MailBox pedirPantalla, MailBox liberarPantalla) {
		
		this.respuestasClientes = respuestasClientes;
		this.caja = caja;
		this.cogerCajaA = cogerCajaA;
		this.cogerCajaB = cogerCajaB;
		this.liberarCajaA = liberarCajaA;
		this.liberarCajaB = liberarCajaB;
		this.pedirPantalla = pedirPantalla;
		this.liberarPantalla = liberarPantalla;
		
		s = new Selector();
		
		s.addSelectable(caja, false);
		s.addSelectable(cogerCajaA, false);
		s.addSelectable(liberarCajaA, false);
		s.addSelectable(cogerCajaB, false);
		s.addSelectable(liberarCajaB, false);
		s.addSelectable(pedirPantalla, false);
		s.addSelectable(liberarPantalla, false);
		
		cogerCajaA.setGuardValue(libreCajaA == true);
		cogerCajaB.setGuardValue(libreCajaB == true);
		pedirPantalla.setGuardValue(librePantalla == true);
		
	}
	
	@Override
	public void run() {
		
		for(int i = 0; i < FIN_FOR; i++){
			switch (s.selectOrBlock()) {
			case 1:
				String mensaje = (String) caja.receive();
	            String [] mensajes = mensaje.split(",");
	            int idc = Integer.parseInt(mensajes[0]);
	            int tiempo = Integer.parseInt(mensajes[1]);
	            String respuesta;
	            if(tiempo >= 5) {
	            	respuesta = Main.A;
	            } else {
	            	respuesta = Main.B;
	            }
	            respuestasClientes[idc].send(respuesta);
				break;
	        case 2:
	            mensaje = (String) cogerCajaA.receive();
	            libreCajaA = false;
	            idc = Integer.parseInt(mensaje);
	            respuestasClientes[idc].send(new String("si"));
	            break;
	        case 3:
	            liberarCajaA.receive();
	            libreCajaA = true;
	            break;
	        case 4:
	            mensaje = (String) cogerCajaB.receive();
	            libreCajaB = false;
	            idc = Integer.parseInt(mensaje);
	            respuestasClientes[idc].send(new String("si"));
	            break;
	        case 5:
	            liberarCajaB.receive();
	            libreCajaB = true;
	            break;
	        case 6:
	            mensaje = (String) pedirPantalla.receive();
	            librePantalla = false;
	            idc = Integer.parseInt(mensaje);
	            respuestasClientes[idc].send(new String("si"));
	            break;
	        case 7:
	            liberarPantalla.receive();
	            librePantalla = true;
	            break;
			}
		}
	}
}
