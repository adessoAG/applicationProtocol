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
package de.adesso.example.application;

import org.javamoney.moneta.Money;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;

/**
 * This interface defines the functionality required for calculation of prices.
 *
 * @author Matthias
 *
 */
public interface PriceCalculator {

	/**
	 * demonstration of a method which is build manually by the configuration
	 *
	 * @param article    article to be calculated
	 * @param appendixes the list of appendixes which may carry information not
	 *                   visible to all participating beans
	 * @return the calculated price which is based on information within appendixes
	 */
	ApplicationProtocol<Money> calculatePrice(Article article, ApplicationProtocol<Money> appendixes);
}
