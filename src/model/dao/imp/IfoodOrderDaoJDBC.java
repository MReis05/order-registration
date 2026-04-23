package model.dao.imp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.Db;
import model.dao.IfoodOrderDao;
import model.entities.IfoodOrder;
import model.entities.enums.Category;
import model.entities.enums.PaymentMethod;
import model.exceptions.DbException;

public class IfoodOrderDaoJDBC implements IfoodOrderDao {

	private Connection conn;
	
	public IfoodOrderDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(IfoodOrder obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO ifoodOrder " + "(OrderValue, DeliveryValue, Category, PaymentMethod, Date, IfoodComission, IfoodPaymentValue, ServiceFee) "
							+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setBigDecimal(1, obj.getOrderValue());
			st.setBigDecimal(2, obj.getDeliveryValue());
			st.setString(3, obj.getCategory().name());
			st.setString(4, obj.getPaymentMethod().name());
			st.setObject(5, obj.getDate());
			st.setBigDecimal(6, obj.getIfoodComission());
			if (obj.getIfoodPaymentValue() == null) {
				obj.setIfoodPaymentValue(new BigDecimal("0.00"));
			}
			st.setBigDecimal(7, obj.getIfoodPaymentValue());
			st.setInt(8, obj.getServiceFee());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				Db.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error!! No rows affecteed");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void update(IfoodOrder obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE ifoodOrder "
					+ "SET OrderValue = ?, DeliveryValue = ?, Category = ?, PaymentMethod = ?, Date = ?, IfoodComission = ?, IfoodPaymentValue = ?, ServiceFee "
					+ "WHERE Id = ?");

			st.setBigDecimal(1, obj.getOrderValue());
			st.setBigDecimal(2, obj.getDeliveryValue());
			st.setString(3, obj.getCategory().name());
			st.setString(4, obj.getPaymentMethod().name());
			st.setObject(5, obj.getDate());
			st.setBigDecimal(6, obj.getIfoodComission());
			st.setBigDecimal(7, obj.getIfoodPaymentValue());
			st.setInt(8, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM ifoodOrder WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public List<IfoodOrder> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT Id, OrderValue, DeliveryValue, Category, PaymentMethod, Date, IfoodComission, IfoodPaymentValue, ServiceFee FROM ifood ORDER BY Id");

			rs = st.executeQuery();

			List<IfoodOrder> list = new ArrayList<>();
            
			while(rs.next()) {
				IfoodOrder obj = instantiateIfoodOrder(rs);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	private IfoodOrder instantiateIfoodOrder(ResultSet rs) throws SQLException {
		IfoodOrder obj = new IfoodOrder();
		obj.setOrderValue(rs.getBigDecimal("OrderValue"));
		obj.setDeliveryValue(rs.getBigDecimal("DeliveryValue"));
		obj.setPaymentMethods(PaymentMethod.valueOf(rs.getString("PaymentMethod")));
		obj.setDate(rs.getObject("Date", LocalDate.class));
		obj.setCategory(Category.valueOf(rs.getString("Category")));
		obj.setIfoodComission(rs.getBigDecimal("IfoodComission"));
		obj.setIfoodPaymentValue(rs.getBigDecimal("IfoodPaymentValue"));
		obj.setServiceFee(rs.getInt("ServiceFee"));
		return obj;
	}

	@Override
	public void resetAll() {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("TRUNCATE TABLE ifoodOrder");

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}	
	}
}
