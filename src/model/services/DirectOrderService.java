package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DirectOrderDao;
import model.entities.DirectOrder;

public class DirectOrderService {

	private DirectOrderDao dao = DaoFactory.createDirectOrderDao();
	
	public List<DirectOrder> findAll (){
		return dao.findAll();
	}
	
	public void saveOrUpdate (DirectOrder obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void delete (DirectOrder obj) {
		dao.deleteById(obj.getId());
	}
	
	public void resetAll() {
		dao.resetAll();
	}
}
