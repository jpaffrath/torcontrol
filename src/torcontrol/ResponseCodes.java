package torcontrol;

/**
 * Response codes as described in https://gitweb.torproject.org/torspec.git/tree/control-spec.txt
 * 
 * @author Julius Paffrath
 *
 */
public final class ResponseCodes {
	public static final int OK = 250;
	public static final int OPERATION_UNNECESSARY = 251;
	public static final int RESOURCE_EXHAUSTED = 451;
	public static final int SYNTAX_ERROR_PROTOCOL = 500;
	public static final int UNRECOGNIZED_COMMAND = 510;
	public static final int UNIMPLEMENTED_COMMAND = 511;
	public static final int SYNTAX_ERROR_COMMAND_ARGUMENT = 512;
	public static final int UNRECOGNIZED_COMMAND_ARGUMENT = 513;
	public static final int AUTHENTICATION_REQUIRED = 514;
	public static final int BAD_AUTHENTICATION = 515;
	public static final int UNSPECIFIED_TOR_ERROR = 550;
	public static final int INTERNAL_ERROR = 551;
	public static final int UNRECOGNIZED_ENTITY = 552;
	public static final int INVALID_CONFIGURATION_VALUE = 553;
	public static final int INVALID_DESCRIPTOR = 554;
	public static final int UNMANAGED_ENTITY = 555;
	public static final int ASYNCHRONOUS_EVENT_NOTIFICATION = 650;
}