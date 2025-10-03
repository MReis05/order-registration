package model.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.dao.DaoFactory;
import model.dao.IfoodOrderDao;
import model.entities.IfoodOrder;

public class IfoodOrderService {

	private IfoodOrderDao dao = DaoFactory.createIfoodOrderDao();
	
	public List<IfoodOrder> findAll (){
		return dao.findAll();
	}
	
	public void saveOrUpdate (IfoodOrder obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void delete (IfoodOrder obj) {
		dao.deleteById(obj.getId());
	}
	
	public void resetAll() {
		dao.resetAll();
	}
	
	public Map<String, Double> total(){
		List<IfoodOrder> list = dao.findAll();
		Map<String, Double> map = new LinkedHashMap<>();
		double totalValue = 0.00;
		double ifoodTotal = 0.00;
		double deliveryTotal = 0.00;
		double forIfoodTotal = 0.00;
		double ifoodComissionTotal = 0.00;
		double serviceFeeP = 0.00;
		double serviceFeeT = 0.00;
		double cashTotal = 0.00;
		double cardTotal = 0.00;
		double pixTotal = 0.00;
		
		for(IfoodOrder ifood : list) {
			totalValue += ifood.getOrderValue();
			ifoodTotal += ifood.getOrderValue();
			deliveryTotal += ifood.getDeliveryValue();
			forIfoodTotal += ifood.getIfoodPaymentValue();
			ifoodComissionTotal += ifood.getIfoodComission();
			if (ifood.getPayment().getPaymentValue() > 0) {
				serviceFeeP += ifood.getServiceFee();
				serviceFeeT += ifood.getServiceFee();
				if (ifood.getServiceFee() > 0) {
					ifoodComissionTotal -= 0.11;
				}
			}
			else {
				serviceFeeT += ifood.getServiceFee();
			}
			switch(ifood.getPayment().getPaymentMethod()) {
			case "Dinheiro":
				cashTotal += ifood.getPayment().getPaymentValue();
				break;
			case "Cartão":
				cardTotal += ifood.getPayment().getPaymentValue();
				break;
			case "Pix":
				pixTotal += ifood.getPayment().getPaymentValue();
			}
		}
		map.put("totalValue", totalValue);
		map.put("ifoodTotal", ifoodTotal);
		map.put("deliveryTotal", deliveryTotal);
		map.put("ifoodPaymentValueTotal", forIfoodTotal);
		map.put("ifoodComissionTotal", ifoodComissionTotal);
		map.put("serviceFeePTotal", serviceFeeP);
		map.put("serviceFeeTTotal", serviceFeeT);
		map.put("cashTotal", cashTotal);
		map.put("cardTotal", cardTotal);
		map.put("pixTotal", pixTotal);
		return map;
	}
}
