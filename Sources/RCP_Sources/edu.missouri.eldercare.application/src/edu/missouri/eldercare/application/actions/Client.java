package edu.missouri.eldercare.application.actions;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.missouri.eldercare.application.utilities.DataUpdateListener;

/**
 * This class implements the client functionality for the multi client chat
 * application. It creates a client and then user input commands to server and
 * listens to server response.
 * 
 * @author Kaustubh
 */
public class Client implements Runnable {

	private static Client instance;
	private BufferedReader breader;
	private Socket clientSocket;
	private boolean connected = true;
	private PrintStream pStream;
	protected static final String HOST_ID = "128.206.83.133";
	// protected static final String HOST_ID = "localhost";
	protected static final int PORT_NUMBER = 15375;
	private ArrayList listeners;

	private Client() {
		try {
			clientSocket = new Socket(HOST_ID, PORT_NUMBER);
			if (clientSocket != null) {
				pStream = new PrintStream(clientSocket.getOutputStream());
				breader = new BufferedReader(new InputStreamReader(clientSocket
						.getInputStream()));
				listeners = new ArrayList();
			}
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host.");
		} catch (IOException e) {
			System.out.println("Server is not Established");
		}
		/*
		 * if (clientSocket != null) { String readLine = null; try { while
		 * ((readLine = breader.readLine()) != null) {
		 * processDisplayData(readLine); System.out.println(readLine);
		 * 
		 * if (readLine.equalsIgnoreCase("logout")) { connected = false; break;
		 * }
		 * 
		 * } } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
	}

	public static Client getInstance() {
		if (instance != null) {
			return instance;
		} else
			return instance = new Client();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String readLine = null;
		/*
		 * Continuously listen the server response and logout the client once
		 * the logout command received from server.
		 */
		if (clientSocket != null) {
			try {
				// while ((readLine = breader.readLine()) != null) {
				while (connected) {
					readLine = breader.readLine();
					if (readLine != null) {
						processDisplayData(readLine);
						// Thread.sleep(300);
						System.out.println(readLine);
					}
					if (!connected)
						break;
					/*
					 * if (readLine.equalsIgnoreCase("logout")) { connected =
					 * false; break; }
					 */
				}
				breader.close();
				pStream.close();
				clientSocket.close();
			} catch (IOException e) {
				/*
				 * If the client connection is lost while the client is
				 * connected, display an error message.
				 */
				System.out.println("Connection with server is lost");
				connected = false;
			}/*
			 * catch (InterruptedException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); }
			 */
		}
	}

	private void processDisplayData(final String displayData) {

		int[] finalData = new int[128];
		if (!displayData.equals("SABCDE")) {
			int n = 0;
			for (int k = 1; k < displayData.length(); k++) {
				if (displayData.charAt(k) == 'A'
						|| displayData.charAt(k) == 'B'
						|| displayData.charAt(k) == 'C'
						|| displayData.charAt(k) == 'D') {
					continue;

				}
				if (displayData.charAt(k) == 'E') {
					k = 100;
					break;
				}
				String str1 = Integer.toBinaryString(displayData.charAt(k));
				String str2 = str1.substring(2);
				for (int i = 0; i < 4; i++) {
					if (n * 4 + i > 127)
						break;
					switch (str2.charAt(i)) {

					case '1':
						finalData[n * 4 + i] = 1;
						break;
					case '0':
						finalData[n * 4 + i] = 0;
						break;
					default:
						// finalData[n * 4 + i] = 0;
						break;
					}

				}
				n++;
			}
		} else {

		}
		dataUpdated(finalData);
	}

	private void dataUpdated(int[] finalData) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			DataUpdateListener listener = (DataUpdateListener) iterator.next();
			listener.dataUpdated(finalData);
		}
	}

	public void addDataUpdateListener(DataUpdateListener listener) {
		listeners.add(listener);
	}

	public void removeDataUpdateListener(DataUpdateListener listener) {
		if (listeners != null && listeners.contains(listener))
			listeners.remove(listener);
	}

	public static boolean isClientNull() {
		if (instance == null)
			return true;
		return false;
	}

	public void logout() {
		connected = false;
		instance = null;
	}
}
