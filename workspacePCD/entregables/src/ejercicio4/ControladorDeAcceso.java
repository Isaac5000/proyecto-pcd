package ejercicio4; //NOSONAR

import messagepassing.MailBox;
import messagepassing.Selector;

public class ControladorDeAcceso extends Thread {
    private static final int TIEMPO_MAXIMO_COLA_RAPIDA = 5;
    private static final int POSICION_ID_CLIENTE = 0;
    private static final int POSICION_TIEMPO_CLIENTE = 1;
    private static final String CADENA_VACIA = "";
    private boolean libreColaRapida = true;
    private boolean libreColaLenta = true;

    private final Selector selector = new Selector();

    private final MailBox determinarColaConTiempo;
    private final MailBox pedirColaRapida;
    private final MailBox pedirColaLenta;
    private final MailBox liberarCola;

    private final MailBox[] buzonesAficionados;

    public ControladorDeAcceso(MailBox determinarColaConTiempo,
                               MailBox pedirColaRapida,
                               MailBox pedirColaLenta,
                               MailBox liberarCola,
                               MailBox[]  buzonesAficionados) {
        this.determinarColaConTiempo = determinarColaConTiempo;
        this.pedirColaRapida = pedirColaRapida;
        this.pedirColaLenta = pedirColaLenta;
        this.liberarCola = liberarCola;
        this.buzonesAficionados = buzonesAficionados;

        selector.addSelectable(determinarColaConTiempo, false);
        selector.addSelectable(pedirColaRapida, false);
        selector.addSelectable(pedirColaLenta, false);
        selector.addSelectable(liberarCola, false);

        pedirColaRapida.setGuardValue(libreColaRapida);
        pedirColaLenta.setGuardValue(libreColaLenta);
    }

    @Override
    public void run() {
        // for(int i = 0; i < 50*3*5; i++){
        while (true) {
            switch (selector.selectOrBlock()){
                case 1:{ // Selector para pedir cola
                    int[] tuplaIDTiempo = (int[]) determinarColaConTiempo.receive();
                    int id = tuplaIDTiempo[POSICION_ID_CLIENTE];
                    int tiempo = tuplaIDTiempo[POSICION_TIEMPO_CLIENTE];
                    if(tiempo <= TIEMPO_MAXIMO_COLA_RAPIDA)
                        buzonesAficionados[id].send(Main.COLA_RAPIDA);
                    else
                        buzonesAficionados[id].send(Main.COLA_LENTA);
                    break;
                }
                case 2: { // Pedir cola rápida
                    int id = (int) pedirColaRapida.receive();
                    libreColaRapida = false;
                    buzonesAficionados[id].send(CADENA_VACIA);
                    break;
                }
                case 3:{ // Pedir cola lenta
                    int id = (int) pedirColaLenta.receive();
                    libreColaLenta = false;
                    buzonesAficionados[id].send(CADENA_VACIA);
                    break;
                }
                case 4: { // Liberar cola
                    char colaSeleccionada = (char) liberarCola.receive();
                    if (colaSeleccionada == Main.COLA_RAPIDA) {
                        libreColaRapida = true;
                    } else {
                        libreColaLenta = true;
                    }
                }
            }
        }
    }
}
