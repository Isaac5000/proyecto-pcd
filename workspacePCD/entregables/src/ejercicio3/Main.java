package ejercicio3; //NOSONAR

public class Main {
	
	public static final int NUMERO_DE_CLIENTES = 50;
	public static final int TIEMPO_MAX_DE_X_TORNO = 5;
	public static final int TIEMPO_MAX_DE_Y_ZONA = 5;
	public static final int TIEMPO_MIN = 1;
	
	protected static final Cliente[] CLIENTES = new Cliente[NUMERO_DE_CLIENTES];
	public static final MonitorTornos MONITOR_TORNOS = new MonitorTornos();
	public static final MonitorZonasGimnasio MONITOR_ZONAS_GIMNASIO = new MonitorZonasGimnasio();
	public static final MonitorPantalla MONITOR_PANTALLA = new MonitorPantalla();
	
	public static void main(String[] args) {
		for(int i = 0; i < NUMERO_DE_CLIENTES; i++) {
			CLIENTES[i] = new Cliente(i);
			CLIENTES[i].start();
		}
		for(Cliente cliente : CLIENTES) {
			try {
				cliente.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
