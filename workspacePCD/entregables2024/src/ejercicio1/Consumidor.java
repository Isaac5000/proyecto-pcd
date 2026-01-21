package ejercicio1;


class Consumidor extends Thread {
	public static final int POSICION_NUMERO_SIGUIENTE = 2;
	public static final int POSICION_OPERACION = 1;
	public static final int SUMA = 1;
	public static final int RESTA = 2;
	
	private int id;

	public Consumidor(int id) {
		this.id = id;
	}

	private int resultado(int posicion) {
		
		int solucion = Main.OPERACIONES[posicion];
		int elementoSiguiente = 0;
		
		// num op num op num op num op num op num 
		// Una definición recursiva un poco rara:
		// coger | 1 = num op num | 2 = 1 op num | 3 = 2 op num | 4 = 3 op num | 5 = 4 op num
		for(int i = 0; i < 5; i++) {
			// Avanzo 2 para quedarme en el elemento despues de la operación
			elementoSiguiente = Main.OPERACIONES[posicion + POSICION_NUMERO_SIGUIENTE];
			// Elijo la operación
			switch (Main.OPERACIONES[posicion + POSICION_OPERACION]) {
			case SUMA:
				solucion += elementoSiguiente;
				break;
			case RESTA:
				solucion -= elementoSiguiente;
				break;
			default: // Multiplicación
				solucion *= elementoSiguiente;
				break;
			}
			// Avanzo 2 para quedarme en la posición del siguiente número
			posicion = posicion + POSICION_NUMERO_SIGUIENTE;
		}
		
		return solucion;

	}

	@Override
	public void run() {
		// Punto donde inicia el hilo
		int inicio = this.id * 11;
		int solucion = resultado(inicio);
		
		// Guardo el valor en el array de soluciones y no hay nigun problema con el resto de hilos
		// ya que cada hilo accede a posiciones diferentes
		Main.RESULTADOS[id] = solucion;
		
		// Aquí si que es necesario usar un cerrojo ya que la pantalla no debe ser un recurso compartido,
		// porque todo se imprimiría en esta de una forma muy caotica y sin ningun orden aparente ya que 
		// cada hilo la utilizaría cuando tubiese oportunidad
		Main.lock.lock();
		try {
			System.out.println("ID:" + this.id + " Valor:" + solucion);
			try {
				Thread.sleep((int) Math.round(Math.random()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			Main.lock.unlock();
		}

	}
}