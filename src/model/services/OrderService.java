package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.OrderDao;
import model.entities.Order;

public class OrderService {

	private OrderDao dao = DaoFactory.createOrderDao();
	
	public List<Order> findAll (){
		return dao.findAll();
	}
	
	public List<Order> findByType(String type){
		return dao.findByType(type);
	}
	
	public void saveOrUpdate (Order obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void delete (Order obj) {
		dao.deleteById(obj.getId());
	}
}
