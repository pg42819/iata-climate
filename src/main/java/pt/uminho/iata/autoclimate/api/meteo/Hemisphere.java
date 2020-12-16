package pt.uminho.iata.autoclimate.api.meteo;

/**
 * Enum representing the northern or southern hemisphere.
 * This is useful for determining the season based on the date.
 */
public enum Hemisphere
{
	Northern(90.0, 0.0001),
	Southern(0.00, -90.0);

	private double _top;
	private double _bottom;

	Hemisphere(double top, double bottom)
	{
		_top = top;
		_bottom = bottom;
	}

	public boolean isIn(double latitude, double longitude)
	{
		return (latitude <= _top && latitude >= _bottom);
	}
}
