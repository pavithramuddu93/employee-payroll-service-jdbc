package blz;

import org.junit.Before;
import blz.EmployeePayrollException;
import blz.EmployeePayrollData;
import blz.EmployeePayrollDBService;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class EmployeePayrollServiceTest {

    EmployeePayrollServiceMain employeePayrollServiceMain;

    @Before
    public void setup() {
        employeePayrollServiceMain = new EmployeePayrollServiceMain();
    }

    @Test
    public void givenEmployeeInDB_whenRetrieved_shouldMatchEmployeeCount() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = employeePayrollServiceMain.retrievingEmployeeData();
        Assert.assertEquals(4,employeePayrollList.size());
    }

    @Test
    public void givenNewSalaryForEmployee_whenUpdated_shouldSyncWithDB() throws EmployeePayrollException, SQLException {
        EmployeePayrollServiceMain employeePayrollServiceMain = new EmployeePayrollServiceMain();
        List<EmployeePayrollData> employeePayrollList = employeePayrollServiceMain.retrievingEmployeeData();
        employeePayrollServiceMain.updateEmployeeSalary("Terisa", 300000.00);
        boolean result = employeePayrollServiceMain.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRange_whenRetrieved_shouldMatchEmployeeCount() throws EmployeePayrollException {
        employeePayrollServiceMain.retrievingEmployeeData();
        LocalDate startDate = LocalDate.of(2018,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollServiceMain.readEmployeePayrollForDate(startDate, endDate);
        Assert.assertEquals(4,employeePayrollData.size());
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperValue() throws EmployeePayrollException {
        employeePayrollServiceMain.retrievingEmployeeData();
        Map<String,Double> averageSalaryByGender = employeePayrollServiceMain.readAverageSalaryByGender();
        Assert.assertTrue(averageSalaryByGender.get("M").equals(50000.00) && averageSalaryByGender.get("F").equals(300000.00));
    }
}