package chatclient1;


import org.kohsuke.args4j.Option;

/**
 * Class with command line options.
 * 
 * @author Adel
 *
 */
public class CommandLineValues {

		// Give it a default value of 4444 sec
		@Option(name = "-p", aliases = {"--port"}, usage="Port Address")
		private int port = 4444;

		public int getPort() {
			return port;
		}
	}

