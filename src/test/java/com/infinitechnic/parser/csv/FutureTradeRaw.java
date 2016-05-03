package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.mapping.CsvCell;
import com.infinitechnic.parser.csv.mapping.CsvMapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@CsvMapping
public final class FutureTradeRaw {
	@CsvCell(name="code")
	private int code1;
	@CsvCell(format = "M/d/yyyy")
	private Date tradeDate;
	private String positionTypeCode;
	private BigInteger quantity;
	private int lotSize;
	@CsvCell(format = "yyyy MMMM")
	private Date contractDeliveryDate;
	private BigDecimal price;
	private String unitCode;
	private String currencyCode;
	private String traderCode;
	private String exchangeCode;
	private String productCode;
	@CsvCell(format = "M/d/yyyy")
	private Date settlementDate;
	private String companyCode;
	private String systemCode;
	private String recordStatusCode;

	public Integer getCode() {
		return code1;
	}

	public void setCode(Integer code) {
		this.code1 = code;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getPositionTypeCode() {
		return positionTypeCode;
	}

	public void setPositionTypeCode(String positionTypeCode) {
		this.positionTypeCode = positionTypeCode;
	}

	public BigInteger getQuantity() {
		return quantity;
	}

	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public Date getContractDeliveryDate() {
		return contractDeliveryDate;
	}

	public void setContractDeliveryDate(Date contractDeliveryDate) {
		this.contractDeliveryDate = contractDeliveryDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getTraderCode() {
		return traderCode;
	}

	public void setTraderCode(String traderCode) {
		this.traderCode = traderCode;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getRecordStatusCode() {
		return recordStatusCode;
	}

	public void setRecordStatusCode(String recordStatusCode) {
		this.recordStatusCode = recordStatusCode;
	}
}