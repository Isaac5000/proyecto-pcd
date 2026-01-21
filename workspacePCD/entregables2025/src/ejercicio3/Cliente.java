package ejercicio3;

import java.util.Random;

public class Cliente extends Thread {

	public static final int TIEMPO_EN_MAQUINA_MAX = 5000; // Variable para limitar el tiempo máximo en el uso de las
															// máquinas
	public static final int TIEMPO_DE_LAVADO_MAX = 10000; // Variable para limitar el tiempo máximo en el tiempo de
															// lavado

	private final int Xi; // Tiempo que se tarda en usar la máquina
	private final int Yi; // Tiempo para hacer el lavado

	private int idCliente;

	public Cliente(int idCliente) {
		this.idCliente = idCliente;
		this.Xi = (new Random()).nextInt(TIEMPO_EN_MAQUINA_MAX);
		this.Yi = (new Random()).nextInt(TIEMPO_DE_LAVADO_MAX);
	}

	@Override
	public void run() {
		int maquinaQueUso = Main.maquina.pedirMaquina();
		try {
			Thread.sleep(this.Xi);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Main.maquina.liberarMaquina(maquinaQueUso);

		int zonaYTiempos[] = Main.lavadero.pedirZonaDeLavado(this.Yi);
		
		Main.pantalla.usarPantalla(maquinaQueUso, this.idCliente,this.Xi,this.Yi,zonaYTiempos);
		
		Main.lavadero.empezarLavado(zonaYTiempos[0]);
		try {
			Thread.sleep(this.Yi);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Main.lavadero.terminarLavado(zonaYTiempos[0], this.Yi);

	}
}
