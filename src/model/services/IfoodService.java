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
	
	public void delete (Integer id) {
		dao.deleteById(id);
	}
	
	public Map<String, Double> total(){
		List<Ifood> list = dao.findAll();
		Map<String, Double> map = new LinkedHashMap<>();
		double totalValue = 0.00;
		double deliveryTotal = 0.00;
		double forIfoodTotal = 0.00;
		double taxTotal = 0.00;
		double taxOfServiceP = 0.00;
		double taxOfServiceT = 0.00;
		double cashTotal = 0.00;
		double cardTotal = 0.00;
		double pixTotal = 0.00;
		
		for(Ifood ifood : list) {
			totalValue += ifood.getOrderValue();
			deliveryTotal += ifood.getDeliveryValue();
			forIfoodTotal += ifood.getForIfood();
			taxTotal += ifood.getTax();
			if (ifood.getPayment().getPaymentValue() > 0) {
				taxTotal -= 0.11;
				taxOfServiceP += ifood.getServiceTax();
				taxOfServiceT += ifood.getServiceTax();
			}
			else {
				taxOfServiceT += ifood.getServiceTax();
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
		map.put("deliveryTotal", deliveryTotal);
		map.put("forIfoodTotal", forIfoodTotal);
		map.put("taxTotal", taxTotal);
		map.put("taxOfServicePTotal", taxOfServiceP);
		map.put("taxOfServiceTTotal", taxOfServiceT);
		map.put("cashTotal", cashTotal);
		map.put("cardTotal", cardTotal);
		map.put("pixTotal", pixTotal);
		return map;
	}
}
