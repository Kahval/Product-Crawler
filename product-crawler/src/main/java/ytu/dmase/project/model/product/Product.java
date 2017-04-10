package ytu.dmase.project.model.product;

import java.awt.Image;
import java.net.URL;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Product {
	
	private final static Logger _logger = LoggerFactory.getLogger(Product.class);
	
	private UUID _uuid;
	private URL _url;
	private String _name;
	private String _brand = "";
	private String _model = "";
	private String _description = "";
	private double _price = -1;
	private Category _category = Category.other;
	private Image _image;
	
	public Product(String productName, URL url)
	{
		_uuid = UUID.randomUUID();
		_name = productName;
		_url = url;
	}
	
	public Product(UUID uuid, String productName, URL url)
	{
		this(productName, url);
		_uuid = uuid;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String productName) {
		this._name = productName;
	}

	public String get_description() {
		return _description;
	}

	public void set_description(String description) {
		this._description = description;
	}

	public double get_price() {
		return _price;
	}

	public void set_price(double price) {
		this._price = price;
	}

	public String get_brand() {
		return _brand;
	}

	public String get_model() {
		return _model;
	}

	public Category get_category() {
		return _category;
	}

	public void set_category(Category category) {
		if(category == null)
		{
			_logger.warn("Tried to set to a null category. Using category 'other' instead");
			category = Category.other;
		}
		this._category = category;
	}

	public UUID get_uuid() {
		return _uuid;
	}

	public URL get_url() {
		return _url;
	}

	public void set_url(URL url) {
		this._url = url;
	}

	public void set_brand(String brand) {
		this._brand = brand;
	}

	public void set_model(String model) {
		this._model = model;
	}
	
	@Override
	public boolean equals(Object obj) {

		if(obj != null && obj instanceof Product)
		{
			UUID thisUUID = get_uuid();
			UUID otherUUID = ((Product)obj).get_uuid();
			return thisUUID.equals(otherUUID);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Product name: %s\t Url: %s",
				get_name(),
				get_url());
	}

	public Image get_image() {
		return _image;
	}

	public void set_image(Image _image) {
		this._image = _image;
	}
}
