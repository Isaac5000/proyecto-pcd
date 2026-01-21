package ejercicio3;

import java.util.concurrent.locks.*;

public class Banco {
	private static final int NUM_MAQUINAS = 3;
	private static final int NUM_MESAS = 4;
	private static final int INICIO_TIEMPO = 0;
	private static final int MESA_ELEGIDA = 1;
	
	private boolean pantallaOcupada = false;						// Sirve para bloquear los que entren al metodo de pedirPantalla si esta ya esta siendo usada
	
	private int numMaquinasOcupadas;
	private boolean[] maquinasLibres = new boolean[NUM_MAQUINAS];	// Se guarda como array de booleanos las máquinas libres
	private ReentrantLock l = new ReentrantLock();					// Cerrojo que utilizarán todas las colas
	private Condition colaMaquinas = l.newCondition();				// Bloquea en la cola para esperar a que halla una máquina
	private Condition colaPantalla = l.newCondition();				// Bloquea en la cola para esperar a la pantalla
	private int[] tiemposMesas = new int[NUM_MESAS];             	// Se guardan los tiempos que faltan en cada mesa
	private Condition[] colasMesas = new Condition[NUM_MESAS];		// Se guardan todas las colas para las mesas
	private boolean[] mesasLibres = new boolean[NUM_MESAS];			// Se guardan como array de booleanos las mesas
	
	public Banco() {
		numMaquinasOcupadas = 0;
		
		for(int i = 0; i < NUM_MAQUINAS; i++) {
			maquinasLibres[i] = true;				// Todas las máquinas libres
		}
		
		for(int i = 0; i < NUM_MESAS; i++) {
			mesasLibres[i] = true;
		}
		
		for(int i = 0; i < NUM_MESAS; i++) {
			colasMesas[i] = l.newCondition();		// Lista de colas para maquinas
			tiemposMesas[i] = INICIO_TIEMPO;		// Iniciar los tiempos de las máquinas
		}
	}
	
	
	// En este metodo el cliente obtiene una máquina si hay alguna libre, si no se esperara en 
	// colaMaquinas hasta que se libere alguna máquina, en tal caso se liberara un cliente al azar
	public int pedirMaquina() {
		l.lock();
		try {
			while(numMaquinasOcupadas >= NUM_MAQUINAS) {
				try {
					colaMaquinas.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Si llega hasta aquí es que hay máquinas libres
			int idMaquina = 0;
			// Busqueda de máquina libre
			while(!maquinasLibres[idMaquina]) 
				idMaquina++;
			
			maquinasLibres[idMaquina] = false;
			numMaquinasOcupadas++;
			return idMaquina;
		} finally {
			l.unlock();
		}
	}
	
	// Aquí se libera la máquina antes de pasar a la mesa
	public void liberarMaquina(int idMaquina) {
		l.lock();
		try {
			maquinasLibres[idMaquina] = true;
			numMaquinasOcupadas--;
			colaMaquinas.signal();
		} finally {
			l.unlock();
		}
	}
	
	// Se pide la pantalla y excoge una máquina
	// Utilizará el identificador de su máquina, la Y del cliente
	public int[] pedirMesa(int tiempoMesaCliente) throws InterruptedException {
		l.lock();
		try {
			// Este es el array para el return
			int[] mesaElegidaYTiempoMesas = new int[MESA_ELEGIDA + NUM_MESAS];
			
			// Se seleciona la máquina que se va a utilizar, se empieza por defecto por la primera
			// y en el bucle compruebo si hay con menores tiempos de espera
			int mesaElegida = 0;
			for(int i = 0; i < NUM_MESAS; i++) {
				if(tiemposMesas[i] < tiemposMesas[mesaElegida]) {
					mesaElegida = i;
				}
				// De esta forma se guardaran los tiempos de las mesas antes de que se meta tambien este cliente
				mesaElegidaYTiempoMesas[MESA_ELEGIDA + i] = tiemposMesas[i];
			}
			
			// Se le suma al tiempo de la máquina el del cliente que va a utilizarla
			tiemposMesas[mesaElegida] += tiempoMesaCliente;
			
			mesaElegidaYTiempoMesas[0] = mesaElegida;
			
			return mesaElegidaYTiempoMesas;
		} finally {
			l.unlock();
		}
	}
	
	public void pedirPantalla() {
		l.lock();
		try {
			while(pantallaOcupada == true) {
				try {
					colaPantalla.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			pantallaOcupada = true;
			
		} finally  {
			l.unlock();
		}
		
	}
	
	public void liberarPantalla() {
		l.lock();
		try {
			pantallaOcupada = false;
			colaPantalla.signal();
		} finally {
			l.unlock();
		}
	}

	
	public void sentarseMesa(int idMesa) {
		l.lock();
		try {
			try {
				while(!mesasLibres[idMesa]) {
					colasMesas[idMesa].await();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mesasLibres[idMesa] = false;
		} finally {
			l.unlock();
		}
	}
	
	public void dejarMesa(int idMesa, int tiempoY) {
		l.lock();
		try {
			tiemposMesas[idMesa] = tiemposMesas[idMesa] - tiempoY;
			mesasLibres[idMesa] = true;
			colasMesas[idMesa].signal();
		} finally {
			l.unlock();
		}
	}
	
	
}
