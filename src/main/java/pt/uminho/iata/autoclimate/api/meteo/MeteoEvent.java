package pt.uminho.iata.autoclimate.api.meteo;

import java.time.LocalDateTime;

/**
 * Simple POJO representing an incoming meteorology report event
 */
public class MeteoEvent
{
	private LocalDateTime _timestamp;
	private String _city;
	private double _latitude;
	private double _longitude;
	private double _temperature;
	private int _humidity;

	public MeteoEvent()
	{
	}

	public LocalDateTime getTimestamp()
	{
		return _timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp)
	{
		_timestamp = timestamp;
	}

	public String getCity()
	{
		return _city;
	}

	public void setCity(String city)
	{
		_city = city;
	}

	public double getLatitude()
	{
		return _latitude;
	}

	public void setLatitude(double latitude)
	{
		_latitude = latitude;
	}

	public double getLongitude()
	{
		return _longitude;
	}

	public void setLongitude(double longitude)
	{
		_longitude = longitude;
	}

	public double getTemperature()
	{
		return _temperature;
	}

	public void setTemperature(double temperature)
	{
		_temperature = temperature;
	}

	public int getHumidity()
	{
		return _humidity;
	}

	public void setHumidity(int humidity)
	{
		_humidity = humidity;
	}

	@Override
	public String toString()
	{
		return "MeteoEvent{" +
			   "_timestamp=" + _timestamp +
			   ", _city='" + _city + '\'' +
			   ", _latitude=" + _latitude +
			   ", _longitude=" + _longitude +
			   ", _temperature=" + _temperature +
			   ", _humidity=" + _humidity +
			   '}';
	}
}
