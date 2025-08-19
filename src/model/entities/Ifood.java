package model.entities;

import java.io.Serializable;

public class Ifood  extends Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Double forIfood;
	private Double fee;
	private Integer serviceFee;
	private String category;

	private PaymentMethod payment = new PaymentMethod();

	public Ifood(Order order) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
		if(order.getOrderValue() != null) {
			feeForIfood(order.getOrderValue());
		}
	}

	public Ifood(Order order, String payment) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
		feeForStore(order.getOrderValue(), payment);
	}

	public Ifood(Order order, Double cutValue, String payment) {
		super(order.getId(), order.getOrderValue() + cutValue, order.getDeliveryValue());
		cutPayments(order.getOrderValue(), cutValue, payment);
	}

	public Double getForIfood() {
		return forIfood;
	}

	public void setForIfood(Double forIfood) {
		this.forIfood = forIfood;
	}
	
	public void setFee (Double fee) {
		this.fee = fee;
	}

	public Double getFee() {
		return fee;
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
		setForIfood(value);
		serviceFee = 1;
		fee = value * 0.1679;
	}

	public void feeForStore(Double value, String paymentMethod) {
		payment.setPaymentMethod(paymentMethod, value);
		serviceFee = 1;
		fee = value * 0.12;
	}


	public void cutPayments(Double order, Double cutValue, String paymentMethod) {
			payment.setPaymentMethod(paymentMethod, order);
		
		forIfood = cutValue;
		fee = (order + cutValue) * 0.12;
		
	}

	public PaymentMethod getPayment() {
		return payment;
	}

	@Override
	public String toString() {
		return "Ifood [forIfood=" + forIfood + ", fee=" + fee + ", serviceFee=" + serviceFee;
	}
}
