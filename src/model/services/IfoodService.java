package model.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.dao.DaoFactory;
import model.dao.IfoodDao;
import model.entities.Ifood;

public class IfoodService {

	private IfoodDao dao = DaoFactory.createIfoodDao();
	
	public List<Ifood> findAll (){
		return dao.findAll();
	}
	
	public void saveOrUpdate (Ifood obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void delete (Ifood obj) {
		dao.deleteById(obj.getId());
	}
	
	public void resetAll() {
		dao.resetAll();
	}
	
	public Map<String, Double> total(){
		List<Ifood> list = dao.findAll();
		Map<String, Double> map = new LinkedHashMap<>();
		double totalValue = 0.00;
		double ifoodTotal = 0.00;
		double deliveryTotal = 0.00;
		double forIfoodTotal = 0.00;
		double feeTotal = 0.00;
		double serviceFeeP = 0.00;
		double serviceFeeT = 0.00;
		double cashTotal = 0.00;
		double cardTotal = 0.00;
		double pixTotal = 0.00;
		
		for(Ifood ifood : list) {
			totalValue += ifood.getOrderValue();
			ifoodTotal += ifood.getOrderValue();
			deliveryTotal += ifood.getDeliveryValue();
			forIfoodTotal += ifood.getForIfood();
			feeTotal += ifood.getFee();
			if (ifood.getPayment().getPaymentValue() > 0) {
				serviceFeeP += ifood.getServiceFee();
				serviceFeeT += ifood.getServiceFee();
				if (ifood.getServiceFee() > 0) {
					feeTotal -= 0.11;
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
		map.put("forIfoodTotal", forIfoodTotal);
		map.put("feeTotal", feeTotal);
		map.put("serviceFeePTotal", serviceFeeP);
		map.put("serviceFeeTTotal", serviceFeeT);
		map.put("cashTotal", cashTotal);
		map.put("cardTotal", cardTotal);
		map.put("pixTotal", pixTotal);
		return map;
	}
}
