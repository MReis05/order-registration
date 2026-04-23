package model.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import model.entities.enums.Category;

public class IfoodOrder  extends Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal ifoodPaymentValue;
	private BigDecimal ifoodComission;
	private Integer serviceFee;
	private Category category;
	
	public IfoodOrder() {
	}
	
	public IfoodOrder(Order order) {
		super(order.getOrderValue(), order.getDeliveryValue(), order.getPaymentMethod(), order.getDate());
	}

	public BigDecimal getIfoodPaymentValue() {
		return ifoodPaymentValue;
	}

	public void setIfoodPaymentValue(BigDecimal ifoodPaymentValue) {
		this.ifoodPaymentValue = ifoodPaymentValue;
	}
	
	public void setIfoodComission (BigDecimal ifoodComission) {
		this.ifoodComission = ifoodComission;
	}

	public BigDecimal getIfoodComission() {
		return ifoodComission;
	}

	public Integer getServiceFee() {
		return serviceFee;
	}
	
	public void setServiceFee(Integer serviceFee) {
		this.serviceFee = serviceFee;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void feeForIfood(BigDecimal value) {
		serviceFee = 1;
		ifoodComission = value.multiply(new BigDecimal("0.1720"));
	}

	public void feeForStore(BigDecimal value) {
		serviceFee = 1;
		ifoodComission = value.multiply(new BigDecimal("0.12"));
	}


	public void cutPayments(BigDecimal order, BigDecimal cutValue) {
		this.setOrderValue(order.add(cutValue));
		ifoodPaymentValue = cutValue;
		ifoodComission = order.add(cutValue).multiply(new BigDecimal("0.12"));
	}
}
