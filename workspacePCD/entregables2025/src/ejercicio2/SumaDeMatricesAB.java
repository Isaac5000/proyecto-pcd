package ejercicio2;

import java.util.Random;

public class SumaDeMatricesAB extends Thread {
	public static final int EMPEZAR_EN_UNO = 1;

	private int idSuma; // Identificador del hilo
	// Matrices que se sumaran en cada bucle
	private int matrizA[][] = new int[Main.LONGITUD_FILAS_COLUMNAS_MATRIZ][Main.LONGITUD_FILAS_COLUMNAS_MATRIZ];
	private int matrizB[][] = new int[Main.LONGITUD_FILAS_COLUMNAS_MATRIZ][Main.LONGITUD_FILAS_COLUMNAS_MATRIZ];
	// Matriz en la que se guardara la suma de las dos anteriores
	private int matrizC[][] = new int[Main.LONGITUD_FILAS_COLUMNAS_MATRIZ][Main.LONGITUD_FILAS_COLUMNAS_MATRIZ];

	public SumaDeMatricesAB(int idSuma) {
		this.idSuma = idSuma;
	}

	/**
	 * Función que le da valores aleatorios entre 0 y 4 a una matriz de 10x10,
	 * 
	 * @param matrizA Es la matriz que se rellenara con nuevos valores aleatorios
	 */
	private static void generarMatriz(int[][] matrizA) {
		for (int i = 0; i < Main.LONGITUD_FILAS_COLUMNAS_MATRIZ; i++) {
			for (int j = 0; j < Main.LONGITUD_FILAS_COLUMNAS_MATRIZ; j++) {
				Random random = new Random();
				matrizA[i][j] = random.nextInt(Main.NUM_5);
			}
		}
	}
	
	/**
	 * Función que recibe una matriz y devuelve un string con en formato necesario para imprimir
	 * @param matriz matriz que va a ser transformada en string
	 * @return matriz transformada en string con el formato que necesito
	 */
	private static String matrizToString(int[][] matriz) {
		String strMatriz = "";
		for (int i = 0; i < Main.LONGITUD_FILAS_COLUMNAS_MATRIZ; i++) {
			for (int j = 0; j < Main.LONGITUD_FILAS_COLUMNAS_MATRIZ; j++) {
				strMatriz += matriz[i][j] + " ";
			}
			if (i < (Main.LONGITUD_FILAS_COLUMNAS_MATRIZ - 1))
				strMatriz += "\n                 ";
			else
				strMatriz += "\n";
		}
		return strMatriz;
	}
	@Override
	public void run() {
		for (int repeticion = 0; repeticion < Main.NUM_REPETICIONES_EN_CADA_HILO; repeticion++) {
			
			//Antes de usar un semaforo se generan las matrices, se suman y se transfoman a string
			generarMatriz(this.matrizA);
			generarMatriz(this.matrizB);

			for (int i = 0; i < Main.LONGITUD_FILAS_COLUMNAS_MATRIZ; i++) {
				for (int j = 0; j < Main.LONGITUD_FILAS_COLUMNAS_MATRIZ; j++) {
					matrizC[i][j] = matrizA[i][j] + matrizB[i][j];
				}
			}

			String mensajeParaPanel = new String("\"Usando panel el hilo " + (this.idSuma + EMPEZAR_EN_UNO) + "\n" 
					+ "Matriz A = " + matrizToString(this.matrizA) 
					+ "Matriz B = " + matrizToString(this.matrizB) 
					+ "Matriz C = " + matrizToString(this.matrizC) 
					+ "Terminando de usar panel el hilo " + (this.idSuma + EMPEZAR_EN_UNO) + "\"\n");

			int panelElegido = 0; // Variable para saber en que posición del array de booleanos e elegido un panel
			try {
				Main.colaPaneles.acquire(); // Usa el semaforo general para acceder a los paneles para así entrar si hay uno de los tres libres
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				Main.mutexVar.acquire(); // Se abquiere la exclusión mútua del array de booleanos para que nadie lo modifique mientras lo leo
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Busco el primer panel libre
			while (!Main.panelesLibres[panelElegido]) {
				panelElegido++;
			}
			Main.panelesLibres[panelElegido] = false; // Pongo el panel elegido a falso ya que lo estoy usando
			Main.mutexVar.release(); // Pongo el semáforo a disposición de otro proceso

			Main.paneles[panelElegido].escribir_mensaje(mensajeParaPanel); // Imprimo en el panel que e escogido

			try {
				Main.mutexVar.acquire(); // Necesito modificar una posición en el array de booleanos donde otros
										 // podrían estar leyendo así que necesito la exclusión mutua
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.panelesLibres[panelElegido] = true; // Se libera el panel que he dejado de usar

			Main.mutexVar.release(); // Aquí se permito que otros accedan al array de booleanos
			Main.colaPaneles.release(); // Por último se le da acceso a otro proceso a los paneles 
		}
	}
}
