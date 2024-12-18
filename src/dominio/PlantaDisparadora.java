package dominio;

/**
 * Representa una planta que puede disparar proyectiles en el juego.
 */
public abstract class PlantaDisparadora extends Planta {
    private int damage;
    private double shootInterval;

    public PlantaDisparadora(String name, int health, int damage, double shootInterval) {
        super(name, health);
        this.damage = damage;
        this.shootInterval = shootInterval;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getShootInterval() {
        return shootInterval;
    }

    public void setShootInterval(double shootInterval) {
        this.shootInterval = shootInterval;
    }

    /**
     * Dispara un proyectil que inflige daño al enemigo.
     *
     * @return la cantidad de daño causado.
     */
    public int shoot() {
        return damage;
    }

    @Override
    public abstract void performAction();

    /**
     * Lógica que debe ejecutarse cuando la planta muere.
     */
    public abstract void onDeath();
}
