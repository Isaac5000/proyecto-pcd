package ejercicio3;

public class Main {
	public static final Banco banco = new Banco();
	public static final int NUMERO_CLIENTES = 50;
	
	
	public static void main(String[] args) {
		Cliente[] clientes = new Cliente[NUMERO_CLIENTES];
		for(int i = 0; i < NUMERO_CLIENTES; i++) {
			clientes[i] = new Cliente(i+1);
		}
		
		for(Cliente cliente : clientes) {
			cliente.start();
		}
		
		for(Cliente cliente : clientes) {
			try {
				cliente.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("El hilo principal ha terminado");
		
	}
	
}
