package com.capgemini;

import java.util.Arrays;
import java.util.List;

import org.junit.*;

import com.capgemini.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
	@Test
	public void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries() {
		EmployeePayrollData [] arrayOfEmps = {
				new EmployeePayrollData(1, "Jeff Bezoz", 100000.0),
				new EmployeePayrollData(2, "Bill Gates", 200000.0),
				new EmployeePayrollData(3, "Mark", 300000.0),
		};
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));	
		employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		Assert.assertEquals(3, entries);
		List<EmployeePayrollData> employeeList = employeePayrollService.readData(IOService.FILE_IO);
		System.out.println(employeeList);
		Assert.assertEquals(3, entries);
	}

}
