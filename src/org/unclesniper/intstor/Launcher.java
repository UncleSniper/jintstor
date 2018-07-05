package org.unclesniper.intstor;

import java.io.File;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class Launcher {

	public static final class Config {

		private File dataDir;

		private int port;

		public Config(File dataDir, int port) {
			this.dataDir = dataDir;
			this.port = port;
		}

		public File getDataDir() {
			return dataDir;
		}

		public void setDataDir(File dataDir) {
			this.dataDir = dataDir;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}

	private static final String LAUNCH_CMD = "java " + Launcher.class.getName();

	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			System.err.println("Usage: " + Launcher.LAUNCH_CMD + " <data-dir> <port>");
			System.exit(1);
		}
		File dataDir = new File(args[0]);
		if(!dataDir.isDirectory()) {
			System.err.println("Not a directory: " + dataDir.getPath());
			System.exit(1);
		}
		int port;
		try {
			port = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException nfe) {
			System.err.println("Not a valid integer: " + args[1]);
			System.exit(1);
			return;
		}
		if(port <= 0 || port > 65535) {
			System.err.println("Port number out of range: " + port);
			System.exit(1);
		}
		Launcher.launch(new Config(dataDir, port));
	}

	public static void launch(Config config) throws Exception {
		Server server = new Server(config.getPort());
		ContextHandler staticContext = new ContextHandler("/static/");
		ResourceHandler resources = new ResourceHandler();
		resources.setBaseResource(Resource.newClassPathResource("/org/unclesniper/intstor/resource/"));
		staticContext.setHandler(resources);
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] {
			staticContext
		});
		RewriteHandler rewriter = new RewriteHandler();
		rewriter.setHandler(contexts);
		rewriter.addRule(new RewritePatternRule("/", "/static/index.html"));
		server.setHandler(rewriter);
		server.start();
		server.join();
	}

}
