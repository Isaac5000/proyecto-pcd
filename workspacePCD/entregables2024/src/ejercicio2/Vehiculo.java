package ejercicio2;

public class Vehiculo extends Thread {
	
	public static final int TIEMPO_CRUCE_VEHICULO = 500;
	public static final int TIEMPO_ESPERA_VEHICULOS = 7000;
	public static final int LIMITE_VEHICULOS = 4;
	
	
	private int idv;
	
	public Vehiculo(int idv) {
		this.idv = idv;
	}
	
	public int getIdv() {
		return idv;
	}
	
	@Override
	public void run() {
		while(true) {
		try {
			Main.semVariablesMutex.acquire();
			
			if(Main.turno != 0 || Main.nNS >= LIMITE_VEHICULOS || (Main.nEO > 0 || Main.nPe > 0)) {
				Main.nNSEsp++;
				Main.semVariablesMutex.release();
				
				Main.colaNS.acquire();
				Main.nNSEsp--;
			}
			
			Main.nNS++;
			// Si hay vehiculos esperando y los vehiculos cruzando no superan el limite entra otro
			
			if(Main.nNSEsp > 0 && Main.nNS < LIMITE_VEHICULOS) {
				Main.colaNS.release();
			}
			else {
				Main.semVariablesMutex.release();
			}
			
			Thread.sleep(TIEMPO_CRUCE_VEHICULO);
			
			Main.printable.acquire();
			System.out.println("Cruzando NS vehiculo: " + idv);
			Main.printable.release();
			
			Main.semVariablesMutex.acquire();
			Main.nNS--;
			
			
			if(Main.turno == 0 && Main.nNSEsp > 0) { // Turno de NS y hay NSEsp
				Main.colaNS.release(); // Pasa NS
			}
			else if(Main.turno == 1 && Main.nEOEsp > 0 && Main.nNS == 0) {
				Main.colaEO.release(); 
			}
			else if(Main.turno == 2 && Main.nPeEsp > 0 && Main.nNS == 0) {
				Main.colaPe.release();
			}
			else {
				Main.semVariablesMutex.release();
			}
			
			
			// Espera 7 segundos antes de entrar al otro semaforo
			Thread.sleep(TIEMPO_ESPERA_VEHICULOS);
			
			
			Main.semVariablesMutex.acquire();
			
			if(Main.turno != 1 || Main.nEO >= LIMITE_VEHICULOS || (Main.nNS > 0 || Main.nPe > 0)) {
				Main.nEOEsp++;
				Main.semVariablesMutex.release();
				
				Main.colaEO.acquire();
				Main.nEOEsp--;
			}
			
			Main.nEO++;
			// Si hay vehiculos esperando y los vehiculos cruzando no superan el limite entra otro
			if(Main.nEOEsp > 0 && Main.nEO < LIMITE_VEHICULOS) {
				Main.colaEO.release();
			}
			else {
				Main.semVariablesMutex.release();
			}
			
			Thread.sleep(TIEMPO_CRUCE_VEHICULO);
			
			Main.printable.acquire();
			System.out.println("Cruzando EO vehiculo: " + idv);
			Main.printable.release();
			
			Main.semVariablesMutex.acquire();
			Main.nEO--;
			
			
			if(Main.turno == 1 && Main.nEOEsp > 0) {
				Main.colaEO.release();
			}
			else if(Main.turno == 0 && Main.nNSEsp > 0 && Main.nEO == 0) {
				Main.colaNS.release();
			}
			else if(Main.turno == 2 && Main.nPeEsp > 0 && Main.nEO == 0) {
				Main.colaPe.release();
			}
			else {
				Main.semVariablesMutex.release();
			}
			
			// Espera 7 segundos antes de entrar al otro semaforo
			Thread.sleep(TIEMPO_ESPERA_VEHICULOS);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	
}
