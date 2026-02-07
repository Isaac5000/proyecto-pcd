package ejercicio1;

public class SumaMatriz extends Thread{
	
	
	@Override
	public void run() {
		
		int metricasDeEntrenamiento[][] = new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];
		int impactoAcumulado[][] = new int[Main.LONGITUD_FILAS_Y_COLUMNAS][Main.LONGITUD_FILAS_Y_COLUMNAS];
		String metricasDeEntrenamientoCadena;
		String impactoAcumuladoCadena;
		
		for (int operacionesRealizadas = 0; operacionesRealizadas < Main.NUM_OPERACIONES; operacionesRealizadas++) {
			
			Main.generarMatriz(metricasDeEntrenamiento);
			
			// Suma de la matriz A + A (2A)
			for (int i = 0; i < Main.LONGITUD_FILAS_Y_COLUMNAS; i++) {
				for (int j = 0; j < Main.LONGITUD_FILAS_Y_COLUMNAS; j++) {
					impactoAcumulado[i][j] = metricasDeEntrenamiento[i][j] + metricasDeEntrenamiento[i][j];
				}
			}
			
			metricasDeEntrenamientoCadena = Main.matrizToString(metricasDeEntrenamiento, Main.OPCION_A);
			impactoAcumuladoCadena = Main.matrizToString(impactoAcumulado, Main.OPCION_R);
			
			/* Aquí se bloquea el cerrojo de la pantalla para asegurar que este recurso no
			 * compartible no sea usado por más de un proceso a la vez
			 */
			Main.cerrojoPantalla.lock();
			
			System.out.println("2A");
			System.out.println(metricasDeEntrenamientoCadena);
			System.out.println("+\n");
			System.out.println(metricasDeEntrenamientoCadena);
			System.out.println("A2");
			System.out.println(impactoAcumuladoCadena);
			
			Main.cerrojoPantalla.unlock();
			
		}
		
	}
}
