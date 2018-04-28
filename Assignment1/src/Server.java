import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/*
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
*/

/*Server class, implements a threadPool, thread and runnable*/
public class Server {
	
	//Worker Queue and worker Threads
	private static BlockingQueue<Sum> workerQueue;
    private static Thread[] workerThreads;
    
    //TreadPool
    static ThreadPool tPool = new ThreadPool(4);
    
    //Main method
	public static void main(String[] args) throws Exception {
		
		//Original done with executor
		//ExecutorService executor = Executors.newFixedThreadPool(3);
		
        System.out.println("The server is running");
        int clientNumber = 0;
        
        ServerSocket listener = new ServerSocket(9191);
        try {
            while (true) {
            	tPool.addTask(new Sum(listener.accept(), clientNumber++));
                //executor.execute(new Sum(listener.accept(), clientNumber++));
                
            }
        } finally {
            listener.close();
        }
    }
	//ThreadPoolClass
	 private static class ThreadPool{
	
    	public ThreadPool(int numThreads) {
    	workerQueue = new LinkedBlockingQueue<Sum>();
       	workerThreads = new Thread[numThreads];
        	
    	int i = 0; 
    	for (Thread t:workerThreads) {
    		i++;
    		t = new Worker("PoolThread "+i);
    		t.start(); 
    		}
    	}	
    	public void addTask(Sum r) {
    		try {
    			workerQueue.put(r);
    		}
    		catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	
    	}
    private class Worker extends Thread{
    	public Worker(String name) {
    		super(name);
    	}
    	
    	public void run() {
    		while(true) {
    			try {
    				Sum sum = workerQueue.take();
    				sum.run();
    			}  catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
    		}
    	}
    }
	}
	//Thread class
	 private static class Sum implements Runnable {
	        private Socket socket;
	        private int clientNumber;

	        public Sum(Socket socket, int clientNumber) {
	            this.socket = socket;
	            this.clientNumber = clientNumber;
	            System.out.println("New connection with client# " + clientNumber + " at " + socket);
	        }
	        public void run() {
	            try {
	                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

	                // Send a welcome message to the client.
	                out.println("Hello, you are client #" + clientNumber + ".");
	               
	                while (true) {
	                    int num1 = Integer.parseInt(in.readLine());
	                    int num2 = Integer.parseInt(in.readLine());
	                    int result = num1*num2;
	                    System.out.println(num1*num2);
	                    out.println(result);
	                    in = null;
	                    break;
	                }
	            } catch (IOException e) {
	            	System.out.println("Error handling client# " + clientNumber + ": " + e);
	            } finally {
	                try {
	                    socket.close();
	                } catch (IOException e) {
	                    System.out.println("Couldn't close a socket, what's going on?");
	                }
	                System.out.println("Connection with client# " + clientNumber + " closed");
	            }
	        }

	    }
	

}
