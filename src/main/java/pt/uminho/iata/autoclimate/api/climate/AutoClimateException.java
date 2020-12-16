package pt.uminho.iata.autoclimate.api.climate;

/**
 * Common runtime exception for problems found in autoclimate control - typically data problems
 */
public class AutoClimateException extends RuntimeException
{
	public AutoClimateException(Throwable cause, String message, String... args)
	{
		super(_format(message, args), cause);
	}

	public AutoClimateException(String message, String... args)
	{
		super(_format(message, args));
	}

	private static String _format(String message, String[] args)
	{
		return args.length == 0 ? message : String.format(message, args);
	}
}
