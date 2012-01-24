/*******************************************************************************
 * Copyright (c) 2012 MadRobot.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the GNU Lesser Public License v2.1
 *  which accompanies this distribution, and is available at
 *  http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *  
 *  Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
 
package com.madrobot.di.wizard.xml.io.xml;

import java.io.IOException;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.madrobot.di.wizard.xml.converters.ErrorWriter;
import com.madrobot.di.wizard.xml.io.NameCoder;
import com.madrobot.di.wizard.xml.io.StreamException;

/**
 * XStream reader that pulls directly from the stream using the XmlPullParser API.
 *
 */
public class XppReader extends AbstractPullReader {

    private final XmlPullParser parser;
    private final Reader reader;

    /**
     * Construct an XppReader.
     * 
     * @param reader the reader with the input data
     * @param parser the XPP parser to use
     * @since 1.4
     */
    public XppReader(Reader reader, XmlPullParser parser) {
        this(reader, parser, new XmlFriendlyNameCoder());
    }

    /**
     * Construct an XppReader.
     * 
     * @param reader the reader with the input data
     * @param parser the XPP parser to use
     * @param nameCoder the coder for XML friendly tag and attribute names
     * @since 1.4
     */
    public XppReader(Reader reader, XmlPullParser parser, NameCoder nameCoder) {
        super(nameCoder);
        this.parser = parser;
        this.reader = reader;
        try {
            parser.setInput(this.reader);
        } catch (XmlPullParserException e) {
            throw new StreamException(e);
        }
        moveDown();
    }

    /**
     * @deprecated As of 1.4, use {@link #XppReader(Reader, XmlPullParser)}  instead
     */
    public XppReader(Reader reader) {
		this(reader, new XmlFriendlyNameCoder());
    }

    /**
     * @since 1.2
     * @deprecated As of 1.4, use {@link #XppReader(Reader, XmlPullParser, NameCoder)}  instead
     */
    public XppReader(Reader reader, XmlFriendlyNameCoder replacer) {
        super(replacer);
        try {
            parser = createParser();
            this.reader = reader;
            parser.setInput(this.reader);
            moveDown();
        } catch (XmlPullParserException e) {
            throw new StreamException(e);
        }
    }
    
    /**
     * To use another implementation of org.xmlpull.v1.XmlPullParser, override this method.
     * @deprecated As of 1.4, use {@link #XppReader(Reader, XmlPullParser)}  instead
     */
    protected XmlPullParser createParser() {
        Exception exception = null;
        try {
            return (XmlPullParser)Class.forName("org.xmlpull.mxp1.MXParser", true, XmlPullParser.class.getClassLoader()).newInstance();
        } catch (InstantiationException e) {
            exception = e;
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        }
        throw new StreamException("Cannot create Xpp3 parser instance.", exception);
    }

    protected int pullNextEvent() {
        try {
            switch(parser.next()) {
                case XmlPullParser.START_DOCUMENT:
                case XmlPullParser.START_TAG:
                    return START_NODE;
                case XmlPullParser.END_DOCUMENT:
                case XmlPullParser.END_TAG:
                    return END_NODE;
                case XmlPullParser.TEXT:
                    return TEXT;
                case XmlPullParser.COMMENT:
                    return COMMENT;
                default:
                    return OTHER;
            }
        } catch (XmlPullParserException e) {
            throw new StreamException(e);
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    protected String pullElementName() {
        return parser.getName();
    }

    protected String pullText() {
        return parser.getText();
    }

    public String getAttribute(String name) {
        return parser.getAttributeValue(null, encodeAttribute(name));
    }

    public String getAttribute(int index) {
        return parser.getAttributeValue(index);
    }

    public int getAttributeCount() {
        return parser.getAttributeCount();
    }

    public String getAttributeName(int index) {
        return decodeAttribute(parser.getAttributeName(index));
    }

    public void appendErrors(ErrorWriter errorWriter) {
        errorWriter.add("line number", String.valueOf(parser.getLineNumber()));
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

}
