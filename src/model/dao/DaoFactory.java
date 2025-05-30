package model.dao;

import db.Db;
import model.dao.imp.IfoodDaoJDBC;
import model.dao.imp.PVDaoJDBC;

public class DaoFactory {

	public static IfoodDao createIfoodDao() {
		return new IfoodDaoJDBC (Db.getConnection());
	}
	
	public static PVDao createPVDao() {
		return new PVDaoJDBC (Db.getConnection());
	}
}
