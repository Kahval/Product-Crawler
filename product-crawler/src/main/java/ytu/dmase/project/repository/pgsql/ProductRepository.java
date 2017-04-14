package ytu.dmase.project.repository.pgsql;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import javax.sql.DataSource;

import java.util.Objects;

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;
import ytu.dmase.project.repository.RepositoryException;

public class ProductRepository implements IProductRepository {
	
	private DataSource _ds;
	private Connection _conn;
	
	private PreparedStatement _getById;
	private PreparedStatement _findByCategory;
	private PreparedStatement _findByName;
	private PreparedStatement _save;
	private PreparedStatement _update;
	private PreparedStatement _delete;
	private PreparedStatement _getByUrl;
	private PreparedStatement _count;
	
	@Inject
	public ProductRepository(DataSource ds) throws SQLException
	{
		Objects.requireNonNull(ds);
		_ds = ds;
		_conn = _ds.getConnection();
		
		_getById 		= _conn.prepareStatement("SELECT * FROM product WHERE uuid=?");
		_findByCategory = _conn.prepareStatement("SELECT * FROM product WHERE category=?");
		_findByName		= _conn.prepareStatement("SELECT * FROM product WHERE name=?");
		_getByUrl 		= _conn.prepareStatement("SELECT * FROM product WHERE url=?");
		_save 			= _conn.prepareStatement("INSERT INTO product (uuid, name, brand, model, description, price, category, url, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		_update 		= _conn.prepareStatement("UPDATE product SET name=?, brand=?, model=?, description=?, price=?, category=?, url=?, update_time=? WHERE uuid=?");
		_delete 		= _conn.prepareStatement("DELETE FROM product WHERE uuid=?");
		_count			= _conn.prepareStatement("SELECT count(uuid) FROM product");

	}

	public Product getById(UUID uuid) throws RepositoryException {
		
		Objects.requireNonNull(uuid, "uuid can't be null.");
		
		try {
			_getById.setObject(1, uuid);
			ResultSet rs = _getById.executeQuery();
			
			if(rs.next())
				return readSingle(rs);			
			else
				return null;
			
		} catch (SQLException e) {

			throw new RepositoryException("An error occured while finding products by uuid.", e);
		}
	}

	public Product getById(String uuid) throws RepositoryException {
		return getById(UUID.fromString(uuid));
	}

	public Iterable<Product> findByCategory(Category category) throws RepositoryException {
		
		Objects.requireNonNull(category, "category can't be null.");
		
		try {
			_findByCategory.setString(0, category.toString());
			ResultSet result = _findByCategory.executeQuery();
			return readMultiple(result);
			
		} catch (SQLException e) {
			
			throw new RepositoryException("An error occured while finding products by category.", e);
		}
	}

	public Iterable<Product> findByKeywords(String... keywords) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<Product> findByName(String productName) throws RepositoryException {
		
		Objects.requireNonNull(productName, "productName can't be null.");
		
		try {
			_findByName.setString(1, productName);
			return readMultiple(_findByName.executeQuery());
			
		} catch (Exception e) {
			
			throw new RepositoryException("An error occured while finding products by name.", e);
		}
	}

	public void save(Product product) throws RepositoryException {
		
		Objects.requireNonNull(product, "product can't be null.");
		
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
			statement.setTimestamp(9, new Timestamp(product.get_updateDate().getTime()));
			statement.execute();
			
		} catch (SQLException e) {
			
			throw new RepositoryException("An error occured while saving a product to repository.", e);
		}
	}

	public void update(Product product) throws RepositoryException {
		
		Objects.requireNonNull(product, "product can't be null.");
		
		try {
			PreparedStatement statement = _update;
			statement.setString(1, product.get_name());
			statement.setString(2, product.get_brand());
			statement.setString(3, product.get_model());
			statement.setString(4, product.get_description());
			statement.setDouble(5, product.get_price());
			statement.setString(6, product.get_category().toString());
			statement.setString(7, product.get_url().toString());
			statement.setTimestamp(8, new Timestamp(product.get_updateDate().getTime()));
			statement.setObject(9, product.get_uuid());

			statement.execute();
			
		} catch (SQLException e) {

			throw new RepositoryException("An error occured while updating a product's information.", e);
		}
	}

	public void delete(Product product) throws RepositoryException {
		
		Objects.requireNonNull(product, "product can't be null.");
		
		try {
			_delete.setObject(1, product.get_uuid());
			_delete.execute();
			
		} catch (SQLException e) {

			throw new RepositoryException("An error occured while deleting a product from repository.", e);
		}
	}
	
	public Product getByUrl(URL url) throws RepositoryException {

		Objects.requireNonNull(url, "url can't be null.");
		
		return getByUrl(url.toString());
	}

	public Product getByUrl(String urlString) throws RepositoryException {
		
		Objects.requireNonNull(urlString, "urlString can't be null.");
		
		try {
			_getByUrl.setString(1, urlString);
			ResultSet rs = _getByUrl.executeQuery();
			if(rs.next())
				return readSingle(rs);
			else
				return null;
			
		} catch (SQLException e) {
			
			throw new RepositoryException("An error occured while finding products by url.", e);
		}
		
	}
	
	public int count() throws RepositoryException {
		
		try {
			ResultSet rs = _count.executeQuery();

			rs.next();
			return rs.getInt("count");
			
		} catch (SQLException e) {
			
			throw new RepositoryException("Error when getting count of products", e);	
		}
	}
	
	private Product readSingle(ResultSet result) throws SQLException, RepositoryException
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
		Timestamp timestamp = result.getTimestamp("update_time");
		
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			
			throw new RepositoryException("Error when parsing url from result set", e);
		}
		
		Product product = new Product(uuid, name, url);
		product.set_brand(brand);
		product.set_model(model);
		product.set_description(desc);
		product.set_price(price);
		product.set_category(Category.fromName(ctg));
		product.set_updateDate(new Date(timestamp.getTime()));
		
		return product;
	}
	
	protected Iterable<Product> readMultiple(ResultSet result) throws SQLException, RepositoryException
	{
		ArrayList<Product> products = new ArrayList<Product>();
		while(result.next())
		{
			products.add(readSingle(result));
		}
		
		return products;
	}
	
	protected void setupDatabase() throws SQLException, IOException
	{
		InputStream is = getClass().getResourceAsStream("postgresql-ddl.txt");
		String ddlScript = IOUtils.toString(is, Charset.forName("utf-8"));
		
		try {
			_conn.setAutoCommit(false);
			
			PreparedStatement statement = _conn.prepareStatement(ddlScript);
			statement.execute();
			
		} catch (SQLException e) {
			
			_conn.rollback();
			_conn.setAutoCommit(false);
		}
	}
}
