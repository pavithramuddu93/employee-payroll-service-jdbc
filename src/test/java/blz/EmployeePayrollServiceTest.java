package blz;

import org.junit.Before;
import blz.EmployeePayrollException;
import blz.EmployeePayrollData;
import blz.EmployeePayrollDBService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static blz.EmployeePayrollDBService.employeePayrollDBService;


public class EmployeePayrollServiceTest {

    EmployeePayrollServiceMain employeePayrollService;

    @Before
    public void setup() {
        employeePayrollService = new EmployeePayrollServiceMain();
    }

    @Test
    public void givenEmployeeInDB_whenRetrieved_shouldMatchEmployeeCount() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = employeePayrollDBService.readData();
        Assert.assertEquals(4,employeePayrollList.size());
    }
}