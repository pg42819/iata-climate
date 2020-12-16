package pt.uminho.iata.autoclimate.meteo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateException;
import pt.uminho.iata.autoclimate.api.meteo.MeteoEvent;
import pt.uminho.iata.autoclimate.api.meteo.MeteoEventSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

/**
 * Simulates a source of meteorological events by reading them from a CSV file.
 *
 * Ordinarily a MeteoEventSource would be based on sensors - but this allows us to test the system.
 */
public class CsvMeteoEventSource implements MeteoEventSource
{
	// Must match: "2012-01-01 00:00:00"
	DateTimeFormatter _DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static final String DATE_TIME_FIELD = "dt_iso";
	private static final String CITY_NAME_FIELD = "city_name";
	private static final String HUMIDITY_FIELD = "humidity";
	private static final String TEMPERATURE_FIELD = "temp";
	private static final String LATITUDE_FIELD = "lat";
	private static final String LONGITUDE_FIELD = "lon";

	private Path _inputFile;
	private Iterator<CSVRecord> _csvRecordIterator;

	public CsvMeteoEventSource(Path inputFile)
	{
		_inputFile = inputFile;
	}

	@Override
	public MeteoEvent nextEvent()
	{
		final CSVRecord csvRecord = _getCsvRecords().next();
		if (csvRecord == null) {
			return null;
		}
		else {
			return _csvToMeteo(csvRecord);
		}
	}

	@Override
	public boolean hasNextEvent()
	{
		return _getCsvRecords().hasNext();
	}

	private Iterator<CSVRecord> _getCsvRecords()
	{
		if (_csvRecordIterator == null) {
			// lazy init the file loading
			try {
				final BufferedReader reader = Files.newBufferedReader(_inputFile);
				final CSVParser csvRecords = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
				_csvRecordIterator = csvRecords.iterator();
			}
			catch (IOException e) {
				throw new AutoClimateException(e, "Cannot open the CSV file at %s", _inputFile.toString());
			}
		}
		return _csvRecordIterator;
	}

	private MeteoEvent _csvToMeteo(CSVRecord record)
	{
		// "dt_iso","city_name","lat","lon","temp","humidity"
		// example line of input:
		// "2012-01-01 00:00:00","Guimaraes",41.44253,-8.291786,9.88,91
		MeteoEvent meteo = new MeteoEvent();
		meteo.setTimestamp(_getDateTimeField(record, DATE_TIME_FIELD));
		meteo.setCity(_getStringNonEmptyField(record, CITY_NAME_FIELD));
		meteo.setLatitude(_getDoubleField(record, LATITUDE_FIELD));
		meteo.setLongitude(_getDoubleField(record, LONGITUDE_FIELD));
		meteo.setTemperature(_getDoubleField(record, TEMPERATURE_FIELD));
		meteo.setHumidity(_getIntField(record, HUMIDITY_FIELD));
		return meteo;
	}

	private String _getStringNonEmptyField(CSVRecord record, String field)
	{
		String value = record.get(field);
		if (value == null) {
			throw new AutoClimateException("The field %s is missing from the CSV record event", field);
		}
		return value;
	}

	private int _getIntField(CSVRecord record, String field)
	{
		String value = _getStringNonEmptyField(record, field);
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException nfe) {
			throw new AutoClimateException(nfe, "Expected the field %s to be a valid integer, but found", field, value);
		}
	}

	private double _getDoubleField(CSVRecord record, String field)
	{
		String value = _getStringNonEmptyField(record, field);
		try {
			final double doubleValue = Double.parseDouble(value);
			return doubleValue;
		}
		catch (NumberFormatException nfe) {
			throw new AutoClimateException(nfe, "Expected the field %s to be a valid real number, but found", field, value);
		}
	}

	private LocalDateTime _getDateTimeField(CSVRecord record, String field)
	{
		String value = _getStringNonEmptyField(record, field);
		try {
			return LocalDateTime.parse(value, _DATE_FORMAT);
		}
		catch (DateTimeParseException pe) {
			throw new AutoClimateException(pe, "Expected the field %s to be a valid date-time, but found: %s", field, value);
		}
	}

}
