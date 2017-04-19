package ytu.dmase.project.tests.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import ytu.dmase.project.model.webpage.WebPage;


public abstract class WebPageUtils {

	public static WebPage createWebPageFromFile(String path, URL url) throws IOException
	{
		InputStream is = WebPageUtils.class.getResourceAsStream(path);
		String html = IOUtils.toString(is, Charset.forName("utf-8"));
		IOUtils.closeQuietly(is);
		
		WebPage page = new WebPage(html, url);
		return page;
	}
	
}
