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
import model.dao.IfoodDao;
import model.entities.Ifood;
import model.entities.Order;

public class IfoodDaoJDBC implements IfoodDao {

	private Connection conn;
	
	public IfoodDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Ifood obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO ifood " + "(OrderValue, DeliveryValue, Tax, ForIfood, PaymentType, PaymentValue, category) "
							+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setDouble(1, obj.getOrderValue());
			st.setDouble(2, obj.getDeliveryValue());
			st.setDouble(3, obj.getTax());
			if (obj.getForIfood() == null) {
				obj.setForIfood(0.00);
			}
			st.setDouble(4, obj.getForIfood());
			if (obj.getPayment().getPaymentValue() == null) {
				obj.getPayment().setPaymentMethod("Ifood", 0.00);
                st.setString(5, obj.getPayment().getPaymentMethod());
                st.setDouble(6, obj.getPayment().getPaymentValue());
				st.setString(7, "Ifood");
			}
			else {
				st.setString(5, obj.getPayment().getPaymentMethod());
				st.setDouble(6, obj.getPayment().getPaymentValue());
				st.setString(7, "Loja");
			}
			

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
	public void update(Ifood obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE ifood "
					+ "SET OrderValue = ?, DeliveryValue = ?, Tax = ?, ForIfood = ?, PaymentType = ?, PaymentValue = ?, category = ? "
					+ "WHERE Id = ?");

			st.setDouble(1, obj.getOrderValue());
			st.setDouble(2, obj.getDeliveryValue());
			st.setDouble(3, obj.getTax());
			st.setDouble(4, obj.getForIfood());
			st.setString(5, obj.getPayment().getPaymentMethod());
			st.setDouble(6, obj.getPayment().getPaymentValue());
			st.setString(7, obj.getCategory());
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
			st = conn.prepareStatement("DELETE FROM ifood WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public List<Ifood> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT Id, OrderValue, DeliveryValue, Tax, ForIfood, PaymentType, PaymentValue, category FROM ifood ORDER BY Id");

			rs = st.executeQuery();

			List<Ifood> list = new ArrayList<>();
            
			while(rs.next()) {
				Ifood obj = InstatiateIfood(rs);
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

	private Ifood InstatiateIfood(ResultSet rs) throws SQLException {
		Ifood obj = new Ifood(new Order(rs.getInt("Id"), rs.getDouble("OrderValue"), rs.getDouble("DeliveryValue")));
		obj.setTax(rs.getDouble("Tax"));
		obj.setForIfood(rs.getDouble("ForIfood"));
		obj.getPayment().setPaymentMethod(rs.getString("PaymentType"), rs.getDouble("PaymentValue"));
		obj.setCategory(rs.getString("category"));
		return obj;
	}

	@Override
	public void resetAll() {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("TRUNCATE TABLE ifood");

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}	
	}
}
