package torcontrol;

/**
 * Control Port Commands as described in https://gitweb.torproject.org/torspec.git/tree/control-spec.txt
 * 
 * @author Julius Paffrath
 *
 */
public final class ControlPortCommands {
	public static final String AUTHENTICATE = "AUTHENTICATE";
	public static final String GETINFO_VERSION = "GETINFO version";
	public static final String QUIT = "QUIT";
}