package net.net16.jeremiahlowe.webserver;

import java.net.ServerSocket;
import java.net.Socket;

public class Delegator implements Runnable {
	private ServerSocket serverSocket;
	private ClientHandlerThread[] clientHandlers;

	@Override
	public void run() {
		try {
			clientHandlers = new ClientHandlerThread[Startup.isConsoleMode() ? Instance.config.getClients() : Instance.gui.getMaxConnections()];
			serverSocket = new ServerSocket(Startup.isConsoleMode() ? Instance.config.getPort() : Instance.gui.getPort());
			while (!Thread.interrupted()) {
				Socket socket = serverSocket.accept();
				try {
					boolean log = Instance.config.getLogDelegator();
					if(log) Utility.log("Delegating client " + Utility.getIP(socket) + " to a seperate thread");
					for (int j = 0; j < clientHandlers.length; j++) {
						if (clientHandlers[j] == null || !clientHandlers[j].isAlive()) {
							clientHandlers[j] = new ClientHandlerThread(Startup.isConsoleMode() ? Instance.config.getHTMLFile() : Instance.gui.getHTML());
							clientHandlers[j].setName("Client-" + j);
							clientHandlers[j].start(socket);
							break;
						}
						if(log) Utility.log("Delegated client " + Utility.getIP(socket) + " to thread " + clientHandlers[j].getName());
					}
				} 
				catch (Exception e) {
					Utility.log("Error delegating client: " + e);
				}
			}
		} 
		catch (Exception e) {
			Utility.log("Error starting server on port " + (Startup.isConsoleMode() ? Instance.config.getPort() : Instance.gui.getPort()) + ": " + e);
		}
	}
	
	public void stopServer(){
		for(int i = 0; i < (Startup.isConsoleMode() ? Instance.config.getClients() : Instance.gui.getMaxConnections()); i++) if(clientHandlers[i] != null) clientHandlers[i].interrupt();
		Instance.delegatorThread.interrupt();
	}
	
	public void closeServerSocket() throws Exception{
		serverSocket.close();
	}
}