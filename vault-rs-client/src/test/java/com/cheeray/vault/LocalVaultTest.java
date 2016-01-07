package com.cheeray.vault;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocalVaultTest extends BaseTest {

	@Test
	public final void test() throws InterruptedException {
		LocalVault v = new LocalVault("etc/vault", configPath);
		v.start();
		Thread.sleep(10000);
		assertTrue(v.isRunning());
		v.halt();
		assertFalse(v.isRunning());
	}

}
