package model.entities;

public class PaymentMethod {

	private String paymentMethod;
	private Double paymentValue;
	
	public PaymentMethod() {
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod, double value) {
		setPaymentValue(value);
		this.paymentMethod = paymentMethod;
	}

	public Double getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(Double paymentValue) {
		this.paymentValue = paymentValue;
	}

}
