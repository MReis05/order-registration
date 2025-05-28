package model.entities;

public class PV extends Order {

	private PaymentMethod payment = new PaymentMethod();
	
	public PV(Order order, String payment) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
	    getPayment().setPaymentMethod(payment, order.getOrderValue());	
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
