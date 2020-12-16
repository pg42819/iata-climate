package pt.uminho.iata.autoclimate.aircon;

import pt.uminho.iata.autoclimate.api.meteo.Hemisphere;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateAction;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateRule;
import pt.uminho.iata.autoclimate.api.meteo.MeteoEvent;

import java.time.LocalDate;
import java.time.MonthDay;

/**
 * An auto climate rule implmentation that represents an ideal climate for a given
 * season of the year: Spring, Summer, Autumn, Winter in the Northern or Southern Hemisphere
 */
public class SeasonalAirConRule implements AutoClimateRule
{
	private String _name;
	private MonthDay _seasonStart;
	private MonthDay _seasonEnd;
	private Hemisphere _hemisphere;
	private double _comfortTemperature;
	private double _temperatureAdjustmentThreshold;

	public SeasonalAirConRule(String name,
							  MonthDay seasonStart, MonthDay seasonEnd, Hemisphere hemisphere,
							  double comfortTemperature, double temperatureAdjustmentThreshold)
	{
		_name = name;
		_seasonStart = seasonStart;
		_seasonEnd = seasonEnd;
		_hemisphere = hemisphere;
		_comfortTemperature = comfortTemperature;
		_temperatureAdjustmentThreshold = temperatureAdjustmentThreshold;
	}

	@Override
	public String getName()
	{
		return _name;
	}

	public boolean appliesToEvent(MeteoEvent meteoEvent)
	{
		if (_hemisphere.isIn(meteoEvent.getLatitude(),
								 meteoEvent.getLongitude())) {
			final LocalDate eventDate = meteoEvent.getTimestamp().toLocalDate();

			final int year = eventDate.getYear();
			final LocalDate seasonStart = _seasonStart.atYear(year);
			final LocalDate seasonEnd = _seasonEnd.atYear(year);
			if ((seasonEnd.isAfter(eventDate) && seasonStart.isBefore(eventDate))
				|| seasonEnd.equals(eventDate) || seasonStart.equals(eventDate)) {
				return true;
			}
		}
		return false;
	}

	public AutoClimateAction processMeteoEvent(MeteoEvent meteoEvent)
	{
		double temperature = meteoEvent.getTemperature();
		double deltaT = _comfortTemperature - temperature;
		// only if the difference is significant, issue an action to change the AC
		if (Math.abs(deltaT) > _temperatureAdjustmentThreshold) {
			AirConClimateAction action = new AirConClimateAction("Adjust Air Conditioning");
			action.setTemperatureAdjustment(deltaT);
			return action;
		}
		return AutoClimateAction.NOOP;
	}
}
