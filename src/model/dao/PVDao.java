package model.dao;

import java.util.List;

import model.entities.PV;

public interface PVDao {

	void insert (PV obj);
	void update (PV obj);
	void deleteById (Integer id);
	List<PV> findAll ();
}
