package com.cheeray.vault.jsonm;

public class UnsealRequest {

	private String key;
	private boolean reset;

	public UnsealRequest() {
	}

	public UnsealRequest(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

}
