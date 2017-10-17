package net.net16.jeremiahlowe.webserver;

import java.net.ServerSocket;
import java.net.Socket;

import net.net16.jeremiahlowe.webserver.utility.Instance;
import net.net16.jeremiahlowe.webserver.utility.Utility;
import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;

public class Delegator implements Runnable {
	private ServerSocket serverSocket;
	private ClientHandlerThread[] clientHandlers;

	@Override
	public void run() {
		try {
			clientHandlers = new ClientHandlerThread[Instance.globalInstance.config.getThreads()];
			serverSocket = new ServerSocket(Instance.globalInstance.config.getPort());
			while (!Thread.interrupted()) {
				Socket socket = serverSocket.accept();
				try {
					Instance.globalInstance.logger.log(LogLevel.Debug, "Delegating client " + Utility.getIP(socket) + " to a seperate thread");
					for (int j = 0; j < clientHandlers.length; j++) {
						if (clientHandlers[j] == null || !clientHandlers[j].isAlive()) {
							clientHandlers[j] = new ClientHandlerThread(Instance.globalInstance.config.getHTMLFile());
							clientHandlers[j].setName("Client-" + j);
							clientHandlers[j].start(socket);
							break;
						}
						Instance.globalInstance.logger.log(LogLevel.Debug, "Delegated client " + Utility.getIP(socket) + " to thread " + clientHandlers[j].getName());
					}
				} 
				catch (Exception e) {
					Instance.globalInstance.logger.log(LogLevel.Error, "Error delegating client: " + e);
				}
			}
			serverSocket.close();
		} 
		catch (Exception e) {
			if(Thread.interrupted()) return;
			Instance.globalInstance.logger.log(LogLevel.Severe, "Error with server on port " + (Instance.globalInstance.config.getPort()) + ": " + e);
		}
	}
	
	public void stopServer(){
		for(int i = 0; i < Instance.globalInstance.config.getThreads(); i++) if(clientHandlers[i] != null) clientHandlers[i].interrupt();
		Instance.globalInstance.delegatorThread.interrupt();
	}
	public void closeServerSocket() throws Exception{
		serverSocket.close();
	}
	public void setServerRunning(boolean isOn){
		if(isOn){
			Instance.globalInstance.logger.log(LogLevel.Info, "Starting server on port " + Instance.globalInstance.config.getPort());
			Instance.globalInstance.delegatorThread = new Thread(Instance.globalInstance.delegator);
			Instance.globalInstance.delegatorThread.start();			
		}
		else{
			Instance.globalInstance.logger.log(LogLevel.Info, "Stopping server and killing spawned threads");
			Instance.globalInstance.delegator.stopServer();
			try{closeServerSocket();}catch(Exception e){}
		}
	}
}