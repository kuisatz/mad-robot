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
package com.madrobot.beans;

import java.lang.ref.Reference;
import java.lang.reflect.Method;

import com.madrobot.lang.reflect.MethodUtils;

/**
 * An EventSetDescriptor describes a group of events that a given Java bean
 * fires.
 * <P>
 * The given group of events are all delivered as method calls on a single event
 * listener interface, and an event listener object can be registered via a call
 * on a registration method supplied by the event source.
 */
public class EventSetDescriptor extends FeatureDescriptor {

	private static String getListenerClassName(Class cls) {
		String className = cls.getName();
		return className.substring(className.lastIndexOf('.') + 1);
	}

	private static Method getMethod(Class cls, String name, int args)
			throws IntrospectionException {
		if (name == null) {
			return null;
		}
		Method method = MethodUtils.findAccessibleMethodIncludeInterfaces(cls,
				name, args, null);
		if (method == null) {
			throw new IntrospectionException("Method not found: " + name
					+ " on class " + cls.getName());
		}
		return method;
	}

	private MethodDescriptor addMethodDescriptor;
	private MethodDescriptor getMethodDescriptor;

	private boolean inDefaultEventSet = true;
	private MethodDescriptor[] listenerMethodDescriptors;

	private Reference listenerMethodsRef;
	private Reference listenerTypeRef;

	private MethodDescriptor removeMethodDescriptor;

	private boolean unicast;

	/**
	 * Creates an <TT>EventSetDescriptor</TT> assuming that you are following
	 * the most simple standard design pattern where a named event
	 * &quot;fred&quot; is (1) delivered as a call on the single method of
	 * interface FredListener, (2) has a single argument of type FredEvent, and
	 * (3) where the FredListener may be registered with a call on an
	 * addFredListener method of the source component and removed with a call on
	 * a removeFredListener method.
	 * 
	 * @param sourceClass
	 *            The class firing the event.
	 * @param eventSetName
	 *            The programmatic name of the event. E.g. &quot;fred&quot;.
	 *            Note that this should normally start with a lower-case
	 *            character.
	 * @param listenerType
	 *            The target interface that events will get delivered to.
	 * @param listenerMethodName
	 *            The method that will get called when the event gets delivered
	 *            to its target listener interface.
	 * @exception IntrospectionException
	 *                if an exception occurs during introspection.
	 */
	public EventSetDescriptor(Class<?> sourceClass, String eventSetName,
			Class<?> listenerType, String listenerMethodName)
			throws IntrospectionException {
		this(sourceClass, eventSetName, listenerType,
				new String[] { listenerMethodName }, "add"
						+ getListenerClassName(listenerType), "remove"
						+ getListenerClassName(listenerType), "get"
						+ getListenerClassName(listenerType) + "s");

		String eventName = capitalize(eventSetName) + "Event";
		Method[] listenerMethods = getListenerMethods();
		if (listenerMethods.length > 0) {
			Class[] args = listenerMethods[0].getParameterTypes();
			// Check for EventSet compliance. Special case for vetoableChange.
			// See 4529996
			if (!"vetoableChange".equals(eventSetName)
					&& !args[0].getName().endsWith(eventName)) {
				throw new IntrospectionException("Method \""
						+ listenerMethodName + "\" should have argument \""
						+ eventName + "\"");
			}
		}
	}

	/**
	 * Creates an <TT>EventSetDescriptor</TT> from scratch using string names.
	 * 
	 * @param sourceClass
	 *            The class firing the event.
	 * @param eventSetName
	 *            The programmatic name of the event set. Note that this should
	 *            normally start with a lower-case character.
	 * @param listenerType
	 *            The Class of the target interface that events will get
	 *            delivered to.
	 * @param listenerMethodNames
	 *            The names of the methods that will get called when the event
	 *            gets delivered to its target listener interface.
	 * @param addListenerMethodName
	 *            The name of the method on the event source that can be used to
	 *            register an event listener object.
	 * @param removeListenerMethodName
	 *            The name of the method on the event source that can be used to
	 *            de-register an event listener object.
	 * @exception IntrospectionException
	 *                if an exception occurs during introspection.
	 */
	public EventSetDescriptor(Class<?> sourceClass, String eventSetName,
			Class<?> listenerType, String listenerMethodNames[],
			String addListenerMethodName, String removeListenerMethodName)
			throws IntrospectionException {
		this(sourceClass, eventSetName, listenerType, listenerMethodNames,
				addListenerMethodName, removeListenerMethodName, null);
	}

