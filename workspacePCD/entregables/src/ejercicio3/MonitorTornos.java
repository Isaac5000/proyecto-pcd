package ejercicio3; //NOSONAR

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTornos {
	private static final int NUMERO_DE_TORNOS = 3;
	private final int[] tiemposEnTornos = new int[NUMERO_DE_TORNOS];
	private final boolean[] tornosLibres = new boolean[NUMERO_DE_TORNOS];
	private final ReentrantLock l = new ReentrantLock();
	private final Condition[] colasDeTornos = new Condition[NUMERO_DE_TORNOS];
	
	private static final int POSICION_POR_DEFECTO = 0;

	public MonitorTornos() {
		for(int i = 0; i < NUMERO_DE_TORNOS; i++) {
			tiemposEnTornos[i] = 0;
			tornosLibres[i] = true;
			colasDeTornos[i] = l.newCondition();
		}
	}
	
	/**
	 * Función que se encarga de seleccionar un torno libre y en caso de no haber uno libre
	 * selecciona el primero con un mejor tiempo.
	 * @return Devuelve el ID del torno elegido.
	 */
	private int buscarTorno() {
		int idTornoSeleccionado = POSICION_POR_DEFECTO;
		for(int idTornoActual = 0; idTornoActual < NUMERO_DE_TORNOS; idTornoActual++) {
			if(tornosLibres[idTornoActual]) {
				return idTornoActual;
			}
			if(tiemposEnTornos[idTornoSeleccionado] > tiemposEnTornos[idTornoActual]) {
				idTornoSeleccionado = idTornoActual;
			}
		}
		return idTornoSeleccionado;
	}

	/**
	 * Función que se encarga de seleccionar un torno para poder usarlo nada más salir de esta.
	 * @param X es el tiempo que va a pasar en el torno quien lo pide
	 * @return devuelve el ID del torno que se selecciona para usarlo
	 */
	public int seleccionarYUsarTorno(int X) {
		l.lock();
		try {
			int idTornoSeleccionado = buscarTorno();
			tiemposEnTornos[idTornoSeleccionado] += X;
			while(!tornosLibres[idTornoSeleccionado]) {
				try {
					colasDeTornos[idTornoSeleccionado].await();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			tornosLibres[idTornoSeleccionado] = false;
			return idTornoSeleccionado;
		} finally {
			l.unlock();
		}
	}

	/**
	 * Función para liberar el torno que se ha usado.
	 * @param X tiempo para restárselo al tiempo en la cola del torno.
	 * @param idTorno se usa para saber que torno se va a liberar.
	 */
	public void salirDeTorno(int X, int idTorno) {
		l.lock();
		try {
			tornosLibres[idTorno] = true;
			tiemposEnTornos[idTorno] -= X;
			colasDeTornos[idTorno].signal();
		} finally {
			l.unlock();
		}
	}
	
}
