package de.adesso.example.framework.core;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationFrameworkBannerTest {

	@Mock
	private PrintStream printStream;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPrintBanner() {
		final ApplicationFrameworkBanner banner = new ApplicationFrameworkBanner();
		banner.printBanner(null, null, this.printStream);

		verify(this.printStream, times(19)).println(anyString());
	}
}
