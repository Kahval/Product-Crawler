package ytu.dmase.project.utils;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class URLUtils {

	public static URL makeUrl(String urlString)
	{
		URL url;
		try {
			url = new URL(urlString);
		} catch(MalformedURLException e)
		{
			throw new IllegalArgumentException(e);
		}
		
		return url;
	}
	
}
