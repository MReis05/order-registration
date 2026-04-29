package model.entities.DTO;

import java.math.BigDecimal;

public class OrderDTO {

	private BigDecimal totalValue;
	private BigDecimal ifoodOrderTotalValue;
	private BigDecimal directOrderTotalValue;
	private BigDecimal deliveryValue;
	private BigDecimal ifoodPaymentValue;
	private BigDecimal ifoodComission;
	private BigDecimal cashTotalValue;
	private BigDecimal cardTotalValue;
	private BigDecimal pixTotalValue;
	private Integer serviceFee;
	
	
	public OrderDTO() {
	}

	public OrderDTO(BigDecimal totalValue, BigDecimal ifoodOrderTotalValue, BigDecimal directOrderTotalValue,
			BigDecimal deliveryValue, BigDecimal ifoodPaymentValue, BigDecimal ifoodComission,
			BigDecimal cashTotalValue, BigDecimal cardTotalValue, BigDecimal pixTotalValue, Integer serviceFee) {
		super();
		this.totalValue = totalValue != null ? totalValue : BigDecimal.ZERO;
		this.deliveryValue = deliveryValue != null ? deliveryValue : BigDecimal.ZERO;
		this.ifoodPaymentValue = ifoodPaymentValue != null ? ifoodPaymentValue : BigDecimal.ZERO;
		this.ifoodComission = ifoodComission != null ? ifoodComission : BigDecimal.ZERO;
		this.serviceFee = serviceFee != null ? serviceFee : 0;
		this.cashTotalValue = cashTotalValue != null ? cashTotalValue : BigDecimal.ZERO;
		this.cardTotalValue = cardTotalValue != null ? cardTotalValue : BigDecimal.ZERO;
		this.pixTotalValue = pixTotalValue != null ? pixTotalValue : BigDecimal.ZERO;
		this.ifoodOrderTotalValue = ifoodOrderTotalValue != null ? ifoodOrderTotalValue : BigDecimal.ZERO;
		this.directOrderTotalValue = directOrderTotalValue != null ? directOrderTotalValue : BigDecimal.ZERO;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}


	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}


	public BigDecimal getIfoodOrderTotalValue() {
		return ifoodOrderTotalValue;
	}


	public void setIfoodOrderTotalValue(BigDecimal ifoodOrderTotalValue) {
		this.ifoodOrderTotalValue = ifoodOrderTotalValue;
	}


	public BigDecimal getDirectOrderTotalValue() {
		return directOrderTotalValue;
	}


	public void setDirectOrderTotalValue(BigDecimal directOrderTotalValue) {
		this.directOrderTotalValue = directOrderTotalValue;
	}


	public BigDecimal getDeliveryValue() {
		return deliveryValue;
	}


	public void setDeliveryValue(BigDecimal deliveryValue) {
		this.deliveryValue = deliveryValue;
	}


	public BigDecimal getIfoodPaymentValue() {
		return ifoodPaymentValue;
	}


	public void setIfoodPaymentValue(BigDecimal ifoodPaymentValue) {
		this.ifoodPaymentValue = ifoodPaymentValue;
	}


	public BigDecimal getIfoodComission() {
		return ifoodComission;
	}


	public void setIfoodComission(BigDecimal ifoodComission) {
		this.ifoodComission = ifoodComission;
	}


	public BigDecimal getCashTotalValue() {
		return cashTotalValue;
	}


	public void setCashTotalValue(BigDecimal cashTotalValue) {
		this.cashTotalValue = cashTotalValue;
	}


	public BigDecimal getCardTotalValue() {
		return cardTotalValue;
	}


	public void setCardTotalValue(BigDecimal cardTotalValue) {
		this.cardTotalValue = cardTotalValue;
	}


	public BigDecimal getPixTotalValue() {
		return pixTotalValue;
	}


	public void setPixTotalValue(BigDecimal pixTotalValue) {
		this.pixTotalValue = pixTotalValue;
	}


	public Integer getServiceFee() {
		return serviceFee;
	}


	public void setServiceFee(Integer serviceFee) {
		this.serviceFee = serviceFee;
	}
}
