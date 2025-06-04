package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.IfoodDao;
import model.entities.Ifood;

public class IfoodService {

	private IfoodDao dao = DaoFactory.createIfoodDao();
	
	public List<Ifood> findAll (){
		return dao.findAll();
	}
	
	public void saveOrUpdate (Ifood obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void delete (Integer id) {
		dao.deleteById(id);
	}
}
