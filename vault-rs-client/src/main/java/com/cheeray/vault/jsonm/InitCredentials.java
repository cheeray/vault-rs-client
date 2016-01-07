package com.cheeray.vault.jsonm;

import com.google.gson.annotations.SerializedName;

public class InitCredentials {

	private String[] keys;

	@SerializedName("root_token")
	private String rootToken;

	public InitCredentials() {
	}

	public InitCredentials(String token, String[] keys) {
		this.rootToken = token;
		this.keys = keys;
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public String getRootToken() {
		return rootToken;
	}

	public void setRootToken(String rootToken) {
		this.rootToken = rootToken;
	}

}
