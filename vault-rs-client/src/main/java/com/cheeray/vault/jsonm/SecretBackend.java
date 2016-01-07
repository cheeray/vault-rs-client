package com.cheeray.vault.jsonm;

public class SecretBackend extends Backend {

	private MountConfig config;

	public MountConfig getConfig() {
		return config;
	}

	public void setConfig(MountConfig config) {
		this.config = config;
	}
}
