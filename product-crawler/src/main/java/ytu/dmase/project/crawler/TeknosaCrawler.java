package ytu.dmase.project.crawler;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.google.inject.Inject;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.service.IProductService;

public class TeknosaCrawler extends ProductCrawler {

	private String _domain = "http://www.teknosa.com";
	
	// Logger classı program geneli ayara göre console, dosyaya veya başka bir yere bilgi yazar.
	private final static Logger _logger = LoggerFactory.getLogger(TeknosaCrawler.class);
	
	// Used for marking logs as parsing for further filterin if needed.
	private final static Marker _parsingMarker = MarkerFactory.getMarker("PARSING");
	
	// Filters for url.
	private final static Pattern _filters = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");
	
	// Used for getting the category fragment from the page url.
	private final static Pattern _categoryPattern = Pattern.compile("((kategori)|(katalog))/(?<ctg>(\\w|-)*)");
	
	
	// TODO: Ürünleri kategorilerine göre ayır.
	// TODO: Ürün resimlerini de al.
	
	@Inject
	public TeknosaCrawler(IProductService service) {
		super(service);
	}

	@Override
	public void visit(Page page) {

		Collection<Product> products = null;
		
		try {
			products = parseProducts(page);
		} catch (UnsupportedEncodingException e) {
			_logger.error(e.getMessage());
		}
		
		// Eldeki ürünleri sisteme veriyoruz.
		if(products.size() > 0)
		{
			handOver(products.toArray(new Product[products.size()]));			
		}
		else
		{
			_logger.info(_parsingMarker, String.format("No products in this page: %s", page.getWebURL().toString()));
		}
		
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
	
	public Collection<Product> parseProducts(Page page) throws UnsupportedEncodingException
	{
		Collection<Product> products = new ArrayList<Product>();
		String charsetName = page.getContentCharset();
		
		if(charsetName == null)
		{
			// Sayfa charset değeri css dosyalarında null olur. 
			return products;
		}
		
		String html = new String(page.getContentData(), charsetName);
		
		// Jsoup kullanarak sayfada içerik araması yapıyoruz.
		// Regex ile de olabilir. Jsoup css selector kullanıyor.
		// Jsoup daha kolay. Şu adresten nasıl kullanılıyor bakabilirsin:
		// https://try.jsoup.org/
		Document doc = Jsoup.parse(html);
		
		// Sayfada ürün bilgisi içeren html elementlerini getir.
		// Ürün bilgisi içeren elementler 'product-list' ve 'grid' 
		// classlarına sahip tek elementin içindeki 'li' elementleridir.
		Elements productListElements = doc.select(".product-list.grid li");
		
		
		// Her bir html elementi için product okuma işlemi yap.
		productListElements.forEach((e) -> {
			Product product = parseProduct(e);
			if(product != null)
				products.add(product);
			else
				_logger.error("Couldn't parse an element.");
		});
		
		// Sayfada ürün bulunmuşsa o sayfadan kategori bilgisi çıkarıp
		// bulunan ürünlerin kategori bilgisini set et.
		if(products.size() > 0)
		{
			Category ctg = categoryFromUrl(page.getWebURL().getURL());
			products.forEach(p -> p.set_category(ctg));
		}
		
		return products;
	}
	
	protected Product parseProduct(Element element)
	{
		// 'product-name' class'ına sahip child elementlerin içindeki texti getir. Bu durumda ürün ismi.
		String productName 	= element.select(".product-name").text();
		
		// 'a' elementinin 'href' attribute'ını oku.
		// 'a' html elementi sitelerde tıklanabilir link vermek için kullanılır.
		// Tıklanınca gidilecek sayfanın adresi 'href' attribute içersindedir.
		String urlString 	= element.select("a").attr("href");
		
		// 'sales' classına sahip ama 'old-sales' classına sahip olmayan elementin yazısını oku.
		// teknosa.com'da indirimden önceki fiyatı göstermek için eski fiyatı tutan
		// html elementine 'sales' classı yanında 'old-sales' classı
		String priceString 	= element.select(".sales:not(.old-sales)").text();
		if(!priceString.equals(""))
		{
			priceString	= priceString.substring(0, priceString.length()-3); // Remove TL from like "489,00 TL"
			priceString = priceString.replace(".", ""); // Remove thousand separator			
		}
		else
		{
			// Stokta olmayan ürün. Fiyat olarak -1 verelim.
			priceString = "-1";
		}
				
		// Ürünün resmi 'img' elementiyle gösteriliyor.
		// Bu elementin 'src' attribute'ü bize resmin adresini verir. 
		//String imgUrlString = element.select("img").attr("src");
		
		Product product = null;
		try {
			
			// Okunan bilgilerle yeni product objesi oluştur.
			product = new Product(productName, new URL(_domain + urlString));
			
			// Okunan fiyat bilgisini ondalıklı sayı ayracı olarak virgül olacak şekilde parse et.
			// Locale.Turkey olmadığı için Locale.France kullandım. Onların da bizimle aynı.
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number number = format.parse(priceString);
			Double price = number.doubleValue();
			
			product.set_price(price);
		} catch (MalformedURLException | ParseException e) {
			_logger.error(e.getMessage());
			return null;
		}
		
		return product;
	}
	
	protected Category categoryFromUrl(String url)
	{
		Matcher matcher = _categoryPattern.matcher(url);
		if(!matcher.find())
		{
			_logger.warn(_parsingMarker, String.format("Category (%s) is different from expected. returning as 'other'.", url));
			return Category.other;
		}
		String ctg = matcher.group("ctg");
		
		if(ctg == null)
			throw new IllegalArgumentException("Couldn't parse category info from url");
		
		switch(ctg)
		{
		case "ses-ve-goruntu":
		case "telekom":
		case "bilgisayar":
		case "fotograf-video-kamera":
		case "beyaz-esya":
			return Category.electronics;
		case "oyun-hobi":
		case "disney":
			return Category.entertainment;
		case "kisisel-bakim":
		case "spor":
			return Category.health;
		}
		
		return null;
	}
	
}
