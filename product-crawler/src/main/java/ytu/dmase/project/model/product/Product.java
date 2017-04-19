package ytu.dmase.project.model.product;

import java.awt.Image;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

public class Product {
	
	private UUID _uuid;
	private URL _url;
	private String _name;
	private String _brand = "";
	private String _model = "";
	private String _description = "";
	private double _price = -1;
	private Category _category = Category.unassigned;
	private Date _updateDate;
	private Image _image;
	
	public Product(String productName, URL url)
	{
		_uuid = UUID.randomUUID();
		_name = productName != null ? productName : "";

		if(url == null)
		{
			throw new IllegalArgumentException("url can't be null.");
		}
		
		_url = url;
		_updateDate = new Date();
	}
	
	public Product(UUID uuid, String productName, URL url)
	{
		this(productName, url);
		_uuid = uuid;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String name) {
		if(name != null)
			this._name = name;
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
		if(category != null)
			this._category = category;
	}

	public UUID get_uuid() {
		return _uuid;
	}

	public URL get_url() {
		return _url;
	}

	public void set_url(URL url) {
		if(url != null)
			this._url = url;
	}

	public void set_brand(String brand) {
		if(brand != null)
			this._brand = brand;
	}

	public void set_model(String model) {
		if(model != null)
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

	public Date get_updateDate() {
		return _updateDate;
	}

	public void set_updateDate(Date _updateDate) {
		this._updateDate = _updateDate;
	}
}
