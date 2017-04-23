package ytu.dmase.project.crawler;

import java.util.regex.Pattern;

import com.google.inject.Inject;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import ytu.dmase.project.service.IPageHandler;


public class HepsiburadaCrawler extends ProductCrawler{
	
	// Filters for url.
	private final static Pattern _filters = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))(\\?.*)?$");
	
	private String _domain = "http://www.hepsiburada.com";
	
	@Inject
	public HepsiburadaCrawler(IPageHandler pageHandler) {
		super(pageHandler);
	}
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		
		// Url hepsiburada.com domaini altında mı?
		boolean isDomainHepsiburada = url.getURL().startsWith(_domain);
		boolean isCtg = url.getURL().matches(".*?\\.com\\/.[^\\/]*?-c-\\d+");
		
		//boolean isCtg = url.getURL().matches("[^\\/].*?-c-\\d+");
		if(isDomainHepsiburada && isCtg)
			System.out.println(url.getURL());
		
		return !_filters.matcher(url.getURL()).matches() && isDomainHepsiburada && isCtg;
	}

	@Override
	public String get_crawlerDomain() {
		
		return _domain;
	}
}
