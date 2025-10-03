package model.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.Db;
import db.DbException;
import model.dao.DirectOrderDao;
import model.entities.Order;
import model.entities.DirectOrder;

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
					"INSERT INTO directorder " + "(OrderValue, DeliveryValue, PaymentType) "
							+ "VALUES " + "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setDouble(1, obj.getOrderValue());
			st.setDouble(2, obj.getDeliveryValue());
			st.setString(3, obj.getPayment().getPaymentMethod());

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
					+ "SET OrderValue = ?, DeliveryValue = ?, PaymentType = ? "
					+ "WHERE Id = ?");

			st.setDouble(1, obj.getOrderValue());
			st.setDouble(2, obj.getDeliveryValue());
			st.setString(3, obj.getPayment().getPaymentMethod());
			st.setInt(4, obj.getId());

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
			st = conn.prepareStatement("SELECT Id, OrderValue, DeliveryValue, PaymentType FROM directorder ORDER BY Id");

			rs = st.executeQuery();

			List<DirectOrder> list = new ArrayList<>();
            
			while(rs.next()) {
				DirectOrder obj = InstatiateDirectOrder(rs);
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
	
	private DirectOrder InstatiateDirectOrder(ResultSet rs) throws SQLException {
		DirectOrder obj = new DirectOrder(new Order(rs.getInt("Id"), rs.getDouble("OrderValue"), rs.getDouble("DeliveryValue")), rs.getString("PaymentType"));
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
