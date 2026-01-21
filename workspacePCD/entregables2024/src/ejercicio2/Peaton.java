package ejercicio2;

public class Peaton extends Thread {
	
	public static final int TIEMPO_CRUCE_PEATONES = 3000;
	public static final int TIEMPO_ESPERA_PEATONES = 8000;
	public static final int LIMITE_PEATONES = 10;
	
	
	private int idp;
	
	public Peaton(int idp) {
		this.idp = idp;
	}
	
	public int getIdp() {
		return idp;
	}
	
	@Override
	public void run() {
		while(true) {
		try {
			
			Main.semVariablesMutex.acquire();	
			
			if(Main.turno != 2 || Main.nPe >= LIMITE_PEATONES || (Main.nNS > 0 || Main.nEO > 0)) {
				Main.nPeEsp++;
				Main.semVariablesMutex.release();
				
				Main.colaPe.acquire();
				Main.nPeEsp--;
			}
			
			Main.nPe++;
			// Si hay vehiculos esperando y los vehiculos cruzando no superan el limite entra otro
			if(Main.nPeEsp > 0 && Main.nPe < LIMITE_PEATONES) {
				Main.colaPe.release();
			}
			else {
				Main.semVariablesMutex.release();
			}
			
			Thread.sleep(TIEMPO_CRUCE_PEATONES);
			
			Main.printable.acquire();
			System.out.println("Cruzando peaton: " + idp);
			Main.printable.release();
			
			Main.semVariablesMutex.acquire();
			Main.nPe--;
			
			if(Main.turno == 2 && Main.nPeEsp > 0) {
				Main.colaPe.release();
			}
			else if(Main.turno == 0 && Main.nNSEsp > 0 && Main.nPe == 0) {
				Main.colaNS.release();
			}
			else if(Main.turno == 1 && Main.nEOEsp > 0 && Main.nPe == 0) {
				Main.colaEO.release();
			}
			else {
				Main.semVariablesMutex.release();
			}
			
			Thread.sleep(TIEMPO_ESPERA_PEATONES);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		
		
	}
}
