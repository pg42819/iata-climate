package pt.uminho.iata.autoclimate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.uminho.iata.autoclimate.aircon.SeasonalAirConRule;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateRule;
import pt.uminho.iata.autoclimate.api.meteo.Hemisphere;
import pt.uminho.iata.autoclimate.handlers.LoggingClimateActionHandler;
import pt.uminho.iata.autoclimate.meteo.CsvMeteoEventSource;
import pt.uminho.iata.autoclimate.api.meteo.MeteoEvent;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.MonthDay;

/**
 * Simulates an Car climate control system that receivers meteorological events
 * and processing them to create actions that are then applied to an action handler.
 *
 * In this simulated form, events are taken from a CSV file instead of from sensors,
 * and the actions are merely printed to a log.
 *
 * Both input and log are provided by command line args.
 * Events are also logged to the console... as are debug messages (configured in logback.xml)
 */
public class AutoClimate
{
	private static final AutoClimateRule[] _DEFAULT_RULES = {
			new SeasonalAirConRule("Northern Summer",
								   MonthDay.of(6, 21),  // start of season
								   MonthDay.of(9, 20),  // end of season
								   Hemisphere.Northern, // hemisphere
								   25.00,               // comfort temp for this season
								   0.1),     			// threshold for temp measurements
			new SeasonalAirConRule("Northern Autumn",
								   MonthDay.of(9, 21),   // start of season
								   MonthDay.of(12, 20),  // end of season
								   Hemisphere.Northern,  // hemisphere
								   15.00,                // comfort temp for this season
								   0.1),     			 // threshold for temp measurements
			new SeasonalAirConRule("Northern Winter",
								   MonthDay.of(12, 21),  // start of season
								   MonthDay.of(3, 20),   // end of season
								   Hemisphere.Northern,  // hemisphere
								   15.00,                // comfort temp for this season
								   0.1),     // threshold for temp measurements
			new SeasonalAirConRule("Northern Spring",
								   MonthDay.of(3, 21),  // start of season
								   MonthDay.of(6, 20),  // end of season
								   Hemisphere.Northern, // hemisphere
								   25.00,               // comfort temp for this season
								   0.1),     // threshold for temp measurements

			// Southern Hemisphere
			new SeasonalAirConRule("Southern Winter",
								   MonthDay.of(6, 21),  // start of season
								   MonthDay.of(9, 20),  // end of season
								   Hemisphere.Southern, // hemisphere
								   15.00,               // comfort temp for this season
								   0.1),     			// threshold for temp measurements
			new SeasonalAirConRule("Southern Spring",
								   MonthDay.of(9, 21),   // start of season
								   MonthDay.of(12, 20),  // end of season
								   Hemisphere.Southern,  // hemisphere
								   25.00,                // comfort temp for this season
								   0.1),     			 // threshold for temp measurements
			new SeasonalAirConRule("Southern Summer",
								   MonthDay.of(12, 21),  // start of season
								   MonthDay.of(3, 20),   // end of season
								   Hemisphere.Southern,  // hemisphere
								   25.00,                // comfort temp for this season
								   0.1),     // threshold for temp measurements
			new SeasonalAirConRule("Southern Autumn",
								   MonthDay.of(3, 21),  // start of season
								   MonthDay.of(6, 20),  // end of season
								   Hemisphere.Southern, // hemisphere
								   15.00,               // comfort temp for this season
								   0.1), 			    // threshold for temp measurements
	};

	private static Logger _log = LoggerFactory.getLogger(AutoClimate.class);

	public AutoClimate()
	{
	}

	private static void _usage()
	{
		_log.error("Usage: AutoClimate INPUT_CSV_FILE [LOG_FILE]");
		_log.info("Reads the current meteo conditions from INPUT_CSV_FILE\n"
				  + "generates an action for the Air Conditioner and writes out\n"
				  + "the action to LOG_FILE if specified or the console if not.");
		System.exit(1);
	}

	public static void main(String... args) throws FileNotFoundException
	{
		if (args.length < 1 || args.length > 2) {
			_usage();
		}

		Path inputFile = Paths.get(args[0]);
		if (!Files.exists(inputFile)) {
			_log.error("Cannot find the input file at {}", inputFile);
			_usage();
		}

		String logFile = null;
		if (args.length > 1) {
			logFile = args[1];
		}

		// NOTE: Normally rules would be read from a configuration file, but here we use some
		// hard coded default rules - mainly for testing the system
		AutoClimateController climateController = new AutoClimateController();
		for (AutoClimateRule defaultRule : _DEFAULT_RULES) {
			climateController.addRule(defaultRule);
		}

		// add a sink for the climate event that goes to a log file
		climateController.setClimateEventSink(new LoggingClimateActionHandler(logFile));
		final CsvMeteoEventSource eventSource = new CsvMeteoEventSource(inputFile);
		while (eventSource.hasNextEvent()) {
			final MeteoEvent meteoEvent = eventSource.nextEvent();
			climateController.eventReceived(meteoEvent);
		}
	}
}
