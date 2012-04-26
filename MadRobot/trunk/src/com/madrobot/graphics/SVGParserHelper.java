/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot.graphics;

import com.madrobot.math.MathUtils;

/**
 * Parses numbers from SVG text. Based on the Batik Number Parser (Apache 2
 * License).
 * 
 */
class SVGParserHelper {

	private char current;

	private int n;

	public int pos;

	private CharSequence s;

	public SVGParserHelper(CharSequence s, int pos) {
		this.s = s;
		this.pos = pos;
		n = s.length();
		current = s.charAt(pos);
	}

	public void advance() {
		current = read();
	}

	public float nextFloat() {
		skipWhitespace();
		float f = parseFloat();
		skipNumberSeparator();
		return f;
	}

	/**
	 * Parses the content of the buffer and converts it to a float.
	 */
	public float parseFloat() {
		int mant = 0;
		int mantDig = 0;
		boolean mantPos = true;
		boolean mantRead = false;

		int exp = 0;
		int expDig = 0;
		int expAdj = 0;
		boolean expPos = true;

		switch (current) {
		case '-':
			mantPos = false;
			// fallthrough
		case '+':
			current = read();
		}

		m1: switch (current) {
		default:
			return Float.NaN;

		case '.':
			break;

		case '0':
			mantRead = true;
			l: for (;;) {
				current = read();
				switch (current) {
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					break l;
				case '.':
				case 'e':
				case 'E':
					break m1;
				default:
					return 0.0f;
				case '0':
				}
			}

		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			mantRead = true;
			l: for (;;) {
				if (mantDig < 9) {
					mantDig++;
					mant = mant * 10 + (current - '0');
				} else {
					expAdj++;
				}
				current = read();
				switch (current) {
				default:
					break l;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				}
			}
		}

		if (current == '.') {
			current = read();
			m2: switch (current) {
			default:
			case 'e':
			case 'E':
				if (!mantRead) {
					reportUnexpectedCharacterError(current);
					return 0.0f;
				}
				break;

			case '0':
				if (mantDig == 0) {
					l: for (;;) {
						current = read();
						expAdj--;
						switch (current) {
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							break l;
						default:
							if (!mantRead) {
								return 0.0f;
							}
							break m2;
						case '0':
						}
					}
				}
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				l: for (;;) {
					if (mantDig < 9) {
						mantDig++;
						mant = mant * 10 + (current - '0');
						expAdj--;
					}
					current = read();
					switch (current) {
					default:
						break l;
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
					}
				}
			}
		}

		switch (current) {
		case 'e':
		case 'E':
			current = read();
			switch (current) {
			default:
				reportUnexpectedCharacterError(current);
				return 0f;
			case '-':
				expPos = false;
			case '+':
				current = read();
				switch (current) {
				default:
					reportUnexpectedCharacterError(current);
					return 0f;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				}
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			}

			en: switch (current) {
			case '0':
				l: for (;;) {
					current = read();
					switch (current) {
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						break l;
					default:
						break en;
					case '0':
					}
				}

			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				l: for (;;) {
					if (expDig < 3) {
						expDig++;
						exp = exp * 10 + (current - '0');
					}
					current = read();
					switch (current) {
					default:
						break l;
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
					}
				}
			}
		default:
		}

		if (!expPos) {
			exp = -exp;
		}
		exp += expAdj;
		if (!mantPos) {
			mant = -mant;
		}

		return MathUtils.buildFloat(mant, exp);
	}

	private char read() {
		if (pos < n) {
			pos++;
		}
		if (pos == n) {
			return '\0';
		} else {
			return s.charAt(pos);
		}
	}

	private void reportUnexpectedCharacterError(char c) {
		throw new RuntimeException("Unexpected char '" + c + "'.");
	}

	public void skipNumberSeparator() {
		while (pos < n) {
			char c = s.charAt(pos);
			switch (c) {
			case ' ':
			case ',':
			case '\n':
			case '\t':
				advance();
				break;
			default:
				return;
			}
		}
	}

	public void skipWhitespace() {
		while (pos < n) {
			if (Character.isWhitespace(s.charAt(pos))) {
				advance();
			} else {
				break;
			}
		}
	}
}