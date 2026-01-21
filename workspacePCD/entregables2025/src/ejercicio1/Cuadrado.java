package ejercicio1;

public class Cuadrado extends Thread {
	
	private int[][] matrizA = new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];
	private int[][] solucion = new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];
	
	/**
	 * Método que genera la matriz con valores aleatorios y hace su cuadrado, asegurándose de 
	 * proteger la pantalla para que varios procesos no entren a la vez a esta
	 */
	@Override
	public void run() {

		for (int num_cuadrado = 0; num_cuadrado < Main.NUM_OPERACIONES; num_cuadrado++) {
			
			// Generación de una matriz con valores aleatorios
			Main.generarMatriz(matrizA);
			
			// Cálculo del cuadrado de la matriz
			for (int i = 0; i < Main.LONGITUD_FILAS_Y_COLUMNAS; i++) {
				for (int j = 0; j < Main.LONGITUD_FILAS_Y_COLUMNAS; j++) {
					solucion[i][j] = 0;
					for (int k = 0; k < Main.LONGITUD_FILAS_Y_COLUMNAS; k++) {
						solucion[i][j] += matrizA[i][k] * matrizA[k][j];
					}
				}
			}
			
			/* Aquí se bloquea el cerrojo de la pantalla, para asegurar que este recurso compartido no
			 * compartible no sea usado por más de un proceso a la vez
			 */
			Main.cerrojoPantalla.lock();
			System.out.println("AxA");
			for (int i = 0; i < Main.LONGITUD_FILAS_Y_COLUMNAS; i++) {
				String derecha = new String("");
				System.out.print(" ");
				for (int j = 0; j < Main.LONGITUD_FILAS_Y_COLUMNAS; j++) {
					System.out.print(matrizA[i][j] + " ");
					derecha += new String(matrizA[i][j] + " ");
				}
				if (i == (Main.LONGITUD_FILAS_Y_COLUMNAS / 2)) {
					System.out.println("x " + derecha);
				} else {
					System.out.println("  " + derecha);
				}
			}

			System.out.println("A\u00B2");
			for (int i = 0; i < Main.LONGITUD_FILAS_Y_COLUMNAS; i++) {
				System.out.print(" ");
				for (int j = 0; j < Main.LONGITUD_FILAS_Y_COLUMNAS; j++) {
					System.out.print(solucion[i][j] + " ");
				}
				System.out.println();
			}
			System.out.println();
			Main.cerrojoPantalla.unlock();
			
		}
	}

}
