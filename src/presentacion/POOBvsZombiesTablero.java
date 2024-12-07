package presentacion;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class POOBvsZombiesTablero extends JFrame {
    private JLabel imageLabel;
    private Clip musicClip;
    private List<String> selectedPlants;
    private JButton pauseButton;
    private boolean isMusicPlaying = true;
    private JButton menuButton;
    private JButton confButton;
    private JButton resetButton;


    public POOBvsZombiesTablero(Clip currentMusic, List<String> selectedPlants) {
        super("POOBvsZombies");
        this.selectedPlants = selectedPlants;
        // Pausar la música actual
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
        prepareElements();
        prepareActions();
        playNewMusic("/musica/musicaTablero.wav"); // Ruta al archivo de música nuevo
    }

    private void prepareElements() {
        menuButton = new JButton("Menú");
        confButton = new JButton("Configuración");
        resetButton = new JButton("Reiniciar");

        menuButton.setBackground(new Color(127, 121, 172));
        menuButton.setForeground(new Color(48, 228, 30));
        menuButton.setFont(new Font("Arial", Font.BOLD, 14));

        confButton.setBackground(new Color(127, 121, 172));
        confButton.setForeground(new Color(48, 228, 30));
        confButton.setFont(new Font("Arial", Font.BOLD, 14));

        resetButton.setBackground(new Color(127, 121, 172));
        resetButton.setForeground(new Color(48, 228, 30));
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));


        // Configuración general de la ventana
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear un panel de capas
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Imagen de fondo escalada
        imageLabel = new JLabel();
        setScaledBackgroundImage();
        imageLabel.setBounds(0, 0, getWidth(), getHeight()); // Ajustar el tamaño al panel
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER); // Añadir al nivel base

        // Panel para los botones de plantas y pala
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10)); // Alinear a la izquierda
        buttonPanel.setOpaque(false); // Hacer transparente
        buttonPanel.setBounds(0, 0, getWidth(), 70); // Posición en la parte superior
        buttonPanel.setBackground(new Color(111, 64, 48));

        // Panel para el boton de pausa
        JPanel pausePanel = new JPanel();
        pausePanel.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10)); // Alinear a la izquierda
        pausePanel.setOpaque(false); // Hacer transparente
        pausePanel.setBounds(-10, 0, getWidth(), 70); // Posición en la parte superior

        // Añadir pala al panel
        addImageButton(pausePanel, "/Imagenes/pala.png", "Acción 6");

        // Boton "pausa"
        pauseButton = new JButton("Pausa");
        pauseButton.setBackground(new Color(127, 121, 172));
        pauseButton.setForeground(new Color(48, 228, 30));
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.addActionListener(e -> showPauseDialog());
        pausePanel.add(pauseButton);


        // Añadir botones con imágenes si fueron seleccionados
        for (int i = 0; i < selectedPlants.size(); i++) {
            if (selectedPlants.get(i).equals("Acción 1")) {
                addImageButton(buttonPanel, "/Imagenes/girasol.png", "Acción 1");
            } else if ( selectedPlants.get(i).equals("Acción 2")) {
                addImageButton(buttonPanel, "/Imagenes/guisante.png", "Acción 2");
            } else if (selectedPlants.get(i).equals("Acción 3")) {
                addImageButton(buttonPanel, "/Imagenes/papa.png", "Acción 3");
            } else if (selectedPlants.get(i).equals("Accion 4")) {
                addImageButton(buttonPanel, "/Imagenes/patata.png", "Accion 4");
            } else if (selectedPlants.get(i).equals("Accion 5")) {
                addImageButton(buttonPanel, "/Imagenes/POOBplanta.png", "Accion 5");
            }
        }



        // Añadir el panel de botones a una capa superior
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);

        // Ajustar la imagen y los botones al cambiar el tamaño de la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                imageLabel.setBounds(0, 0, getWidth(), getHeight());
                buttonPanel.setBounds(0, 0, getWidth(), 90);
                setScaledBackgroundImage();
            }
        });

        // Agregar un MouseListener para detectar clics en el tablero
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtener las coordenadas del clic
                int x = e.getX();
                int y = e.getY();

                // Convertir las coordenadas del clic en las celdas de la cuadrícula 5x9
                int row = y / (getHeight() / 5); // Dividir la altura de la ventana entre las 5 filas
                int col = x / (getWidth() / 9); // Dividir el ancho de la ventana entre las 9 columnas

                System.out.println("Clic en la celda: (" + row + ", " + col + ")");
            }
        });
    }


    private void setScaledBackgroundImage() {
        // Cargar y escalar la imagen para que ocupe toda la ventana
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Imagenes/tablero.png")); // Ruta relativa
        Image scaledImage = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
    }

    private void playNewMusic(String musicPath) {
        try {
            // Obtén el recurso como InputStream desde el classpath
            InputStream audioSrc = getClass().getResourceAsStream(musicPath);
            if (audioSrc == null) {
                System.err.println("Error: No se encontró el recurso: " + musicPath);
                return;
            }

            // Carga el InputStream en un AudioInputStream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);

            // Configura y reproduce el audio
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Reproducir en bucle
            clip.start();
            isMusicPlaying = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    private String getImagePath(String actionCommand) {
        switch (actionCommand) {
            case "Acción 1":
                return "/Imagenes/girasol.png";
            case "Acción 2":
                return "/Imagenes/guisante.png";
            case "Acción 3":
                return "/Imagenes/papa.png";
            default:
                return null;
        }
    }

    private void showPauseDialog() {
        JDialog settingsDialog = new JDialog(this, "Pausa", true);
        settingsDialog.setLayout(new BorderLayout());
        settingsDialog.setSize(300, 200);
        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.getContentPane().setBackground(new Color(8, 105, 14));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.add(Box.createVerticalGlue()); // Centrar verticalmente
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(new Color(73, 67, 77));
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(menuButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(confButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(resetButton);
        menuPanel.add(Box.createVerticalGlue()); // Agrega espacio al final


        JPanel contentPanel = createContentPanel();

        settingsDialog.add(menuPanel, BorderLayout.CENTER);

        // Botón de Cerrar
        JButton closeButton = createButton("ACEPTAR");
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> settingsDialog.dispose());

        settingsDialog.add(closeButton, BorderLayout.SOUTH);
        settingsDialog.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(127, 121, 172));
        button.setForeground(new Color(48, 228, 30));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(new Color(73, 67, 77));

        // Activar Música
        JCheckBox musicCheckBox = new JCheckBox("ACTIVAR MUSICA");
        musicCheckBox.setSelected(isMusicPlaying);
        musicCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
        musicCheckBox.setForeground(new Color(127, 121, 172));
        musicCheckBox.setBackground(new Color(73, 67, 77));
        musicCheckBox.addActionListener(e -> toggleMusic(musicCheckBox.isSelected()));

        // Pantalla Completa
        JCheckBox fullScreenCheckBox = new JCheckBox("PANTALLA COMPLETA");
        fullScreenCheckBox.setSelected(getExtendedState() == JFrame.MAXIMIZED_BOTH);
        fullScreenCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
        fullScreenCheckBox.setForeground(new Color(127, 121, 172));
        fullScreenCheckBox.setBackground(new Color(73, 67, 77));
        fullScreenCheckBox.addActionListener(e -> toggleFullScreen(fullScreenCheckBox.isSelected()));

        contentPanel.add(musicCheckBox);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(fullScreenCheckBox);

        return contentPanel;
    }

    private void addImageButton(JPanel panel, String imagePath, String actionCommand) {
        try {
            // Crear el botón con la imagen como ícono
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);

                // Escalar la imagen al tamaño deseado (ejemplo: 70x70 píxeles)
                int buttonSize = 67;
                Image scaledImage = originalIcon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                JButton button = new JButton(scaledIcon);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize)); // Establecer tamaño fijo
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setFocusPainted(false);

                // Agregar un listener para manejar la acción del botón
                button.setActionCommand(actionCommand);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Botón presionado: " + e.getActionCommand());
                    }
                });

                // Añadir el botón al panel
                button.setBounds(500, 500, buttonSize, buttonSize);
                panel.add(button);
            } else {
                System.err.println("No se encontró la imagen: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Alternar música según el estado del JCheckBox
    public void toggleMusic(boolean playMusic) {
        if (playMusic) {
            playBackgroundMusic("/musica/musicaTablero.wav");
        } else {
            stopBackgroundMusic();
        }
    }

    // Alternar pantalla completa
    public void toggleFullScreen(boolean fullScreen) {
        if (fullScreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            dispose();
            setUndecorated(true);
            setVisible(true);
        } else {
            dispose();
            setUndecorated(false);
            setSize(1024, 768);
            setExtendedState(JFrame.NORMAL);
            setVisible(true);
            setLocationRelativeTo(null);
        }
    }

    private Clip clip;

    public void playBackgroundMusic(String resourcePath) {
        try {
            // Obtén el recurso como InputStream desde el classpath
            InputStream audioSrc = getClass().getResourceAsStream(resourcePath);
            if (audioSrc == null) {
                System.err.println("Error: No se encontró el recurso: " + resourcePath);
                return;
            }

            // Carga el InputStream en un AudioInputStream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);

            // Configura y reproduce el audio
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Reproducir en bucle
            clip.start();
            isMusicPlaying = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    // Detener música de fondo
    private void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        isMusicPlaying = false;
    }

    private void prepareActions() {
        if (menuButton != null) {
            menuButton.addActionListener(e -> goToMenu());
        }
        if (confButton != null) {
            confButton.addActionListener(e -> showConfigurationDialog());
        }
        if (resetButton != null) {
            resetButton.addActionListener(e -> resetGame());
        }
    }

    private void goToMenu() {
        stopBackgroundMusic();
        POOBvsZombiesGUI menu = new POOBvsZombiesGUI();
        menu.setVisible(true);
        dispose();
    }

    private void showConfigurationDialog() {
        JPanel contentPanel = createContentPanel();
        JOptionPane.showMessageDialog(this, contentPanel, "Configuración", JOptionPane.PLAIN_MESSAGE);
    }

    private void resetGame() {
        POOBvsZombiesChoosePlants plantas = new POOBvsZombiesChoosePlants(clip);
        plantas.setVisible(true);
        dispose();
    }
}
