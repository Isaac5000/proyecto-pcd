package ejercicio3;

public class Main {
	
	public static Maquina maquina = new Maquina(); 
	public static Lavadero lavadero = new Lavadero();
	public static Pantalla pantalla = new Pantalla();
	
	public static final int NUM_CLIENTES = 50;
	public static final int EMPEZAR_EN_UNO = 1;
	
	public static Cliente clientes[] = new Cliente[NUM_CLIENTES];
	
	public static void main(String[] args) {
		for(int i = 0; i < NUM_CLIENTES; i++) {
			clientes[i] = new Cliente(i + EMPEZAR_EN_UNO);
			clientes[i].start();
		}
		
		for(int i = 0; i < NUM_CLIENTES; i++) {
			try {
				clientes[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
