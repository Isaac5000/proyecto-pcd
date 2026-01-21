package ejercicio1;

class Sumador {
	private int[] array;
	
	public Sumador(int[] array) {
		this.array = array;
	}
	
	public void sumar() {
		int suma = 0;
		for(int i = 0; i < array.length; i++) {
			suma = suma + array[i];
		}
		
		System.out.println("La solución de las sumas es: " + suma);
	}
	
}