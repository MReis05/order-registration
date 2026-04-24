package model.dao;

import java.util.List;

import model.entities.Order;

public interface OrderDao {

	void insert (Order obj);
	void update (Order obj);
	void deleteById (Long id);
	List<Order> findAll();
	List<Order> findByType(String type);
}
