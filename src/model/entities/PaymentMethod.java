package model.entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PaymentMethod {

	private String paymentMethod;
	private Double paymentValue;
	
	public PaymentMethod() {
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}
	
	public StringProperty getPaymentMethodProperty() {
        return new SimpleStringProperty(paymentMethod);
    }

	public void setPaymentMethod(String paymentMethod, double value) {
		setPaymentValue(value);
		this.paymentMethod = paymentMethod;
	}

	public Double getPaymentValue() {
		return paymentValue;
	}
	
	public ObjectProperty<Double> getPaymentValueProperty() {
        return new SimpleDoubleProperty(paymentValue != null ? paymentValue : 0.0).asObject(); 
    }

	public void setPaymentValue(Double paymentValue) {
		this.paymentValue = paymentValue;
	}

}
