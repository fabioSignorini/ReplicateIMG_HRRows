package sgs.util.replicateTableRows;

import static sgs.util.replicateTableRows.CommonConstants.CONFIG_FILENAME;
import static sgs.util.replicateTableRows.CommonConstants.CONFIG_ORACLE_PWD;
import static sgs.util.replicateTableRows.CommonConstants.CONFIG_ORACLE_URL;
import static sgs.util.replicateTableRows.CommonConstants.CONFIG_ORACLE_USER;
import static sgs.util.replicateTableRows.CommonConstants.INSERT_01;
import static sgs.util.replicateTableRows.CommonConstants.QUERY_FILENAME;
import static sgs.util.replicateTableRows.CommonConstants.SELECT_01;
import static sgs.util.replicateTableRows.CommonConstants.SELECT_02;
import static sgs.util.replicateTableRows.CommonConstants.SELECT_03;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * @author gs01491
 * 
 */
public class DBManager
{
	private static final Logger			logger		= Logger.getLogger(DBManager.class.getName());

	private static Connection			connection	= null;

	private static PreparedStatement	pstmt		= null;
	
	private static Statement			stmt 		= null;

	private static ResultSet			rs			= null;

	static
	{
		try
		{
			logger.info("Loading query file [" + QUERY_FILENAME + "]");
			URL queriesUrl = DBManager.class.getClassLoader().getResource(QUERY_FILENAME);
			QueryManager.loadResources(new URL[] { queriesUrl });
		}
		catch (IllegalArgumentException | IOException e)
		{
			logger.error("Failed to load resource [" + QUERY_FILENAME + "]", e);
		}
	}

	public static void initialize()
	{
		Properties prop = new Properties();
		try
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
			
			Properties dbCredentials = new Properties();
			dbCredentials.put("user", prop.getProperty(CONFIG_ORACLE_USER));
			dbCredentials.put("password", prop.getProperty(CONFIG_ORACLE_PWD));
			connection = DriverManager.getConnection(prop.getProperty(CONFIG_ORACLE_URL), dbCredentials);
			logger.info("Successfully retrieved database connection to Oracle DB "
					+ prop.getProperty(CONFIG_ORACLE_URL));
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		pstmt = null;
		rs = null;
	}
	
	public static BigDecimal executeQuerySELECT_01()
	{
		long startTime = 0L;
		String sql = null;
		BigDecimal maxId = null;

		try
		{
			sql = QueryManager.getQuery(SELECT_01);
		}
		catch (IllegalArgumentException e)
		{
			logger.error(e);
			e.printStackTrace();
		}

		try
		{
			stmt = connection.createStatement();
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			logger.info("Executed query: " + Utils.linearize(sql) + ", execution time is [" + (System.currentTimeMillis() - startTime)
					+ "]");
			while (rs.next())
			{
				maxId = rs.getBigDecimal("MAX_ID");
			}
		}
		catch (SQLException e)
		{
			logger.error("Errors occurred while performing SQL related operations");
			e.printStackTrace();
		}
		return maxId;
	}

	public static IMG_HR_Bean executeQuerySELECT_02()
	{
		long startTime = 0L;
		IMG_HR_Bean imgHrBean = null;
		String sql = null;

		try
		{
			sql = QueryManager.getQuery(SELECT_02);
		}
		catch (IllegalArgumentException e)
		{
			logger.error(e);
			e.printStackTrace();
		}

		try
		{
			stmt = connection.createStatement();
			startTime = System.currentTimeMillis();
			rs = stmt.executeQuery(sql);
			logger.info("Executed query: " + Utils.linearize(sql) + ", execution time is [" + (System.currentTimeMillis() - startTime)
					+ "]");
			while (rs.next())
			{
				imgHrBean = new IMG_HR_Bean();
				imgHrBean = makeIMG_HR_Bean(rs, imgHrBean);
			}
		}
		catch (SQLException e)
		{
			logger.error("Errors occurred while performing SQL related operations");
			e.printStackTrace();
		}
		return imgHrBean;
	}
	
