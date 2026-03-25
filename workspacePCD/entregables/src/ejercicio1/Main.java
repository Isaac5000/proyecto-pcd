package ejercicio1; //NOSONAR

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
	public static final int NUM_OPERACIONES = 10;
	public static final int LONGITUD_FILAS_Y_COLUMNAS = 3;
	public static final int VALOR_MAX_MATRIZ = 10;
	public static final int VALOR_MIN_MATRIZ = 1;
	public static final Random RANDOM = new Random();

	/* Este cerrojo se encarga de bloquear la sección crítica de la pantalla para que no acceda
	 * más de un hilo a la vez */ 
	public static final ReentrantLock CERROJO_PANTALLA = new ReentrantLock();
	
	/**
	 * Función que le da valores aleatorios entre 1 y 9 a una matriz de 3x3,
	 * @param matriz Es la matriz que se rellenara con núevos valores aleatorios
	 */
	public static void generarMatriz(int[][] matriz){
		for(int i = 0; i < LONGITUD_FILAS_Y_COLUMNAS; i++) {
			for(int j = 0; j < LONGITUD_FILAS_Y_COLUMNAS; j++) {
				matriz[i][j] = VALOR_MIN_MATRIZ +
						RANDOM.nextInt(VALOR_MAX_MATRIZ);
			}
		}
	}
	
	
	/**
	 * Función principal del programa donde se ejecutan dos hilos, uno que se encarga de 
	 * hacer el cuadrado de una matriz y otro el doble de una matriz.
	 * @param args no hay nada en esta variable.
	 */
	public static void main(String[] args) {
		CuadradoMatriz cuadrado = new CuadradoMatriz();
		SumaMatriz suma = new SumaMatriz();
		cuadrado.start();
		suma.start();
		
		try {
			cuadrado.join();
			suma.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
