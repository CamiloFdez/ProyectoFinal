package dominio;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Representa un zombi con una cubeta en la cabeza, que le proporciona una gran protección.
 */
public class ZombieCubeta extends Zombie {
    private static final int COSTO = 200;
    private static final int VIDA_CUBETA = 700;
    private static final int VIDA_BASICA = 100;
    private static final int DAMAGE = 100;
    private static final double INTERVALO_ATAQUE = 0.5;

    private int vidaCubeta;

    /**
     * Constructor para crear un zombi con cubeta, con atributos predefinidos.
     */
    public ZombieCubeta() {
        super("Zombie Cubeta", VIDA_BASICA, DAMAGE, INTERVALO_ATAQUE);
        this.vidaCubeta = VIDA_CUBETA;
    }

    /**
     * Disminuye la salud del zombi, primero reduciendo la vida de la cubeta y luego la salud básica si es necesario.
     * @param amount La cantidad de daño a aplicar.
     */
    @Override
    public void decreaseHealth(int amount) {
        if (vidaCubeta > 0) {
            int dañoRestante = amount - vidaCubeta; // Calcula el daño sobrante
            vidaCubeta -= amount;

            if (vidaCubeta <= 0) {
                vidaCubeta = 0; // La cubeta se destruye
                if (dañoRestante > 0) {
                    super.decreaseHealth(dañoRestante); // Aplica el daño sobrante a la salud básica
                }
            }
        } else {
            super.decreaseHealth(amount); // Si la cubeta ya no está, el daño va a la salud básica
        }
    }


    /**
     * Verifica si el zombi tiene la cubeta en su cabeza.
     * @return true si el zombi tiene la cubeta, false si ya no le queda.
     */
    public boolean hasCubeta() {
        return vidaCubeta > 0;
    }

    /**
     * Obtiene la vida restante de la cubeta.
     * @return La cantidad de vida de la cubeta.
     */
    public int getVidaCubeta() {
        return vidaCubeta;
    }

    /**
     * Obtiene el costo de este zombi en cerebros.
     * @return El costo del zombi.
     */
    @Override
    public int getCosto() {
        return COSTO;
    }

    /**
     * Hace que el zombi se mueva en línea recta hacia las plantas.
     */
    @Override
    public void move() {
        System.out.println(getName() + " avanza en línea recta hacia las plantas.");
    }

    /**
     * Hace que el zombi ataque a una planta, reduciendo su salud.
     * Los ataques ocurren en intervalos de tiempo definidos.
     * @param plant La planta que será atacada por el zombi.
     */
    @Override
    public void attack(Planta plant) {
        if (plant != null && plant.isAlive()) {
            Timer attackTimer = new Timer();
            attackTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (plant.isAlive()) {
                        plant.decreaseHealth(getDamage());
                        System.out.println(getName() + " mordió a " + plant.getName() +
                                " causando " + getDamage() + " de daño. Salud restante de la planta: " + plant.getHealth());
                    } else {
                        System.out.println(plant.getName() + " ha sido destruida.");
                        attackTimer.cancel();
                    }
                }
            }, 0, (long) (getAttackInterval() * 1000));
        }
    }
}


