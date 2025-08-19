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
import model.dao.PVDao;
import model.entities.Order;
import model.entities.PV;

public class PVDaoJDBC implements PVDao {

	private Connection conn;
	
	public PVDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(PV obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO pv " + "(OrderValue, DeliveryValue, PaymentType) "
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
	public void update(PV obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE pv "
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
			st = conn.prepareStatement("DELETE FROM pv WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
		
	}

	@Override
	public List<PV> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT Id, OrderValue, DeliveryValue, PaymentType FROM pv ORDER BY Id");

			rs = st.executeQuery();

			List<PV> list = new ArrayList<>();
            
			while(rs.next()) {
				PV obj = InstatiatePV(rs);
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
	
	private PV InstatiatePV(ResultSet rs) throws SQLException {
		PV obj = new PV(new Order(rs.getInt("Id"), rs.getDouble("OrderValue"), rs.getDouble("DeliveryValue")), rs.getString("PaymentType"));
		return obj;
	}

	@Override
	public void resetAll() {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("TRUNCATE TABLE pv");

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}	
	}

}
