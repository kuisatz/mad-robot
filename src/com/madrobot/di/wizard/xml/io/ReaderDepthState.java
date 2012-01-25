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

package com.madrobot.di.wizard.xml.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Maintains the state of a pull reader at various states in the document depth.
 * 
 * Used by the {@link BinaryStreamReader}
 * 
 * @since 1.2
 */
class ReaderDepthState {

	private static final String EMPTY_STRING = "";

	private static class State {
		String name;
		String value;
		List attributes;
		boolean hasMoreChildren;
		State parent;
	}

	private static class Attribute {
		String name;
		String value;
	}

	private State current;

	public void push() {
		State newState = new State();
		newState.parent = current;
		current = newState;
	}

	public void pop() {
		current = current.parent;
	}

	public String getName() {
		return current.name;
	}

	public void setName(String name) {
		current.name = name;
	}

	public String getValue() {
		return current.value == null ? EMPTY_STRING : current.value;
	}

	public void setValue(String value) {
		current.value = value;
	}

	public boolean hasMoreChildren() {
		return current.hasMoreChildren;
	}

	public void setHasMoreChildren(boolean hasMoreChildren) {
		current.hasMoreChildren = hasMoreChildren;
	}

	public void addAttribute(String name, String value) {
		Attribute attribute = new Attribute();
		attribute.name = name;
		attribute.value = value;
		if (current.attributes == null) {
			current.attributes = new ArrayList();
		}
		current.attributes.add(attribute);
	}

	public String getAttribute(String name) {
		if (current.attributes == null) {
			return null;
		} else {
			// For short maps, it's faster to iterate then do a hashlookup.
			for (Iterator iterator = current.attributes.iterator(); iterator.hasNext();) {
				Attribute attribute = (Attribute) iterator.next();
				if (attribute.name.equals(name)) {
					return attribute.value;
				}
			}
			return null;
		}
	}

	public String getAttribute(int index) {
		if (current.attributes == null) {
			return null;
		} else {
			Attribute attribute = (Attribute) current.attributes.get(index);
			return attribute.value;
		}
	}

	public String getAttributeName(int index) {
		if (current.attributes == null) {
			return null;
		} else {
			Attribute attribute = (Attribute) current.attributes.get(index);
			return attribute.name;
		}
	}

	public int getAttributeCount() {
		return current.attributes == null ? 0 : current.attributes.size();
	}

	public Iterator getAttributeNames() {
		if (current.attributes == null) {
			return Collections.EMPTY_SET.iterator();
		} else {
			final Iterator attributeIterator = current.attributes.iterator();
			return new Iterator() {
				@Override
				public boolean hasNext() {
					return attributeIterator.hasNext();
				}

				@Override
				public Object next() {
					Attribute attribute = (Attribute) attributeIterator.next();
					return attribute.name;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
	}

}
