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
	
	/* Define control commands 3.7 SIGNAL */
	public static final String SIGNAL_RELOAD        = "SIGNAL RELOAD";
	public static final String SIGNAL_SHUTDOWN      = "SIGNAL SHUTDOWN";
	public static final String SIGNAL_DUMP          = "SIGNAL DUMP";
	public static final String SIGNAL_DEBUG         = "SIGNAL DEBUG";
	public static final String SIGNAL_HALT          = "SIGNAL HALT";
	public static final String SIGNAL_CLEARDNSCACHE = "SIGNAL CLEARDNSCACHE";
	public static final String SIGNAL_NEWNYM        = "SIGNAL NEWNYM";
	public static final String SIGNAL_HEARTBEAT     = "SIGNAL HEARTBEAT";
	public static final String SIGNAL_DORMANT       = "SIGNAL DORMANT";
	public static final String SIGNAL_ACTIVE        = "SIGNAL ACTIVE";
}