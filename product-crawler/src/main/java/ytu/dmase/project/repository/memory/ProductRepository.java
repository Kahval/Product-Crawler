package ytu.dmase.project.repository.memory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;

public class ProductRepository implements IProductRepository {

	private HashMap<UUID, Product> _products = new HashMap<UUID, Product>();
	
	public Product GetById(UUID uuid) {
		return _products.get(uuid);
	}

	public Product GetById(String uuidString) {
		UUID uuid = UUID.fromString(uuidString);
		return GetById(uuid);
	}

	public Iterable<Product> FindByCategory(Category category) {

		ArrayList<Product> products = new ArrayList<Product>();
		
		for(UUID uuid : _products.keySet())
		{
			Product product = GetById(uuid);
			if(product.get_category().equals(category))
			{
				products.add(product);
			}
		}
		
		return products;
	}

	public Iterable<Product> FindByKeywords(String... keywords) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<Product> FindByName(String productName) {

		ArrayList<Product> products = new ArrayList<Product>();
		
		for(UUID uuid : _products.keySet())
		{
			Product product = GetById(uuid);
			if(product.get_name().equals(productName))
			{
				products.add(product);
			}
		}
		
		return products;
	}

	public void Save(Product product) {
		
		UUID uuid = product.get_uuid();
		if(_products.containsKey(uuid))
		{
			_products.put(uuid, product);
		}
	}

	public void Update(Product product) {
		// TODO Auto-generated method stub
		// no need.
	}

	public void Delete(Product product) {

		UUID uuid = product.get_uuid();
		if(_products.containsKey(uuid))
		{
			_products.remove(uuid);
		}
	}

	public Product GetByUrl(URL url) {
		
		for(UUID uuid : _products.keySet())
		{
			Product product = GetById(uuid);
			if(product.get_url().equals(url))
			{
				return product;
			}
		}
		
		return null;
	}

	public Product GetByUrl(String urlString) {
		
		try {
			return GetByUrl(new URL(urlString));
			
		} catch (MalformedURLException e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
		
		return null;
	}

}