	/**
	 * This constructor creates an EventSetDescriptor from scratch using string
	 * names.
	 * 
	 * @param sourceClass
	 *            The class firing the event.
	 * @param eventSetName
	 *            The programmatic name of the event set. Note that this should
	 *            normally start with a lower-case character.
	 * @param listenerType
	 *            The Class of the target interface that events will get
	 *            delivered to.
	 * @param listenerMethodNames
	 *            The names of the methods that will get called when the event
	 *            gets delivered to its target listener interface.
	 * @param addListenerMethodName
	 *            The name of the method on the event source that can be used to
	 *            register an event listener object.
	 * @param removeListenerMethodName
	 *            The name of the method on the event source that can be used to
	 *            de-register an event listener object.
	 * @param getListenerMethodName
	 *            The method on the event source that can be used to access the
	 *            array of event listener objects.
	 * @exception IntrospectionException
	 *                if an exception occurs during introspection.
	 * @since 1.4
	 */
	public EventSetDescriptor(Class<?> sourceClass, String eventSetName,
			Class<?> listenerType, String listenerMethodNames[],
			String addListenerMethodName, String removeListenerMethodName,
			String getListenerMethodName) throws IntrospectionException {
		if (sourceClass == null || eventSetName == null || listenerType == null) {
			throw new NullPointerException();
		}
		setName(eventSetName);
		setClass0(sourceClass);
		setListenerType(listenerType);

		Method[] listenerMethods = new Method[listenerMethodNames.length];
		for (int i = 0; i < listenerMethodNames.length; i++) {
			// Check for null names
			if (listenerMethodNames[i] == null) {
				throw new NullPointerException();
			}
			listenerMethods[i] = getMethod(listenerType,
					listenerMethodNames[i], 1);
		}
		setListenerMethods(listenerMethods);

		setAddListenerMethod(getMethod(sourceClass, addListenerMethodName, 1));
		setRemoveListenerMethod(getMethod(sourceClass,
				removeListenerMethodName, 1));

		// Be more forgiving of not finding the getListener method.
		Method method = MethodUtils.findAccessibleMethodIncludeInterfaces(
				sourceClass, getListenerMethodName, 0, null);
		if (method != null) {
			setGetListenerMethod(method);
		}
	}

	/*
	 * Package-private dup constructor This must isolate the new object from any
	 * changes to the old object.
	 */
	EventSetDescriptor(EventSetDescriptor old) {
		super(old);
		if (old.listenerMethodDescriptors != null) {
			int len = old.listenerMethodDescriptors.length;
			listenerMethodDescriptors = new MethodDescriptor[len];
			for (int i = 0; i < len; i++) {
				listenerMethodDescriptors[i] = new MethodDescriptor(
						old.listenerMethodDescriptors[i]);
			}
		}
		listenerTypeRef = old.listenerTypeRef;

		addMethodDescriptor = old.addMethodDescriptor;
		removeMethodDescriptor = old.removeMethodDescriptor;
		getMethodDescriptor = old.getMethodDescriptor;

		unicast = old.unicast;
		inDefaultEventSet = old.inDefaultEventSet;
	}

	/*
	 * Package-private constructor Merge two event set descriptors. Where they
	 * conflict, give the second argument (y) priority over the first argument
	 * (x).
	 * 
	 * @param x The first (lower priority) EventSetDescriptor
	 * 
	 * @param y The second (higher priority) EventSetDescriptor
	 */
	EventSetDescriptor(EventSetDescriptor x, EventSetDescriptor y) {
		super(x, y);
		listenerMethodDescriptors = x.listenerMethodDescriptors;
		if (y.listenerMethodDescriptors != null) {
			listenerMethodDescriptors = y.listenerMethodDescriptors;
		}

		listenerTypeRef = x.listenerTypeRef;
		if (y.listenerTypeRef != null) {
			listenerTypeRef = y.listenerTypeRef;
		}

		addMethodDescriptor = x.addMethodDescriptor;
		if (y.addMethodDescriptor != null) {
			addMethodDescriptor = y.addMethodDescriptor;
		}

		removeMethodDescriptor = x.removeMethodDescriptor;
		if (y.removeMethodDescriptor != null) {
			removeMethodDescriptor = y.removeMethodDescriptor;
		}

		getMethodDescriptor = x.getMethodDescriptor;
		if (y.getMethodDescriptor != null) {
			getMethodDescriptor = y.getMethodDescriptor;
		}

		unicast = y.unicast;
		if (!x.inDefaultEventSet || !y.inDefaultEventSet) {
			inDefaultEventSet = false;
		}
	}

