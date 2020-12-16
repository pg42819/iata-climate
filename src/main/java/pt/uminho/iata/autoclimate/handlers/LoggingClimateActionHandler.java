package pt.uminho.iata.autoclimate.handlers;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateAction;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateActionHandler;

/**
 * Implements an action handler by logging the action to a file.
 * Used mainly in simulation: a real action handler would adjust climate control
 *
 * The log format is
 *   airconditioning+{temperatura oC}
 *
 * where temperatura is the absolute value of the temperature adjustment to make
 */
public class LoggingClimateActionHandler implements AutoClimateActionHandler
{
	private final Logger _logger;

	/**
	 * Create a new handler to log to a specific file.
	 *
	 * @param logFile if null, logging goes only to stdout
	 */
	public LoggingClimateActionHandler(String logFile)
	{
		_logger = LoggerFactory.getLogger("ClimateActionHandler");

		if (logFile != null) {
			LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
			FileAppender fileAppender = new FileAppender();
			fileAppender.setContext(loggerContext);
			fileAppender.setName("ClimateActionHandlerAppender");
			// set the file name
			fileAppender.setFile(logFile);

			PatternLayoutEncoder encoder = new PatternLayoutEncoder();
			encoder.setContext(loggerContext);
			encoder.setPattern("%d{HH:mm:ss.SSS}: %msg%n");
			encoder.start();
			fileAppender.setEncoder(encoder);
			fileAppender.start();
			((ch.qos.logback.classic.Logger)_logger).addAppender(fileAppender);
		}
	}

	public void handleAction(AutoClimateAction action)
	{
		if (action == AutoClimateAction.NOOP) {
			_logger.debug("Ignoring NOOP action");
		}
		else {
			_logger.info(action.toString());
		}
	}
}
