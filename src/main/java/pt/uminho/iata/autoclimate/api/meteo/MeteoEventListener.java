package pt.uminho.iata.autoclimate.api.meteo;

/**
 * General listener to meteoroligical events
 */
public interface MeteoEventListener
{
	void eventReceived(MeteoEvent meteoEvent);
}
