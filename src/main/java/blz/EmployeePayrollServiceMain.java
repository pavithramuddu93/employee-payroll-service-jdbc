package blz;

import blz.EmployeePayrollValidation;
import blz.EmployeePayrollData;
import blz.EmployeePayrollDBService;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollServiceMain {
    private List<EmployeePayrollData> employeePayrollList = new ArrayList<>();

    public EmployeePayrollServiceMain() {
    }

    public EmployeePayrollServiceMain(List<EmployeePayrollData> employeePayrollList) {
        this.employeePayrollList = employeePayrollList;
    }

    /**
     * Main Method or Starting point of this program.
     *
     * @param args
     */
    public static void main(String[] args) throws EmployeePayrollValidation{
        EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
        employeePayrollDBService.getConnection();
    }

}