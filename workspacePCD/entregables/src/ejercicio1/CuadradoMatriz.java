package ejercicio1; //NOSONAR

public class CuadradoMatriz extends Thread {

	private final int[][] metricasDeEntrenamiento =
			new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];

	/**
	 * Función para calcular el cuadrado de una matriz.
	 * @param matriz es a la que se le va ha realizar el cuadrado.
	 * @return devuelve el cuadrado de la matriz.
	 */
	private int[][] calcularCuadradoMatriz(int[][] matriz) {
		int[][] cuadrado = new int[matriz.length][matriz[0].length];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				cuadrado[i][j] = 0;
				for (int k = 0; k < matriz.length; k++) {
					cuadrado[i][j] += matriz[i][k] * matriz[k][j];
				}
			}
		}
		return cuadrado;
	}

	/**
	 * Imprime en el formato correcto del ejercicio.
	 * @param matriz es la que se usa para hacer el cuadrado.
	 * @param solucion es la solución del cálculo.
	 */
	@SuppressWarnings("java:S106")
	private void imprimirCuadradoDeMatriz(int[][] matriz, int[][] solucion ) {
		System.out.println("A x A\n");

		for (int[] fila : matriz) {
			for (int valor : fila) {
				System.out.print(valor + "a ");
			}
			System.out.println();
		}
		System.out.println("\nx\n");

		for (int[] fila : matriz) {
			for (int valor : fila) {
				System.out.print(valor + "a ");
			}
			System.out.println();
		}

		System.out.println("\nA2\n");

		for (int[] fila : solucion) {
			for (int valor : fila) {
				System.out.print(valor + "r ");
			}
			System.out.println();
		}
		System.out.println();
	}




	@Override
	public void run() {

		int[][] impactoAcumulado;

		for (int operacionesRealizadas = 0; operacionesRealizadas < Main.NUM_OPERACIONES; operacionesRealizadas++) {

			Main.generarMatriz(metricasDeEntrenamiento);
			impactoAcumulado = calcularCuadradoMatriz(metricasDeEntrenamiento);

			/*
			 * Aquí se bloquea el cerrojo de la pantalla para asegurar que este recurso no
			 * compartible no sea usado por más de un proceso a la vez.
			 */
			Main.CERROJO_PANTALLA.lock();
			
			imprimirCuadradoDeMatriz(metricasDeEntrenamiento, impactoAcumulado);

			Main.CERROJO_PANTALLA.unlock();

		}

	}
}
