package com.capgemini;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class EmployeePayrollFileIOService {
	public static String PAY_ROLL_FILE_NAME = "payroll-file.txt";
	public void writeData(List<EmployeePayrollData> employeePayrollList) {
		StringBuffer empBuffer = new StringBuffer();
		employeePayrollList.forEach(employee -> {
			String employeeDataString = employee.toString().concat("\n");
			empBuffer.append(employeeDataString);
		});
		try {
			Files.write(Paths.get(PAY_ROLL_FILE_NAME), empBuffer.toString().getBytes());
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public long countNoOfEntries() {
		long entries = 0;
		try {
			entries = Files.lines(new File(PAY_ROLL_FILE_NAME).toPath()).count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}

}
