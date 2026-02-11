package ejercicio1;

public class CuadradoMatriz extends Thread {

	@Override
	public void run() {

		int metricasDeEntrenamiento[][] = new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];
		int impactoAcumulado[][] = new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];

		for (int operacionesRealizadas = 0; operacionesRealizadas < Main.NUM_OPERACIONES; operacionesRealizadas++) {

			Main.generarMatriz(metricasDeEntrenamiento);

			// Cálculo del cuadrado de la matriz
			for (int i = 0; i < Main.LONGITUD_FILAS_Y_COLUMNAS; i++) {
				for (int j = 0; j < Main.LONGITUD_FILAS_Y_COLUMNAS; j++) {
					impactoAcumulado[i][j] = 0;
					for (int k = 0; k < Main.LONGITUD_FILAS_Y_COLUMNAS; k++) {
						impactoAcumulado[i][j] += metricasDeEntrenamiento[i][k] * metricasDeEntrenamiento[k][j];
					}
				}
			}


			/*
			 * Aquí se bloquea el cerrojo de la pantalla para asegurar que este recurso no
			 * compartible no sea usado por más de un proceso a la vez
			 */
			Main.cerrojoPantalla.lock();
			
			System.out.println("A x A\n");
			
			for (int[] fila : metricasDeEntrenamiento) {
				for (int valor : fila) {
					System.out.print(valor + "a ");
				}
				System.out.println();
			}
			System.out.println("\nx\n");

			for (int[] fila : metricasDeEntrenamiento) {
				for (int valor : fila) {
					System.out.print(valor + "a ");
				}
				System.out.println();
			}
			
			System.out.println("\nA2\n");
			
			for (int[] fila : impactoAcumulado) {
				for (int valor : fila) {
					System.out.print(valor + "r ");
				}
				System.out.println();
			}
			System.out.println();

			Main.cerrojoPantalla.unlock();

		}

	}
}
