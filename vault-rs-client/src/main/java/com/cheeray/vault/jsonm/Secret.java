package com.cheeray.vault.jsonm;

public class Secret {

	private String value;

	public Secret() {
	}

	public Secret(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
