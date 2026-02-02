package ejercicio1;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
	public static final int NUM_OPERACIONES = 10;
	public static final int LONGITUD_FILAS_Y_COLUMNAS = 3;
	public static final int NUM_0_9 = 10;

	/* Este cerrojo se encarga de bloquear la sección crítica de la pantalla para que no acceda
	 * más de un hilo a la vez */ 
	public static ReentrantLock cerrojoPantalla = new ReentrantLock();
	
	/**
	 * Función que le da valores aleatorios entre 0 y 9 a una matriz de 3x3,
	 * @param matrizA Es la matriz que se rellenara con núevos valores aleatorios
	 */
	public static void generarMatriz(int[][] matriz){
		for(int i = 0; i < LONGITUD_FILAS_Y_COLUMNAS; i++) {
			for(int j = 0; j < LONGITUD_FILAS_Y_COLUMNAS; j++) {
				Random random = new Random();
				matriz[i][j] = random.nextInt(NUM_0_9);
			}
		}
	}
	
	/**
	 * Función para pasar una matriz a String.
	 * @param matriz 
	 * @return
	 */
	public static String matrizToString(int[][] matriz) {
		StringBuilder matrizCasiString = new StringBuilder();
		for(int[] fila : matriz) {
			for(int valor : fila) {
				matrizCasiString.append(valor).append(" ");
			}
			matrizCasiString.append("\n");
		}
		String matrizString = matrizCasiString.toString();
		return matrizString;
	}
	
	
	public static void main(String[] args) {
		Cuadrado cuadrado = new Cuadrado();
		Suma suma = new Suma();
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
