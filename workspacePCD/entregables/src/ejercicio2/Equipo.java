package ejercicio2; //NOSONAR

import java.util.Arrays;

@SuppressWarnings("java:S2696")
public class Equipo extends Thread{
		
		public static final int POSICION_DE_SPRINT = 0;
		public static final int PRIMER_EJERCICIO_SACAR_MAX = 1;
		
		private final int idEquipo;

		//Estructura con las 4 matrices
		private final int[][][] tablaDeMatricesDeEjercicios = new int[Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO]
				[Main.NUMERO_DE_EJERCICIOS][Main.NUMERO_DE_EJERCICIOS];
		//Matriz con las mejores marcas en cada deporte de cada deportista
		private final int[][] matrizResumenR = new int[Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO]
				[Main.NUMERO_DE_EJERCICIOS];
	    
	    public Equipo(int idEquipo) {
	        this.idEquipo = idEquipo;
		}
	    
	    /**
	     * Función para extraer las mejores marcas de una matriz de ejercicios
	     * @param ejercicios matriz cuyas filas representan un ejercicio y contienen sus marcas.
	     * @return devuelve un vector con las mejores marcas en cada ejercicio en orden
	     */
	    private static int[] mejoresMarcas(int[][] ejercicios) {
	    	int[] vectorDeMarcas = new int[Main.NUMERO_DE_EJERCICIOS];
	    	//Guardo sprint con el valor mínimo, ya que es una prueba de velocidad.
			if(Arrays.stream(ejercicios[POSICION_DE_SPRINT]).min().isPresent()) {
				vectorDeMarcas[POSICION_DE_SPRINT] =
						Arrays.stream(ejercicios[POSICION_DE_SPRINT]).min().getAsInt();
			}
	    	//El resto de valores los guardo con el máximo salto, lanzamiento, resistencia, y agilidad.
	    	for(int numEjercicios = PRIMER_EJERCICIO_SACAR_MAX; numEjercicios < Main.NUMERO_DE_EJERCICIOS;
				numEjercicios++) {
				if(Arrays.stream(ejercicios[numEjercicios]).max().isPresent()) {
					vectorDeMarcas[numEjercicios] =
							Arrays.stream(ejercicios[numEjercicios]).max().getAsInt();
				}
			}
	    	
	    	return vectorDeMarcas;
	    }
	    
	    /**
	     * Función que recibe una matriz de 4x5 y devuelve un string con el formato necesario para imprimir.
	     * @param matriz matriz que va a ser transformada en string (4 filas x 5 columnas)
	     * @return matriz transformada en string con el formato que necesito
	     */
	    private static String matrizToString(int[][] matriz) {
	        StringBuilder strMatriz = new StringBuilder();
	        for (int i = 0; i < Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO; i++) {
	            for (int j = 0; j < Main.NUMERO_DE_EJERCICIOS; j++) {
	                strMatriz.append(matriz[i][j]).append(" ");
	            }
	            strMatriz.append("\n");
	        }
	        return strMatriz.toString();
	    }
	    
	    
	    @Override
	    public void run() {
	    	
	    	for(int secuenciasPorEquipo = 0; secuenciasPorEquipo < Main.NUMERO_DE_SECUENCIAS;
	    			secuenciasPorEquipo++) {

	    		// Primero se genera una matriz por jugador
	    		for(int numJugadores = 0; numJugadores < Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO; 
	    				numJugadores++) {
	    			Main.generarMatriz(tablaDeMatricesDeEjercicios[numJugadores]);
	    		}
	    		
	    		// Se seleccionan y guardan las mejores marcas de cada jugador
	    		// sprint, salto, lanzamiento, resistencia, y agilidad
	    		for(int numJugadores = 0; numJugadores < Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO; 
	    				numJugadores++) {
	    			matrizResumenR[numJugadores] = mejoresMarcas(tablaDeMatricesDeEjercicios[numJugadores]);
	    		}
	    		
	    		// Bloqueo el acceso a los paneles para que solo puedan estar el mismo número de procesos
	    		// que paneles hay.
				try {
					Main.SEMAFORO_DE_PANELES.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		// Bloqueo el acceso a la varible que puede ser usada por más de un proceso a la vez para
	    		// lectura y escritura, por un semáforo binario.
				try {
					Main.MUTEX_VARIABLES_COMPARTIDAS.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

	    		// Llegado este punto hay un panel libre asegurado por lo que uso un while para encontrar uno.
	    		int idPanel = 0;
	    		while(!Main.PANELES_LIBRES[idPanel]) {
	    			idPanel++;
	    		}
	    		Main.PANELES_LIBRES[idPanel] = false;
	    		// Dejo libre el semáforo binario
	    		Main.MUTEX_VARIABLES_COMPARTIDAS.release();
	    		
	    		String mensajeParaPanel = ("Usando panel " + idPanel + " el hilo (equipo) " + idPanel + "\n\n"
	    				+ "Matriz R (Resumen del equipo " + idEquipo + ": mejores marcas) =\n"
						+ matrizToString(matrizResumenR) + "\n"
	    				+ "Terminando de usar panel " + idPanel + " el hilo (equipo) " + idEquipo + "\n");
	    		
	    		Main.PANELES[idPanel].escribir_mensaje(mensajeParaPanel);
	    		
				try {
					Main.MUTEX_VARIABLES_COMPARTIDAS.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Main.PANELES_LIBRES[idPanel] = true;
				Main.MUTEX_VARIABLES_COMPARTIDAS.release();
				Main.SEMAFORO_DE_PANELES.release();
	    	}
	    }

}
