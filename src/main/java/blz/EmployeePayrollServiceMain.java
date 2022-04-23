package blz;

import blz.EmployeePayrollException;
import blz.EmployeePayrollData;
import blz.EmployeePayrollDBService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeePayrollServiceMain {

    private List<EmployeePayrollData> employeePayrollList;

    private EmployeePayrollDBService employeePayrollDBService;

    public List<EmployeePayrollData> getEmployeePayrollList() {
        return employeePayrollList;
    }

    public EmployeePayrollServiceMain(){
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollServiceMain(List<EmployeePayrollData> employeePayrollList){
        this();
        this.employeePayrollList = employeePayrollList;
    }
    /**
     * Main Method or Starting point of this program.
     *
     * @param args
     */
    public static void main(String[] args) throws EmployeePayrollException {
        EmployeePayrollServiceMain employeePayrollServiceMain = new EmployeePayrollServiceMain();
        employeePayrollServiceMain.retrievingEmployeeData();
    }

    /**
     * UC2 - Method for retrieving data from database.
     * @return : List of employee details
     * @throws EmployeePayrollException
     */
    public List<EmployeePayrollData> retrievingEmployeeData() throws EmployeePayrollException {
        this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    /**
     * UC3 - Method for updating the salary of the employee using name.
     * @param name : name of employee
     * @param salary : new salary
     * @throws SQLException
     * @throws EmployeePayrollException
     */
    public void updateEmployeeSalary(String name, double salary) throws SQLException, EmployeePayrollException {
        int result = employeePayrollDBService.updateData(name,salary);
        if ( result == 0 ) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null) employeePayrollData.setSalary(salary);
    }

    /**
     * UC3 - Method to get employee payroll data from employee payroll list using employee name
     * @param name : employee name
     * @return : object of employee detail
     */
    private EmployeePayrollData getEmployeePayrollData(String name) {
        EmployeePayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }

    /**
     * UC3 - Method for checking weather database is synchronous or not
     * @param name
     * @return
     */
    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeeData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    /**
     * UC5 -  Method for read data from database in give data range
     * @param startDate : start date
     * @param endDate : end date
     * @return : total employee object
     * @throws EmployeePayrollException
     */
    public List<EmployeePayrollData> readEmployeePayrollForDate(LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        return  employeePayrollDBService.getEmployeePayrollForDateRange(startDate,endDate);
    }
}