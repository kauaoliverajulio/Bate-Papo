import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServidorChat {

    private static List<PrintWriter> writers = new ArrayList<>();
    private static Map<PrintWriter, String> usuarios = new HashMap<>();

    public static void main(String[] args) {
        final int PORTA = 5000;

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor de chat iniciado na porta " + PORTA);
            System.out.println("Aguardando conexões de clientes...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writers.add(writer);

                Thread clientThread = new Thread(new ClientHandler(clientSocket, writer));
                clientThread.start();
            }

        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void broadcast(String message) {
        for (PrintWriter writer : writers) {
            writer.println(message);
        }
    }

    private static void broadcastUsuarios() {
        StringBuilder sb = new StringBuilder("USERS:");
        boolean first = true;
        for (String nome : usuarios.values()) {
            if (!first) sb.append(",");
            sb.append(nome);
            first = false;
        }
        broadcast(sb.toString());
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String nomeUsuario = "Desconhecido";

        public ClientHandler(Socket socket, PrintWriter writer) {
            this.clientSocket = socket;
            this.writer = writer;
            try {
                this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // Recebe o salt como primeira mensagem
                String linha = reader.readLine();
                if (linha != null && linha.startsWith("SALT:")) {
                    // Recebe o nome do usuário na próxima linha
                    linha = reader.readLine();
                }
                if (linha != null && !linha.isEmpty()) {
                    nomeUsuario = linha;
                    usuarios.put(writer, nomeUsuario);
                    broadcastUsuarios();
                }
                String message;
                while ((message = reader.readLine()) != null) {
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println(nomeUsuario + " desconectou.");
            } finally {
                writers.remove(writer);
                usuarios.remove(writer);
                broadcastUsuarios();
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}