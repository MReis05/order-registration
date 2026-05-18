package model.dao;

import java.util.List;

import model.entities.DirectOrder;

public interface DirectOrderDao {

	void insert (DirectOrder obj);
	void update (DirectOrder obj);
	void deleteById (Integer id);
	void resetAll();
	List<DirectOrder> findAll ();
}
