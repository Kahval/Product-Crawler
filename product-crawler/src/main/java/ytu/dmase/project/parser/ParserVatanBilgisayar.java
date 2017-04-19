package ytu.dmase.project.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.model.webpage.WebPage;
import ytu.dmase.project.utils.URLUtils;

public class ParserVatanBilgisayar implements IProductParser {

	private URL _domainUrl = URLUtils.makeUrl("http://www.vatanbilgisayar.com");
	
	@Override
	public ArrayList<Product> parse(WebPage webPage) throws ProductParseException {

		ArrayList<Product> products = new ArrayList<Product>();
		
		Document doc = Jsoup.parse(webPage.get_html());
		
		// emosInfinite ems-inline
		Elements productListElements = doc.select("ul.emosInfinite.ems-inline li.ems-prd.filter,li.ems-prc.selected");
		for (Element elem : productListElements) {

			String productName 	= elem.select(".ems-prd-name").text();
			
			String urlString = webPage.get_url().toString() + elem.select(".ems-prd-name a").attr("href");
			URL url;
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				
				throw new ProductParseException("Error when parsing product url.", e);
			}
			
			// 'sales' classına sahip ama 'old-sales' classına sahip olmayan elementin yazısını oku.
			// teknosa.com'da indirimden önceki fiyatı göstermek için eski fiyatı tutan
			// html elementine 'sales' classı yanında 'old-sales' classı
			String priceString 	= elem.select(".urunListe_satisFiyat").text();
					
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
		
		return products;
	}

	@Override
	public URL get_domain() {
		
		return _domainUrl;
	}
	
}
