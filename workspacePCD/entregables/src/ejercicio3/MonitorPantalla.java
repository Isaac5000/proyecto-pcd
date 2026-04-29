package ejercicio3; //NOSONAR

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("java:S106")
public class MonitorPantalla {
	private final ReentrantLock l = new ReentrantLock();
	private final Condition colaPantalla = l.newCondition();
	private boolean pantallaLibre = true;
	
	/**
	 * Recibe una cadena de texto y la imprime en exclusión mutua.
	 * @param mensaje es la cadena para imprimir.
	 */
	public void usarPantalla(String mensaje) {
		l.lock();
		try {
			while (!pantallaLibre) {
				try {
					colaPantalla.await();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			pantallaLibre = false;
			System.out.println(mensaje);
			pantallaLibre = true;
			colaPantalla.signal();
		} finally {
			l.unlock();
		}
	}
}
