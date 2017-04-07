package ytu.dmase.project.model.product;

public class Category {
	
	private final String _name;
	
	private Category(String name) 
	{
		_name = name;
	}
	
	public String get_name() {
		return _name;
	}
	
	@Override
	public String toString() {
		return get_name();
	}

	public final static Category electronics = new Category("Electronics");
	public final static Category clothing = new Category("Clothing");
	public final static Category entertainment = new Category("Entertainment");
	public final static Category foods = new Category("Foods");
	public final static Category health = new Category("Beauty, Health");
	public final static Category tools = new Category("Tools");
	public final static Category other = new Category("Other");
	
	public static Category fromName(String name)
	{
		Category[] categories = {
				electronics,
				clothing,
				entertainment,
				foods,
				health,
				tools,
				other
		};
		
		for (int i = 0; i < categories.length; i++) {
			Category category = categories[i];
			if(name.equals(category.get_name()))
				return category;
		}
		
		return null;
	}
}
