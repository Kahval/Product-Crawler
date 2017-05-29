package ytu.dmase.project.model.product;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.UUID;


public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final PropertyChangeSupport _pcs = new PropertyChangeSupport(this);
	
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

	public void set_name(String newName) {
		
		if(newName == null || newName.isEmpty())
			throw new IllegalArgumentException("Invalid product name.");
		
		String oldName = _name;
		_name = newName;
		_pcs.firePropertyChange("name", oldName, newName);
	}

	public String get_description() {
		return _description;
	}

	public void set_description(String newDescription) {
		
		if(newDescription == null)
			newDescription = "";
		
		String oldDescription = _description;
		this._description = newDescription;
		_pcs.firePropertyChange("description", oldDescription, newDescription);
	}

	public double get_price() {
		return _price;
	}

	public void set_price(double newPrice) {
		
		// Product price can only be -1 if non-positive
		// -1 indicates that product's price is unavailable
		if(newPrice < 0 && newPrice != -1)
			throw new IllegalArgumentException("Invalid product price.");
		
		double oldPrice = _price;
		_price = newPrice;
		_pcs.firePropertyChange("price", oldPrice, newPrice);
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

	public void set_category(Category newCategory) {
		
		Category oldCategory = _category;
		_category = newCategory;
		_pcs.firePropertyChange("category", oldCategory, newCategory);
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

	public void set_brand(String newBrand) {
		if(newBrand == null)
			newBrand = "";
		
		String oldBrand = _brand;
		_brand = newBrand;
		_pcs.firePropertyChange("brand", oldBrand, newBrand);
	}

	public void set_model(String newModel) {
		if(newModel == null)
			newModel = "";
		
		String oldModel = _model;
		_model = newModel;
		_pcs.firePropertyChange("brand", oldModel, newModel);
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
		return String.format("Product name: %-64s Url: %s",
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
	
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		_pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) 
	{
        _pcs.removePropertyChangeListener(listener);
    }
}
