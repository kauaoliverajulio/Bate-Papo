import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class FormSwing extends JFrame {
    private JTextArea areaMensagens;
    private JTextField campoTexto;
    private JButton botaoEnviar;
    private PrintWriter writer;
    private BufferedReader reader;
    private String nome;

    public FormSwing() {
        super("Bate-Papo");

        areaMensagens = new JTextArea(15, 30);
        areaMensagens.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaMensagens);

        campoTexto = new JTextField(20);
        botaoEnviar = new JButton("Enviar");

        botaoEnviar.addActionListener(e -> enviarMensagem());
        campoTexto.addActionListener(e -> enviarMensagem());

        JPanel painelInferior = new JPanel();
        painelInferior.add(campoTexto);
        painelInferior.add(botaoEnviar);

        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);
        this.add(painelInferior, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        conectarServidor("127.0.0.1", 5000);
        nome = JOptionPane.showInputDialog(this, "Digite seu nome:");
        new Thread(this::receberMensagens).start();
    }

    private void conectarServidor(String host, int porta) {
        try {
            Socket socket = new Socket(host, porta);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor.");
            System.exit(1);
        }
    }

    private void enviarMensagem() {
        String texto = campoTexto.getText().trim();
        if (!texto.isEmpty()) {
            writer.println(nome + ": " + texto);
            campoTexto.setText("");
        }
    }

    private void receberMensagens() {
        String mensagem;
        try {
            while ((mensagem = reader.readLine()) != null) {
                areaMensagens.append(mensagem + "\n");
            }
        } catch (IOException e) {
            areaMensagens.append("Conexão encerrada.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FormSwing::new);
    }
}