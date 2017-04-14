package ytu.dmase.project.crawler;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import ytu.dmase.project.model.webpage.WebPage;
import ytu.dmase.project.service.IPageHandler;

public abstract class ProductCrawler extends WebCrawler {
	
	private final static Logger _logger = LoggerFactory.getLogger(ProductCrawler.class);
	
	private IPageHandler _pageHandler;
	
	protected ProductCrawler(IPageHandler pageHandler)
	{
		_pageHandler = pageHandler;
	}
	
	@Override
	public void visit(Page page) {
		
		super.visit(page);
		
		URL url;
		try {
			url = new URL(page.getWebURL().getURL());
		} catch (MalformedURLException e) {
			_logger.error("Error when creating URL object from page weburl string.", e);
			return;
		}
		
		String html;
		try {
			html = new String(page.getContentData(), page.getContentCharset());
		} catch (UnsupportedEncodingException e) {
			_logger.error("Error getting html content as String from page object.", e);
			return;
		}
		
		WebPage webPage = new WebPage(html, url);
		get_pageHandler().handle(webPage);
	}
		
	public abstract String get_crawlerDomain();
	
	public IPageHandler get_pageHandler()
	{
		return _pageHandler;
	}
}
