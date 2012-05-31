
package com.madrobot.net.util.cache;

/**
 * This enumeration represents the various ways a response can be generated
 * by the {@link com.madrobot.net.util.cache.CachingHttpClient};
 * if a request is executed with an {@link org.apache.http.protocol.HttpContext}
 * then a parameter with one of these values will be registered in the
 * context under the key
 * {@link com.madrobot.net.util.cache.CachingHttpClient#CACHE_RESPONSE_STATUS}.
 */
public enum CacheResponseStatus {

    /** The response was generated directly by the caching module. */
    CACHE_MODULE_RESPONSE,

    /** A response was generated from the cache with no requests sent
     * upstream.
     */
    CACHE_HIT,

    /** The response came from an upstream server. */
    CACHE_MISS,

    /** The response was generated from the cache after validating the
     * entry with the origin server.
     */
    VALIDATED;

}
