package ejercicio4; //NOSONAR

import messagepassing.MailBox;

import java.util.Random;

public class Aficionado extends Thread {
    public static final int NUMERO_ITERACIONES = 5;
    public static final int CAMINAR_A_BANOS = 5;
    public static final int TIEMPO_MINIMO = 1;
    public static final int TIEMPO_MAXIMO_TORNO = 10;
    private static final Random RANDOM = new Random();

    public static final int PETICIONES_POR_HILO_A_CONTROLADOR = 3;

    private final int idAficionado;
    private final MailBox buzonAficionado;
    private final MailBox determinarColaConTiempo;
    private final MailBox pedirColaRapida;
    private final MailBox pedirColaLenta;
    private final MailBox liberarCola;
    private final MailBox pedirPantalla;

    public Aficionado(int idAficionado,
                      MailBox buzonAficionado,
                      MailBox determinarColaConTiempo,
                      MailBox pedirColaRapida,
                      MailBox pedirColaLenta,
                      MailBox liberarCola,
                      MailBox pedirPantalla) {
        this.idAficionado = idAficionado;
        this.buzonAficionado = buzonAficionado;
        this.determinarColaConTiempo = determinarColaConTiempo;
        this.pedirColaRapida = pedirColaRapida;
        this.pedirColaLenta = pedirColaLenta;
        this.liberarCola = liberarCola;
        this.pedirPantalla = pedirPantalla;
    }

    @Override
    public void run() {
        int T = 0;
        int tiempoParaIrABano = 0;
        for(int i = 0; i < NUMERO_ITERACIONES; i++) {
            tiempoParaIrABano = RANDOM.nextInt(CAMINAR_A_BANOS) + TIEMPO_MINIMO;
            try {
                Thread.sleep(tiempoParaIrABano);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            T = RANDOM.nextInt(TIEMPO_MAXIMO_TORNO) + TIEMPO_MINIMO;
            int[] tuplaIDTiempo = new int[]{idAficionado, T};
            determinarColaConTiempo.send(tuplaIDTiempo);
            char colaSeleccionada = (char) buzonAficionado.receive();

            if (colaSeleccionada == Main.COLA_RAPIDA) {
                pedirColaRapida.send(idAficionado);
            } else {
                pedirColaLenta.send(idAficionado);
            }

            buzonAficionado.receive();
            try{
                Thread.sleep(T);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            liberarCola.send(colaSeleccionada);
            pedirPantalla.receive();
            System.out.println("“Aficionado " + idAficionado + " ha usado la cola " + colaSeleccionada + "\n"
            + "Tiempo de validación = " + T + "\n"
            + "Thread.sleep(" + T + ")\n"
            + "Aficionado " + idAficionado + " liberando la cola " + colaSeleccionada  + "”\n" );
            pedirPantalla.send(Main.TOKEN_PANTALLA);
        }
    }

}
