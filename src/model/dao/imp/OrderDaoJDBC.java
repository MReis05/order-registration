package model.dao.imp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.Db;
import model.dao.OrderDao;
import model.entities.DirectOrder;
import model.entities.IfoodOrder;
import model.entities.Order;
import model.entities.DTO.OrderDTO;
import model.entities.enums.Category;
import model.entities.enums.PaymentMethod;
import model.entities.enums.Type;
import model.exceptions.DbException;

public class OrderDaoJDBC implements OrderDao {

	private Connection conn;
	
	public OrderDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Order obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
	                "INSERT INTO orders " + 
	                "(order_type, order_value, delivery_value, payment_method, order_date, " +
	                "category, ifood_comission, ifood_payment_value, service_fee, ifood_direct_payment_value) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
	                Statement.RETURN_GENERATED_KEYS);
			
			
			st.setBigDecimal(2, obj.getOrderValue());
	        st.setBigDecimal(3, obj.getDeliveryValue());
	        st.setString(4, obj.getPaymentMethod().name());
	        st.setObject(5, obj.getDate());
	        
	        if(obj instanceof IfoodOrder) {
	        	IfoodOrder ifood = (IfoodOrder) obj;
	        	st.setString(1, obj.getType().name());
	        	st.setString(6, ifood.getCategory().name());
	            st.setBigDecimal(7, ifood.getIfoodComission());
	            st.setBigDecimal(8, ifood.getIfoodPaymentValue() != null ? ifood.getIfoodPaymentValue() : BigDecimal.ZERO);
	            st.setBigDecimal(9, ifood.getServiceFee() != null ? ifood.getServiceFee() : BigDecimal.ZERO);
	            st.setBigDecimal(10, ifood.getIfoodDirectPaymentValue() != null ? ifood.getIfoodDirectPaymentValue() : BigDecimal.ZERO);
	        }
	        else {
	        	st.setString(1, obj.getType().name());
	        	st.setNull(6, Types.VARCHAR);
	            st.setNull(7, Types.DECIMAL);
	            st.setNull(8, Types.DECIMAL);
	            st.setNull(9, Types.INTEGER);
	            st.setBigDecimal(10, BigDecimal.ZERO);
	        }

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
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
	public void update(Order obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE orders SET " + 
	                "order_value = ?, delivery_value = ?, payment_method = ?, order_date = ?, " +
	                "category = ?, ifood_comission = ?, ifood_payment_value = ?, service_fee = ?, ifood_direct_payment_value = ? " +
	                "WHERE id = ?");

	        st.setBigDecimal(1, obj.getOrderValue());
	        st.setBigDecimal(2, obj.getDeliveryValue());
	        st.setString(3, obj.getPaymentMethod().name());
	        st.setObject(4, obj.getDate());

	        if(obj instanceof IfoodOrder) {
	            IfoodOrder ifood = (IfoodOrder) obj;
	            st.setString(5, ifood.getCategory().name());
	            st.setBigDecimal(6, ifood.getIfoodComission());
	            st.setBigDecimal(7, ifood.getIfoodPaymentValue());
	            st.setBigDecimal(8, ifood.getServiceFee());
	            st.setBigDecimal(9, ifood.getIfoodDirectPaymentValue());
	        } else {
	        	st.setNull(5, Types.VARCHAR);
	            st.setNull(6, Types.DECIMAL);
	            st.setNull(7, Types.DECIMAL);
	            st.setNull(8, Types.INTEGER);
	            st.setBigDecimal(9, BigDecimal.ZERO);
	        }
	        
	        st.setLong(10, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Long id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM orders WHERE Id = ?");

			st.setLong(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public List<Order> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM orders ORDER BY Id");
			
	        rs = st.executeQuery();

	        List<Order> list = new ArrayList<>();
	        
	        while(rs.next()) {
	            String type = rs.getString("order_type");
	            Order obj;

	            if ("IFOOD".equals(type)) {
	                obj = instantiateIfoodOrder(rs);
	            } else {
	                obj = instantiateDirectOrder(rs);
	            }
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

	@Override
	public List<Order> findByTypeAndDate(Type type, LocalDate date) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM orders WHERE order_type = ? AND order_date = ? ORDER BY Id");
			
			st.setString(1, type.name());
			st.setObject(2, date);
			
	        rs = st.executeQuery();

	        List<Order> list = new ArrayList<>();
	        
	        while(rs.next()) {
	            Order obj = new Order();

	            if (type == Type.VIA_IFOOD) {
	                obj = instantiateIfoodOrder(rs);
	            } else if (type == Type.VIA_PEDIDO_DIRETO) {
	                obj = instantiateDirectOrder(rs);
	            }
	            
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
	
	@Override
	public OrderDTO getTotalsByDate(LocalDate date, LocalDate finalDate) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT SUM(o.order_value) AS total_orders, "
			           + "SUM(o.delivery_value) AS total_deliveries, "
			           + "SUM(o.ifood_payment_value) AS total_ifood_payments, "
			           + "SUM(o.ifood_comission) AS total_comissions, "
			           + "SUM(o.service_fee) AS total_fees, "
			           + "SUM(CASE WHEN o.order_type = 'VIA_IFOOD' THEN o.order_value ELSE 0 END) AS total_ifood, "
			           + "SUM(CASE WHEN o.order_type = 'VIA_PEDIDO_DIRETO' THEN o.order_value ELSE 0 END) AS total_direct, "
			           + "SUM(CASE WHEN payment_method = 'DINHEIRO' THEN (CASE WHEN o.order_type = 'VIA_PEDIDO_DIRETO' THEN o.order_value ELSE o.ifood_direct_payment_value END) ELSE 0 END) AS total_cash, "
			           + "SUM(CASE WHEN payment_method = 'CARTÃO' THEN (CASE WHEN o.order_type = 'VIA_PEDIDO_DIRETO' THEN o.order_value ELSE o.ifood_direct_payment_value END) ELSE 0 END) AS total_card, "
			           + "SUM(CASE WHEN payment_method = 'PIX' THEN (CASE WHEN o.order_type = 'VIA_PEDIDO_DIRETO' THEN o.order_value ELSE o.ifood_direct_payment_value END) ELSE 0 END) AS total_pix "
			           + "FROM orders o "
			           + "WHERE order_date BETWEEN ? AND ?");
			
			st.setObject(1, date);
			st.setObject(2, finalDate);
			
	        rs = st.executeQuery();
	        
	        
	        if(rs.next()) {
	        	OrderDTO obj = new OrderDTO(rs.getBigDecimal("total_orders"), rs.getBigDecimal("total_ifood"), rs.getBigDecimal("total_direct"),
	        			rs.getBigDecimal("total_deliveries"), rs.getBigDecimal("total_ifood_payments"), rs.getBigDecimal("total_comissions"),
	        			rs.getBigDecimal("total_cash"), rs.getBigDecimal("total_card"), rs.getBigDecimal("total_pix"), rs.getBigDecimal("total_fees"));
	        	 return obj;
	        }
	        else {
	        	return null;
	        }
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}
	
	private IfoodOrder instantiateIfoodOrder(ResultSet rs) throws SQLException {
		IfoodOrder obj = new IfoodOrder();
		obj.setId(rs.getLong("id"));
		obj.setOrderValue(rs.getBigDecimal("order_value"));
		obj.setDeliveryValue(rs.getBigDecimal("delivery_value"));
		obj.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
		obj.setDate(rs.getObject("order_date", LocalDate.class));
		obj.setCategory(Category.valueOf(rs.getString("category")));
		obj.setIfoodComission(rs.getBigDecimal("ifood_Comission"));
		obj.setIfoodPaymentValue(rs.getBigDecimal("ifood_payment_value"));
		obj.setIfoodDirectPaymentValue(rs.getBigDecimal("ifood_direct_payment_value"));
		obj.setServiceFee(rs.getBigDecimal("service_fee"));
		obj.setType(Type.valueOf(rs.getString("order_type")));
		return obj;
	}
	
	private DirectOrder instantiateDirectOrder(ResultSet rs) throws SQLException {
		DirectOrder obj = new DirectOrder();
		obj.setId(rs.getLong("id"));
		obj.setDate(rs.getObject("order_date", LocalDate.class));
		obj.setDeliveryValue(rs.getBigDecimal("delivery_value"));
		obj.setOrderValue(rs.getBigDecimal("order_value"));
		obj.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
		obj.setType(Type.valueOf(rs.getString("order_type")));
		return obj;
	}
}
