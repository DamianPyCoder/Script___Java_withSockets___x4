import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 44444;

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Establece el nombre del cliente
        System.out.print("Introduce tu nombre: ");
        String name = "cliente" + (int) (Math.random() * 1000);
        out.println(name);

        // Inicia el hilo de escucha de mensajes del servidor
        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String inputLine;
                    while ((inputLine = socketIn.readLine()) != null) {
                        System.out.println(inputLine);
                    }
                } catch (IOException e) {
                    System.err.println("Error en la comunicación con el servidor: " + e.getMessage());
                }
            }
        });
        listener.start();

        // Lee los mensajes del usuario y los envía al servidor
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            out.println(inputLine);
        }

        // Cierra la conexión
        clientSocket.close();
    }
}
