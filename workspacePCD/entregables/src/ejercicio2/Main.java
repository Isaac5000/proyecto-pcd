package ejercicio2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    
    public static final int NUMERO_DE_PANELES = 3;
    public static final int NUMERO_DE_EQUIPOS = 10;
    public static final int NUMERO_DE_DEPORTISTAS_POR_EQUIPO = 4;
    public static final int NUMERO_DE_SECUENCIAS = 3;
    public static final int NUMERO_DE_EJERCICIOS = 5;
    public static final int INICIALIZAR_SEMAFORO_BINARIO = 1;
	public static final int NUM_0_9 = 10;
    
    public static Panel[] paneles = new Panel[NUMERO_DE_PANELES];
    public static boolean[] panelesLibres = new boolean[NUMERO_DE_PANELES];
    public static Semaphore mutexVariablesCompartidas = new Semaphore(INICIALIZAR_SEMAFORO_BINARIO);
    public static Semaphore semaforoDePaneles = new Semaphore(NUMERO_DE_PANELES);
    
    public static Equipo[] equipos = new Equipo[NUMERO_DE_EQUIPOS];
    
    /**
	 * Función que le da valores aleatorios entre 0 y 9 a una matriz de 5x5,
	 * @param matriz Es la matriz que se rellenara con núevos valores aleatorios
	 */
	public static void generarMatriz(int[][] matriz){
		for(int i = 0; i < NUMERO_DE_EJERCICIOS; i++) {
			for(int j = 0; j < NUMERO_DE_EJERCICIOS; j++) {
				Random random = new Random();
				matriz[i][j] = random.nextInt(NUM_0_9);
			}
		}
	}
	
	public static void main(String[] args) {
		//Bucle para generar los paneles uno al lado del otro sin superponerse e inicializo el array de 
		//booleanos
		for (int numPanel = 0; numPanel < NUMERO_DE_PANELES; numPanel++) {
			paneles[numPanel] = new Panel(new String("Panel " + (numPanel + 1)), (numPanel * 410), 0);
			panelesLibres[numPanel] = true;
		}
		
		for(int numEquipo = 0; numEquipo < NUMERO_DE_EQUIPOS; numEquipo++) {
			equipos[numEquipo] = new Equipo(numEquipo);
			equipos[numEquipo].start();
		}
		
		for(int numEquipo = 0; numEquipo < NUMERO_DE_EQUIPOS; numEquipo++) {
			try {
				equipos[numEquipo].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
