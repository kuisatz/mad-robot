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
package com.madrobot.text;

import java.util.ArrayList;

import android.graphics.Paint;

/**
 * <p>
 * Operations on Strings that contain words.
 * </p>
 * 
 * <p>
 * This class tries to handle <code>null</code> input gracefully. An exception
 * will not be thrown for a <code>null</code> input. Each method documents its
 * behaviour in more detail.
 * </p>
 * 
 */
public class WordUtils {

	// -----------------------------------------------------------------------
	/**
	 * Abbreviates a string nicely.
	 * 
	 * This method searches for the first space after the lower limit and
	 * abbreviates the String there. It will also append any String passed as a
	 * parameter to the end of the String. The upper limit can be specified to
	 * forcibly abbreviate a String.
	 * 
	 * @param str
	 *            the string to be abbreviated. If null is passed, null is
	 *            returned. If the empty String is passed, the empty string is
	 *            returned.
	 * @param lower
	 *            the lower limit.
	 * @param upper
	 *            the upper limit; specify -1 if no limit is desired. If the
	 *            upper limit is lower than the lower limit, it will be adjusted
	 *            to be the same as the lower limit.
	 * @param appendToEnd
	 *            String to be appended to the end of the abbreviated string.
	 *            This is appended ONLY if the string was indeed abbreviated.
	 *            The append does not count towards the lower or upper limits.
	 * @return the abbreviated String.
	 * @since 2.4
	 */
	public static String abbreviate(String str, int lower, int upper,
			String appendToEnd) {
		// initial parameter checks
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return StringUtils.EMPTY;
		}

		// if the lower value is greater than the length of the string,
		// set to the length of the string
		if (lower > str.length()) {
			lower = str.length();
		}
		// if the upper value is -1 (i.e. no limit) or is greater
		// than the length of the string, set to the length of the string
		if (upper == -1 || upper > str.length()) {
			upper = str.length();
		}
		// if upper is less than lower, raise it to lower
		if (upper < lower) {
			upper = lower;
		}

