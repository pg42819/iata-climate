package pt.uminho.iata.autoclimate.api.meteo;

public interface MeteoEventSource
{
	MeteoEvent nextEvent();

	boolean hasNextEvent();
}
