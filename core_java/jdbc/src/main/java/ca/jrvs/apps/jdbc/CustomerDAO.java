package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerDAO extends DataAccessObject<Customer> {

  private final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);

  private static final String INSERT = "INSERT INTO customer (first_name, last_name, " +
      "email, phone, address, city, state, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String GET_ONE = "SELECT customer_id, first_name, last_name, " +
      "email, phone, address, city, state, zipcode FROM customer WHERE customer_id=?";

  private static final String UPDATE = "UPDATE customer SET first_name = ?, last_name = ?, " +
      "email = ?, phone = ?, address = ?, city = ?, state = ?, zipcode = ? WHERE customer_id = ?";

  private static final String DELETE = "DELETE FROM customer WHERE customer_id = ?";

  /**
   * Constructor to create a CustomerDAO instance.
   *
   * @param connection
   */
  public CustomerDAO(Connection connection) {
    super(connection);
  }

  /**
   * This method finds and returns the customer by their Id. It reflects the Read operation in
   * CRUD.
   *
   * @param id
   * @return Customer
   */
  public Customer findById(long id) {

    Customer customer = new Customer();

    try {

      PreparedStatement statement = this.connection.prepareStatement(GET_ONE);

      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        customer.setId(rs.getLong("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zipcode"));
      }

    } catch (SQLException e) {
      logger.error("Error while accessing database", e);
      throw new RuntimeException("Error while accessing database", e);
    }

    return customer;
  }

  public List<Customer> findAll() {
    return null;
  }

  /**
   * This method is used to update a customer and reflects the Update operation in CRUD.
   *
   * @param dto
   * @return Customer
   */
  public Customer update(Customer dto) {

    Customer customer = null;

    try {
      this.connection.setAutoCommit(false);
    } catch (SQLException e) {
      logger.error("Error while accessing database", e);
      throw new RuntimeException("Error while accessing database", e);
    }

    try {

      PreparedStatement statement = this.connection.prepareStatement(UPDATE);

      statement.setString(1, dto.getFirstName());
      statement.setString(2, dto.getLastName());
      statement.setString(3, dto.getEmail());
      statement.setString(4, dto.getPhone());
      statement.setString(5, dto.getAddress());
      statement.setString(6, dto.getCity());
      statement.setString(7, dto.getState());
      statement.setString(8, dto.getZipCode());
      statement.setLong(9, dto.getId());

      this.connection.commit();

      statement.execute();
      customer = this.findById(dto.getId());

    } catch (SQLException e) {

      try {
        this.connection.rollback();
      } catch (SQLException sqle) {
        logger.error("Error while accessing database", sqle);
        throw new RuntimeException("Error while accessing database", sqle);
      }

      logger.error("Error while accessing database", e);
      throw new RuntimeException("Error while accessing database", e);
    }
    return customer;

  }

  /**
   * This method creates a customer and reflects the Create operation in CRUD.
   *
   * @param dto
   * @return Customer
   */
  public Customer create(Customer dto) {

    try {

      PreparedStatement statement = this.connection.prepareStatement(INSERT);
      statement.setString(1, dto.getFirstName());
      statement.setString(2, dto.getLastName());
      statement.setString(3, dto.getEmail());
      statement.setString(4, dto.getPhone());
      statement.setString(5, dto.getAddress());
      statement.setString(6, dto.getCity());
      statement.setString(7, dto.getState());
      statement.setString(8, dto.getZipCode());
      statement.execute();
      int id = this.getLastVal(CUSTOMER_SEQUENCE);
      return this.findById(id);

    } catch (SQLException e) {
      logger.error("Error while accessing database", e);
      throw new RuntimeException("Error while accessing database", e);
    }

  }

  /**
   * This method is used to delete a customer and reflects the Delete operation in CRUD.
   *
   * @param id
   */
  public void delete(long id) {

    try {

      PreparedStatement statement = this.connection.prepareStatement(DELETE);
      statement.setLong(1, id);
      statement.execute();

    } catch (SQLException e) {
      logger.error("Error while accessing database", e);
      throw new RuntimeException("Error while accessing database", e);
    }

  }
}
