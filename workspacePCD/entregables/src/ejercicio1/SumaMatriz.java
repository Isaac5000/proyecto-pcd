package ejercicio1; //NOSONAR

public class SumaMatriz extends Thread{

	private final int[][] metricasDeEntrenamiento =
			new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];

	/**
	 * Función para sumar la matriz consigo misma.
	 * @param matriz matriz que se va a sumar consigo misma
	 * @return matriz sumada consigo misma.
	 */
	private int[][] calcularSumaMatriz(int[][] matriz) {
		int[][] suma = new int[matriz.length][matriz[0].length];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				suma[i][j] = matriz[i][j] + matriz[i][j];
			}
		}
		return suma;
	}

	/**
	 * Imprime en el formato correcto del ejercicio.
	 * @param matriz es la que se usa para hacer la suma.
	 * @param solucion es la solución del cálculo.
	 */
	@SuppressWarnings("java:S106")
	private void imprimirSumaDeMatriz(int[][] matriz, int[][] solucion) {
		System.out.println("A + A\n");

		for (int[] fila : matriz) {
			for (int valor : fila) {
				System.out.print(valor + "a ");
			}
			System.out.println();
		}
		System.out.println("\n+\n");

		for (int[] fila : matriz) {
			for (int valor : fila) {
				System.out.print(valor + "a ");
			}
			System.out.println();
		}

		System.out.println("\n2A\n");

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
			// Suma de la matriz A + A (2A)
			impactoAcumulado = calcularSumaMatriz(metricasDeEntrenamiento);

			/* Aquí se bloquea el cerrojo de la pantalla para asegurar que este recurso no
			 * compartible no sea usado por más de un proceso a la vez
			 */
			Main.CERROJO_PANTALLA.lock();
			
			imprimirSumaDeMatriz(metricasDeEntrenamiento, impactoAcumulado);
			
			Main.CERROJO_PANTALLA.unlock();
			
		}
		
	}
}
