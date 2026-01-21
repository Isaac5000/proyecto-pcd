package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Maquina {
	private ReentrantLock l = new ReentrantLock();
	
	private static final int NUM_MAQUINAS = 3;
	
	private volatile int numMaquinasOcupadas = 0; // Número de máquinas ocupadas 
	private Condition colaBuscarMaquina = l.newCondition(); // Una cola(condition) de los procesos que no han podido encontrar máquinas libres
	private boolean maquinasLibres[] = new boolean[NUM_MAQUINAS]; // Array de booleanos para indicar que máquinas estan libres
	
	public Maquina() {
		for (int i = 0; i < NUM_MAQUINAS; i++) {
			this.maquinasLibres[i] = true;
		}
	}
	
	/**
	 * Función para obtener una de las máquinas cuando queden libres.
	 * 
	 * @return Devuelve el número de la máquina que se a elegido.
	 */
	public int pedirMaquina() {
		l.lock();
		try {
			// En este bucle solo se entrará y se permanecerá mientras no hallan máquinas
			// disponibles
			while (numMaquinasOcupadas >= NUM_MAQUINAS) {
				try {
					colaBuscarMaquina.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int maquinaSeleccionada = 0;
			while (!maquinasLibres[maquinaSeleccionada]) {
				maquinaSeleccionada++;
			}
			maquinasLibres[maquinaSeleccionada] = false; // Dejo ocupada la máquina que he elegido
			numMaquinasOcupadas++; // Le sumo uno al número de máquinas ocupadas
			return maquinaSeleccionada; // Devuelvo el número de la máquina seleccionada
		} finally {
			l.unlock();
		}
	}

	/**
	 * Método que se encarga de liberar una máquina que alguien ya ha terminado de
	 * usar
	 * 
	 * @param maquinaParaLiberar Es el identificador de la máquina que se va ha
	 *                           dejar libre
	 */
	public void liberarMaquina(int maquinaParaLiberar) {
		l.lock();
		try {
			maquinasLibres[maquinaParaLiberar] = true;
			numMaquinasOcupadas--;
			colaBuscarMaquina.signal();
		} finally {
			l.unlock();
		}
	}
}
