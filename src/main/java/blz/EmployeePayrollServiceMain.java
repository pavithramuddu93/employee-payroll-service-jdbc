package blz;

import blz.EmployeePayrollException;
import blz.EmployeePayrollData;
import blz.EmployeePayrollDBService;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollServiceMain {
    private List<EmployeePayrollData> employeePayrollList = new ArrayList<>();


    public static void main(String[] args) throws EmployeePayrollException {
        EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
        employeePayrollDBService.getConnection();
        System.out.println(employeePayrollDBService.readData());
    }

}