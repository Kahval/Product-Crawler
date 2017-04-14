package ytu.dmase.project.model.webpage;

import java.net.URL;
import java.util.Date;

public class WebPage {
	
	private String _html;
	private URL _url;
	private Date _crawlDate;
	
	public WebPage(String html, URL url)
	{
		_html = html;
		_url = url;
		_crawlDate = new Date();
	}
	
	public WebPage(String html, URL url, Date crawlDate)
	{
		this(html, url);
		_crawlDate = crawlDate;
	}

	public String get_html() {
		return _html;
	}

	public URL get_url() {
		return _url;
	}

	public Date get_crawlDate() {
		return _crawlDate;
	}
}
