package model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import model.entities.enums.PaymentMethod;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private BigDecimal orderValue;
	private BigDecimal deliveryValue;
	private PaymentMethod paymentMethod;
	private LocalDate date;

	public Order() {
	}

	public Order(BigDecimal orderValue, BigDecimal deliveryValue, PaymentMethod paymentMethod, LocalDate date) {
		this.orderValue = orderValue;
		this.deliveryValue = deliveryValue;
		this.paymentMethod = paymentMethod;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(BigDecimal orderValue) {
		this.orderValue = orderValue.setScale(2, RoundingMode.HALF_EVEN);
	}

	public BigDecimal getDeliveryValue() {
		return deliveryValue;
	}

	public void setDeliveryValue(BigDecimal deliveryValue) {
		this.deliveryValue = deliveryValue.setScale(2, RoundingMode.HALF_EVEN);
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethods(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}
}
