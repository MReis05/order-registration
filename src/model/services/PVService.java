package model.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.dao.DaoFactory;
import model.dao.PVDao;
import model.entities.PV;

public class PVService {

	private PVDao dao = DaoFactory.createPVDao();
	
	public List<PV> findAll (){
		return dao.findAll();
	}
	
	public void saveOrUpdate (PV obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void delete (PV obj) {
		dao.deleteById(obj.getId());
	}
	
	public void resetAll() {
		dao.resetAll();
	}
	
	public Map<String, Double> total(){
		List<PV> list = dao.findAll();
		Map<String, Double> map = new LinkedHashMap<>();
		double totalValue = 0.00;
		double pvTotal = 0.00;
		double deliveryTotal = 0.00;
		double cashTotal = 0.00;
		double cardTotal = 0.00;
		double pixTotal = 0.00;
		
		for(PV pv : list) {
			totalValue += pv.getOrderValue();
			pvTotal += pv.getOrderValue();
			deliveryTotal += pv.getDeliveryValue();
			switch(pv.getPayment().getPaymentMethod()) {
			case "Dinheiro":
				cashTotal += pv.getPayment().getPaymentValue();
				break;
			case "Cartão":
				cardTotal += pv.getPayment().getPaymentValue();
				break;
			case "Pix":
				pixTotal += pv.getPayment().getPaymentValue();
			}
		}
		map.put("totalValue", totalValue);
		map.put("pvTotal", pvTotal);
		map.put("deliveryTotal", deliveryTotal);
		map.put("cashTotal", cashTotal);
		map.put("cardTotal", cardTotal);
		map.put("pixTotal", pixTotal);
		return map;
	}
}
