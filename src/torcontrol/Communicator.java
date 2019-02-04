package torcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import static torcontrol.ControlPortCommands.*;

/**
 * Class to communicate with a tor server
 * 
 * @author Julius Paffrath
 *
 */
public class Communicator {
	private String server;
	private int port;
	
	private boolean isConnected;
	private Socket socket;
	private OutputStream streamOut;
	private InputStream streamIn;
	private PrintStream streamPrint;
	
	/**
	 * Constructor for the Communicator
	 * 
	 * @param server address of the tor server
	 * @param port port of the tor server
	 */
	public Communicator(String server, int port) {
		this.server = server;
		this.port = port;
		
		this.isConnected = false;
	}
	
	/**
	 * Connects to the tor server
	 */
	private void connect() {
		if (this.isConnected) {
			return;
		}
		
		try {
			this.socket = new Socket(this.server, this.port);
			this.streamOut = socket.getOutputStream();
			this.streamIn = socket.getInputStream();
			this.streamPrint = new PrintStream(this.streamOut, true);
			this.isConnected = true;
		}
		catch (IOException e) {
			this.isConnected = false;
			System.err.println("Connection to tor server " + this.server + ":" + this.port + " failed!");
		}
	}
	
	/**
	 * Sends the specified command to the tor server
	 * 
	 * If the current instance is not connected,
	 * send() tries to connect to the server
	 * 
	 * @param cmd command
	 * @return the answer from the server
	 */
	private String send(String cmd) {
		this.connect();
		this.streamPrint.println(cmd);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.streamIn));
		StringBuffer bufferAnswer = new StringBuffer();
        
        try {
        	bufferAnswer.append(reader.readLine());
		}
        catch (IOException e) {
        	System.err.println("Send command failed!");
			return null;
		}
		
		return bufferAnswer.toString();
	}
	
	/**
	 * Parses the given tor server's response based on sent command
	 * 
	 * @param answer response from tor server
	 * @param command command send to tor server
	 * @return the parsed response as a string
	 */
	private String parseResponse(String answer, String command) {
		int code = ResponseCodes.INTERNAL_ERROR;
		String msg = "";
		
		if (answer.length() < 3) {
			System.err.println("Corrupt answer from server! [" + answer + "]");
			return msg;
		}
		
		try {
			code = Integer.parseInt(answer.substring(0, 3));
		}
		catch (NumberFormatException e) {
			System.err.println("Answer code from server can't be parsed! [" + answer + "]");
			return msg;
		}
			
		// cut leading return code from message
		msg = answer.substring(4);
			
		// switch return codes based on sent command
		switch (code) {
			case ResponseCodes.OK:
				if (command == ControlPortCommands.AUTHENTICATE) {
					return "SUCCESS";
				}
				if (command == ControlPortCommands.GETINFO_VERSION) {
					int indexStart = msg.indexOf("version=") + "version=".length();
					int indexEnd = msg.indexOf(" ");
					
					return msg.substring(indexStart, indexEnd);
				}
				return "";
			case ResponseCodes.AUTHENTICATION_REQUIRED:
				System.err.println("Server needs authentication!");
			case ResponseCodes.BAD_AUTHENTICATION:
				System.err.println("Authentication failed!");
				return "FAILURE";
			case ResponseCodes.UNRECOGNIZED_ENTITY:
				if (command == ControlPortCommands.SIGNAL_RELOAD) {
					System.err.println("Send Signal Reload failed");
					return "";
				}
				if (command == ControlPortCommands.SIGNAL_DUMP) {
					System.err.println("Send Signal Dump failed");
					return "";
				}
			default:
				System.err.println("Code not implemented: " + code);
				return "";
		}
	}
	
	/**
	 * Closes the socket connection
	 */
	private void close() {
		try {
			if (this.socket.isClosed() == false) {
				this.socket.close();
			}
		}
		catch (IOException e) { }
	}
	
	/**
	 * Public methods
	 */
	
	/**
	 * Authentication of the client against the server
	 * 
	 * Implements the HASHEDPASSWORD authentication method
	 * 
	 * @param pass password
	 * @return true if authentication was successfully
	 */
	public boolean authenticate(String pass) {
		String answer = this.send(AUTHENTICATE + " \"" + pass + '"');
		return this.parseResponse(answer, AUTHENTICATE) == "SUCCESS" ? true : false;
	}
	
	/**
	 * Gets the version of the server's software
	 * 
	 * @return version of the server's software
	 */
	public String getVersion() {
		return this.parseResponse(this.send(GETINFO_VERSION), GETINFO_VERSION);
	}
	
	/**
	 * Reload: reload config items
	 */
	public void sendSignalReload() {
		this.parseResponse(this.send(SIGNAL_RELOAD), SIGNAL_RELOAD);
	}
	
	/**
	 * Controlled shutdown: if server is an OP, exit immediately
	 * 
	 * If it's an OR, close listeners and exit after
	 * ShutdownWaitLength seconds.
	 */
	public void sendSignalShutdown() {
		this.parseResponse(this.send(SIGNAL_SHUTDOWN), SIGNAL_SHUTDOWN);
	}
	
	/**
	 * Dump stats: log information about open connections and circuits
	 */
	public void sendSignalDump() {
		this.parseResponse(this.send(SIGNAL_DUMP), SIGNAL_DUMP);
	}
	
	/**
	 * Debug: switch all open logs to loglevel debug
	 */
	public void sendSignalDebug() {
		this.parseResponse(this.send(SIGNAL_DEBUG), SIGNAL_DEBUG);
	}
	
	/**
	 * Immediate shutdown: clean up and exit now
	 */
	public void sendSignalHalt() {
		this.parseResponse(this.send(SIGNAL_HALT), SIGNAL_HALT);
	}
	/**
	 * Switch to clean circuits, so new application requests
	 * don't share any circuits with old ones.
	 * 
	 * Also clears the client-side DNS cache.
	 * (Tor MAY rate-limit its response to this signal.)
	 */
	public void sendSignalNewNYM() {
		this.parseResponse(this.send(SIGNAL_NEWNYM), SIGNAL_NEWNYM);
	}
	
	/**
	 * Tells the server to hang up on this controller connection
	 * 
	 * This command can be used before authenticating
	 */
	public void quit() {
		this.send(QUIT);
		this.close();
	}
}