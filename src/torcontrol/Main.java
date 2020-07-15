package torcontrol;

/**
 * torcontrol main class
 * 
 * @author Julius Paffrath
 *
 */
public class Main {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("torcontrol <IP Tor Server> <Port Tor Server>");
			System.exit(-1);
		}
		
		String torIP = args[0];
		int torPort = Integer.parseInt(args[1]);
		
		Communicator communicator = new Communicator(torIP, torPort);
		
		if (communicator.authenticate("PASS") == false) {
			System.err.println("Authentication failed");
			communicator.quit();
			System.exit(-1);
		}
		
		String version = communicator.getVersion();
		System.out.println("Tor Server Version: " + version);
		
		communicator.sendSignalDormant();
		
		communicator.quit();
	}
}