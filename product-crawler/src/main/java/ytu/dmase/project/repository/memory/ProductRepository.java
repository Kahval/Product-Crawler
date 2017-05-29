package ytu.dmase.project.repository.memory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;

public class ProductRepository implements IProductRepository {

	private HashMap<UUID, Product> _products = new HashMap<UUID, Product>();
	
	public Product getById(UUID uuid) {
		return _products.get(uuid);
	}

	public Product getById(String uuidString) {
		UUID uuid = UUID.fromString(uuidString);
		return getById(uuid);
	}

	public List<Product> findByCategory(Category category) {

		ArrayList<Product> products = new ArrayList<Product>();
		
		for(UUID uuid : _products.keySet())
		{
			Product product = getById(uuid);
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

	public List<Product> findByName(String productName) {

		ArrayList<Product> products = new ArrayList<Product>();
		
		for(UUID uuid : _products.keySet())
		{
			Product product = getById(uuid);
			if(product.get_name().equals(productName))
			{
				products.add(product);
			}
		}
		
		return products;
	}

	public void save(Product product) {
		
		UUID uuid = product.get_uuid();
		if(_products.containsKey(uuid))
		{
			_products.put(uuid, product);
		}
	}

	public void update(Product product) {
		// TODO Auto-generated method stub
		// no need.
	}

	public void delete(Product product) {

		UUID uuid = product.get_uuid();
		if(_products.containsKey(uuid))
		{
			_products.remove(uuid);
		}
	}

	public Product getByUrl(URL url) {
		
		for(UUID uuid : _products.keySet())
		{
			Product product = getById(uuid);
			if(product.get_url().equals(url))
			{
				return product;
			}
		}
		
		return null;
	}

	public Product getByUrl(String urlString) {
		
		try {
			return getByUrl(new URL(urlString));
			
		} catch (MalformedURLException e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
		
		return null;
	}

	@Override
	public List<Product> findByKeywords(String... keywords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		return _products.size();
	}

}
