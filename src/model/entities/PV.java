package model.entities;

import java.io.Serializable;

public class PV extends Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private PaymentMethod payment = new PaymentMethod();
	
	public PV(Order order) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
	}
	
	public PV(Order order, String payment) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
	    this.payment.setPaymentMethod(payment, order.getOrderValue());	
	}

	public PaymentMethod getPayment() {
		return payment;
	}

	public void setPayment(PaymentMethod payment) {
		this.payment = payment;
	}

	@Override
	public String toString() {
		return payment.getPaymentMethod();
	}

}
