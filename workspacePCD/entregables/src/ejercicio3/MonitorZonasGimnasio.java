package ejercicio3; //NOSONAR

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorZonasGimnasio {
    private static final int NUMERO_DE_ZONAS = 4;
    private static final int MAQUINAS_POR_ZONA = 5;

    private final int[] maquinasPorZonaLibres = new int[NUMERO_DE_ZONAS];
    private final Condition[] colasDeZonas = new Condition[NUMERO_DE_ZONAS];
    private final int[] tiemposEnZonas = new int[NUMERO_DE_ZONAS];
    private boolean bicicletaPremiumLibre = true;
    private final ReentrantLock l = new ReentrantLock();
    private final Condition colaBicicletaPremium = l.newCondition();

    // Constantes para que no hallan números mágicos
    private static final int MEJOR_ZONA_POR_DEFECTO = 0;
    private static final int TIEMPO_INICIAL_POR_ZONA = 0;
    private static final int ESPACIO_PARA_ZONA_Y_TIEMPOS = 1 + NUMERO_DE_ZONAS;
    private static final int POSICION_INICIAL_DE_TIEMPOS = 1;
    private static final Random RANDOM = new Random();
    private static final int POSICION_DE_ID_DE_ZONA = 0;

    public MonitorZonasGimnasio(){
        for (int i = 0; i < NUMERO_DE_ZONAS; i++) {
            maquinasPorZonaLibres[i] = MAQUINAS_POR_ZONA;
            colasDeZonas[i] = l.newCondition();
            tiemposEnZonas[i] = TIEMPO_INICIAL_POR_ZONA;
        }
    }

    /**
     * Función auxiliar para sacar una zono y todos los tiempos en las zonas en un array.
     * En caso de haber más de una zona libre se selecciona una aleatoria, si estuvieran
     * todas ocupadas se elige la que mejor tiempo tenga.
     * @return array que en la primera posición tiene el ID de la zona elegida
     * y en el resto de posiciones los tiempos de todas las zonas.
     */
    private int[] obtenerZonaYTodosLosTiempos(){

        int[] zonaElegidaYTiempos = new int[ESPACIO_PARA_ZONA_Y_TIEMPOS];
        ArrayList<Integer> zonasConMaquinasLibres = new ArrayList<>();
        int zonaLlenaConMejorTiempo = MEJOR_ZONA_POR_DEFECTO;
        for(int i = 0; i < NUMERO_DE_ZONAS; i++){
            if(maquinasPorZonaLibres[i] > 0){
                zonasConMaquinasLibres.add(i);
            }
            else if(tiemposEnZonas[zonaLlenaConMejorTiempo] > tiemposEnZonas[i]){
                zonaLlenaConMejorTiempo = i;
            }
            zonaElegidaYTiempos[i + POSICION_INICIAL_DE_TIEMPOS] = tiemposEnZonas[i];
        }
        if(!zonasConMaquinasLibres.isEmpty()){
            zonaElegidaYTiempos[POSICION_DE_ID_DE_ZONA] = zonasConMaquinasLibres
                    .get(RANDOM.nextInt(zonasConMaquinasLibres.size()));
        }
        else{
            zonaElegidaYTiempos[POSICION_DE_ID_DE_ZONA] = zonaLlenaConMejorTiempo;
        }
        return zonaElegidaYTiempos;
    }

    /**
     * Función que se encarga de seleccionar una zona y devolverla junto a los tiempos.
     * Si estuviera llena nos bloquearíamos hasta que se liberara una máquina.
     * @param Y es el tiempo que va a estar en la zona quien la pide.
     * @return posición de zona elegida en el primer elemento del array y el resto son
     * los tiempos.
     */
    public int[] accederAZonaYRecuperarTiempos(int Y) {
        l.lock();
        try {
            int[] idZonaYTiempos = obtenerZonaYTodosLosTiempos();
            int idZona = idZonaYTiempos[POSICION_DE_ID_DE_ZONA];
            tiemposEnZonas[idZona] += Y;

            while (maquinasPorZonaLibres[idZona] <= 0) {
                try {
                    colasDeZonas[idZona].await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            maquinasPorZonaLibres[idZona]--;

            return idZonaYTiempos;
        } finally {
            l.unlock();
        }
    }

    public void entrarEnBicicletaPremium() {
        l.lock();
        try {
            while (!bicicletaPremiumLibre) {
                try {
                    colaBicicletaPremium.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            bicicletaPremiumLibre = false;
        } finally {
            l.unlock();
        }
    }

    public void salirDeBicicletaPremium() {
        l.lock();
        try {
            bicicletaPremiumLibre = true;
            colaBicicletaPremium.signal();
        } finally {
            l.unlock();
        }
    }

    public void salirDeZona(int Y, int idZona) {
        l.lock();
        try {
            maquinasPorZonaLibres[idZona]++;
            tiemposEnZonas[idZona] -= Y;
            colasDeZonas[idZona].signal();
        } finally {
            l.unlock();
        }
    }
}
