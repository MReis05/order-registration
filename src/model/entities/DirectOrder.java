package model.entities;

import java.io.Serializable;

public class DirectOrder extends Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public DirectOrder() {
	}
	
	public DirectOrder(Order order) {
		super(order.getOrderValue(), order.getDeliveryValue(), order.getPaymentMethod(), order.getType(), order.getDate());
	}

}
