package ejercicio4; //NOSONAR

import messagepassing.MailBox;

public class Main {
    public static final int NUMERO_DE_AFICIONADOS = 50;
    protected static final Aficionado[] AFICIONADOS = new Aficionado[NUMERO_DE_AFICIONADOS];
    protected static final MailBox[] buzonesAficionados = new MailBox[NUMERO_DE_AFICIONADOS];
    public static final MailBox determinarColaConTiempo = new MailBox();
    public static final MailBox pedirColaRapida = new MailBox();
    public static final MailBox pedirColaLenta = new MailBox();
    public static final MailBox liberarCola = new MailBox();
    public static final MailBox pedirPantalla = new MailBox();

    public static final ControladorDeAcceso CONTROLADOR_DE_ACCESO =
            new ControladorDeAcceso(determinarColaConTiempo, pedirColaRapida, pedirColaLenta, liberarCola,
                    buzonesAficionados);

    public static final char COLA_RAPIDA = 'R';
    public static final char COLA_LENTA = 'L';
    public static final char TOKEN_PANTALLA = 'P';

    public static void main(String[] args) {
        pedirPantalla.send(TOKEN_PANTALLA);
        CONTROLADOR_DE_ACCESO.start();
        for (int i = 0; i < NUMERO_DE_AFICIONADOS; i++) {
            buzonesAficionados[i] = new MailBox();
            AFICIONADOS[i] = new Aficionado(i, buzonesAficionados[i], determinarColaConTiempo, pedirColaRapida,
                    pedirColaLenta, liberarCola, pedirPantalla);
            AFICIONADOS[i].start();
        }
        for (int i = 0; i < NUMERO_DE_AFICIONADOS; i++) {
            try {
                AFICIONADOS[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            CONTROLADOR_DE_ACCESO.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Fin del hilo principal."); //NOSONAR
    }
}
