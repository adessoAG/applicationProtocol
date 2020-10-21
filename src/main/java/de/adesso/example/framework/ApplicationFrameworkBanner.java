package de.adesso.example.framework;

import java.io.PrintStream;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

public class ApplicationFrameworkBanner implements Banner {

	private static final String[] BANNER = {
			"",
			"     _                _ _           _   _               _____",
			"    / \\   _ __  _ __ | (_) ___ __ _| |_(_) ___  _ __   |  ___| __ __ _ _ __ ___   _____      _____  _ __| |",
			"   / _ \\ | '_ \\| '_ \\| | |/ __/ _` | __| |/ _ \\| '_ \\  | |_ | '__/ _` | '_ ` _ \\ / _ \\ \\ /\\ / / _ \\| '__| |/ /",
			"  / ___ \\| |_) | |_) | | | (_| (_| | |_| | (_) | | | | |  _|| | | (_| | | | | | |  __/\\ V  V / (_) | |  |   <",
			" /_/   \\_\\ .__/| .__/|_|_|\\___\\__,_|\\__|_|\\___/|_| |_| |_|  |_|  \\__,_|_| |_| |_|\\___| \\_/\\_/ \\___/|_|  |_|\\_\\",
			"",
			" | |__   |_|_ _|_| ___  __| |",
			" | '_ \\ / _` / __|/ _ \\/ _` |  / _ \\| '_ \\",
			" | |_) | (_| \\__ \\  __/ (_| | | (_) | | | |",
			" |_.__/ \\__,_|___/\\___|\\__,_|  \\___/|_| |_|",
			"",
			" / ___| _ __  _ __(_)",
			" \\___ \\| '_ \\| '__| | '_ \\ / _` |",
			"  ___) | |_) | |  | | | | | (_| |",
			" |____/| .__/|_|  |_|_| |_|\\__, |",
			"       |_|                 |___/           " };

	private static final String SPRING_BOOT = " :: Spring Boot :: ";

	private static final int STRAP_LINE_SIZE = 42;

	@Override
	public void printBanner(final Environment environment, final Class<?> sourceClass, final PrintStream printStream) {
		for (final String line : BANNER) {
			printStream.println(line);
		}
		String version = SpringBootVersion.getVersion();
		version = (version != null) ? " (v" + version + ")" : "";
		final StringBuilder padding = new StringBuilder();
		while (padding.length() < STRAP_LINE_SIZE - (version.length() + SPRING_BOOT.length())) {
			padding.append(" ");
		}

		printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, padding.toString(),
				AnsiStyle.FAINT, version));
		printStream.println();
	}

}
