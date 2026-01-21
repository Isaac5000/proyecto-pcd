package ejercicio3;

import java.util.Random;

public class Cliente extends Thread {
	public static final int TIEMPO_MAQUINA_MAX = 5000;
	public static final int TIEMPO_MESA_MAX = 10000;
	
	
	private int idc;
	private int Xi;
	private int Yi;
	
	
	
	public Cliente(int idc){
		this.idc = idc;
		this.Xi = new Random().nextInt(TIEMPO_MAQUINA_MAX);
		this.Yi = new Random().nextInt(TIEMPO_MESA_MAX);
	}
	

	public int getIdc() {
		return idc;
	}

	public int getXi() {
		return Xi;
	}

	public int getYi() {
		return Yi;
	}
	
	@Override
	public void run() {
		try {
			int numMaquina = Main.banco.pedirMaquina();
			Thread.sleep(Xi);
			
			Main.banco.liberarMaquina(numMaquina);
			
			int[] mesas = Main.banco.pedirMesa(Yi);
			
			Main.banco.pedirPantalla();
			int mesaEscogida = mesas[0] + 1;
			System.out.println("--------------------------------------------------------------");
			System.out.println("Cliente " + idc + " ha solicitado su servicio en la máquina: " + numMaquina);
			System.out.println("Tiempo en solicitar el servicio: " + Xi);
			System.out.println("Será atendido en la mesa: " + mesaEscogida);
			System.out.println("Tiempo en la mesa = " + Yi);
			System.out.println("Tiempo de espera en la mesa1=" + mesas[1] + ", mesa2="+ mesas[2] + ", mesa3=" + mesas[3]+ ", mesa4=" + mesas[4]);
			System.out.println("--------------------------------------------------------------");
			Main.banco.liberarPantalla();
			
			Main.banco.sentarseMesa(mesas[0]);
			Thread.sleep(Yi);
			Main.banco.dejarMesa(mesas[0], Yi);
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
