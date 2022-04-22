package blz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

    static EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollDBService() {

    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    /**
     * Method for create connection to the database.
     *
     * @return : Connection
     * @throws EmployeePayrollException
     */
    public Connection getConnection() throws EmployeePayrollException {
        Connection con = null;
        try {
            Class.forName("java.sql.DriverManager");
            System.out.println("Driver loaded");
            String username = "root";
            String password = "root";
            String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
            System.out.println("connecting to database : " + jdbcURL);
            con = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("connection is successfully " + con);
        } catch (Exception e) {
            throw new EmployeePayrollException(e.getMessage());
        }
        return con;
    }

    /**
     * Method for retrieving data from database.
     * @return : list of EmployeePayrollData
     * @throws EmployeePayrollException
     */
    public List<EmployeePayrollData> readData() throws EmployeePayrollException {

        String query = "SELECT * FROM employee_payroll; ";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();

        try(Connection connection = this.getConnection();) {
            EmployeePayrollData emData = new EmployeePayrollData();
            Statement statement =  connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                emData.setId(result.getInt("id"));
                emData.setName(result.getString("name"));
                emData.setGender(result.getString("gender"));
                emData.setSalary(result.getDouble("salary"));
                emData.setStartDate(result.getDate("start_date").toLocalDate());
                employeePayrollList.add(emData);
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage());
        }
        return employeePayrollList;

    }
}