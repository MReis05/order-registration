package model.services;

import java.time.LocalDate;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.OrderDao;
import model.entities.Order;
import model.entities.DTO.OrderDTO;
import model.entities.enums.Type;

public class OrderService {

	private OrderDao dao = DaoFactory.createOrderDao();
	
	public List<Order> findAll (){
		return dao.findAll();
	}
	
	public List<Order> findByTypeAndDate(Type type, LocalDate date){
		return dao.findByTypeAndDate(type, date);
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
	
	public OrderDTO getTotalsByDate(LocalDate date, LocalDate finalDate) {
		return dao.getTotalsByDate(date, finalDate);
	}
}
