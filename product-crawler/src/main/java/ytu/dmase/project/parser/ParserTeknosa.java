package ytu.dmase.project.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.model.webpage.WebPage;
import ytu.dmase.project.utils.URLUtils;

public class ParserTeknosa implements IProductParser {
	
	private URL _domainUrl = URLUtils.makeUrl("http://www.teknosa.com");
	
	// Used for getting the category fragment from the page url.
	private final static Pattern _categoryPattern = Pattern.compile("((kategori)|(katalog))/(?<ctg>(\\w|-)*)");
		
	
	@Override
	public Collection<Product> parse(WebPage webPage) throws ProductParseException
	{
		Collection<Product> products = new ArrayList<Product>();
		
		// Jsoup kullanarak sayfada içerik araması yapıyoruz.
		// Regex ile de olabilir. Jsoup css selector kullanıyor.
		// Jsoup daha kolay. Şu adresten nasıl kullanılıyor bakabilirsin:
		// https://try.jsoup.org/
		Document doc = Jsoup.parse(webPage.get_html());
		
		// Sayfada ürün bilgisi içeren html elementlerini getir.
		// Ürün bilgisi içeren elementler 'product-list' ve 'grid' 
		// classlarına sahip tek elementin içindeki 'li' elementleridir.
		Elements productListElements = doc.select(".product-list.grid li");
		
		// Her bir html elementi için product bilgisi oku.
		for (Element elem : productListElements) {
			// 'product-name' class'ına sahip child elementlerin içindeki texti getir. Bu durumda ürün ismi.
			String productName 	= elem.select(".product-name").text();
			
			// 'a' elementinin 'href' attribute'ını oku.
			// 'a' html elementi sitelerde tıklanabilir link vermek için kullanılır.
			// Tıklanınca gidilecek sayfanın adresi 'href' attribute içersindedir.
			String urlString = webPage.get_url().toString() + elem.select("a").attr("href");
			URL url;
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				
				throw new ProductParseException("Error when parsing product url.", e);
			}
			
			
			
			// 'sales' classına sahip ama 'old-sales' classına sahip olmayan elementin yazısını oku.
			// teknosa.com'da indirimden önceki fiyatı göstermek için eski fiyatı tutan
			// html elementine 'sales' classı yanında 'old-sales' classı
			String priceString 	= elem.select(".sales:not(.old-sales)").text();
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
				product = new Product(productName, url);
				
				// Okunan fiyat bilgisini ondalıklı sayı ayracı olarak virgül olacak şekilde parse et.
				// Locale.Turkey olmadığı için Locale.France kullandım. Onların da bizimle aynı.
				NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
				Number number = format.parse(priceString);
				Double price = number.doubleValue();
				
				product.set_price(price);
			} catch (ParseException e) {
				throw new ProductParseException("Error when parsing product price.", e);
			}
			
			products.add(product);
		}
		
		// Sayfada ürün bulunmuşsa o sayfadan kategori bilgisi çıkarıp
		// bulunan ürünlerin kategori bilgisini set et.
		if(products.size() > 0)
		{
			Category ctg = categoryFromUrl(webPage.get_url());
			products.forEach(p -> p.set_category(ctg));
		}
		
		return products;
	}
	
	// Ürün kategori bilgisini url adresine göre tahmin eder.
	protected Category categoryFromUrl(URL url)
	{
		String urlStr = url.toString();
		Matcher matcher = _categoryPattern.matcher(urlStr);
		if(!matcher.find())
		{
			return Category.unassigned;
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

	@Override
	public URL get_domain() {

		return _domainUrl;
	}
}
