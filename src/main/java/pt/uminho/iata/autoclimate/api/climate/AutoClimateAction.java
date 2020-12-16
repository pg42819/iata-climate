package pt.uminho.iata.autoclimate.api.climate;

/**
 * Represents an action to take for the Auto Climate control
 */
public interface AutoClimateAction
{
	/** NOOP indicates that no action is to be taken */
	AutoClimateAction NOOP = new BaseAutoClimateAction("No Action");

	String getDescription();

	void setDescription(String description);
}
