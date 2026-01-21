package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Lavadero {

	private ReentrantLock l = new ReentrantLock();

	private static final int NUM_ZONAS_DE_LAVADO = 4;
	private static final int ZONA_DE_LAVADO_ELEGIDA = 1;

	private Condition colasDeZonasDeLavado[] = new Condition[NUM_ZONAS_DE_LAVADO]; // Array en el que hay una cola(condition) para cada zona de lavado
	private boolean lavaderosLibres[] = new boolean[NUM_ZONAS_DE_LAVADO]; // Array de booleanos que indica que lavaderos se encuentran libres
	private int tiemposColasZonasDeLavado[] = new int[NUM_ZONAS_DE_LAVADO]; // Es un array con los tiempos de espera de cada lavadero

	public Lavadero() {

		for (int i = 0; i < NUM_ZONAS_DE_LAVADO; i++) {
			colasDeZonasDeLavado[i] = l.newCondition();
			lavaderosLibres[i] = true;
			tiemposColasZonasDeLavado[i] = 0;
		}
	}

	

	/**
	 * Función que se encarga de darnos una zona para lavar el coche en el lavadero.
	 * 
	 * @param tiempoDeLavado Es el tiempo que tardaremos en dejar libre la zona de
	 *                       lavado.
	 * @return Array que en la primera posición tiene la zona de lavado que se nos
	 *         ha dado y en las siguientes posiciones los tiempos de cada zona de
	 *         lavado.
	 */
	public int[] pedirZonaDeLavado(int tiempoDeLavado) {
		l.lock();
		try {

			int[] zonaDeLavadoYTiempos = new int[ZONA_DE_LAVADO_ELEGIDA + NUM_ZONAS_DE_LAVADO];
			zonaDeLavadoYTiempos[0] = 0;
			for (int i = 0; i < NUM_ZONAS_DE_LAVADO; i++) {
				// En esta condición se guarda zona de lavado con menor tiempo en la primera
				// posicion del array que se devuelve
				if (tiemposColasZonasDeLavado[i] < tiemposColasZonasDeLavado[zonaDeLavadoYTiempos[0]]) {
					zonaDeLavadoYTiempos[0] = i;
				}
				// Se van guardando los tiempos de todas las zonas de lavado antes de incluir el
				// tiempo de la que elijamos
				zonaDeLavadoYTiempos[ZONA_DE_LAVADO_ELEGIDA + i] = tiemposColasZonasDeLavado[i];
			}

			// Al tiempo de la cola elegida se le suma el tiempo del cliente nuevo que ha
			// entrado
			tiemposColasZonasDeLavado[zonaDeLavadoYTiempos[0]] += tiempoDeLavado;

			return zonaDeLavadoYTiempos; // Este array a sido formado antes de incluir el tiempo del nuevo cliente
		} finally {
			l.unlock();
		}
	}

	/**
	 * Método para esperar nuestro turno de lavado en nuestra cola elegida
	 * 
	 * @param zonaDeLavadoElegida Es necesario este parametro para saber que zona de
	 *                            lavado emos elegido
	 */
	public void empezarLavado(int zonaDeLavadoElegida) {
		l.lock();
		try {
			while (!lavaderosLibres[zonaDeLavadoElegida]) {
				try {
					colasDeZonasDeLavado[zonaDeLavadoElegida].await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lavaderosLibres[zonaDeLavadoElegida] = false;
		} finally {
			l.unlock();
		}
	}

	/**
	 * Método para dejar nuestra zona de lavado libre para que otro entre
	 * 
	 * @param zonaDeLavadoElegida Es la zona de lavado que vamos a liberar
	 * @param tiempoDeLavado      Es nuestro tiempo de lavado, el cual se le debe de
	 *                            restar al tiempo total de la cola en la que
	 *                            estabamos
	 */
	public void terminarLavado(int zonaDeLavadoElegida, int tiempoDeLavado) {
		l.lock();
		try {
			lavaderosLibres[zonaDeLavadoElegida] = true;
			tiemposColasZonasDeLavado[zonaDeLavadoElegida] -= tiempoDeLavado;
			colasDeZonasDeLavado[zonaDeLavadoElegida].signal();
		} finally {
			l.unlock();
		}
	}

}
