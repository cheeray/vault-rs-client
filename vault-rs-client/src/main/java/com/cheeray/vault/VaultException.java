package com.cheeray.vault;

import java.io.IOException;

public class VaultException extends Exception {
	private static final long serialVersionUID = 1L;

	public VaultException(String msg) {
		super(msg);
	}

	public VaultException(IOException e) {
		super(e);
	}

}
