package sgs.util.replicateTableRows;

import static sgs.util.replicateTableRows.CommonConstants.BATCH_INSERT_SIZE;
import static sgs.util.replicateTableRows.CommonConstants.COLLECT_BATCH_INSERT_RETURN_CODES;
import static sgs.util.replicateTableRows.CommonConstants.CONFIG_FILENAME;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

/**
 * @author gs01491
 * 
 */
public class ReplicateTableRows
{
	private static final Logger	logger	= Logger.getLogger(ReplicateTableRows.class.getName());
	private static Properties prop 		= new Properties();
	private static long startTime 		= 0L;

	public static void main(String[] args) throws SQLException, IOException
	{
		IMG_HR_Bean imgHrBean = null;
		Scanner in = new Scanner(System.in);
		final String MENU = "************************************************************************\n* Selezionere 1 per iniziare la replicazione dal massimo ID in tabella *\n* Selezionare 2 per iniziare la replicazione da un ID digitato         *\n* Selezionare 3 per uscire                                             *\n************************************************************************";
		final String MENU_ID = "Digitare l'ID di partenza per la replicazione:";
		final String NUMBER_OF_ROWS_TO_REPLICATE = "Digitare il numero di replicazioni richieste:";
		final String INPUT_NOT_VALID = "Input non valido!";
		int choice = 0;
		int id_digitato = 0;
		int num_rows_to_replicate = 0;
		
		do
		{
			do
			{
				System.out.println(MENU);
				while (!in.hasNextInt())
				{
					System.out.println(INPUT_NOT_VALID);
					in.next();
				}
				choice = in.nextInt();
				if (choice < 1 || choice > 3)
				{
					System.out.println(INPUT_NOT_VALID);
				}
			} while (choice < 1 || choice > 3);
			
			switch (choice)
			{
			case 1:
				commonInitOperations();
				imgHrBean = DBManager.executeQuerySELECT_02();
				
				do
				{
					System.out.println(NUMBER_OF_ROWS_TO_REPLICATE);
					while (!in.hasNextInt())
					{
						System.out.println(INPUT_NOT_VALID);
						in.next();
					}
					num_rows_to_replicate = in.nextInt();
					if (num_rows_to_replicate < 1)
					{
						System.out.println(INPUT_NOT_VALID);
					}
				} while (num_rows_to_replicate < 1);
				
				commonFinalOperations(imgHrBean, DBManager.executeQuerySELECT_01(), num_rows_to_replicate);
				break;
			
			case 2:
				do
				{
					System.out.println(MENU_ID);
					while (!in.hasNextInt())
					{
						System.out.println(INPUT_NOT_VALID);
						in.next();
					}
					id_digitato = in.nextInt();
					if (id_digitato < 0)
					{
						System.out.println(INPUT_NOT_VALID);
					}
				} while (id_digitato < 0);
				
				commonInitOperations();
				imgHrBean = DBManager.executeQuerySELECT_03(new BigDecimal(id_digitato));
				
				do
				{
					System.out.println(NUMBER_OF_ROWS_TO_REPLICATE);
					while (!in.hasNextInt())
					{
						System.out.println(INPUT_NOT_VALID);
						in.next();
					}
					num_rows_to_replicate = in.nextInt();
					if (num_rows_to_replicate < 1)
					{
						System.out.println(INPUT_NOT_VALID);
					}
				} while (num_rows_to_replicate < 1);
				
				commonFinalOperations(imgHrBean, new BigDecimal(id_digitato), num_rows_to_replicate);
				break;
				
			case 3:
				System.out.println("Bye!");
				break;
				
			default:
				break;
			}
			
		} while (choice != 3);
		in.close();
	}
	
	private static void commonInitOperations()
	{
		try
		{
			prop = Utils.getPropertiesFromFile(CONFIG_FILENAME, prop);
		}
		catch (FileNotFoundException e)
		{
			logger.error("Unable to find " + CONFIG_FILENAME);
			e.printStackTrace();
		}
		
		logger.info("Executing initialization operations ...");
		DBManager.initialize();
	}
	
	private static void commonFinalOperations(IMG_HR_Bean imgHrBean, BigDecimal startID, int num_rows_to_replicate)
	{
		System.out.println("Executing...");
		startTime = System.currentTimeMillis();
		DBManager.executeQueryINSERT_01(imgHrBean, num_rows_to_replicate, Integer.parseInt(prop.getProperty(BATCH_INSERT_SIZE)), Boolean.parseBoolean(prop.getProperty(COLLECT_BATCH_INSERT_RETURN_CODES)));
		DbUtils.closeQuietly(DBManager.getConnection(), DBManager.getPstmt(), DBManager.getRs());
		
		System.out.println("Execution completed, " + num_rows_to_replicate + " rows has been replicated in the IMG_HR table based on the one with ID=" + startID + ", last ID is now " + startID.add(new BigDecimal(num_rows_to_replicate)) + "\nCheck the logs for further details");
		System.out.println("Total execution time is: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "sec (" + TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - startTime) + "min)\n");
	}
}


// TODO SQLTimeoutException 