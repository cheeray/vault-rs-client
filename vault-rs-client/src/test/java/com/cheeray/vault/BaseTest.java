package com.cheeray.vault;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class BaseTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	String configPath;

	@Before
	public void initialize() throws IOException {
		final File cfgFile = folder.newFile("vault.json");
		File fd = folder.newFolder("vault");
		try (FileWriter fw = new FileWriter(cfgFile)) {
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write("backend \"file\" {  path = \"" + fd.getAbsolutePath()
						+ "\"}");
				bw.newLine();
				bw.write("listener \"tcp\" {  address = \"localhost:8200\"  tls_disable = 1}");
			}
		}
		configPath = cfgFile.getAbsolutePath();
	}

	@After
	public void clean() throws IOException {
	}
}
