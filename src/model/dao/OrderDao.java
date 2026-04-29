package model.dao;

import java.time.LocalDate;
import java.util.List;

import model.entities.Order;
import model.entities.DTO.OrderDTO;
import model.entities.enums.Type;

public interface OrderDao {

	void insert (Order obj);
	void update (Order obj);
	void deleteById (Long id);
	List<Order> findAll();
	List<Order> findByTypeAndDate(Type type, LocalDate date);
	OrderDTO getTotalsByDate(LocalDate date, LocalDate finalDate);
}
