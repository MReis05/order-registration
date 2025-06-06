package model.entities;

import java.io.Serializable;

public class Ifood  extends Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Double forIfood;
	private Double tax;
	private Integer serviceTax;
	private String category;

	private PaymentMethod payment = new PaymentMethod();

	public Ifood(Order order) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
		if(order.getOrderValue() != null) {
			taxForIfood(order.getOrderValue());
		}
	}

	public Ifood(Order order, String payment) {
		super(order.getId(), order.getOrderValue(), order.getDeliveryValue());
		taxForStore(order.getOrderValue(), payment);
	}

	public Ifood(Order order, double cutValue, String payment) {
		super(order.getId(), order.getOrderValue() + cutValue, order.getDeliveryValue());
		cutPayments(order.getOrderValue(), cutValue, payment);
	}

	public Double getForIfood() {
		return forIfood;
	}

	public void setForIfood(Double forIfood) {
		this.forIfood = forIfood;
	}
	
	public void setTax (Double tax) {
		this.tax = tax;
	}

	public Double getTax() {
		return tax;
	}

	public Integer getServiceTax() {
		return serviceTax;
	}
	
	public void setServiceTax(Integer serviceTax) {
		this.serviceTax = serviceTax;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void taxForIfood(double value) {
		setForIfood(value);
		serviceTax = serviceTax(value);
		tax = value * 0.1679;
	}

	public void taxForStore(double value, String paymentMethod) {
		payment.setPaymentMethod(paymentMethod, value);
		serviceTax = serviceTax(value);
		tax = value * 0.12;
	}

	public int serviceTax(double value) {
		int taxOfService = 0;
		if (value <= 24.99) {
			taxOfService = 1;
		}
		return taxOfService;
	}

	public void cutPayments(double order, double cutValue, String paymentMethod) {
			payment.setPaymentMethod(paymentMethod, order);
		
		forIfood = cutValue;
		tax = (order + cutValue) * 0.12;
		
	}

	public PaymentMethod getPayment() {
		return payment;
	}

	@Override
	public String toString() {
		return "Ifood [forIfood=" + forIfood + ", tax=" + tax + ", serviceTax=" + serviceTax;
	}
}
