package ytu.dmase.project.crawler;

import java.util.regex.Pattern;


import com.google.inject.Inject;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import ytu.dmase.project.service.IPageHandler;

public class TeknosaCrawler extends ProductCrawler {

	// Filters for url.
	private final static Pattern _filters = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))(\\?.*)?$");
	
	private String _domain = "http://www.teknosa.com";
	
	// TODO: Ürün resimlerini de al.
	
	@Inject
	public TeknosaCrawler(IPageHandler pageHandler) {
		super(pageHandler);
	}

	@Override
	public void visit(Page page) {

		// Base class 'ProductCrawler' page objesini gereken handler'a iletiyor.
		super.visit(page);
	}
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		
		// Url teknosa.com domaini altında mı?
		boolean isDomainTeknosa = url.getURL().startsWith(_domain);
		
		// jpg, mp3, css tarzı sayfalara girme
		String href = url.getURL().toLowerCase();
		boolean isNormalPage = !_filters.matcher(href).matches();
		
		// Ürün araması sonucu gelen ürünlerden kategori bilgisi çıkaramıyoruz.
		boolean isSearchLink = url.getURL().startsWith(_domain + "/arama");
		
		// Url ürünün kendi sayfasına mı gidiyor?
		boolean isProductUrl = url.getURL().contains("urunler");
		
		// Url ürünün kendi sayfasına gitmesin. Çünkü ürün bilgileri
		// ürünleri toplu listeleyen sayfadan çoklu okunuyor.
		// Ürünün kendi sayfasına gitmek için bir sebep yok.
		boolean visit = isDomainTeknosa && !isProductUrl && isNormalPage && !isSearchLink;
		if(visit)
		{
			//_logger.info(String.format("Will visit: %s", url.toString()));
		}
		
		return visit;
	}

	// Her bir ProductCrawler objesi domaine bilgisine sahip olmalı.
	// Crawlerlar'a seed verilirken bu bilgi kullanılır.
	// Örnek domain 'www.teknosa.com' 'http://teknosa.com/'
	@Override
	public String get_crawlerDomain() {
		return _domain;
	}
}
