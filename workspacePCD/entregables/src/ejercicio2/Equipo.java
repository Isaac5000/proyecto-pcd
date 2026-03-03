package ejercicio2;

import java.util.Arrays;

public class Equipo extends Thread{
		
		public static final int SPRINT = 0;
		public static final int PRIMER_EJERCICIO_MAX = 1;
		
		private int idEquipo;
	    
	    public Equipo(int idEquipo) {
	        this.idEquipo = idEquipo;
	        }
	    
	    /**
	     * Función para extraer las mejores marcas de una matriz de ejercicios
	     * @param ejercicios
	     * @return
	     */
	    private int[] mejoresMarcas(int[][] ejercicios) {
	    	int[] vectorDeMarcas = new int[Main.NUMERO_DE_EJERCICIOS];
	    	//Guardo sprint con el valor mínimo ya que es una pruba de velocidad
	    	vectorDeMarcas[SPRINT] = Arrays.stream(ejercicios[SPRINT]).min().getAsInt();
	    	//El resto de valores los guardo con el máximo salto, lanzamiento, resistencia, y agilidad.
	    	for(int numEjercicios = PRIMER_EJERCICIO_MAX; numEjercicios < Main.NUMERO_DE_EJERCICIOS;
	    			numEjercicios++) {
	    		vectorDeMarcas[numEjercicios] = Arrays.stream(ejercicios[numEjercicios]).max().getAsInt();
	    	}
	    	
	    	return vectorDeMarcas;
	    }
	    
	    /**
	     * Función que recibe una matriz de 4x5 y devuelve un string con el formato necesario para imprimir
	     * @param matriz matriz que va a ser transformada en string (4 filas x 5 columnas)
	     * @return matriz transformada en string con el formato que necesito
	     */
	    private static String matrizToString(int[][] matriz) {
	        String strMatriz = "";
	        for (int i = 0; i < Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO; i++) {
	            for (int j = 0; j < Main.NUMERO_DE_EJERCICIOS; j++) {
	                strMatriz += matriz[i][j] + " ";
	            }
	            strMatriz += "\n";
	        }
	        return strMatriz;
	    }
	    
	    
	    @Override
	    public void run() {
	    	
	    	//Estructura con las 4 matrices
	    	int[][][] tablaDeMatricesDeEjercicios = new int[Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO]
	    			[Main.NUMERO_DE_EJERCICIOS][Main.NUMERO_DE_EJERCICIOS]; 
	    	
	    	//Matriz con las mejores marcas en cada deporte de cada deportista
	    	int[][] matrizResumenR = new int[Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO]
	    			[Main.NUMERO_DE_EJERCICIOS];
	    	
	    	
	    	for(int secuenciasPorEquipo = 0; secuenciasPorEquipo < Main.NUMERO_DE_SECUENCIAS;
	    			secuenciasPorEquipo++) {
	    		//Primero se genera una matriz por jugador
	    		for(int numJugadores = 0; numJugadores < Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO; 
	    				numJugadores++) {
	    			Main.generarMatriz(tablaDeMatricesDeEjercicios[numJugadores]);
	    		}
	    		
	    		//Se seleccionan y guardan las mejores marcas de cada jugador
	    		//sprint, salto, lanzamiento, resistencia, y agilidad
	    		for(int numJugadores = 0; numJugadores < Main.NUMERO_DE_DEPORTISTAS_POR_EQUIPO; 
	    				numJugadores++) {
	    			matrizResumenR[numJugadores] = mejoresMarcas(tablaDeMatricesDeEjercicios[numJugadores]);
	    		}
	    		
	    		//Bloqueo el acceso a los paneles para que solo puedan estar el mismo número de procesos
	    		//que paneles hay.
				try {
					Main.semaforoDePaneles.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		//Bloqueo el acceso a la varible que puede ser usada por más de un proceso a la vez para 
	    		//lectura y escritura, por un semáforo binario. 
				try {
					Main.mutexVariablesCompartidas.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		//Llegado este punto hay un panel libre asegurado por lo que uso un while para encontrar uno.
	    		int idPanel = 0;
	    		while(Main.panelesLibres[idPanel] != true) {
	    			idPanel++;
	    		}
	    		Main.panelesLibres[idPanel] = false;
	    		// Dejo libre el semáforo binario
	    		Main.mutexVariablesCompartidas.release();
	    		
	    		String mensajeParaPanel = new String("Usando panel " + idPanel + " el hilo (equipo) " + idPanel + "\n\n"
	    				+ "Matriz R (Resumen del equipo " + idEquipo + ": mejores marcas=\n" + matrizToString(matrizResumenR) + "\n"  
	    				+ "Terminando de usar panel " + idPanel + " el hilo (equipo) " + idEquipo + "\n");
	    		
	    		Main.paneles[idPanel].escribir_mensaje(mensajeParaPanel);
	    		
				try {
					Main.mutexVariablesCompartidas.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Main.panelesLibres[idPanel] = true;
				Main.mutexVariablesCompartidas.release();
				Main.semaforoDePaneles.release();
	    	}
	    }

}
