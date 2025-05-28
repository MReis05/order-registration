package model.entities;

public class Order {

	private Integer id;
	private Double orderValue;
	private Double deliveryValue;

	public Order(Integer id, Double orderValue, Double deliveryValue) {
		this.id = id;
		this.orderValue = orderValue;
		this.deliveryValue = deliveryValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(Double orderValue) {
		this.orderValue = orderValue;
	}

	public Double getDeliveryValue() {
		return deliveryValue;
	}

	public void setDeliveryValue(Double deliveryValue) {
		this.deliveryValue = deliveryValue;
	}

}
