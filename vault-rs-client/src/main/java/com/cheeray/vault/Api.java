package com.cheeray.vault;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

import com.cheeray.vault.jsonm.Backend;
import com.cheeray.vault.jsonm.InitCredentials;
import com.cheeray.vault.jsonm.InitStatus;
import com.cheeray.vault.jsonm.MountConfig;
import com.cheeray.vault.jsonm.Policies;
import com.cheeray.vault.jsonm.Rules;
import com.cheeray.vault.jsonm.SealStatus;
import com.cheeray.vault.jsonm.Secret;
import com.cheeray.vault.jsonm.SecretBackend;
import com.cheeray.vault.jsonm.SecretConfig;
import com.cheeray.vault.jsonm.SecretLease;
import com.cheeray.vault.jsonm.UnsealRequest;

/**
 * Vault Rest API.
 * 
 * @author Chengwei.Yan
 * 
 */
public interface Api {
	public static final String ACCEPT_APPLICATION_JSON = "Accept: application/json";
	public static final String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";
	public static final String X_VAULT_TOKEN = "X-Vault-Token";

	/**
	 * Initialise Vault.
	 */
	public static interface Init {

		@GET("/v1/sys/init")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<InitStatus> getInitStatus(@Header(X_VAULT_TOKEN) String token);

		@PUT("/v1/sys/init")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<InitCredentials> init(@Body SecretConfig config);
	}

	/**
	 * Un/Seal Vault.
	 */
	public static interface Seal {
		@GET("/v1/sys/seal-status")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<SealStatus> getSealStatus(@Header(X_VAULT_TOKEN) String token);

		@PUT("/v1/sys/seal")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<Void> seal(@Header(X_VAULT_TOKEN) String token);

		@PUT("/v1/sys/unseal")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<SealStatus> unseal(@Body UnsealRequest unsealRequest,
				@Header(X_VAULT_TOKEN) String token);
	}

	/**
	 * Authentication.
	 */
	public static interface Auth {
		@GET("/v1/sys/auth")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<Map<String, Backend>> listAuthBackends(
				@Header(X_VAULT_TOKEN) String token);

		@POST("/v1/sys/auth/{mountPoint}")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<Void> create(@Path("mountPoint") String mountPoint,
				@Body Backend backend, @Header(X_VAULT_TOKEN) String token);

		@DELETE("/v1/sys/auth/{mountPoint}")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<Void> delete(@Path("mountPoint") String mountPoint,
				@Header(X_VAULT_TOKEN) String token);
	}

	/**
	 * Mount security.
	 */
	public static interface Mount {
		@GET("/v1/sys/mounts")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<Map<String, SecretBackend>> getMounts(
				@Header(X_VAULT_TOKEN) String token);

		@GET("/v1/sys/mounts/{mountPoint}/tune")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<MountConfig> getMountConfig(@Path("mountPoint") String mountPoint,
				@Header(X_VAULT_TOKEN) String token);

		@POST("/v1/sys/mounts/{mountPoint}")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<Void> mount(@Path("mountPoint") String mountPoint,
				@Body SecretBackend mount, @Header(X_VAULT_TOKEN) String token);

		@POST("/v1/sys/mounts/{mountPoint}/tune")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<Void> tune(@Path("mountPoint") String mountPoint,
				@Body MountConfig config, @Header(X_VAULT_TOKEN) String token);
	}

	/**
	 * Security Policies.
	 */
	public static interface Policy {
		@GET("/v1/sys/policy")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<Policies> list(@Header(X_VAULT_TOKEN) String token);

		@GET("/v1/sys/policy/{name}")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<Rules> rules(@Path("name") String policyName,
				@Header(X_VAULT_TOKEN) String token);

		@PUT("/v1/sys/policy/{name}")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<Void> update(@Path("name") String policyName, @Body Rules rules,
				@Header(X_VAULT_TOKEN) String token);

		@DELETE("/v1/sys/policy/{name}")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<Void> update(@Path("name") String policyName,
				@Header(X_VAULT_TOKEN) String token);
	}

	/**
	 * Storage.
	 */
	public interface Store {
		@GET("/v1/secret/{key}")
		@Headers({ ACCEPT_APPLICATION_JSON })
		Call<SecretLease> read(@Path("key") String key,
				@Header(X_VAULT_TOKEN) String token);

		@POST("/v1/secret/{key}")
		@Headers({ CONTENT_TYPE_APPLICATION_JSON, ACCEPT_APPLICATION_JSON })
		Call<Void> write(@Path("key") String key, @Body Secret value,
				@Header(X_VAULT_TOKEN) String token);
	}
}