	/**
	 * Creates an <TT>EventSetDescriptor</TT> from scratch using
	 * <TT>java.lang.reflect.Method</TT> and <TT>java.lang.Class</TT> objects.
	 * 
	 * @param eventSetName
	 *            The programmatic name of the event set.
	 * @param listenerType
	 *            The Class for the listener interface.
	 * @param listenerMethods
	 *            An array of Method objects describing each of the event
	 *            handling methods in the target listener.
	 * @param addListenerMethod
	 *            The method on the event source that can be used to register an
	 *            event listener object.
	 * @param removeListenerMethod
	 *            The method on the event source that can be used to de-register
	 *            an event listener object.
	 * @exception IntrospectionException
	 *                if an exception occurs during introspection.
	 */
	public EventSetDescriptor(String eventSetName, Class<?> listenerType,
			Method listenerMethods[], Method addListenerMethod,
			Method removeListenerMethod) throws IntrospectionException {
		this(eventSetName, listenerType, listenerMethods, addListenerMethod,
				removeListenerMethod, null);
	}

	/**
	 * This constructor creates an EventSetDescriptor from scratch using
	 * java.lang.reflect.Method and java.lang.Class objects.
	 * 
	 * @param eventSetName
	 *            The programmatic name of the event set.
	 * @param listenerType
	 *            The Class for the listener interface.
	 * @param listenerMethods
	 *            An array of Method objects describing each of the event
	 *            handling methods in the target listener.
	 * @param addListenerMethod
	 *            The method on the event source that can be used to register an
	 *            event listener object.
	 * @param removeListenerMethod
	 *            The method on the event source that can be used to de-register
	 *            an event listener object.
	 * @param getListenerMethod
	 *            The method on the event source that can be used to access the
	 *            array of event listener objects.
	 * @exception IntrospectionException
	 *                if an exception occurs during introspection.
	 * @since 1.4
	 */
	public EventSetDescriptor(String eventSetName, Class<?> listenerType,
			Method listenerMethods[], Method addListenerMethod,
			Method removeListenerMethod, Method getListenerMethod)
			throws IntrospectionException {
		setName(eventSetName);
		setListenerMethods(listenerMethods);
		setAddListenerMethod(addListenerMethod);
		setRemoveListenerMethod(removeListenerMethod);
		setGetListenerMethod(getListenerMethod);
		setListenerType(listenerType);
	}

	/**
	 * Creates an <TT>EventSetDescriptor</TT> from scratch using
	 * <TT>java.lang.reflect.MethodDescriptor</TT> and <TT>java.lang.Class</TT>
	 * objects.
	 * 
	 * @param eventSetName
	 *            The programmatic name of the event set.
	 * @param listenerType
	 *            The Class for the listener interface.
	 * @param listenerMethodDescriptors
	 *            An array of MethodDescriptor objects describing each of the
	 *            event handling methods in the target listener.
	 * @param addListenerMethod
	 *            The method on the event source that can be used to register an
	 *            event listener object.
	 * @param removeListenerMethod
	 *            The method on the event source that can be used to de-register
	 *            an event listener object.
	 * @exception IntrospectionException
	 *                if an exception occurs during introspection.
	 */
	public EventSetDescriptor(String eventSetName, Class<?> listenerType,
			MethodDescriptor listenerMethodDescriptors[],
			Method addListenerMethod, Method removeListenerMethod)
			throws IntrospectionException {
		setName(eventSetName);
		this.listenerMethodDescriptors = listenerMethodDescriptors;
		setAddListenerMethod(addListenerMethod);
		setRemoveListenerMethod(removeListenerMethod);
		setListenerType(listenerType);
	}

	/**
	 * Gets the method used to add event listeners.
	 * 
	 * @return The method used to register a listener at the event source.
	 */
	public synchronized Method getAddListenerMethod() {
		return (addMethodDescriptor != null ? addMethodDescriptor.getMethod()
				: null);
	}

	/**
	 * Gets the method used to access the registered event listeners.
	 * 
	 * @return The method used to access the array of listeners at the event
	 *         source or null if it doesn't exist.
	 * @since 1.4
	 */
	public synchronized Method getGetListenerMethod() {
		return (getMethodDescriptor != null ? getMethodDescriptor.getMethod()
				: null);
	}

