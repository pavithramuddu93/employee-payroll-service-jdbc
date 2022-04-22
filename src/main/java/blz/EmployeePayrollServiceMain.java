package blz;

import blz.EmployeePayrollException;
import blz.EmployeePayrollData;
import blz.EmployeePayrollDBService;

import java.sql.SQLException;
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

    public static void main(String[] args) throws EmployeePayrollException {
//        EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
//        employeePayrollDBService.getConnection();
//        employeePayrollDBService.readData();
    }

    public List<EmployeePayrollData> retrievingEmployeeData() throws EmployeePayrollException {
        this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    public void updateEmployeeSalary(String name, double salary) throws SQLException, EmployeePayrollException {
        int result = employeePayrollDBService.updateData(name,salary);
        if ( result == 0 ) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null) employeePayrollData.setSalary(salary);
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        EmployeePayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeeData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }
}