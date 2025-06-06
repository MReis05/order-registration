package model.dao;

import java.util.List;

import model.entities.Ifood;

public interface IfoodDao {

	void insert (Ifood obj);
	void update (Ifood obj);
	void deleteById (Integer id);
	void resetAll();
	List<Ifood> findAll ();
}
