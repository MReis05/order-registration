package model.services;

import java.util.List;

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
}
