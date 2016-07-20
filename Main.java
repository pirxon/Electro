package mysql;

import java.sql.*;
import java.util.Scanner;

public class Main implements DB_properties {
	
	private static Scanner sc; 
	
	public static void main(String[] args) {

		Connection conn = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			System.out.println("-- Программа учета электроэнергии -- " 
					+ "\n1. Вывести всю таблицу"
					+ "\n2. Вывести показания электросчетчика за указанную дату (год, месяц)"
					+ "\n3. Ввести показания на текущую дату" 
					+ "\n4. Удалить запись по дате"
					+ "\n5. Расчитать разницу показаний за указанный месяц");

			switch (readIntValue("\nВыберите пункт меню : ")) {

			case 1:
				Jdbc.showTable(conn);
				break;

			case 2:
				Jdbc.outputCurrCount(conn, readIntValue("Введите год (в формате YYYY): "), readIntValue("Введите месяц (число 1-12): "));
				break;

			case 3:
				Jdbc.inputCount(conn, readIntValue("Введите показания электросчетчика: "));    
				break;

			case 4:
				Jdbc.deleteCount(conn, readStringValue("Удаление показаний, введите точную дату (YYYY-MM-DD): "));
				break;

			case 5:
				Jdbc.calcCount(conn, readIntValue("Введите месяц текущего года (1-12): "));
				break;

			}
			sc.close();
			
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("Goodbye!");
	}

	private static int readIntValue(String question) {
	     sc = new Scanner(System.in);
	     System.out.print(question);
	     int m = sc.nextInt();
	     return m;
	}
	
	private static String readStringValue(String question) {
	     sc = new Scanner(System.in);
	     System.out.print(question);
	     String data = sc.nextLine();
	     return data;
	}

}


