package ejercicio2;

import java.util.concurrent.Semaphore;

public class Main {

	public static final int NUM_PANELES = 3;
	public static final int NUM_PROCESOS = 10;
	public static Semaphore colaPaneles = new Semaphore(NUM_PANELES); // Semáforo general para garantizar que no entran
																		// más procesos cuando no queden paneles
	public static Semaphore mutexVar = new Semaphore(1); // Semáforo binario para proteger el array de booleanos
															// siguiente

	public static boolean[] panelesLibres = new boolean[NUM_PANELES]; // Array de booleanos para saber que paneles están
																		// libres
	public static Panel paneles[] = new Panel[NUM_PANELES]; // Array de paneles
	public static SumaDeMatricesAB[] programasSumaDeMatricesAB = new SumaDeMatricesAB[NUM_PROCESOS];

	/*
	 * Variables para generar las matrices. La primera es para indicar las
	 * dimensiones de las matrices. La segunda es para indicar el valor mínimo que
	 * tendran los números en las matrices. La tercera es para indicar el valor
	 * máximo que tendran los números en las matrices
	 */
	public static final int LONGITUD_FILAS_COLUMNAS_MATRIZ = 10;
	public static final int NUM_5 = 5;

	// Variable para indicar cuantas veces se va ha ejecutar cada hilo
	public static final int NUM_REPETICIONES_EN_CADA_HILO = 3;

	public static void main(String[] args) {

		// Bucle para generar los paneles uno al lado del otro sin superponerse e inicializo el array de booleanos
		for (int i = 0; i < NUM_PANELES; i++) {
			paneles[i] = new Panel(new String("Panel " + (i + 1)), (i * 400), 0);
			panelesLibres[i] = true;
		}

		for (int i = 0; i < NUM_PROCESOS; i++) {
			programasSumaDeMatricesAB[i] = new SumaDeMatricesAB(i);
			programasSumaDeMatricesAB[i].start();
		}
		
		for (int i = 0; i < NUM_PROCESOS; i++) {
			try {
				programasSumaDeMatricesAB[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
