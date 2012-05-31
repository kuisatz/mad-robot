package com.madrobot.net;

import com.madrobot.net.util.cache.annotation.Immutable;

/**
 * Records static constants for various HTTP header names.
 * 
 * @since 4.1
 */
@Immutable
public class HeaderConstants {

	/**
	 * Represents the HTTP GET, POST, PUT and DELETE
	 */
	public enum HttpMethod {
		/**
		 * Http DELETE method
		 */
		DELETE,
		/**
		 * Http GET method
		 */
		GET,
		/**
		 * Http POST method
		 */
		POST,
		/**
		 * Http PUT method
		 */
		PUT,
		/** Http head */
		HEAD,
		/** Http Options */
		OPTIONS,
		/** Http Trace */
		TRACE;
	}

	// public static final String GET_METHOD = "GET";
	// public static final String HEAD_METHOD = "HEAD";
	// public static final String OPTIONS_METHOD = "OPTIONS";
	// public static final String PUT_METHOD = "PUT";
	// public static final String DELETE_METHOD = "DELETE";
	// public static final String TRACE_METHOD = "TRACE";

	public static final String LAST_MODIFIED = "Last-Modified";
	public static final String IF_MATCH = "If-Match";
	public static final String IF_RANGE = "If-Range";
	public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String IF_NONE_MATCH = "If-None-Match";

	public static final String PRAGMA = "Pragma";
	public static final String MAX_FORWARDS = "Max-Forwards";
	public static final String ETAG = "ETag";
	public static final String EXPIRES = "Expires";
	public static final String AGE = "Age";
	public static final String VARY = "Vary";
	public static final String ALLOW = "Allow";
	public static final String VIA = "Via";
	public static final String PUBLIC = "public";
	public static final String PRIVATE = "private";

	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String CACHE_CONTROL_NO_STORE = "no-store";
	public static final String CACHE_CONTROL_NO_CACHE = "no-cache";
	public static final String CACHE_CONTROL_MAX_AGE = "max-age";
	public static final String CACHE_CONTROL_MAX_STALE = "max-stale";
	public static final String CACHE_CONTROL_MIN_FRESH = "min-fresh";
	public static final String CACHE_CONTROL_MUST_REVALIDATE = "must-revalidate";
	public static final String CACHE_CONTROL_PROXY_REVALIDATE = "proxy-revalidate";
	public static final String STALE_IF_ERROR = "stale-if-error";
	public static final String STALE_WHILE_REVALIDATE = "stale-while-revalidate";
	
	public static final String HTTP_EXPECT_CONTINUE_PARAM = "http.protocol.expect-continue";
	public static final String HTTP_SINGLE_COOKIE_PARAM = "http.protocol.single-cookie-header";
	/**
	 * HTTP session socket time-out key
	 */
	public static final String HTTP_SOCKET_TIME_OUT_PARAM = "http.socket.timeout";
	public static final String HTTP_PROTOCOL_VERSION ="http.protocol.version";
	

	public static final String WARNING = "Warning";
	public static final String RANGE = "Range";
	public static final String CONTENT_RANGE = "Content-Range";
	public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
	public static final String AUTHORIZATION = "Authorization";

}
