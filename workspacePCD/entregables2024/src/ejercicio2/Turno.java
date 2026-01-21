package ejercicio2;

public class Turno extends Thread {
	
	public static final int LIMITE_TIEMPO_SEMAFORO = 5000;
	
	@Override
	public void run() {
		while(true) {
			try {
				// Hago que se espere 5 segundos, transcurrido ese tiempo usa el semaforo de las variables,
				// cambia el turno al siguiente, en el caso de no haber nadie cruzando se manda una señal al
				// siguiente semaforo en verde
				Thread.sleep(LIMITE_TIEMPO_SEMAFORO);
				Main.semVariablesMutex.acquire();
				if(Main.turno < 2)
					Main.turno++;
				else Main.turno = 0;
				
				if(Main.nNS == 0 && Main.nEO == 0 && Main.nPe == 0) {
					if((Main.turno == 0) && (Main.nNSEsp > 0)) {
						Main.colaNS.release();
					}
					else if((Main.turno == 1) && (Main.nEOEsp > 0)) {
						Main.colaEO.release();
					}
					else if((Main.turno == 2) && (Main.nPeEsp > 0)) {
						Main.colaPe.release();
					}
				}
				else Main.semVariablesMutex.release();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
