package com.cheeray.vault;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import com.cheeray.vault.Api.Auth;
import com.cheeray.vault.Api.Init;
import com.cheeray.vault.Api.Seal;
import com.cheeray.vault.Api.Store;
import com.cheeray.vault.jsonm.Backend;
import com.cheeray.vault.jsonm.InitCredentials;
import com.cheeray.vault.jsonm.InitStatus;
import com.cheeray.vault.jsonm.SealStatus;
import com.cheeray.vault.jsonm.Secret;
import com.cheeray.vault.jsonm.SecretConfig;
import com.cheeray.vault.jsonm.SecretLease;
import com.cheeray.vault.jsonm.UnsealRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

/**
 * Client to connect a remote or local Vault server. A local server will be
 * started if local is enabled.
 * 
 * @author Chengwei.Yan
 * 
 */
public class Vault implements Closeable {

	private static final Logger LOG = LoggerFactory.getLogger(Vault.class);

	private final Retrofit retrofit;
	private final boolean local;
	private final String serverPath;
	private final String cfgPath;
	private final int maxRetires;
	private final Init init;
	private final Seal seal;
	private final Auth auth;
	private final Store store;

	private LocalVault localVault = null;
	private InitCredentials credentials = null;

	/**
	 * Local Vault.
	 * 
	 * @param serverPath
	 *            Server execution path.
	 * @param cfgPath
	 *            Server configuration file path.
	 * @param port
	 *            The port number.
	 * @param maxRetires
	 *            The max retries for connection.
	 * @throws VaultException
	 * @throws IOException
	 */
	public Vault(String serverPath, String cfgPath, int port, int maxRetires)
			throws VaultException, IOException {
		this(new URL("http", "localhost", port, ""), true, serverPath, cfgPath,
				maxRetires);
	}

	/**
	 * Remote Vault.
	 * 
	 * @param host
	 *            The name of remote host.
	 * @param port
	 *            The port number.
	 * @param maxRetires
	 * @throws VaultException
	 * @throws IOException
	 */
	public Vault(String host, int port, int maxRetires) throws VaultException,
			IOException {
		this(new URL("http", host, port, ""), false, null, null, maxRetires);
	}

	/**
	 * Constructor.
	 */
	public Vault(final URL url, boolean local, String serverPath,
			String cfgPath, int maxRetires) throws VaultException, IOException {
		this.local = local;
		this.serverPath = serverPath;
		this.cfgPath = cfgPath;
		this.maxRetires = maxRetires;
		GsonConverterFactory conv = GsonConverterFactory
				.create(new GsonBuilder().setFieldNamingPolicy(
						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create());
		retrofit = new Retrofit.Builder().addConverterFactory(conv)
				.baseUrl(url.toExternalForm()).build();
		this.init = retrofit.create(Api.Init.class);
		this.seal = retrofit.create(Api.Seal.class);
		this.auth = retrofit.create(Api.Auth.class);
		this.store = retrofit.create(Api.Store.class);
	}

	/**
	 * Read a secret key.
	 * 
	 * @param key
	 *            The secret key.
	 * @return a value string.
	 * @throws IOException
	 * @throws VaultException
	 */
	public String read(String key) throws IOException, VaultException {
		SecretLease read = exec(store.read(key, credentials.getRootToken()));
		Secret secret = read.getData();
		return secret != null ? secret.getValue() : null;
	}

	/**
	 * Write a secret.
	 * 
	 * @param key
	 *            The key.
	 * @param value
	 *            The secret value.
	 * @throws IOException
	 * @throws VaultException
	 */
	public void write(String key, String value) throws IOException,
			VaultException {
		exec(store.write(key, new Secret(value), credentials.getRootToken()));
	}

	public InitCredentials init(int shares, int threshold)
			throws VaultException {
		final SecretConfig cfg = new SecretConfig(shares, threshold);
		LOG.info("Initializes a new Vault.");
		return init(cfg, maxRetires);
	}

	/**
	 * Open the vault.
	 * 
	 * @param token
	 * @param keys
	 * @throws VaultException
	 * @throws IOException
	 */
	public void open(String token, String... keys) throws VaultException,
			IOException {
		if (token == null || keys == null) {
			throw new VaultException("Missing root token and keys.");
		}
		credentials = new InitCredentials(token, keys);
		init(null, maxRetires);

		if (credentials == null) {
			throw new VaultException("Boot failed.");
		}
		final SealStatus sealStatus = exec(seal.getSealStatus(credentials
				.getRootToken()));
		if (sealStatus.isSealed()) {
			unseal();
		}

		// Auth ...
		auth();

		// Mounts ...

		// Read write
	}

	private InitCredentials init(SecretConfig cfg, int retries)
			throws VaultException {
		try {
			Response<InitStatus> statusRsp = init.getInitStatus(
					credentials != null ? credentials.getRootToken() : null)
					.execute();
			InitStatus initStatus = statusRsp.body();
			if (!initStatus.isInitialized()) {
				if (cfg != null) {
					return exec(init.init(cfg));
				} else {
					throw new VaultException("Vault is not initialised.");
				}
			} else if (credentials == null) {
				throw new VaultException("Token and keys are required.");
			}
			return credentials;
		} catch (IOException e) {
			if (retries > 0) {
				retries--;
				if (local) {
					// Not started, start local one if enabled.
					if (localVault == null || !localVault.isAlive()) {
						LOG.info("Start a new local Vault server.");
						localVault = new LocalVault(serverPath, cfgPath);
						localVault.start();
						LOG.info("Local Vault server started.");
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						LOG.error("Interrupted while booting.");
					}
					return init(cfg, retries);
				} else {
					throw new VaultException("Please start the remote Vault.");
				}
			} else {
				throw new VaultException("Max retry exhausted.");
			}
		}
	}

	private void unseal() throws VaultException, IOException {
		SealStatus status = null;
		for (String key : credentials.getKeys()) {
			status = exec(seal.unseal(new UnsealRequest(key),
					credentials.getRootToken()));
		}
		if (status == null || status.isSealed()) {
			throw new VaultException("Failed to unseal Vault.");
		}
	}

	private void auth() throws VaultException, IOException {
		for (Backend b : exec(auth.listAuthBackends(credentials.getRootToken()))
				.values()) {
			auth.create(b.getType(), b, credentials.getRootToken());
		}
	}

	@Override
	public void close() throws IOException {
		SealStatus sealStatus;
		try {
			sealStatus = exec(seal.getSealStatus(credentials.getRootToken()));
		} catch (VaultException e) {
			throw new IOException(e);
		}
		if (!sealStatus.isSealed()) {
			try {
				exec(seal.seal(credentials.getRootToken()));
			} catch (VaultException e) {
				throw new IOException(e);
			}
		}
		if (localVault != null && localVault.isAlive()) {
			localVault.halt();
		}
	}

	/**
	 * Execute a retrofit call.
	 * 
	 * @param call
	 *            The retrofit call.
	 * @return a respond value.
	 * @throws VaultException
	 *             while error occurred.
	 */
	public static <T> T exec(Call<T> call) throws VaultException {
		Response<T> response;
		try {
			response = call.execute();
		} catch (IOException e) {
			throw new VaultException(e);
		}

		if (response.isSuccess()) {
			return response.body();
		} else {
			throw new VaultException(response.code() + ":" + response.message());
		}
	}
}
