package ejercicio1;

import java.util.Random;

public class Generador {
	public static final int PAR = 2;
	public static final int NUM_ALEATORIO = 10;
	public static final int OP_ALEATORIO = 3;
	
	private int[] array;

	public Generador(int[] array) {
		this.array = array;
	}
	
	// Se encarga de generar los 11 valores que van en cada 11 posiciones
	private void genera11(int i) {
		Random random = new Random();
		for (int j = 0; j < 11; j++) {
			if (j % PAR == 0) {
				int numero = random.nextInt(NUM_ALEATORIO);
				this.array[j + i] = numero;
			} else {
				int operacion = random.nextInt(OP_ALEATORIO) + 1;
				this.array[j + i] = operacion;

			}
		}
	}
	
	// Función principal para generar valores
	public void generar() {
		for (int i = 0; i < this.array.length; i += 11) {
			this.genera11(i);
		}
	}

	public void imprimirArray() {
		int n = 0;
		System.out.print("Fila " + n + ": ");
		for (int valor : this.array) {
			System.out.print(valor + " ");
			n++;
			if (n % 11 == 0  && n != 110) {
				System.out.println();
				System.out.print("Fila " + n/11 + ": ");
			}
			
		}
		System.out.println("\n");
	}
}
