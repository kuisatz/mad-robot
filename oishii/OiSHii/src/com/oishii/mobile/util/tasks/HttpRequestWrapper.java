package com.oishii.mobile.util.tasks;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.oishii.mobile.util.HttpSettings;

public class HttpRequestWrapper<T> {
	public URI requestURI;
	/*integer to identify the operation. an activity may user*/
	public int operationID;
	public IHttpCallback callback;
	public HttpSettings httpSettings=new HttpSettings();
	public List<NameValuePair> httpParams=new ArrayList<NameValuePair>();
}