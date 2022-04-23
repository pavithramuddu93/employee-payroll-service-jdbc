package blz;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import blz.EmployeePayrollException;
import blz.EmployeePayrollData;

import java.util.HashMap;
import java.util.Map;

public class EmployeePayrollDBService {
    private PreparedStatement employeePayrollDataStatement;

    private static EmployeePayrollDBService employeePayrollDBService;

    private EmployeePayrollDBService() {

    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    /**
     * UC1 - Method for create connection to the database.
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
     * UC2 - Method for retrieving data from database.
     * @return : list of EmployeePayrollData
     * @throws EmployeePayrollException
     */
    public List<EmployeePayrollData> readData() throws EmployeePayrollException {
        String query = "SELECT * FROM employee_payroll; ";
        return this.getEmployeePayrollDataUsingDB(query);

    }

    /**
     * UC4 - Method for prepared statement
     * @param name
     * @return
     */
    public List<EmployeePayrollData> getEmployeeData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1,name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollList;
    }

    /**
     * UC3 - Method for retrieving data from database. Method to process the resultSet.
     * @param resultSet
     * @return
     */
    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollData emData = new EmployeePayrollData();
        try {
            while (resultSet.next()){
                emData.setId(resultSet.getInt("id"));
                emData.setName(resultSet.getString("name"));
                emData.setGender(resultSet.getString("gender"));
                emData.setSalary(resultSet.getDouble("salary"));
                emData.setStartDate(resultSet.getDate("start_date").toLocalDate());
                employeePayrollList.add(emData);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollList;
    }

    /**
     * UC5 - Method for retrieving data from database for a given date range.
     * @param startDate
     * @param endDate
     * @return
     */
    public List<EmployeePayrollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        String query = String.format("SELECT * FROM employee_payroll WHERE start_date BETWEEN '%s' AND '%s';",Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(query);
    }

    /**
     * UC6 - Method for getting average salary by gender from database
     * @return : map of gender and avg salary.
     */
    public Map<String, Double> getAverageSalaryByGender() throws EmployeePayrollException {
        String query = "SELECT gender, AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender;";
        Map<String,Double> genderToAverageSalaryMap = new HashMap<>();
        try(Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                String gender = result.getString("gender");
                double salary = result.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender,salary);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return genderToAverageSalaryMap;
    }

    /**
     * Helper or Generic method for retrieving data from database.
     * @param query
     * @return
     * @throws EmployeePayrollException
     */
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String query) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();

        try(Connection connection = this.getConnection();) {
            EmployeePayrollData emData = new EmployeePayrollData();
            Statement statement =  connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            employeePayrollList = this.getEmployeePayrollData(result);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage());
        }
        return employeePayrollList;
    }

    /**
     * UC4 - object for preparedStatement
     */
    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String query = "SELECT * FROM employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(query);
        } catch (EmployeePayrollException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * UC3 - Method for updating the employee data using Statement
     * @param name : employee name
     * @param salary : employee new salary
     * @return
     * @throws EmployeePayrollException
     * @throws SQLException
     */
    public int updateData(String name, double salary) throws EmployeePayrollException, SQLException {
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    /**
     * UC3 - Helper method for updating the employee data using Statement
     * @param name : employee name
     * @param salary : employee new salary
     * @return
     * @throws EmployeePayrollException
     */
    private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String query = String.format("update employee_payroll set salary = %.2f where name = '%s';",salary,name);
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        }catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage());
        }
    }

}