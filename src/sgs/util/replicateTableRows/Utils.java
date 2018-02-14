package sgs.util.replicateTableRows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author gs01491
 * 
 */
public class Utils
{
	private static final Logger	logger	= Logger.getLogger(Utils.class.getName());

	public static Properties getPropertiesFromFile(String fileName, Properties prop) throws FileNotFoundException
	{
		InputStream input = null;
		try
		{
			input = Utils.class.getClassLoader().getResourceAsStream(fileName);
			if (input == null)
			{
				throw new FileNotFoundException();
			}
			prop.load(input);
		}
		catch (IOException e)
		{
			logger.error("Problem occurred while loading the properties file " + fileName + ", check logged exceptions");
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(input);
		}
		return prop;
	}
	
	public static String linearize(String input)
	{
		String newLine = System.getProperty("line.separator");
		return StringUtils.replace(input, newLine, " ");
	}

}
