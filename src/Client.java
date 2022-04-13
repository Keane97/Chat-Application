import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final Socket clientSocket;                 // Object of Socket.
        final BufferedReader in;                    // Object of BufferedReader. Used to read data from the clientSocket Object.
        final PrintWriter out;                      // Object of PrintWriter. Used to write data into the ClientSocket object.
        final Scanner sc = new Scanner(System.in);  // Read data from the users Keyboard.

        try {
            clientSocket = new Socket("127.0.0.1", 5000); //Statically defines the host(Server) IP and the Port which is open for comms.
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread sender = new Thread(new Runnable() {
                String msg; //Stores the chat message sent by the user
                @Override //override the "run" method of Thread
                public void run() {
                    while (true){
                        msg = sc.nextLine(); // Reads the data from the users keyboard.
                        out.println(msg); // Write Data stored in msg into the clientSocket
                        out.flush(); // Forces the sending of the data
                    }
                }
            });
            sender.start();

            Thread receive = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg = in.readLine(); //Read data from the clientSocket using the "in" object;
                        // While the client is still connected to the server
                        while (msg !=null){
                            System.out.println("Server: " + msg); //Print to the screen what the client has sent
                            msg = in.readLine();    // Read data from the clientSocket using the "in" object
                        }
                        // If msg == null it means the client is no longer connected.
                        System.out.println("The Client has Disconnected");


                        //Closing of open sockets and streams (!IMPORTANT)
                        out.close();
                        clientSocket.close();

                    }catch (IOException exception){
                        exception.printStackTrace();
                    }
                }
            });
            receive.start();

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
