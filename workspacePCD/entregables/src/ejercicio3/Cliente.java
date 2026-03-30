package ejercicio3; //NOSONAR

import java.util.Random;

public class Cliente extends Thread {
	
	private final int idCliente;
	private static final Random RANDOM = new Random();
	private int X = RANDOM.nextInt(Main.TIEMPO_MAX_DE_X_TORNO) + Main.TIEMPO_MIN;
	private int Y = RANDOM.nextInt(Main.TIEMPO_MAX_DE_Y_ZONA) + Main.TIEMPO_MIN;
	private boolean usarBicicletaPremium = false;

	public static final int POSICION_DE_ID_DE_ZONA = 0;
	public static final int POSICION_DE_CARDIO_EN_MONITOR = 0;
	public static final int CIEN_POR_CIENTO = 10;
	public static final int TREINTA_POR_CIENTO = 3;
	public static final int CARDIO = 1;
	public static final int FUERZA = 2;
	public static final int FUNCIONAL = 3;
	public static final int ESTIRAMIENTOS = 4;


	public Cliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	@Override
	public void run() {
		int idTorno = Main.MONITOR_TORNOS.seleccionarYUsarTorno(X);
		try {
			Thread.sleep(X);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Main.MONITOR_TORNOS.salirDeTorno(X, idTorno);
		int[] idZonaYTiempos = Main.MONITOR_ZONAS_GIMNASIO.accederAZonaYRecuperarTiempos(Y);
		int idZona = idZonaYTiempos[POSICION_DE_ID_DE_ZONA];

		// {0,1,2} es true, {3 ,4 ,5 ,6 ,7 ,8 , 9} es false
		if(idZona == POSICION_DE_CARDIO_EN_MONITOR) {
			usarBicicletaPremium = (RANDOM.nextInt(CIEN_POR_CIENTO) < TREINTA_POR_CIENTO);
		}

		String mensaje =
				"--------------------------------------------------------------\n"
				+ "Cliente " + this.idCliente + " ha pasado por el torno: " + idTorno
				+ "\n"
				+ "Tiempo en el torno (acceso): " + this.X + "\n"
				+ "Zona elegida: " + idZona + "\n"
				+ "Tiempo de entrenamiento: " + this.Y + "\n"
				+ "Estimación de espera (sin incluirse a sí mismo):\n"
				+ "  Zona1(Cardio)=" + idZonaYTiempos[CARDIO]
				+ ",     Zona2(Fuerza)=" + idZonaYTiempos[FUERZA]
				+ ",     Zona3(Funcional)=" + idZonaYTiempos[FUNCIONAL] + "\n"
				+ "Zona4(Estiramientos)=" + idZonaYTiempos[ESTIRAMIENTOS] + "\n"
				+ "Espera bicicleta premium (si aplica)="
				+ (usarBicicletaPremium ? "Sí" : "No") + "\n"
				+ "--------------------------------------------------------------\n";
		Main.MONITOR_PANTALLA.usarPantalla(mensaje);
		try {
			Thread.sleep(Y);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(usarBicicletaPremium) {
			Main.MONITOR_ZONAS_GIMNASIO.salirDeBicicletaPremium();
		}
		Main.MONITOR_ZONAS_GIMNASIO.salirDeZona(Y, idZona);
	}
}
