package com.cheeray.vault;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start a local Vault server.
 * 
 * @author Chengwei.Yan
 * 
 */
public class LocalVault extends Thread {

	private final static Logger logger = LoggerFactory
			.getLogger(LocalVault.class);

	private AtomicBoolean running = new AtomicBoolean(false);

	private Process process;

	private final String serverPath;
	private final String cfgPath;

	public LocalVault(String serverPath, String cfgPath) {
		if (serverPath == null || cfgPath == null) {
			throw new IllegalArgumentException(
					"Server path and configuration path are required.");
		}
		this.serverPath = serverPath;
		this.cfgPath = cfgPath;
	}

	@Override
	public void run() {
		try {
			logger.info("Start local Vault server ...");
			process = Runtime.getRuntime().exec(
					serverPath + " server -config=" + cfgPath);
			running.set(true);
			log(process.getInputStream(), false);
			log(process.getErrorStream(), true);
			process.waitFor();
			if (process.exitValue() > 0) {
				running.set(false);
				// Error ...
				log(process.getErrorStream(), true);
			}

		} catch (IOException | InterruptedException ex) {
			logger.error("Failed start Vault server.", ex);
		}
	}

	private void log(final InputStream is, boolean error) throws IOException {
		if (is != null) {
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					is));
			String line;
			while ((line = in.readLine()) != null) {
				if (error) {
					logger.error(line);
				} else {
					logger.info(line);
				}
			}
		}
	}

	public boolean isRunning() {
		return running.get();
	}

	public void halt() {
		process.destroy();
		running.set(false);
	}
}
