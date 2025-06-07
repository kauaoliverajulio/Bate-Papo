import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorChat {

    // Lista para armazenar os PrintWriters de todos os clientes conectados.
    // PrintWriter é usado para enviar mensagens para os clientes.
    private static List<PrintWriter> writers = new ArrayList<>();

    public static void main(String[] args) {
        final int PORTA = 5000; // Define a porta em que o servidor irá rodar.

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor de chat iniciado na porta " + PORTA);
            System.out.println("Aguardando conexões de clientes...");

            while (true) { // Loop infinito para aceitar novas conexões
                // O método accept() bloqueia a execução até que um cliente se conecte.
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Cria um PrintWriter para este novo cliente.
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writers.add(writer);

                // Cria e inicia uma nova thread para lidar com as mensagens deste cliente.
                Thread clientThread = new Thread(new ClientHandler(clientSocket, writer));
                clientThread.start();
            }

        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para transmitir a mensagem para todos os clientes conectados.
    private static void broadcast(String message) {
        for (PrintWriter writer : writers) {
            writer.println(message);
        }
    }

    // Classe interna para lidar com a comunicação de um cliente específico.
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket, PrintWriter writer) {
            this.clientSocket = socket;
            this.writer = writer;
            try {
                // Prepara para ler as mensagens que vêm do cliente.
                InputStreamReader streamReader = new InputStreamReader(clientSocket.getInputStream());
                this.reader = new BufferedReader(streamReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                // Loop que fica lendo as mensagens do cliente.
                while ((message = reader.readLine()) != null) {
                    System.out.println("Mensagem recebida: " + message);
                    // Retransmite a mensagem para todos os outros clientes.
                    broadcast(message);
                }
            } catch (IOException e) {
                // Se ocorrer um erro (ex: cliente desconectou), remove o cliente da lista.
                System.out.println("Cliente desconectado.");
            } finally {
                if (writer != null) {
                    writers.remove(writer);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}