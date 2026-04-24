package model.dao;

import db.Db;
import model.dao.imp.OrderDaoJDBC;

public class DaoFactory {

	public static OrderDao createOrderDao() {
		return new OrderDaoJDBC (Db.getConnection());
	}
}
