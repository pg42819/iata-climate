package pt.uminho.iata.autoclimate.api.climate;

import pt.uminho.iata.autoclimate.api.meteo.MeteoEvent;

/**
 * An Auto climate rule that processes a meteorological event and produces an action.
 */
public interface AutoClimateRule
{
	/**
	 * Name of the rule
	 */
	String getName();

	/**
	 * Determines if this rule is applicable for the given event.
	 *
	 * Controllers should generally call the processMeteoEvent if this returns true.
	 *
	 * @param meteoEvent a meteo event to test
	 * @return true if this rule is applicable
	 */
	boolean appliesToEvent(MeteoEvent meteoEvent);

	AutoClimateAction processMeteoEvent(MeteoEvent meteoEvent);
}
