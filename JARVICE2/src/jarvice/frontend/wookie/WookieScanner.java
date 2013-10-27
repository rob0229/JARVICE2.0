package jarvice.frontend.wookie;

import jarvice.frontend.*;
import jarvice.frontend.wookie.tokens.*;
import static jarvice.frontend.Source.EOF;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.frontend.wookie.WookieTokenType.*;

/**
 * <h1>PascalScanner</h1>
 * 
 * <p>
 * The Pascal scanner.
 * </p>
 * 
 * <p>
 * Copyright (c) 2009 by Ronald Mak
 * </p>
 * <p>
 * For instructional purposes only. No warranties.
 * </p>
 */
public class WookieScanner extends Scanner {
	/**
	 * Constructor
	 * 
	 * @param source
	 *            the source to be used with this scanner.
	 */
	public WookieScanner(Source source) {
		super(source);
	}

	/**
	 * Extract and return the next Pascal token from the source.
	 * 
	 * @return the next token.
	 * @throws Exception
	 *             if an error occurred.
	 */
	protected Token extractToken() throws Exception {
		skipWhiteSpace();

		Token token;
		char currentChar = currentChar();

		// Construct the next token. The current character determines the
		// token type.
		if (currentChar == EOF) {
			token = new EofToken(source);
		} else if (Character.isLetter(currentChar)) {
			token = new WookieWordToken(source);
		} else if (Character.isDigit(currentChar)) {
			token = new WookieNumberToken(source);
		} else if (currentChar == '\'') {
			token = new WookieStringToken(source);
		} else if (WookieTokenType.SPECIAL_SYMBOLS.containsKey(Character
				.toString(currentChar))) {
			token = new WookieSpecialSymbolToken(source);
		} else {
			token = new WookieErrorToken(source, INVALID_CHARACTER,
					Character.toString(currentChar));
			nextChar(); // consume character
		}

		return token;
	}

	/**
	 * Skip whitespace characters by consuming them. A comment is whitespace.
	 * 
	 * @throws Exception
	 *             if an error occurred.
	 */
	private void skipWhiteSpace() throws Exception {

		String s = " ";
		char currentChar = currentChar();

		while (Character.isWhitespace(currentChar) || (currentChar == '/')
				|| (currentChar == '#')) {

			// Start of import statement?
			if (currentChar == '#') {
				do {
					currentChar = nextChar(); // consume import characters
				} while ((currentChar != ';'));
				// Found closing '}'?
				if (currentChar == ';') {
					currentChar = nextChar(); // consume the '}'
				}
			}

			// comment line

			else if ((currentChar == '/') && (source.peekChar() == '*')) {

				currentChar = nextChar(); // consumes '/'
				if (currentChar == '*') {
					do {
						currentChar = nextChar(); // consume comment characters
					} while (!((currentChar == '*') && (source.peekChar() == '/')));
					// found closing */ ?
					if ((currentChar == '*') && (source.peekChar() == '/')) {
						currentChar = nextChar(); // consume the '*'
						currentChar = nextChar(); // consume the '/'
					}
				}
			}

			else if ((currentChar == '/') && (source.peekChar() == '/')) {

				currentChar = nextChar(); // consumes '/'
				if (currentChar == '/') {
					do {

						currentChar = nextChar(); // consume comment characters
					} while (!(currentChar == source.EOL));
				}
			}

			// Not a comment.
			else {
				currentChar = nextChar(); // consume whitespace character
			}
		}
	}
}
