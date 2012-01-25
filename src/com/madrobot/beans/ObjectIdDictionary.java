package com.madrobot.beans;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Store IDs against given object references.
 * <p>
 * Behaves similar to java.util.IdentityHashMap, but in JDK1.3 as well.
 * Additionally the implementation keeps track of orphaned IDs by using a
 * WeakReference to store the reference object.
 * </p>
 */
public class ObjectIdDictionary {

	private final Map map = new HashMap();
	private final ReferenceQueue queue = new ReferenceQueue();

	private static interface Wrapper {
		@Override
		int hashCode();

		@Override
		boolean equals(Object obj);

		@Override
		String toString();

		Object get();
	}

	private static class IdWrapper implements Wrapper {

		private final Object obj;
		private final int hashCode;

		public IdWrapper(Object obj) {
			hashCode = System.identityHashCode(obj);
			this.obj = obj;
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			return obj == ((Wrapper) other).get();
		}

		@Override
		public String toString() {
			return obj.toString();
		}

		@Override
		public Object get() {
			return obj;
		}
	}

	private class WeakIdWrapper extends WeakReference implements Wrapper {

		private final int hashCode;

		public WeakIdWrapper(Object obj) {
			super(obj, queue);
			hashCode = System.identityHashCode(obj);
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public boolean equals(Object other) {
			return get() == ((Wrapper) other).get();
		}

		@Override
		public String toString() {
			Object obj = get();
			return obj == null ? "(null)" : obj.toString();
		}
	}

	public void associateId(Object obj, Object id) {
		map.put(new WeakIdWrapper(obj), id);
		cleanup();
	}

	public Object lookupId(Object obj) {
		Object id = map.get(new IdWrapper(obj));
		return id;
	}

	public boolean containsId(Object item) {
		boolean b = map.containsKey(new IdWrapper(item));
		return b;
	}

	public void removeId(Object item) {
		map.remove(new IdWrapper(item));
		cleanup();
	}

	public int size() {
		cleanup();
		return map.size();
	}

	private void cleanup() {
		WeakIdWrapper wrapper;
		while ((wrapper = (WeakIdWrapper) queue.poll()) != null) {
			map.remove(wrapper);
		}
	}
}
