package ytu.dmase.project.repository.pgsql;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.sql.DataSource;

import com.google.inject.Inject;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;
import ytu.dmase.project.repository.RepositoryException;

public class ProductRepository implements IProductRepository {
	
	protected DataSource _ds;
	
	protected static String _getById = "SELECT * FROM product WHERE uuid=?";
	protected static String _getByCategory = "SELECT * FROM product WHERE category=?";
	protected static String _findByName = "SELECT * FROM product WHERE name=?";
	protected static String _save = "INSERT INTO product "
			+ "(uuid, name, brand, model, description, price, category, url, update_time) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	protected static String _update = "UPDATE product SET name=?, brand=?, model=?, "
			+ "description=?, price=?, category=?, url=?, update_time=? WHERE uuid=?";
	protected static String _delete = "DELETE FROM product WHERE uuid=?";
	protected static String _getByUrl = "SELECT * FROM product WHERE url=?";
	protected static String _count = "SELECT count(uuid) FROM product";
	
	
	@Inject
	public ProductRepository(DataSource ds) throws SQLException
	{
		Objects.requireNonNull(ds);
		_ds = ds;
	}

	public Product getById(UUID uuid) throws RepositoryException {
		
		Objects.requireNonNull(uuid, "uuid can't be null.");
		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			PreparedStatement statement = conn.prepareStatement(_getById);
			statement.setObject(1, uuid);
			ResultSet rs = statement.executeQuery();
			
			if(rs.next())
				return readSingle(rs);			
			else
				return null;
			
		} catch (SQLException e) {

			throw new RepositoryException("An error occured while finding products by uuid.", e);
		} finally
		{
			closeConnection(conn);
		}
	}

	public Product getById(String uuid) throws RepositoryException {
		return getById(UUID.fromString(uuid));
	}

	public List<Product> findByCategory(Category category) throws RepositoryException {
		
		Objects.requireNonNull(category, "category can't be null.");
		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			PreparedStatement statement = conn.prepareStatement(_getByCategory);
			statement.setString(0, category.toString());
			return readMultiple(statement.executeQuery());
			
		} catch (SQLException e) {
			
			throw new RepositoryException("An error occured while finding products by category.", e);
		} finally
		{
			closeConnection(conn);
		}
	}

	public List<Product> findByKeywords(String... keywords) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Product> findByName(String productName) throws RepositoryException {
		
		Objects.requireNonNull(productName, "productName can't be null.");
		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			PreparedStatement statement = conn.prepareStatement(_findByName);
			statement.setString(1, productName);
			return readMultiple(statement.executeQuery());
			
		} catch (Exception e) {
			
			throw new RepositoryException("An error occured while finding products by name.", e);
		} finally
		{
			closeConnection(conn);
		}
	}

	public void save(Product product) throws RepositoryException {
		
		Objects.requireNonNull(product, "product can't be null.");
		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			PreparedStatement statement = conn.prepareStatement(_save);;
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
		} finally
		{
			closeConnection(conn);
		}
	}

	public void update(Product product) throws RepositoryException {
		
		Objects.requireNonNull(product, "product can't be null.");
		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			PreparedStatement statement = conn.prepareStatement(_update);
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
		} finally
		{
			closeConnection(conn);
		}
	}

	public void delete(Product product) throws RepositoryException {
		
		Objects.requireNonNull(product, "product can't be null.");
		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			PreparedStatement statement = conn.prepareStatement(_delete);
			statement.setObject(1, product.get_uuid());
			statement.execute();
			
		} catch (SQLException e) {

			throw new RepositoryException("An error occured while deleting a product from repository.", e);
		} finally
		{
			closeConnection(conn);
		}
	}
	
	public Product getByUrl(URL url) throws RepositoryException {

		Objects.requireNonNull(url, "url can't be null.");
		
		return getByUrl(url.toString());
	}

	public Product getByUrl(String urlString) throws RepositoryException {
		
		Objects.requireNonNull(urlString, "urlString can't be null.");
		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			PreparedStatement statement = conn.prepareStatement(_getByUrl);
			statement.setString(1, urlString);
			ResultSet rs = statement.executeQuery();
			if(rs.next())
				return readSingle(rs);
			else
				return null;
			
		} catch (SQLException e) {
			
			throw new RepositoryException("An error occured while finding products by url.", e);
		} finally
		{
			closeConnection(conn);
		}
		
	}
	
	public int count() throws RepositoryException {

		Connection conn = null;
		
		try {
			conn = _ds.getConnection();
			ResultSet rs = conn.createStatement()
					.executeQuery(_count);

			rs.next();
			return rs.getInt("count");
			
		} catch (SQLException e) {
			
			throw new RepositoryException("Error when getting count of products", e);	
		} finally
		{
			closeConnection(conn);
		}
	}
	
	protected Product readSingle(ResultSet result) throws SQLException, RepositoryException
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
	
	protected List<Product> readMultiple(ResultSet result) throws SQLException, RepositoryException
	{
		ArrayList<Product> products = new ArrayList<Product>();
		while(result.next())
		{
			products.add(readSingle(result));
		}
		
		return products;
	}
	
	protected void closeConnection(Connection conn)
	{
		try
		{
			if(conn != null)
				conn.close();
		} 
		catch(SQLException e)
		{
			// do nothing
		}
	}
}
