package pt.uminho.iata.autoclimate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateAction;
import pt.uminho.iata.autoclimate.api.climate.AutoClimateRule;
import pt.uminho.iata.autoclimate.api.meteo.MeteoEventListener;
import pt.uminho.iata.autoclimate.handlers.LoggingClimateActionHandler;
import pt.uminho.iata.autoclimate.api.meteo.MeteoEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the auto climate system by processing meteo events through a set
 * of rules and processing the actions that the rules produce.
 */
public class AutoClimateController implements MeteoEventListener
{
	private LoggingClimateActionHandler _climateActionHandler;
	private List<AutoClimateRule> _rules;
	private static Logger _log = LoggerFactory.getLogger(AutoClimateController.class);

	public AutoClimateController()
	{
		_rules = new ArrayList<>();
	}

	public void addRule(AutoClimateRule rule)
	{
		_rules.add(rule);
	}

	public void setClimateEventSink(LoggingClimateActionHandler climateActionHandler)
	{
		_climateActionHandler = climateActionHandler;
	}

	public void eventReceived(MeteoEvent meteoEvent)
	{
		AutoClimateAction action = _processMeteoEvent(meteoEvent);
		_log.debug("Action for event: {} : {}", action.getDescription(), action.toString());
		_climateActionHandler.handleAction(action);
	}

	private AutoClimateAction _processMeteoEvent(MeteoEvent meteoEvent)
	{
		_log.debug("Processing event: {}", meteoEvent);
		for (AutoClimateRule rule : _rules) {
			// find the first rule in the list that applies to this event
			// and return the action it prescribes for the given event
			if (rule.appliesToEvent(meteoEvent)) {
				_log.debug("Found applicable rule: {}", rule.getName());
				return rule.processMeteoEvent(meteoEvent);
			}
		}

		_log.debug("No applicable rule found");
		// default to the do-nothing action
		return AutoClimateAction.NOOP;
	}
}