	/**
	 * Gets the <code>MethodDescriptor</code>s of the target listener interface.
	 * 
	 * @return An array of <code>MethodDescriptor</code> objects for the target
	 *         methods within the target listener interface that will get called
	 *         when events are fired.
	 */
	public synchronized MethodDescriptor[] getListenerMethodDescriptors() {
		return listenerMethodDescriptors;
	}

	/**
	 * Gets the methods of the target listener interface.
	 * 
	 * @return An array of <TT>Method</TT> objects for the target methods within
	 *         the target listener interface that will get called when events
	 *         are fired.
	 */
	public synchronized Method[] getListenerMethods() {
		Method[] methods = getListenerMethods0();
		if (methods == null) {
			if (listenerMethodDescriptors != null) {
				methods = new Method[listenerMethodDescriptors.length];
				for (int i = 0; i < methods.length; i++) {
					methods[i] = listenerMethodDescriptors[i].getMethod();
				}
			}
			setListenerMethods(methods);
		}
		return methods;
	}

	private Method[] getListenerMethods0() {
		return (Method[]) getObject(listenerMethodsRef);
	}

	/**
	 * Gets the <TT>Class</TT> object for the target interface.
	 * 
	 * @return The Class object for the target interface that will get invoked
	 *         when the event is fired.
	 */
	public Class<?> getListenerType() {
		return (Class) getObject(listenerTypeRef);
	}

	/**
	 * Gets the method used to remove event listeners.
	 * 
	 * @return The method used to remove a listener at the event source.
	 */
	public synchronized Method getRemoveListenerMethod() {
		return (removeMethodDescriptor != null ? removeMethodDescriptor
				.getMethod() : null);
	}

	/**
	 * Reports if an event set is in the &quot;default&quot; set.
	 * 
	 * @return <TT>true</TT> if the event set is in the &quot;default&quot; set.
	 *         Defaults to <TT>true</TT>.
	 */
	public boolean isInDefaultEventSet() {
		return inDefaultEventSet;
	}

	/**
	 * Normally event sources are multicast. However there are some exceptions
	 * that are strictly unicast.
	 * 
	 * @return <TT>true</TT> if the event set is unicast. Defaults to
	 *         <TT>false</TT>.
	 */
	public boolean isUnicast() {
		return unicast;
	}

	private synchronized void setAddListenerMethod(Method method) {
		if (method == null) {
			return;
		}
		if (getClass0() == null) {
			setClass0(method.getDeclaringClass());
		}
		addMethodDescriptor = new MethodDescriptor(method);
	}

	private synchronized void setGetListenerMethod(Method method) {
		if (method == null) {
			return;
		}
		if (getClass0() == null) {
			setClass0(method.getDeclaringClass());
		}
		getMethodDescriptor = new MethodDescriptor(method);
	}

	/**
	 * Marks an event set as being in the &quot;default&quot; set (or not). By
	 * default this is <TT>true</TT>.
	 * 
	 * @param inDefaultEventSet
	 *            <code>true</code> if the event set is in the
	 *            &quot;default&quot; set, <code>false</code> if not
	 */
	public void setInDefaultEventSet(boolean inDefaultEventSet) {
		this.inDefaultEventSet = inDefaultEventSet;
	}

	private void setListenerMethods(Method[] methods) {
		if (methods == null) {
			return;
		}
		if (listenerMethodDescriptors == null) {
			listenerMethodDescriptors = new MethodDescriptor[methods.length];
			for (int i = 0; i < methods.length; i++) {
				listenerMethodDescriptors[i] = new MethodDescriptor(methods[i]);
			}
		}
		listenerMethodsRef = createReference(methods, true);
	}

	private void setListenerType(Class cls) {
		listenerTypeRef = createReference(cls);
	}

	private synchronized void setRemoveListenerMethod(Method method) {
		if (method == null) {
			return;
		}
		if (getClass0() == null) {
			setClass0(method.getDeclaringClass());
		}
		removeMethodDescriptor = new MethodDescriptor(method);
	}

	/**
	 * Mark an event set as unicast (or not).
	 * 
	 * @param unicast
	 *            True if the event set is unicast.
	 */
	public void setUnicast(boolean unicast) {
		this.unicast = unicast;
	}
}
