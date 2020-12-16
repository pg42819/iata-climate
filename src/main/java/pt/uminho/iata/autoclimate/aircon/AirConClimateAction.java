package pt.uminho.iata.autoclimate.aircon;

import pt.uminho.iata.autoclimate.api.climate.BaseAutoClimateAction;

/**
 * An air-conditioning adjustment action to adjust the automobile air conditioning
 * in response to a meteorological sensor event.
 */
public class AirConClimateAction extends BaseAutoClimateAction
{
	private double _temperatureAdjustment;

	public AirConClimateAction(String description)
	{
		super(description);
		_temperatureAdjustment = 0;
	}

	public AirConClimateAction()
	{
		this("No description");
	}

	public double getTemperatureAdjustment()
	{
		return _temperatureAdjustment;
	}

	public void setTemperatureAdjustment(double temperatureAdjustment)
	{
		_temperatureAdjustment = temperatureAdjustment;
	}

	public String toString()
	{
		// this format matches the specification for logging temperature adjustments
		String sign = _temperatureAdjustment < 0 ? "-" : "+";
		// use absolute value since the sign is specified explicitly
		double adjustment = Math.abs(_temperatureAdjustment);
		return String.format("airconditioning%s{%.2f \u00B0C}", sign, adjustment);
	}
}
