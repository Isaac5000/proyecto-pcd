package ejercicio2; //NOSONAR

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

	// Constantes para que no hallan números mágicos
    public static final int NUMERO_DE_PANELES = 3;
    public static final int NUMERO_DE_EQUIPOS = 10;
    public static final int NUMERO_DE_DEPORTISTAS_POR_EQUIPO = 4;
    public static final int NUMERO_DE_SECUENCIAS = 3;
    public static final int NUMERO_DE_EJERCICIOS = 5;
    public static final int INICIALIZAR_SEMAFORO_BINARIO = 1;
	public static final int VALOR_MAX_MATRIZ = 9;
	public static final int VALOR_MIN_MATRIZ = 1;
	public static final Random RANDOM = new Random();

	public static final int NORMALIZAR_NUMERO_DE_PANEL = 1;
	public static final int NORMALIZAR_POSICION_X_DE_PANEL = 415;
	public static final int NORMALIZAR_POSICION_Y_DE_PANEL = 0;

	// Variables para importantes
    protected static final Panel[] PANELES = new Panel[NUMERO_DE_PANELES];
    protected static final boolean[] PANELES_LIBRES = new boolean[NUMERO_DE_PANELES];
    public static final Semaphore MUTEX_VARIABLES_COMPARTIDAS = new Semaphore(INICIALIZAR_SEMAFORO_BINARIO);
    public static final Semaphore SEMAFORO_DE_PANELES = new Semaphore(NUMERO_DE_PANELES);
    protected static final Equipo[] EQUIPOS = new Equipo[NUMERO_DE_EQUIPOS];
    
    /**
	 * Función que le da valores aleatorios entre 1 y 10 a una matriz de 5x5,
	 * @param matriz Es la matriz que se rellenara con núevos valores aleatorios
	 */
	public static void generarMatriz(int[][] matriz){
		for(int i = 0; i < NUMERO_DE_EJERCICIOS; i++) {
			for(int j = 0; j < NUMERO_DE_EJERCICIOS; j++) {

				matriz[i][j] = RANDOM.nextInt(VALOR_MAX_MATRIZ) + VALOR_MIN_MATRIZ;
			}
		}
	}

	public static void main(String[] args) {
		//Bucle para generar los paneles uno al lado del otro sin superponerse e inicializo el array de 
		//booleanos
		for (int numPanel = 0; numPanel < NUMERO_DE_PANELES; numPanel++) {
			PANELES[numPanel] = new Panel("Panel " + (numPanel + NORMALIZAR_NUMERO_DE_PANEL),
					(numPanel * NORMALIZAR_POSICION_X_DE_PANEL), NORMALIZAR_POSICION_Y_DE_PANEL);
			PANELES_LIBRES[numPanel] = true;
		}
		
		for(int numEquipo = 0; numEquipo < NUMERO_DE_EQUIPOS; numEquipo++) {
			EQUIPOS[numEquipo] = new Equipo(numEquipo);
			EQUIPOS[numEquipo].start();
		}


		for(int numEquipo = 0; numEquipo < NUMERO_DE_EQUIPOS; numEquipo++) {
			try {
				EQUIPOS[numEquipo].join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		System.out.println("Fin del hilo principal."); //NOSONAR
	}

}
