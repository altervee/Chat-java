import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Log extends JFrame {
    private JButton button1;
    private JTextField textField1;

    public Log() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(10, 20, 80, 25);
        panel.add(lblUsuario);

        textField1 = new JTextField();
        textField1.setBounds(100, 20, 160, 25);
        panel.add(textField1);

        button1 = new JButton("Ingresar");
        button1.setBounds(100, 60, 100, 25);
        panel.add(button1);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = textField1.getText();
                if (nombreUsuario.isEmpty()) {
                    JOptionPane.showMessageDialog(Log.this, "Por favor, ingrese un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (nombreUsuarioEnUso(nombreUsuario)) {
                    JOptionPane.showMessageDialog(Log.this, "El nombre de usuario ya está en uso. Por favor, elija otro.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Avanzar a la siguiente pantalla
                    dispose(); // Cerrar la ventana actual del log
                    // Código para abrir la siguiente pantalla aquí
                }
            }
        });
    }

    // Método para verificar si el nombre de usuario ya está en uso
    private boolean nombreUsuarioEnUso(String nombreUsuario) {
        // Aquí deberías implementar la lógica para verificar si el nombre de usuario ya está en uso en tu sistema.
        // Por ejemplo, podrías consultar una base de datos o una estructura de datos en memoria que almacene los nombres de usuario.
        // Por ahora, simplemente retornamos false para simular que el nombre de usuario no está en uso.
        return false;
    }

    public static void main(String[] args) {
        Log log = new Log();
        log.setVisible(true);
    }
}