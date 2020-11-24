/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example.framework.core;

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
			"     _                _ _           _   _               _____                                            _",
			"    / \\   _ __  _ __ | (_) ___ __ _| |_(_) ___  _ __   |  ___| __ __ _ _ __ ___   _____      _____  _ __| | _",
			"   / _ \\ | '_ \\| '_ \\| | |/ __/ _` | __| |/ _ \\| '_ \\  | |_ | '__/ _` | '_ ` _ \\ / _ \\ \\ /\\ / / _ \\| '__| |/ /",
			"  / ___ \\| |_) | |_) | | | (_| (_| | |_| | (_) | | | | |  _|| | | (_| | | | | | |  __/\\ V  V / (_) | |  |   <",
			" /_/   \\_\\ .__/| .__/|_|_|\\___\\__,_|\\__|_|\\___/|_| |_| |_|  |_|  \\__,_|_| |_| |_|\\___| \\_/\\_/ \\___/|_|  |_|\\_\\",
			"  _      |_|   |_|         _ ",
			" | |__   __   ___  ___  __| |    ___  _ __",
			" | '_ \\ / _` / __|/ _ \\/ _` |   / _ \\| '_ \\",
			" | |_) | (_| \\__ \\  __/ (_| |  | (_) | | | |",
			" |_.__/ \\__,_|___/\\___|\\__,_|   \\___/|_| |_|",
			"  ____",
			" / ___| _ __  _ __(_)_ __   __ _",
			" \\___ \\| '_ \\| '__| | '_ \\ / _` |",
			"  ___) | |_) | |  | | | | | (_| |",
			" |____/| .__/|_|  |_|_| |_|\\__, |",
			"       |_|                 |___/           ",
			"" };

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
