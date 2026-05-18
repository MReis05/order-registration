CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_type VARCHAR(20) NOT NULL, 
    order_value DECIMAL(10, 2), 
    delivery_value DECIMAL(10, 2),
    payment_method VARCHAR(50),
    order_date DATE,

    ifood_payment_value DECIMAL(10, 2),
    ifood_comission DECIMAL(10, 2),
    ifood_direct_payment_value DECIMAL(10, 2),
    service_fee DECIMAL(10, 2),
    category VARCHAR(50)
);

