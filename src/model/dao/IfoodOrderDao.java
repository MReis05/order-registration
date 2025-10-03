package model.dao;

import java.util.List;

import model.entities.IfoodOrder;

public interface IfoodOrderDao {

	void insert (IfoodOrder obj);
	void update (IfoodOrder obj);
	void deleteById (Integer id);
	void resetAll();
	List<IfoodOrder> findAll ();
}
