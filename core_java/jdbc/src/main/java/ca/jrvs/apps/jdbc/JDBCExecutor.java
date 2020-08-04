package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JDBCExecutor {

  public static final Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);

  public static void main(String[] args) {

    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "hplussport",
        "postgres", "password");

    try {
      Connection connection = dcm.getConnection();
      OrderDAO orderDAO = new OrderDAO(connection);
      Order order = orderDAO.findById(1000);
      logger.info(order.toString());
    } catch (SQLException e) {
      logger.error("Error while accessing database", e);
    }

  }

}
