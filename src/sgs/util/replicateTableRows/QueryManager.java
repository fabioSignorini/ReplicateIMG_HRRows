package sgs.util.replicateTableRows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public final class QueryManager
{
	private static final Logger			logger		= Logger.getLogger(QueryManager.class.getName());

	private static Map<String, String>	queriesMap	= new HashMap<String, String>();

	public static String getQuery(String queryKey) throws IllegalArgumentException
	{
		return getQuery(queryKey, false);
	}

	public static String getQuery(String queryKey, boolean linearizeQueryInOneRow) throws IllegalArgumentException
	{
		String query = queriesMap.get(queryKey);

		if (query == null)
		{
			throw new IllegalArgumentException("Failed to retrieve query from file. Query name [" + queryKey + "]");
		}

		query = linearizeQueryInOneRow ? Utils.linearize(query) : query;

		return query;
	}

	public static void loadResources(URL[] resources) throws IllegalArgumentException, IOException
	{
		for (URL r : resources)
		{
			InputStream is = null;
			Properties properties = new Properties();
			try
			{
				is = r.openStream();
				properties.loadFromXML(is);
			}
			catch (IOException e)
			{
				throw new IOException("Errors occurred while attempting to open/load query file " + r);
			}
			finally 
			{
				IOUtils.closeQuietly(is);
			}

			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements())
			{
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);

				if (queriesMap.containsKey(key))
				{
					throw new IllegalArgumentException("Duplicate query key [" + key + "]");
				}
				logger.info("Adding query to in-memory map [" + key + "]");
				queriesMap.put(key, value.trim());
			}
			logger.info("Query file " + r + " correctly loaded");
		}
		logger.info("All query files correctly loaded");
	}
}