		StringBuilder result = new StringBuilder();
		int index = StringUtils.indexOf(str, " ", lower);
		if (index == -1) {
			result.append(str.substring(0, upper));
			// only if abbreviation has occured do we append the appendToEnd
			// value
			if (upper != str.length()) {
				result.append(StringUtils.defaultString(appendToEnd));
			}
		} else if (index > upper) {
			result.append(str.substring(0, upper));
			result.append(StringUtils.defaultString(appendToEnd));
		} else {
			result.append(str.substring(0, index));
			result.append(StringUtils.defaultString(appendToEnd));
		}
		return result.toString();
	}

	// Capitalizing
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Capitalizes all the whitespace separated words in a String. Only the
	 * first letter of each word is changed. To convert the rest of each word to
	 * lowercase at the same time, use {@link #capitalizeFully(String)}.
	 * </p>
	 * 
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A
	 * <code>null</code> input String returns <code>null</code>. Capitalization
	 * uses the unicode title case, normally equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.capitalize(null)        = null
	 * WordUtils.capitalize("")          = ""
	 * WordUtils.capitalize("i am FINE") = "I Am FINE"
	 * </pre>
	 * 
	 * @param str
	 *            the String to capitalize, may be null
	 * @return capitalized String, <code>null</code> if null String input
	 * @see #uncapitalize(String)
	 * @see #capitalizeFully(String)
	 */
	public static String capitalize(String str) {
		return capitalize(str, null);
	}

	/**
	 * <p>
	 * Capitalizes all the delimiter separated words in a String. Only the first
	 * letter of each word is changed. To convert the rest of each word to
	 * lowercase at the same time, use {@link #capitalizeFully(String, char[])}.
	 * </p>
	 * 
	 * <p>
	 * The delimiters represent a set of characters understood to separate
	 * words. The first string character and the first non-delimiter character
	 * after a delimiter will be capitalized.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * Capitalization uses the unicode title case, normally equivalent to upper
	 * case.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.capitalize(null, *)            = null
	 * WordUtils.capitalize("", *)              = ""
	 * WordUtils.capitalize(*, new char[0])     = *
	 * WordUtils.capitalize("i am fine", null)  = "I Am Fine"
	 * WordUtils.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
	 * </pre>
	 * 
	 * @param str
	 *            the String to capitalize, may be null
	 * @param delimiters
	 *            set of characters to determine capitalization, null means
	 *            whitespace
	 * @return capitalized String, <code>null</code> if null String input
	 * @see #uncapitalize(String)
	 * @see #capitalizeFully(String)
	 * @since 2.1
	 */
	public static String capitalize(String str, char... delimiters) {
		int delimLen = (delimiters == null ? -1 : delimiters.length);
		if (str == null || str.length() == 0 || delimLen == 0) {
			return str;
		}
		int strLen = str.length();
		StringBuilder buffer = new StringBuilder(strLen);
		boolean capitalizeNext = true;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);

			if (isDelimiter(ch, delimiters)) {
				buffer.append(ch);
				capitalizeNext = true;
			} else if (capitalizeNext) {
				buffer.append(Character.toTitleCase(ch));
				capitalizeNext = false;
			} else {
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts all the whitespace separated words in a String into capitalized
	 * words, that is each word is made up of a titlecase character and then a
	 * series of lowercase characters.
	 * </p>
	 * 
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A
	 * <code>null</code> input String returns <code>null</code>. Capitalization
	 * uses the unicode title case, normally equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.capitalizeFully(null)        = null
	 * WordUtils.capitalizeFully("")          = ""
	 * WordUtils.capitalizeFully("i am FINE") = "I Am Fine"
	 * </pre>
	 * 
	 * @param str
	 *            the String to capitalize, may be null
	 * @return capitalized String, <code>null</code> if null String input
	 */
	public static String capitalizeFully(String str) {
		return capitalizeFully(str, null);
	}

	/**
	 * <p>
	 * Converts all the delimiter separated words in a String into capitalized
	 * words, that is each word is made up of a titlecase character and then a
	 * series of lowercase characters.
	 * </p>
	 * 
	 * <p>
	 * The delimiters represent a set of characters understood to separate
	 * words. The first string character and the first non-delimiter character
	 * after a delimiter will be capitalized.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * Capitalization uses the unicode title case, normally equivalent to upper
	 * case.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.capitalizeFully(null, *)            = null
	 * WordUtils.capitalizeFully("", *)              = ""
	 * WordUtils.capitalizeFully(*, null)            = *
	 * WordUtils.capitalizeFully(*, new char[0])     = *
	 * WordUtils.capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
	 * </pre>
	 * 
	 * @param str
	 *            the String to capitalize, may be null
	 * @param delimiters
	 *            set of characters to determine capitalization, null means
	 *            whitespace
	 * @return capitalized String, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String capitalizeFully(String str, char... delimiters) {
		int delimLen = (delimiters == null ? -1 : delimiters.length);
		if (str == null || str.length() == 0 || delimLen == 0) {
			return str;
		}
		str = str.toLowerCase();
		return capitalize(str, delimiters);
	}

	/**
	 * Wraps the given string so it fits on the specified lines. First of all it
	 * is split at the line-breaks ('\n'), subsequently the substrings are split
	 * when they do not fit on a single line.
	 * 
	 * @param string
	 *            The string which should be wrapped
	 * @param font
	 *            The font which is used to display the font
	 * @param firstLineWidth
	 *            The allowed width for the first line
	 * @param lineWidth
	 *            The allowed width for all other lines, lineWidth >=
	 *            firstLineWidth
	 * @return The array containing the substrings
	 */
	public static final String[] doTextWrap(final String string,
			final Paint font, int firstLineWidth, final int lineWidth) {
		boolean hasLineBreaks = (string.indexOf('\n') != -1);
		float completeWidth = font.measureText(string);
		if (((completeWidth <= firstLineWidth) && !hasLineBreaks)) { // ||
																		// (value.
			// length()
			// <= 1) ) {
			// the given string fits on the first line:
			// if (hasLineBreaks) {
			// return split( "complete/linebreaks:" + completeWidth + "> " +
			// value, '\n');
			// } else {
			return new String[] { string };
			// }
		}
		// the given string does not fit on the first line:
		ArrayList<String> lines = new ArrayList<String>();
		if (!hasLineBreaks) {
			wrap(string, font, completeWidth, firstLineWidth, lineWidth, lines);
		} else {
			// now the string will be splitted at the line-breaks and
			// then each line is processed:
			char[] valueChars = string.toCharArray();
			int lastIndex = 0;
			char c = ' ';
			int lineBreakCount = 0;
			for (int i = 0; i < valueChars.length; i++) {
				c = valueChars[i];
				boolean isCRLF = ((c == 0x0D) && (i < valueChars.length - 1) && (valueChars[i + 1] == 0x0A));
				if ((c == '\n') || (i == valueChars.length - 1) || isCRLF) {
					lineBreakCount++;
					String line = null;
					if (i == valueChars.length - 1) {
						line = new String(valueChars, lastIndex, (i + 1)
								- lastIndex);
						// System.out.println("wrap: adding last line " + line
						// );
					} else {
						line = new String(valueChars, lastIndex, i - lastIndex);
						// System.out.println("wrap: adding " + line );
					}
					completeWidth = font.measureText(line);
					if (completeWidth <= firstLineWidth) {
						lines.add(line);
					} else {
						wrap(line, font, completeWidth, firstLineWidth,
								lineWidth, lines);
					}
					if (isCRLF) {
						i++;
					}
					lastIndex = i + 1;
					// after the first line all line widths are the same:
					firstLineWidth = lineWidth;
				} // for each line
			} // for all chars
				// special case for lines that end with \n: add a further line
			if ((lineBreakCount > 1) && ((c == '\n') || (c == 10))) {
				lines.add(" ");
			}
		}
		String[] ret = new String[lines.size()];
		lines.toArray(ret);
		// new String[lines.size()];
		// System.out.println("Array Size " + ret.length);
		return ret;// (String[]) CollectionUtil.toArray(lines);//
		// ines.toArray(new
		// String
		// [lines.size()]);
	}

	/**
	 * Returns a string that fits into the given<code> width</code>
	 * 
	 * @param string
	 *            to fit
	 * @param width
	 *            of the area to render the string
	 * @param font
	 *            used for rendering
	 * @param endPadding
	 *            String to be used for the end padding eg: ellipsis (...) for
	 *            instance
	 * @return <code>string</code> if it fits in the string width, else the
	 *         string content that fits and with trailing dots (...)
	 */
	public static String getFittingString(final String string,
			final float width, final Paint font, final String endPadding) {
		if (font.measureText(string) < width) {
			return string;
		} else {
			int endPad = (int) font.measureText(endPadding);
			for (int i = 0; i < string.length(); i++) {
				if (font.measureText(string.substring(0, i)) + endPad > width) {
					endPad = i;
					break;
				}
			}
			/* final check to see if it conforms to the width */
			if (font.measureText(string.substring(0, endPad) + endPadding) > width) {
				for (; font.measureText(string.substring(0, endPad)
						+ endPadding) > width;) {
					endPad--;
				}
			}
			return new String(string.substring(0, endPad) + endPadding);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Extracts the initial letters from each word in the String.
	 * </p>
	 * 
	 * <p>
	 * The first letter of the string and all first letters after whitespace are
	 * returned as a new string. Their case is not changed.
	 * </p>
	 * 
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.initials(null)             = null
	 * WordUtils.initials("")               = ""
	 * WordUtils.initials("Ben John Lee")   = "BJL"
	 * WordUtils.initials("Ben J.Lee")      = "BJ"
	 * </pre>
	 * 
	 * @param str
	 *            the String to get initials from, may be null
	 * @return String of initial letters, <code>null</code> if null String input
	 * @see #initials(String,char[])
	 * @since 2.2
	 */
	public static String initials(String str) {
		return initials(str, null);
	}

	/**
	 * <p>
	 * Extracts the initial letters from each word in the String.
	 * </p>
	 * 
	 * <p>
	 * The first letter of the string and all first letters after the defined
	 * delimiters are returned as a new string. Their case is not changed.
	 * </p>
	 * 
	 * <p>
	 * If the delimiters array is null, then Whitespace is used. Whitespace is
	 * defined by {@link Character#isWhitespace(char)}. A <code>null</code>
	 * input String returns <code>null</code>. An empty delimiter array returns
	 * an empty String.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.initials(null, *)                = null
	 * WordUtils.initials("", *)                  = ""
	 * WordUtils.initials("Ben John Lee", null)   = "BJL"
	 * WordUtils.initials("Ben J.Lee", null)      = "BJ"
	 * WordUtils.initials("Ben J.Lee", [' ','.']) = "BJL"
	 * WordUtils.initials(*, new char[0])         = ""
	 * </pre>
	 * 
	 * @param str
	 *            the String to get initials from, may be null
	 * @param delimiters
	 *            set of characters to determine words, null means whitespace
	 * @return String of initial letters, <code>null</code> if null String input
	 * @see #initials(String)
	 * @since 2.2
	 */
	public static String initials(String str, char... delimiters) {
		if (str == null || str.length() == 0) {
			return str;
		}
		if (delimiters != null && delimiters.length == 0) {
			return "";
		}
		int strLen = str.length();
		char[] buf = new char[strLen / 2 + 1];
		int count = 0;
		boolean lastWasGap = true;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);

			if (isDelimiter(ch, delimiters)) {
				lastWasGap = true;
			} else if (lastWasGap) {
				buf[count++] = ch;
				lastWasGap = false;
			} else {
				// ignore ch
			}
		}
		return new String(buf, 0, count);
	}

	// -----------------------------------------------------------------------
	/**
	 * Is the character a delimiter.
	 * 
	 * @param ch
	 *            the character to check
	 * @param delimiters
	 *            the delimiters
	 * @return true if it is a delimiter
	 */
	private static boolean isDelimiter(char ch, char[] delimiters) {
		if (delimiters == null) {
			return Character.isWhitespace(ch);
		}
		for (int i = 0, isize = delimiters.length; i < isize; i++) {
			if (ch == delimiters[i]) {
				return true;
			}
		}
		return false;
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Swaps the case of a String using a word based algorithm.
	 * </p>
	 * 
	 * <ul>
	 * <li>Upper case character converts to Lower case</li>
	 * <li>Title case character converts to Lower case</li>
	 * <li>Lower case character after Whitespace or at start converts to Title
	 * case</li>
	 * <li>Other Lower case character converts to Upper case</li>
	 * </ul>
	 * 
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.swapCase(null)                 = null
	 * StringUtils.swapCase("")                   = ""
	 * StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 * 
	 * @param str
	 *            the String to swap case, may be null
	 * @return the changed String, <code>null</code> if null String input
	 */
	public static String swapCase(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		StringBuilder buffer = new StringBuilder(strLen);

		boolean whitespace = true;
		char ch = 0;
		char tmp = 0;

		for (int i = 0; i < strLen; i++) {
			ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				tmp = Character.toLowerCase(ch);
			} else if (Character.isTitleCase(ch)) {
				tmp = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				if (whitespace) {
					tmp = Character.toTitleCase(ch);
				} else {
					tmp = Character.toUpperCase(ch);
				}
			} else {
				tmp = ch;
			}
			buffer.append(tmp);
			whitespace = Character.isWhitespace(ch);
		}
		return buffer.toString();
	}

	/**
	 * Convert name of format THIS_IS_A_NAME to ThisIsAName For each letter: if
	 * not '_' then convert to lower case and add to output string if '_' then
	 * skip letter and add next letter to output string without converting to
	 * lower case
	 * 
	 * @param sqlNotation
	 * @return A name complaint with naming convention for Java classes,
	 *         converted from SQL name
	 */
	public static String toJavaClassName(String sqlNotation) {
		StringBuilder sb = new StringBuilder();
		char[] buf = sqlNotation.toCharArray();
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (i == 0) {
				sb.append(buf[i]);
			} else if (c != '_') {
				sb.append(Character.toLowerCase(c));
			} else {
				i++;
				if (i < buf.length) {
					sb.append(buf[i]);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Convert name of format THIS_IS_A_NAME to thisIsAName For each letter: if
	 * not '_' then convert to lower case and add to output string if '_' then
	 * skip letter and add next letter to output string without converting to
	 * lower case
	 * 
	 * @param sqlNotation
	 * @return A name complaint with naming convention for Java methods and
	 *         fields, converted from SQL name
	 */
	public static String toJavaMethodName(String sqlNotation) {
		StringBuilder dest = new StringBuilder();
		char[] src = sqlNotation.toCharArray();

		for (int i = 0; i < src.length; i++) {
			char c = src[i];
			boolean isFirstChar = (i == 0) ? true : false;

			if (isFirstChar || c != '_') {
				dest.append(Character.toLowerCase(c));
			} else {
				i++;
				if (i < src.length) {
					dest.append(src[i]);
				}
			}
		}
		return dest.toString();
	}

	/**
	 * Convert name of format thisIsAName to THIS_IS_A_NAME For each letter: if
	 * lower case then convert to upper case if upper case then add '_' to
	 * output before ading letter
	 * 
	 * @param javaNotation
	 * @return SQL name translated from Java name
	 */
	public static String toSQLName(String javaNotation) {
		if (javaNotation.equalsIgnoreCase("_id"))
			return "_id";

		StringBuilder sb = new StringBuilder();
		char[] buf = javaNotation.toCharArray();

		for (int i = 0; i < buf.length; i++) {
			char prevChar = (i > 0) ? buf[i - 1] : 0;
			char c = buf[i];
			char nextChar = (i < buf.length - 1) ? buf[i + 1] : 0;
			boolean isFirstChar = (i == 0) ? true : false;

			// "AbCd"->"AB_CD"
			// "ABCd"->"AB_CD"
			// "AbCD"->"AB_CD"
			// "ShowplaceDetailsVO"->"SHOWPLACE_DETAILS_VO"
			if (isFirstChar || Character.isLowerCase(c)) {
				sb.append(Character.toUpperCase(c));
			} else if (Character.isUpperCase(c)) {
				if (Character.isLetterOrDigit(prevChar)) {
					if (Character.isLowerCase(prevChar)) {
						sb.append('_').append(Character.toUpperCase(c));
					} else if (nextChar > 0 && Character.isLowerCase(nextChar)) {
						sb.append('_').append(Character.toUpperCase(c));
					} else {
						sb.append(c);
					}
				} else {
					sb.append(c);
				}
			}
		}

		return sb.toString();
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Uncapitalizes all the whitespace separated words in a String. Only the
	 * first letter of each word is changed.
	 * </p>
	 * 
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.uncapitalize(null)        = null
	 * WordUtils.uncapitalize("")          = ""
	 * WordUtils.uncapitalize("I Am FINE") = "i am fINE"
	 * </pre>
	 * 
	 * @param str
	 *            the String to uncapitalize, may be null
	 * @return uncapitalized String, <code>null</code> if null String input
	 * @see #capitalize(String)
	 */
	public static String uncapitalize(String str) {
		return uncapitalize(str, null);
	}

	/**
	 * <p>
	 * Uncapitalizes all the whitespace separated words in a String. Only the
	 * first letter of each word is changed.
	 * </p>
	 * 
	 * <p>
	 * The delimiters represent a set of characters understood to separate
	 * words. The first string character and the first non-delimiter character
	 * after a delimiter will be uncapitalized.
	 * </p>
	 * 
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A
	 * <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.uncapitalize(null, *)            = null
	 * WordUtils.uncapitalize("", *)              = ""
	 * WordUtils.uncapitalize(*, null)            = *
	 * WordUtils.uncapitalize(*, new char[0])     = *
	 * WordUtils.uncapitalize("I AM.FINE", {'.'}) = "i AM.fINE"
	 * </pre>
	 * 
	 * @param str
	 *            the String to uncapitalize, may be null
	 * @param delimiters
	 *            set of characters to determine uncapitalization, null means
	 *            whitespace
	 * @return uncapitalized String, <code>null</code> if null String input
	 * @see #capitalize(String)
	 * @since 2.1
	 */
	public static String uncapitalize(String str, char... delimiters) {
		int delimLen = (delimiters == null ? -1 : delimiters.length);
		if (str == null || str.length() == 0 || delimLen == 0) {
			return str;
		}
		int strLen = str.length();
		StringBuilder buffer = new StringBuilder(strLen);
		boolean uncapitalizeNext = true;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);

			if (isDelimiter(ch, delimiters)) {
				buffer.append(ch);
				uncapitalizeNext = true;
			} else if (uncapitalizeNext) {
				buffer.append(Character.toLowerCase(ch));
				uncapitalizeNext = false;
			} else {
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}

	/**
	 * Utility method to take a string and convert it to normal Java variable
	 * name capitalization. This normally means converting the first character
	 * from upper case to lower case, but in the (unusual) special case when
	 * there is more than one character and both the first and second characters
	 * are upper case, we leave it alone.
	 * <p>
	 * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays as
	 * "URL".
	 * 
	 * @param name
	 *            The string to be decapitalized.
	 * @return The decapitalized version of the string.
	 */
	public static String simpleUncapitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		if (name.length() > 1 && Character.isUpperCase(name.charAt(1))
				&& Character.isUpperCase(name.charAt(0))) {
			return name;
		}
		char chars[] = name.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

	// Wrapping
	// --------------------------------------------------------------------------
	/**
	 * <p>
	 * Wraps a single line of text, identifying words by <code>' '</code>.
	 * </p>
	 * 
	 * <p>
	 * New lines will be separated by the system property line separator. Very
	 * long words, such as URLs will <i>not</i> be wrapped.
	 * </p>
	 * 
	 * <p>
	 * Leading spaces on a new line are stripped. Trailing spaces are not
	 * stripped.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.wrap(null, *) = null
	 * WordUtils.wrap("", *) = ""
	 * </pre>
	 * 
	 * @param str
	 *            the String to be word wrapped, may be null
	 * @param wrapLength
	 *            the column to wrap the words at, less than 1 is treated as 1
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength) {
		return wrap(str, wrapLength, null, false);
	}

	/**
	 * <p>
	 * Wraps a single line of text, identifying words by <code>' '</code>.
	 * </p>
	 * 
	 * <p>
	 * Leading spaces on a new line are stripped. Trailing spaces are not
	 * stripped.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.wrap(null, *, *, *) = null
	 * WordUtils.wrap("", *, *, *) = ""
	 * </pre>
	 * 
	 * @param str
	 *            the String to be word wrapped, may be null
	 * @param wrapLength
	 *            the column to wrap the words at, less than 1 is treated as 1
	 * @param newLineStr
	 *            the string to insert for a new line, <code>null</code> uses
	 *            the system property line separator
	 * @param wrapLongWords
	 *            true if long words (such as URLs) should be wrapped
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength, String newLineStr,
			boolean wrapLongWords) {
		if (str == null) {
			return null;
		}
		if (newLineStr == null) {
			newLineStr = "\n";// SystemUtils.LINE_SEPARATOR;
		}
		if (wrapLength < 1) {
			wrapLength = 1;
		}
		int inputLineLength = str.length();
		int offset = 0;
		StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

		while ((inputLineLength - offset) > wrapLength) {
			if (str.charAt(offset) == ' ') {
				offset++;
				continue;
			}
			int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

			if (spaceToWrapAt >= offset) {
				// normal case
				wrappedLine.append(str.substring(offset, spaceToWrapAt));
				wrappedLine.append(newLineStr);
				offset = spaceToWrapAt + 1;

			} else {
				// really long word or URL
				if (wrapLongWords) {
					// wrap really long word one line at a time
					wrappedLine.append(str.substring(offset, wrapLength
							+ offset));
					wrappedLine.append(newLineStr);
					offset += wrapLength;
				} else {
					// do not wrap really long word, just extend beyond limit
					spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
					if (spaceToWrapAt >= 0) {
						wrappedLine
								.append(str.substring(offset, spaceToWrapAt));
						wrappedLine.append(newLineStr);
						offset = spaceToWrapAt + 1;
					} else {
						wrappedLine.append(str.substring(offset));
						offset = inputLineLength;
					}
				}
			}
		}

		// Whatever is left in line is short enough to just pass through
		wrappedLine.append(str.substring(offset));

		return wrappedLine.toString();
	}

	/**
	 * Wraps the given string so that the substrings fit into the the given
	 * line-widths. It is expected that the specified lineWidth >=
	 * firstLineWidth. The resulting substrings will be added to the given
	 * ArrayList. When the complete string fits into the first line, it will be
	 * added to the list. When the string needs to be split to fit on the lines,
	 * it is tried to split the string at a gap between words. When this is not
	 * possible, the given string will be split in the middle of the
	 * corresponding word.
	 * 
	 * 
	 * @param value
	 *            The string which should be wrapped
	 * @param font
	 *            The font which is used to display the font
	 * @param completeWidth
	 *            The complete width of the given string for the specified font.
	 * @param firstLineWidth
	 *            The allowed width for the first line
	 * @param lineWidth
	 *            The allowed width for all other lines, lineWidth >=
	 *            firstLineWidth
	 * @param list
	 *            The list to which the substrings will be added.
	 */
	private static void wrap(String value, Paint font, float completeWidth,
			float firstLineWidth, float lineWidth, ArrayList<String> list) {
		char[] valueChars = value.toCharArray();
		int startPos = 0;
		int lastSpacePos = -1;
		int lastSpacePosLength = 0;
		int currentLineWidth = 0;
		for (int i = 0; i < valueChars.length; i++) {
			char c = valueChars[i];
			currentLineWidth += font.measureText(String.valueOf(c));// .charWidth(c);
			if (c == '\n') {
				list.add(new String(valueChars, startPos, i - startPos));
				lastSpacePos = -1;
				startPos = i + 1;
				currentLineWidth = 0;
				firstLineWidth = lineWidth;
				i = startPos;
			} else if ((currentLineWidth > firstLineWidth) && (i > 0)) {
				if ((c == ' ') || (c == '\t')) {
					list.add(new String(valueChars, startPos, i - startPos));
					startPos = ++i;
					currentLineWidth = 0;
					lastSpacePos = -1;
				} else if (lastSpacePos == -1) {
					if (i > startPos + 1) {
						i--;
					}
					// System.out.println("value=" + value + ", i=" + i +
					// ", startPos=" + startPos);
					list.add(new String(valueChars, startPos, i - startPos));
					startPos = i;
					currentLineWidth = 0;
				} else {
					currentLineWidth -= lastSpacePosLength;
					list.add(new String(valueChars, startPos, lastSpacePos
							- startPos));
					startPos = lastSpacePos + 1;
					lastSpacePos = -1;
				}
				firstLineWidth = lineWidth;
			} else if ((c == ' ') || (c == '\t')) {
				lastSpacePos = i;
				lastSpacePosLength = currentLineWidth;
			}

		}
		// add tail:
		list.add(new String(valueChars, startPos, valueChars.length - startPos));

	}

	/**
	 * <p>
	 * <code>WordUtils</code> instances should NOT be constructed in standard
	 * programming. Instead, the class should be used as
	 * <code>WordUtils.wrap("foo bar", 20);</code>.
	 * </p>
	 * 
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean
	 * instance to operate.
	 * </p>
	 */
	public WordUtils() {
		super();
	}

}