	public static IMG_HR_Bean executeQuerySELECT_03(final BigDecimal ID)
	{
		long startTime = 0L;
		IMG_HR_Bean imgHrBean = null;
		String sql = null;

		try
		{
			sql = QueryManager.getQuery(SELECT_03);
		}
		catch (IllegalArgumentException e)
		{
			logger.error(e);
			e.printStackTrace();
		}

		try
		{
			pstmt = connection.prepareStatement(sql);
			pstmt.setBigDecimal(1, ID);
			startTime = System.currentTimeMillis();
			rs = pstmt.executeQuery();
			logger.info("Executed query: " + Utils.linearize(sql) + ", execution time is [" + (System.currentTimeMillis() - startTime)
					+ "]");
			while (rs.next())
			{
				imgHrBean = new IMG_HR_Bean();
				imgHrBean = makeIMG_HR_Bean(rs, imgHrBean);
			}
		}
		catch (SQLException e)
		{
			logger.error("Errors occurred while performing SQL related operations");
			e.printStackTrace();
		}
		return imgHrBean;
	}
	
	public static void executeQueryINSERT_01(IMG_HR_Bean imgHrBean, int numberOfRowsToReplicate, int batchInsertSize, Boolean doCollectBatchInsertReturnCodes)
	{
		long startTime = 0L;
		String sql = null;
		List<Integer> insertReturnCodesList = new ArrayList<Integer>();

		try
		{
			sql = QueryManager.getQuery(INSERT_01);
		}
		catch (IllegalArgumentException e)
		{
			logger.error(e);
			e.printStackTrace();
		}
		
		startTime = System.currentTimeMillis();
		try
		{
			pstmt = connection.prepareStatement(sql);
			for (int i = 1; i <= numberOfRowsToReplicate; i++)
			{
				pstmt.setBigDecimal(1, imgHrBean.getId().add(new BigDecimal(i)));
				pstmt.setBigDecimal(2, imgHrBean.getIstituto());
				pstmt.setBigDecimal(3, imgHrBean.getAbi());
				pstmt.setBigDecimal(4, imgHrBean.getCab());
				pstmt.setBigDecimal(5, imgHrBean.getAssegno().add(new BigDecimal(i)));
				pstmt.setBigDecimal(6, computeNextCra(imgHrBean.getCra(), new BigDecimal(i)));
				pstmt.setString(7, imgHrBean.getStatoFirma());
				pstmt.setString(8, imgHrBean.getStatoInvioDoc());
				pstmt.setBlob(9, imgHrBean.getPdf());
				pstmt.setTimestamp(10, imgHrBean.getTimestampIns());
				pstmt.setString(11, imgHrBean.getPgmIns());
				pstmt.setString(12, imgHrBean.getUserIns());
				pstmt.setString(13, imgHrBean.getTipoAssegno());
				pstmt.setString(14, imgHrBean.getfConforme());
				pstmt.setTimestamp(15, imgHrBean.getTimestampAggFir());
				pstmt.setTimestamp(16, imgHrBean.getTimestampAggInv());
				pstmt.setBlob(17, imgHrBean.getTerzaImmagine());
				pstmt.setString(18, imgHrBean.getStatoInvioCons());
				pstmt.addBatch();
				
				if (i % batchInsertSize == 0)
				{
					if (doCollectBatchInsertReturnCodes)
					{
						insertReturnCodesList.addAll(Arrays.asList(ArrayUtils.toObject(pstmt.executeBatch())));
					}
					else
					{
						pstmt.executeBatch();
					}
					// Empties this Statement object's current list of SQL commands
					pstmt.clearBatch();
				}
			}
			
			if (doCollectBatchInsertReturnCodes)
			{
				insertReturnCodesList.addAll(Arrays.asList(ArrayUtils.toObject(pstmt.executeBatch())));
			}
			else
			{
				pstmt.executeBatch();
			}
			// Empties this Statement object's current list of SQL commands
			pstmt.clearBatch();
			logger.info("Executed query: " + Utils.linearize(sql) + ", execution time is [" + (System.currentTimeMillis() - startTime) + "]");
			
			checkInsertReturnCodesList(insertReturnCodesList);
		}
		catch (SQLException e)
		{
			logger.error("Errors occurred while performing SQL related operations");
			e.printStackTrace();
		}
	}

