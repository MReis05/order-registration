package model.dao;

import db.Db;
import model.dao.imp.DirectOrderDaoJDBC;
import model.dao.imp.IfoodOrderDaoJDBC;

public class DaoFactory {

	public static IfoodOrderDao createIfoodOrderDao() {
		return new IfoodOrderDaoJDBC (Db.getConnection());
	}
	
	public static DirectOrderDao createDirectOrderDao() {
		return new DirectOrderDaoJDBC (Db.getConnection());
	}
}
