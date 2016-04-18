package org.xululabs.flickwiz.service.coreclassess;

import java.net.MalformedURLException;
import java.net.URL;

public class URLFactory {
	public static URL create(String urlString) {
	    try {
	      return new URL(urlString);
	    } catch (MalformedURLException e) {
	      throw new RuntimeException(e);
	    }
	  }
}
