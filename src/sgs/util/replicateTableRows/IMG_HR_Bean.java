package sgs.util.replicateTableRows;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Timestamp;

/**
 * @author gs01491
 *
 */
public class IMG_HR_Bean 
{
	private BigDecimal id;
	
	private BigDecimal istituto;
	
	private BigDecimal abi;
	
	private BigDecimal cab;
	
	private BigDecimal assegno;
	
	private BigDecimal cra;
	
	private String statoFirma;
	
	private String statoInvioDoc;
	
	private Blob pdf;
	
	private Timestamp timestampIns;
	
	private String pgmIns;
	
	private String userIns;
	
	private String tipoAssegno;
	
	private String fConforme;
	
	private Timestamp timestampAggFir;
	
	private Timestamp timestampAggInv;
	
	private Blob terzaImmagine;
	
	private String statoInvioCons;
	
	public IMG_HR_Bean(BigDecimal id, BigDecimal istituto, BigDecimal abi, BigDecimal cab, BigDecimal assegno,
			BigDecimal cra, String statoFirma, String statoInvioDoc, Blob pdf, Timestamp timestampIns, String pgmIns,
			String userIns, String tipoAssegno, String fConforme, Timestamp timestampAggFir, Timestamp timestampAggInv,
			Blob terzaImmagine) 
	{
		super();
		this.id = id;
		this.istituto = istituto;
		this.abi = abi;
		this.cab = cab;
		this.assegno = assegno;
		this.cra = cra;
		this.statoFirma = statoFirma;
		this.statoInvioDoc = statoInvioDoc;
		this.pdf = pdf;
		this.timestampIns = timestampIns;
		this.pgmIns = pgmIns;
		this.userIns = userIns;
		this.tipoAssegno = tipoAssegno;
		this.fConforme = fConforme;
		this.timestampAggFir = timestampAggFir;
		this.timestampAggInv = timestampAggInv;
		this.terzaImmagine = terzaImmagine;
	}
	
	public IMG_HR_Bean()
	{
		;
	}

	public BigDecimal getId() 
	{
		return id;
	}

	public void setId(BigDecimal id) 
	{
		this.id = id;
	}

	public BigDecimal getIstituto() 
	{
		return istituto;
	}

	public void setIstituto(BigDecimal istituto) 
	{
		this.istituto = istituto;
	}

	public BigDecimal getAbi() 
	{
		return abi;
	}

	public void setAbi(BigDecimal abi) 
	{
		this.abi = abi;
	}

	public BigDecimal getCab() 
	{
		return cab;
	}

	public void setCab(BigDecimal cab) 
	{
		this.cab = cab;
	}

	public BigDecimal getAssegno() 
	{
		return assegno;
	}

	public void setAssegno(BigDecimal assegno) 
	{
		this.assegno = assegno;
	}

	public BigDecimal getCra() 
	{
		return cra;
	}

	public void setCra(BigDecimal cra) 
	{
		this.cra = cra;
	}

	public String getStatoFirma() 
	{
		return statoFirma;
	}

	public void setStatoFirma(String statoFirma) 
	{
		this.statoFirma = statoFirma;
	}

	public String getStatoInvioDoc() 
	{
		return statoInvioDoc;
	}

	public void setStatoInvioDoc(String statoInvioDoc) 
	{
		this.statoInvioDoc = statoInvioDoc;
	}

	public Blob getPdf() 
	{
		return pdf;
	}

	public void setPdf(Blob pdf) 
	{
		this.pdf = pdf;
	}

	public Timestamp getTimestampIns() 
	{
		return timestampIns;
	}

	public void setTimestampIns(Timestamp timestampIns) 
	{
		this.timestampIns = timestampIns;
	}

	public String getPgmIns() 
	{
		return pgmIns;
	}

	public void setPgmIns(String pgmIns) 
	{
		this.pgmIns = pgmIns;
	}

	public String getUserIns() 
	{
		return userIns;
	}

	public void setUserIns(String userIns) 
	{
		this.userIns = userIns;
	}

	public String getTipoAssegno() 
	{
		return tipoAssegno;
	}

	public void setTipoAssegno(String tipoAssegno) 
	{
		this.tipoAssegno = tipoAssegno;
	}

	public String getfConforme() 
	{
		return fConforme;
	}

	public void setfConforme(String fConforme) 
	{
		this.fConforme = fConforme;
	}

	public Timestamp getTimestampAggFir() 
	{
		return timestampAggFir;
	}

	public void setTimestampAggFir(Timestamp timestampAggFir) 
	{
		this.timestampAggFir = timestampAggFir;
	}

	public Timestamp getTimestampAggInv() 
	{
		return timestampAggInv;
	}

	public void setTimestampAggInv(Timestamp timestampAggInv) 
	{
		this.timestampAggInv = timestampAggInv;
	}

	public Blob getTerzaImmagine() 
	{
		return terzaImmagine;
	}

	public void setTerzaImmagine(Blob terzaImmagine) 
	{
		this.terzaImmagine = terzaImmagine;
	}

	public String getStatoInvioCons()
	{
		return statoInvioCons;
	}

	public void setStatoInvioCons(String statoInvioCons) 
	{
		this.statoInvioCons = statoInvioCons;
	}

}
