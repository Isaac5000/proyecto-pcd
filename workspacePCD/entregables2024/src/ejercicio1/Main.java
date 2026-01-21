package ejercicio1;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
	
	public static final int TAMANO_OPERACIONES = 110; 
	public static final int TAMANO_RESULTADOS = 10; 
    public static final int[] OPERACIONES = new int[TAMANO_OPERACIONES];
    public static final int[] RESULTADOS = new int[TAMANO_RESULTADOS];
    public static ReentrantLock lock = new ReentrantLock();
	
	public static void main(String[] args) {

		// Lleno el array de la forma num op num op num op num op num op num
		Generador generador = new Generador(OPERACIONES);
		generador.generar();
		// generador.imprimirArray(); // Solo imprime el array si se quita el comentario en filas de 11

		// Crear 10 hilos con la id de 0-9
		Consumidor[] consumidores = new Consumidor[10];
		int idNueva = 0;
		for (int i = 0; i < 10; i++) {
			consumidores[i] = new Consumidor(idNueva);
			idNueva = idNueva + 1;
		}
		
		// Ejecuta el run de los hilos
		for (Consumidor consumidor : consumidores) {
			consumidor.start(); 
		}
		
		// Espera a que su hilo correspondiente se ejecute
		for (Consumidor consumidor : consumidores) {
			try {
				consumidor.join(); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Imprimir solución del sumador
		Sumador sumador = new Sumador(RESULTADOS);
		sumador.sumar();
		

	}
}