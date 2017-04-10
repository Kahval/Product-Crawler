package ytu.dmase.project.repository.pgsql;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;

public class ProductRepository implements IProductRepository {
	
	private final static Logger _logger = LoggerFactory.getLogger(ProductRepository.class);
	
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
		setupDatabase();
		
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
		
		try {
			_getById.setObject(1, uuid);
			ResultSet rs = _getById.executeQuery();
			
			if(rs.next())
				return readSingle(rs);			
			else
				return null;
			
		} catch (SQLException e) {

			_logger.error("An error occured while finding products by uuid.", e);
			return null;
		}
	}

	public Product GetById(String uuid) {
		return GetById(UUID.fromString(uuid));
	}

	public Iterable<Product> FindByCategory(Category category) {
		
		try {
			_findByCategory.setString(0, category.toString());
			ResultSet result = _findByCategory.executeQuery();
			return readMultiple(result);
			
		} catch (SQLException e) {
			
			_logger.error("An error occured while finding products by category.", e);
			return new ArrayList<Product>();
		}
	}

	public Iterable<Product> FindByKeywords(String... keywords) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<Product> FindByName(String productName) {
		
		try {
			_findByName.setString(1, productName);
			return readMultiple(_findByName.executeQuery());
			
		} catch (Exception e) {
			
			_logger.error("An error occured while finding products by name.", e);
			return new ArrayList<Product>();
		}
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
			
			_logger.error("An error occured while saving a product to repository.", e);
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

			_logger.error("An error occured while updating a product's information.", e);
		}
	}

	public void Delete(Product product) {
		
		try {
			_delete.setObject(1, product.get_uuid());
			_delete.execute();
			
		} catch (SQLException e) {

			_logger.error("An error occured while deleting a product from repository.", e);
		}
	}
	
	public Product GetByUrl(URL url) {

		return GetByUrl(url.toString());
	}

	public Product GetByUrl(String urlString) {
		
		try {
			_getByUrl.setString(1, urlString);
			ResultSet rs = _getByUrl.executeQuery();
			if(rs.next())
				return readSingle(rs);
			else
				return null;
			
		} catch (Exception e) {
			
			_logger.error("An error occured while finding products by url.", e);
			return null;
		}
		
	}
	
	private Product readSingle(ResultSet result) throws SQLException
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
		
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			_logger.error("Error when parsing url from result set", e);
			return null;
		}
		
		Product product = new Product(uuid, name, url);
		product.set_brand(brand);
		product.set_model(model);
		product.set_description(desc);
		product.set_price(price);
		product.set_category(Category.fromName(ctg));
		
		return product;
	}
	
	protected Iterable<Product> readMultiple(ResultSet result) throws SQLException
	{
		ArrayList<Product> products = new ArrayList<Product>();
		while(result.next())
		{
			products.add(readSingle(result));
		}
		
		return products;
	}
	
	private void setupDatabase()
	{
		try {
			PreparedStatement statement = _conn.prepareStatement(
					"SELECT * FROM information_schema.tables WHERE table_name=?");
			
			statement.setString(1, "product");
			if(!statement.executeQuery().next())
			{
				_logger.info("product table couldn't found. Creating table...");
				createProductTable();
			}
			
			statement.setString(1, "price_history");
			if(!statement.executeQuery().next())
			{
				_logger.info("price_history table couldn't found. Creating table...");
				createPriceHistoryTable();
			}
			
		} catch (SQLException e) {
			
			_logger.error("An error occured while setting up the repository database.", e);
		}
	}
	
	private void createProductTable()
	{
		String sql = "CREATE TABLE public.product "
				+ "("
					+ "uuid UUID PRIMARY KEY NOT NULL, "
					+ "name TEXT NOT NULL, "
					+ "brand TEXT, "
					+ "model TEXT, "
					+ "description TEXT, "
					+ "price REAL, "
					+ "category TEXT, "
					+ "url TEXT NOT NULL, "
					+ "update_date TIMESTAMP DEFAULT now() NOT NULL "
				+ "); "
				+ "CREATE UNIQUE INDEX product_pkey ON public.product (uuid); "
				+ "CREATE UNIQUE INDEX product_url_key ON public.product (url);";
		
		try {
			_conn.createStatement().execute(sql);
		} catch (SQLException e) {

			_logger.error("An error occred while creating the product table", e);
		}
		
		_logger.info("table 'product' has been created.");
	}
	
	private void createPriceHistoryTable()
	{
		String sql = "CREATE TABLE public.price_history "
				+ "( "
					+ "uuid UUID NOT NULL, "
					+ "price REAL, "
					+ "date TIMESTAMP DEFAULT now(), "
					+ "CONSTRAINT price_history_uuid_fkey FOREIGN KEY (uuid) "
					+ "REFERENCES product (uuid) ON DELETE CASCADE ON UPDATE CASCADE "
				+ "); "
				+ "CREATE UNIQUE INDEX price_history_uuid_date_pk ON public.price_history (uuid, date);";
		
		try {
			_conn.createStatement().execute(sql);
		} catch (SQLException e) {
			
			_logger.error("An error occred while creating the price_history table", e);
		}
		
		_logger.info("table 'price_history' has been created.'");
	}
}
