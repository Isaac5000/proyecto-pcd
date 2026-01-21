package ejercicio2;

import java.util.concurrent.Semaphore;

public class Main {
	public static final int NUM_VEHICULOS = 50;
	public static final int NUM_PEATONES = 100;
	
	public static Semaphore semVariablesMutex = new Semaphore(1); // Semaforo que regula las variables compartidas
	public static Turno elegirTurno = new Turno(); // Variable global para regular los turnos como hilo
	
	public static Semaphore colaNS = new Semaphore(0); // Semaforo que regula la cola NS
	public static Semaphore colaEO = new Semaphore(0); // Semaforo que regula la cola EO
	public static int nNS = 0; // Coches cruzando NS
	public static int nEO = 0; // Coches cruzando EO
	public static int nNSEsp = 0; // Coches esperando NS
	public static int nEOEsp = 0; // Coches esperando EO
	
	public static Semaphore colaPe = new Semaphore(0); // Semaforo que regula la cola de peatones
	public static int nPe = 0; // Peatones cruzando
	public static int nPeEsp = 0; // Peatones esperando
	
	
	public static int turno = 0; // 0 = NS, 1 = EO y 2 = peatones
	
	public static Semaphore printable = new Semaphore(1);
	
	public static void main(String[] args) {
		
		Peaton[] peatones = new Peaton[NUM_PEATONES];
		Vehiculo[] vehiculos = new Vehiculo[NUM_VEHICULOS];
		
		for(int i = 0; i < NUM_VEHICULOS; i++) {
			vehiculos[i] = new Vehiculo(i);
		}
		
		for(int i = 0; i < NUM_PEATONES; i++) {
			peatones[i] = new Peaton(i);
		}
		
		elegirTurno.start();
		
		for(Vehiculo vehiculo : vehiculos) {
			vehiculo.start();
		}
		
		for(Peaton peaton : peatones) {
			peaton.start();
		}
		
		
		for(Peaton peaton : peatones) {
			try {
				peaton.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(Vehiculo vehiculo : vehiculos) {
			try {
				vehiculo.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			elegirTurno.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("El hilo principal ha terminado");
		
	}
}
