package com.cheeray.vault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class VaultTest extends BaseTest {

	@Test
	@Ignore
	public final void testRemote() throws Exception {
		try (Vault v = new Vault("etc/vault", "etc/vault.json", 8200, 3)) {
			v.open("457ab9d8-4ab3-1830-762b-bf65ea16e24b",
					"f5887e2dc6a580640e34bce81d12942e4b08c08562ca6b499a207f280cb4524f01",
					"4c1b2e16ea543eb3d55395d65cb09d7ade9e8f87b2155a2486bf12d2b62eb3bc02",
					"a964996a91828c41b0dec89e8df050dbb4033e4eba1ead7e0d1516085e14409b03");
			v.write("a", "b");
			String value = v.read("a");
			assertNotNull(value);
			assertEquals("b", value);
		} catch (IOException | VaultException e) {
			throw e;
		}

	}

	@Test
	public final void testLocal() throws Exception {
		try (Vault v = new Vault("etc/vault", configPath, 8200, 3)) {
			v.open(null);
			v.write("a", "b");
			String value = v.read("a");
			assertNotNull(value);
			assertEquals("b", value);
		} catch (IOException | VaultException e) {
			throw e;
		}

	}
}