	private static IMG_HR_Bean makeIMG_HR_Bean(ResultSet rs, IMG_HR_Bean imgHrBean) throws SQLException
	{
		imgHrBean.setId(rs.getBigDecimal("ID"));
		imgHrBean.setIstituto(rs.getBigDecimal("ISTITUTO"));
		imgHrBean.setAbi(rs.getBigDecimal("ABI"));
		imgHrBean.setCab(rs.getBigDecimal("CAB"));
		imgHrBean.setAssegno(rs.getBigDecimal("ASSEGNO"));
		imgHrBean.setCra(rs.getBigDecimal("CRA"));
		imgHrBean.setStatoFirma(rs.getString("STATO_FIRMA"));
		imgHrBean.setStatoInvioDoc(rs.getString("STATO_INVIO_DOC"));
		imgHrBean.setPdf(rs.getBlob("PDF"));
		imgHrBean.setTimestampIns(rs.getTimestamp("TIMESTAMP_INS"));
		imgHrBean.setPgmIns(rs.getString("PGM_INS"));
		imgHrBean.setUserIns(rs.getString("USER_INS"));
		imgHrBean.setTipoAssegno(rs.getString("TIPO_ASSEGNO"));
		imgHrBean.setfConforme(rs.getString("F_CONFORME"));
		imgHrBean.setTimestampAggFir(rs.getTimestamp("TIMESTAMP_AGG_FIR"));
		imgHrBean.setTimestampAggInv(rs.getTimestamp("TIMESTAMP_AGG_INV"));
		imgHrBean.setTerzaImmagine(rs.getBlob("TERZA_IMMAGINE"));
		imgHrBean.setStatoInvioCons(rs.getString("STATO_INVIO_CONS"));
		return imgHrBean;
	}
	
	public static String getSelect_01Query()
	{
		String sql = null;
		try
		{
			sql = QueryManager.getQuery(SELECT_01);
		}
		catch (IllegalArgumentException e)
		{
			logger.error(e);
			e.printStackTrace();
		}
		return sql;
	}
	
	public static String getSelect_02Query()
	{
		String sql = null;
		try
		{
			sql = QueryManager.getQuery(SELECT_02);
		}
		catch (IllegalArgumentException e)
		{
			logger.error(e);
			e.printStackTrace();
		}
		return sql;
	}
	
	public static String getInsert_01Query()
	{
		String sql = null;
		try
		{
			sql = QueryManager.getQuery(INSERT_01);
		}
		catch (IllegalArgumentException e)
		{
			logger.error(e);
			e.printStackTrace();
		}
		return sql;
	}
	
	private static void checkInsertReturnCodesList(List<Integer> insertReturnCodesList)
	{
		for (int i = 0; i < insertReturnCodesList.size(); i++)
		{
			switch (insertReturnCodesList.get(i)) 
			{
			case 1:
				break;
			case Statement.SUCCESS_NO_INFO:
				logger.warn("Execution of batch query returns SUCCESS_NO_INFO for insert number " + (i+1));
				break;
			case Statement.EXECUTE_FAILED:
				logger.error("Execution of batch query returns EXECUTE_FAILED for insert number " + (i+1));
				break;
			default:
				break;
			}
		}
	}
	
	private static BigDecimal computeNextCra(BigDecimal cra, BigDecimal i)
	{
		final int MODULUS = 13;
		String prefix = new BigDecimal(cra.toPlainString().substring(0, cra.toPlainString().length() - 2)).add(i).toPlainString();
		String suffix = new BigDecimal(prefix).remainder(new BigDecimal(MODULUS)).toPlainString();
		suffix = suffix.length() > 1 ? suffix : "0".concat(suffix);
		
		return new BigDecimal(prefix.concat(suffix));
	}

	public static Connection getConnection()
	{
		return connection;
	}

	public static PreparedStatement getPstmt()
	{
		return pstmt;
	}

	public static ResultSet getRs()
	{
		return rs;
	}

}