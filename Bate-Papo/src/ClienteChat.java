import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteChat {

    public static void main(String[] args) {
        // Endereço IP do servidor. "localhost" ou "127.0.0.1" se o servidor estiver rodando na mesma máquina.
        final String ENDERECO_SERVIDOR = "127.0.0.1";
        final int PORTA = 5000; // Porta deve ser a mesma do servidor.

        try (Socket socket = new Socket(ENDERECO_SERVIDOR, PORTA);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado ao servidor de chat. Digite seu nome:");
            String nome = scanner.nextLine();
            System.out.println("Olá, " + nome + "! Você pode começar a enviar mensagens.");
            System.out.println("Digite 'sair' para encerrar o chat.");

            // Cria e inicia uma thread para ouvir as mensagens do servidor.
            Thread serverListenerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverReader.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Desconectado do servidor.");
                }
            });
            serverListenerThread.start();


            // Loop principal para ler a entrada do usuário e enviar para o servidor.
            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                if ("sair".equalsIgnoreCase(userInput)) {
                    break;
                }
                writer.println(nome + ": " + userInput);
            }

        } catch (UnknownHostException e) {
            System.err.println("Endereço do servidor não encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Não foi possível conectar ao servidor: " + e.getMessage());
        } finally {
            System.out.println("Conexão encerrada.");
        }
    }
}