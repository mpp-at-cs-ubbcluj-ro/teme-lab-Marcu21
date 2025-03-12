package repository;

import model.Car;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements Repository<Car, Integer> {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        String sql = "INSERT INTO cars (manufacturer, model, year) VALUES (?, ?, ?)";

        try (PreparedStatement preStmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            preStmt.executeUpdate();

            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    elem.setID(generatedKeys.getInt(1));
                }
            }

            logger.trace("Saved {} instances", elem);
        } catch (SQLException ex) {
            logger.error("Error DB", ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }


    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM cars");
             ResultSet result = preStmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt("id");
                String manufacturer = result.getString("manufacturer");
                String model = result.getString("model");
                int year = result.getInt("year");
                Car car = new Car(manufacturer, model, year);
                car.setID(id);
                cars.add(car);
            }
        } catch (SQLException e) {
            logger.error("Error DB", e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void delete(Car elem) {
        logger.traceEntry("deleting task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "DELETE FROM cars WHERE id = ?")) {
            preStmt.setInt(1, elem.getID());
            preStmt.executeUpdate();
            logger.trace("Deleted {}", elem);
        } catch (SQLException e) {
            logger.error("Error DB", e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Car elem, Integer id) {
        logger.traceEntry("updating task {} with id {}", elem, id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "UPDATE cars SET manufacturer = ?, model = ?, year = ? WHERE id = ?")) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            preStmt.setInt(4, id);
            preStmt.executeUpdate();
            logger.trace("Updated {}", elem);
        } catch (SQLException e) {
            logger.error("Error DB", e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public Car findById(Integer id) {
        logger.traceEntry("finding task by id {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(
                "SELECT * FROM cars WHERE id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setID(id);
                    logger.traceExit(car);
                    return car;
                }
            }
        } catch (SQLException e) {
            logger.error("Error DB", e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Collection<Car> getAll() {
        return (Collection<Car>) findAll();
    }
}
