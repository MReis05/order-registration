package model.entities;

import java.io.Serializable;

public class IfoodOrder  extends Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Double ifoodPaymentValue;
	private Double ifoodComission;
	private Integer serviceFee;
	private String category;

	private PaymentMethod payment = new PaymentMethod();

	public IfoodOrder(Order order) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
		if(order.getOrderValue() != null) {
			feeForIfood(order.getOrderValue());
		}
	}

	public IfoodOrder(Order order, String payment) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
		feeForStore(order.getOrderValue(), payment);
	}

	public IfoodOrder(Order order, Double cutValue, String payment) {
		super(order.getId(), order.getOrderValue() + cutValue, order.getDeliveryValue());
		cutPayments(order.getOrderValue(), cutValue, payment);
	}

	public Double getIfoodPaymentValue() {
		return ifoodPaymentValue;
	}

	public void setIfoodPaymentValue(Double ifoodPaymentValue) {
		this.ifoodPaymentValue = ifoodPaymentValue;
	}
	
	public void setIfoodComission (Double ifoodComission) {
		this.ifoodComission = ifoodComission;
	}

	public Double getIfoodComission() {
		return ifoodComission;
	}

	public Integer getServiceFee() {
		return serviceFee;
	}
	
	public void setServiceFee(Integer serviceFee) {
		this.serviceFee = serviceFee;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void feeForIfood(Double value) {
		setIfoodPaymentValue(value);
		serviceFee = 1;
		ifoodComission = value * 0.171875;
	}

	public void feeForStore(Double value, String paymentMethod) {
		payment.setPaymentMethod(paymentMethod, value);
		serviceFee = 1;
		ifoodComission = value * 0.12;
	}


	public void cutPayments(Double order, Double cutValue, String paymentMethod) {
			payment.setPaymentMethod(paymentMethod, order);
		
		ifoodPaymentValue = cutValue;
		ifoodComission = (order + cutValue) * 0.12;
		
	}

	public PaymentMethod getPayment() {
		return payment;
	}
}
