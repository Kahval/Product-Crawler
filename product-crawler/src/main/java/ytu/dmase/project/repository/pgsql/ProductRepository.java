package ytu.dmase.project.repository.pgsql;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import com.google.inject.Inject;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;

public class ProductRepository implements IProductRepository {
	
	private Connection _conn;
	
	private PreparedStatement _getById;
	private PreparedStatement _findByCategory;
	private PreparedStatement _findByName;
	private PreparedStatement _save;
	private PreparedStatement _update;
	private PreparedStatement _delete;
	private PreparedStatement _getByUrl;
	
	@Inject
	public ProductRepository(Connection connection)
	{
		_conn = connection;
		
		try {
			
			_getById 		= _conn.prepareStatement("SELECT * FROM product WHERE uuid=?");
			_findByCategory = _conn.prepareStatement("SELECT * FROM product WHERE category=?");
			_findByName		= _conn.prepareStatement("SELECT * FROM product WHERE name=?");
			_getByUrl 		= _conn.prepareStatement("SELECT * FROM product WHERE url=?");
			_save 			= _conn.prepareStatement("INSERT INTO product (uuid, name, brand, model, description, price, category, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			_update 		= _conn.prepareStatement("UPDATE product SET name=?, brand=?, model=?, description=?, price=?, category=?, url=? WHERE uuid=?");
			_delete 		= _conn.prepareStatement("DELETE FROM product WHERE uuid=?");
			
		} catch (SQLException e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
	}

	public Product GetById(UUID uuid) {
		
		Product product;

		try {
			_getById.setObject(0, uuid);
			ResultSet results = _getById.executeQuery();
			
			if(results.next())
			{
				product = MapResultToProduct(results);		
				return product;		
			}
		} catch (SQLException e) {
			
			String error = e.getSQLState();
			
			// Table exists ?
			if(error.equals("42P01"))
			{
				createTable();
			}
				
		} catch (MalformedURLException e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
		
		return null;
	}

	public Product GetById(String uuid) {
		return GetById(UUID.fromString(uuid));
	}

	public Iterable<Product> FindByCategory(Category category) {
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		try {
			_findByCategory.setString(0, category.toString());
			ResultSet results = _findByCategory.executeQuery();
			
			while(results.next())
			{
				Product product = MapResultToProduct(results);
				products.add(product);
			}
			
			return products;
			
		} catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
		
		return null;
	}

	public Iterable<Product> FindByKeywords(String... keywords) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<Product> FindByName(String productName) {
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		try {
			_findByName.setString(0, productName);
			ResultSet results = _findByName.executeQuery();
			
			while(results.next())
			{
				Product product = MapResultToProduct(results);
				products.add(product);
			}
			
			return products;
			
		} catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
		
		return products;
	}

	public void Save(Product product) {
		
		
		try {
			PreparedStatement statement = _save;
			statement.setObject(1, product.get_uuid());
			statement.setString(2, product.get_name());
			statement.setString(3, product.get_brand());
			statement.setString(4, product.get_model());
			statement.setString(5, product.get_description());
			statement.setDouble(6, product.get_price());
			statement.setString(7, product.get_category().toString());
			statement.setString(8, product.get_url().toString());

			statement.execute();
			
		} catch (SQLException e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
	}

	public void Update(Product product) {
		
		try {
			PreparedStatement statement = _update;
			statement.setString(1, product.get_name());
			statement.setString(2, product.get_brand());
			statement.setString(3, product.get_model());
			statement.setString(4, product.get_description());
			statement.setDouble(5, product.get_price());
			statement.setString(6, product.get_category().toString());
			statement.setString(7, product.get_url().toString());
			statement.setObject(8, product.get_uuid());

			statement.execute();
			
		} catch (SQLException e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
	}

	public void Delete(Product product) {
		
		try {
			_delete.setObject(1, product.get_uuid());
			_delete.execute();
			
		} catch (SQLException e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
	}
	
	public Product GetByUrl(URL url) {

		return GetByUrl(url.toString());
	}

	public Product GetByUrl(String urlString) {
		
		try {
			_getByUrl.setString(1, urlString);
			ResultSet results = _getByUrl.executeQuery();
			
			if(results.next())
			{
				Product product = MapResultToProduct(results);
				return product;
			}
			
		} catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
		}
		
		return null;
	}
	
	private Product MapResultToProduct(ResultSet result) throws SQLException, MalformedURLException
	{
		String uuidString = result.getString("uuid");
		UUID uuid = UUID.fromString(uuidString);
		String name  = result.getString("name");
		String brand = result.getString("brand");
		String model = result.getString("model");
		String desc  = result.getString("description");
		Double price = result.getDouble("price");
		String ctg   = result.getString("category");
		String urlString = result.getString("url");
		URL url = new URL(urlString);
		
		Product product = new Product(uuid, name, url);
		product.set_brand(brand);
		product.set_model(model);
		product.set_description(desc);
		product.set_price(price);
		product.set_category(Category.fromName(ctg));
		
		return product;
	}
	
	private void createTable()
	{
		
	}
}
