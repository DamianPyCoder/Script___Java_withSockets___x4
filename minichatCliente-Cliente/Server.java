import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    // Lista de clientes conectados
    private static List<ClientHandler> clients = new ArrayList<>();
    
    // Puerto para la conexión
    private static final int PORT = 44444;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado en el puerto " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado desde la dirección: " + clientSocket.getRemoteSocketAddress());

            // Establece el nombre del cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String name = in.readLine();

            // Crea un nuevo controlador de cliente y lo agrega a la lista de clientes
            ClientHandler client = new ClientHandler(name, clientSocket);
            clients.add(client);

            // Inicia el hilo del controlador de cliente
            client.start();
        }
    }

    // Envía un mensaje a todos los clientes, excepto al remitente
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}

class ClientHandler extends Thread {
    private String name;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(String name, Socket clientSocket) throws IOException {
        this.name = name;
        this.clientSocket = clientSocket;

        // Configura la entrada y salida del socket
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public void run() {
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                // Envía el mensaje recibido a todos los clientes
                Server.broadcast(name + ": " + inputLine, this);
            }
        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
        }
    }

    // Envía un mensaje al cliente
    public void sendMessage(String message) {
        out.println(message);
    }
}
