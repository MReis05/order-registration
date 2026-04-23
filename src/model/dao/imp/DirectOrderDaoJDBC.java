package model.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.Db;
import model.dao.DirectOrderDao;
import model.entities.DirectOrder;
import model.entities.enums.PaymentMethod;
import model.exceptions.DbException;

public class DirectOrderDaoJDBC implements DirectOrderDao {

	private Connection conn;
	
	public DirectOrderDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(DirectOrder obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO directorder " + "(OrderValue, DeliveryValue, PaymentMethod, Date) "
							+ "VALUES " + "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setBigDecimal(1, obj.getOrderValue());
			st.setBigDecimal(2, obj.getDeliveryValue());
			st.setString(3, obj.getPaymentMethod().name());
			st.setObject(4, obj.getDate());

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
	public void update(DirectOrder obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE directorder "
					+ "SET OrderValue = ?, DeliveryValue = ?, PaymentMethod = ?, Date= ? "
					+ "WHERE Id = ?");

			st.setBigDecimal(1, obj.getOrderValue());
			st.setBigDecimal(2, obj.getDeliveryValue());
			st.setString(3, obj.getPaymentMethod().name());
			st.setObject(4, obj.getDate());
			st.setInt(5, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM directorder WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
		
	}

	@Override
	public List<DirectOrder> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT Id, OrderValue, DeliveryValue, PaymentMethod, Date FROM directorder ORDER BY Id");

			rs = st.executeQuery();

			List<DirectOrder> list = new ArrayList<>();
            
			while(rs.next()) {
				DirectOrder obj = instantiateDirectOrder(rs);
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
	
	private DirectOrder instantiateDirectOrder(ResultSet rs) throws SQLException {
		DirectOrder obj = new DirectOrder();
		obj.setId(rs.getInt("Id"));
		obj.setDate(rs.getObject("Date", LocalDate.class));
		obj.setDeliveryValue(rs.getBigDecimal("DeliveryValue"));
		obj.setOrderValue(rs.getBigDecimal("OrderValue"));
		obj.setPaymentMethods(PaymentMethod.valueOf(rs.getString("PaymentMethod")));
		return obj;
	}

	@Override
	public void resetAll() {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("TRUNCATE TABLE directorder");

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}	
	}

}
