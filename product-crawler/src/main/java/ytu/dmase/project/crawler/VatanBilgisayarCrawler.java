package ytu.dmase.project.crawler;

import java.util.regex.Pattern;

import com.google.inject.Inject;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import ytu.dmase.project.service.IPageHandler;

public class VatanBilgisayarCrawler extends ProductCrawler {

	private final static Pattern _filters = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))(\\?.*)?$");
	
	private String _domain = "http://www.vatanbilgisayar.com";
	
	@Inject
	public VatanBilgisayarCrawler(IPageHandler pageHandler) {
		super(pageHandler);
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		
		String href = url.getURL().toLowerCase();
		boolean isDomainVatan = href.startsWith(_domain);
		
		// Kullanıcıya hitap eden bir sayfa mı?
		boolean isNormalPage = !_filters.matcher(href).matches();
		
		// Sitedeki arama linklerine girme
		boolean isSearchLink = href.startsWith(_domain + "/arama");
		
		// Url ürünün kendi sayfasına mı gidiyor?
		// ürün sayfalarının url'leri '.html' ile bitiyor. Bu adresleri filtereleyelim.
		boolean isProductUrl = href.contains("html");
		
		boolean visit = isDomainVatan && !isProductUrl && isNormalPage && !isSearchLink;
		if(visit)
		{
			//_logger.info(String.format("Will visit: %s", url.toString()));
		}
		
		return visit;
	}
	
	@Override
	public String get_crawlerDomain() {
		
		return _domain;
	}
	
	
	
}
