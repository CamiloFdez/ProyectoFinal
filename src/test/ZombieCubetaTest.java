package test;

import static org.junit.jupiter.api.Assertions.*;
import dominio.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class ZombieCubetaTest {
    @org.junit.jupiter.api.Test
    void testDañoAcumuladoZombieCubeta() {
        ZombieCubeta zombieCubeta = new ZombieCubeta();
        int vidaCubetaInicial = 700;
        int vidaBasicaInicial = 100;

        // Reducir la vida de la cubeta completamente
        zombieCubeta.decreaseHealth(vidaCubetaInicial);
        assertFalse(zombieCubeta.hasCubeta(), "La cubeta debería haberse destruido completamente");
        assertEquals(vidaBasicaInicial, zombieCubeta.getHealth(), "La salud del zombi debería estar intacta después de perder la cubeta");

        // Reducir la salud básica del zombi
        zombieCubeta.decreaseHealth(50);
        assertEquals(vidaBasicaInicial - 50, zombieCubeta.getHealth(), "La salud del zombi debería disminuir después de perder la cubeta");
    }

    @org.junit.jupiter.api.Test
    public void testZombieCubetaResistenciaExtra() {
        ZombieCubeta zombie = new ZombieCubeta();

        // Aplica daño parcial (no suficiente para destruir la cubeta)
        zombie.decreaseHealth(200);
        assertTrue(zombie.hasCubeta(), "Zombie Cubeta debe seguir teniendo la cubeta después de recibir daño parcial.");
        assertEquals(500, zombie.getVidaCubeta(), "La vida de la cubeta debería reducirse a 500.");
        assertEquals(100, zombie.getHealth(), "La salud básica no debería disminuir mientras la cubeta resiste el daño.");
    }


    @org.junit.jupiter.api.Test
    public void testZombieCubetaSinResistencia() {
        ZombieCubeta zombie = new ZombieCubeta();
        zombie.decreaseHealth(800); // Daño total supera la cubeta y la salud básica
        assertEquals(0, zombie.getHealth(), "Zombie Cubeta debería quedar sin salud después del daño sobrante.");
    }

    @org.junit.jupiter.api.Test
    public void testZombieCubetaPierdeCubeta() {
        ZombieCubeta zombieCubeta = new ZombieCubeta();

        // Infligir daño hasta que pierda la cubeta
        zombieCubeta.decreaseHealth(700);

        assertFalse(zombieCubeta.hasCubeta(), "El zombie debería perder la cubeta después de recibir 700 de daño.");
        assertEquals(100, zombieCubeta.getHealth(), "La salud básica del zombie debería permanecer intacta después de perder la cubeta.");
    }

    @org.junit.jupiter.api.Test
    public void testZombieCubetaDañoExcesivo() {
        ZombieCubeta zombieCubeta = new ZombieCubeta();

        // Infligir daño total, incluyendo el daño básico
        zombieCubeta.decreaseHealth(850);

        assertFalse(zombieCubeta.hasCubeta(), "El zombie debería perder la cubeta.");
        assertEquals(0, zombieCubeta.getHealth(), "El zombie debería ser destruido tras recibir daño total.");
    }

    @org.junit.jupiter.api.Test
    public void testZombieCubetaGetCosto() {
        ZombieCubeta zombieCubeta = new ZombieCubeta();
        int costoEsperado = 200; // Según el valor definido en la clase ZombieCubeta
        assertEquals(costoEsperado, zombieCubeta.getCosto(), "El costo del ZombieCubeta debería ser 200.");
    }

    @org.junit.jupiter.api.Test
    public void testZombieCubetaMove() {
        ZombieCubeta zombieCubeta = new ZombieCubeta();

        // Captura la salida estándar
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        zombieCubeta.move();

        // Restaura la salida estándar
        System.setOut(System.out);

        // Elimina espacios y saltos de línea innecesarios
        String salidaReal = outContent.toString().trim();
        String mensajeEsperado = zombieCubeta.getName() + " avanza en línea recta hacia las plantas.";

        assertEquals(mensajeEsperado, salidaReal, "El método move debería imprimir el mensaje correcto.");
    }


    @org.junit.jupiter.api.Test
    public void testZombieCubetaAttack() throws InterruptedException {
        ZombieCubeta zombieCubeta = new ZombieCubeta();
        Planta planta = new Planta("Planta Básica", 200) {
            @Override
            public void performAction() {

            }
        }; // Planta con salud inicial de 200

        zombieCubeta.attack(planta);

        // Esperar lo suficiente para dos ataques (suponiendo que el intervalo es de 0.5 segundos)
        Thread.sleep(1100);

        int saludEsperada = 200 - (2 * zombieCubeta.getDamage()); // 2 ataques de 100 de daño cada uno
        assertEquals(saludEsperada, planta.getHealth(), "La salud de la planta debería reducirse por dos ataques consecutivos.");
    }

}

