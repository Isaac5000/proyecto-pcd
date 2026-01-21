package ejercicio3;

import java.util.concurrent.locks.ReentrantLock;

public class Pantalla {
	
	private ReentrantLock l = new ReentrantLock();
	
	public Pantalla() {
	}
	

	public void usarPantalla(int maquinaQueUso, int idCliente,int Xi,int Yi,int[] zonaYTiempos) {
		l.lock();
		try {
			System.out.println(
					"--------------------------------------------------------------\n"
					+ "“Cliente " + idCliente + " ha solicitado su servicio en la máquina: " + (maquinaQueUso + Main.EMPEZAR_EN_UNO) + "\n"
					+ "Tiempo en solicitar el servicio: " + Xi + "\n"
					+ "El coche será lavado en el lavadero nº: " + (zonaYTiempos[0] + Main.EMPEZAR_EN_UNO) + "\n"
					+ "Tiempo en lavar el coche= " + Yi + "\n"
					+ "Tiempo de espera en lavadero1=" + zonaYTiempos[1] + ",lavadero2=" + zonaYTiempos[2] + ",lavadero3=" + zonaYTiempos[3] + ",lavadero4=" + zonaYTiempos[4] + "”\n"
					+ "--------------------------------------------------------------");
		} finally {
			l.unlock();
		}
	}

}
