import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	
	int num1, num2; 
	private BufferedReader in;
    private PrintWriter out;
    
	public Client(int num1, int num2) {
		this.num1 =num1; 
		this.num2 = num2; 
	}
	
	public void connectToServer() throws IOException {

        String serverAddress = "127.0.0.1";

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9191);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        out.println(num1);
        out.println(num2);
        // Consume the initial welcoming messages from the server
        for (int i = 0; i <2; i++) {
           System.out.println(in.readLine() + "\n");
        }
        //System.out.println(in.readLine());
        socket.close();
    }
	
	 public static void main(String[] args) throws IOException {
	        /*String serverAddress = JOptionPane.showInputDialog(
	            "Enter IP Address of a machine that is\n" +
	            "running the date service on port 9090:");
	        */
		 	ArrayList <Client> clients = new ArrayList<Client>();
		 	clients.add(new Client(10,3)); 
		 	clients.add(new Client(51,3)); 
			clients.add(new Client(54,3)); 
		 	clients.add(new Client(23,3)); 
			clients.add(new Client(10,6)); 
		 	clients.add(new Client(23,4)); 
			clients.add(new Client(10,4)); 
		 	clients.add(new Client(2,11)); 
			clients.add(new Client(93,2)); 
		 	clients.add(new Client(83,3)); 
			
		 	for(Client c: clients) {
		 		c.connectToServer();
		 	}
	        System.exit(0);
	    }

}
