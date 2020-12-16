package pt.uminho.iata.autoclimate.api.climate;

/**
 * Simple base class for an auto climate action
 */
public class BaseAutoClimateAction implements AutoClimateAction
{
	private String _description;

	public BaseAutoClimateAction(String description)
	{
		_description = description;
	}

	public BaseAutoClimateAction()
	{
		this("No description");
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}

	public String toString()
	{
		return _description;
	}
}
