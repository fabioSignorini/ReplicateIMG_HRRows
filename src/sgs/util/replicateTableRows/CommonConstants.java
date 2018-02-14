package sgs.util.replicateTableRows;

/**
 * @author gs01491
 * 
 */
public final class CommonConstants
{
	public static final String	CONFIG_FILENAME						= "config.properties";

	public static final String	QUERY_FILENAME						= "getReplicateRowsQueries.xml";

	public static final String	SELECT_01							= "SELECT_01";
	
	public static final String	SELECT_02							= "SELECT_02";
	
	public static final String	SELECT_03							= "SELECT_03";
	
	public static final String 	INSERT_01		   					= "INSERT_01";

	public static final String	NUMBER_OF_ROWS_TO_REPLICATE			= "NUMBER_OF_ROWS_TO_REPLICATE";
	
	public static final String	BATCH_INSERT_SIZE					= "BATCH_INSERT_SIZE";
	
	public static final String	COLLECT_BATCH_INSERT_RETURN_CODES	= "COLLECT_BATCH_INSERT_RETURN_CODES";
	
	public static final String	CONFIG_ORACLE_USER					= "ORACLE_USER";

	public static final String	CONFIG_ORACLE_PWD					= "ORACLE_PWD";

	public static final String	CONFIG_ORACLE_URL					= "ORACLE_URL";
	
	public static final String CONFIG_ID_TABLE   					= "ID";
	
	/**
	 * The caller references the constants using
	 * <tt>CommonConstants.CONSTANT_FIELD</tt>, and so on. Thus, the caller
	 * should be prevented from constructing objects of this class, by declaring
	 * this private constructor.
	 */
	private CommonConstants()
	{
		throw new AssertionError();
	}

}
