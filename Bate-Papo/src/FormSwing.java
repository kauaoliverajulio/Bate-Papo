import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class FormSwing extends JFrame {
    private SecretKeySpec chaveAES;
    private JTextArea areaMensagens;
    private JTextField campoTexto;
    private JButton botaoEnviar;
    private PrintWriter writer;
    private BufferedReader reader;
    private String nome;
    private String senha;
    private byte[] salt;
    private DefaultListModel<String> usuariosModel = new DefaultListModel<>();
    private JList<String> listaUsuarios = new JList<>(usuariosModel);

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

        JScrollPane scrollUsuarios = new JScrollPane(listaUsuarios);
        scrollUsuarios.setPreferredSize(new Dimension(120, 240));

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(scrollUsuarios, BorderLayout.EAST);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        this.setContentPane(painelPrincipal);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        conectarServidor("127.0.0.1", 5000);
        nome = JOptionPane.showInputDialog(this, "Digite seu nome:");
        senha = JOptionPane.showInputDialog(this, "Digite a senha do chat:");
        try {
            salt = CriptografiaAES.gerarSalt();
            // Envia o salt para o servidor como primeira mensagem
            writer.println("SALT:" + Base64.getEncoder().encodeToString(salt));
            // Envia o nome do usuário como segunda mensagem
            writer.println(nome);
            chaveAES = CriptografiaAES.gerarChave(senha, salt);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar chave AES.");
            System.exit(1);
        }
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
            try {
                String mensagemCriptografada = CriptografiaAES.criptografar(nome + ": " + texto, chaveAES);
                // Envia o salt junto com a mensagem criptografada
                String saltBase64 = Base64.getEncoder().encodeToString(salt);
                writer.println(saltBase64 + ":" + mensagemCriptografada);
                campoTexto.setText("");
            } catch (Exception ex) {
                areaMensagens.append("Erro ao criptografar mensagem.\n");
            }
        }
    }

    private void receberMensagens() {
        String linha;
        try {
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("SALT:")) {
                    // Ignora, pois já recebeu o próprio salt
                    continue;
                }
                if (linha.startsWith("USERS:")) {
                    atualizarListaUsuarios(linha.substring(6));
                    continue;
                }
                try {
                    // Espera formato saltBase64:mensagemCriptografada
                    String[] partes = linha.split(":", 2);
                    if (partes.length != 2) {
                        areaMensagens.append("Mensagem inválida recebida.\n");
                        continue;
                    }
                    byte[] saltRecebido = Base64.getDecoder().decode(partes[0]);
                    SecretKeySpec chaveRecebida = CriptografiaAES.gerarChave(senha, saltRecebido);
                    String mensagem = CriptografiaAES.descriptografar(partes[1], chaveRecebida);
                    areaMensagens.append(mensagem + "\n");
                } catch (Exception ex) {
                    areaMensagens.append("Erro ao descriptografar mensagem.\n");
                }
            }
        } catch (IOException e) {
            areaMensagens.append("Conexão encerrada.\n");
        }
    }

    private void atualizarListaUsuarios(String lista) {
        SwingUtilities.invokeLater(() -> {
            usuariosModel.clear();
            if (!lista.trim().isEmpty()) {
                for (String nome : lista.split(",")) {
                    usuariosModel.addElement(nome);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FormSwing::new);
    }
}