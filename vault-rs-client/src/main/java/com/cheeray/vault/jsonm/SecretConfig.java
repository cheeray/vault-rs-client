package com.cheeray.vault.jsonm;

import com.google.gson.annotations.SerializedName;

public class SecretConfig {

	@SerializedName("secret_shares")
	private int shares;

	@SerializedName("secret_threshold")
	private int threshold;

	@SerializedName("pgp_keys ")
	private int pgpKeys;

	public SecretConfig() {
	}

	public SecretConfig(int shares, int threshold) {
		this.shares = shares;
		this.threshold = threshold;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getPgpKeys() {
		return pgpKeys;
	}

	public void setPgpKeys(int pgpKeys) {
		this.pgpKeys = pgpKeys;
	}
}
