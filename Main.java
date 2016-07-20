package mysql;

import java.sql.*;
import java.util.Scanner;

public class Main implements DB_properties {

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
			System.out.print("\nВыберите пункт меню : ");
			Scanner sc = new Scanner(System.in);
			int menu = sc.nextInt();
			
			switch (menu) {

			case 1:
				Jdbc.showTable(conn);
				break;

			case 2:
				Jdbc.outputCurrCount(conn);
				break;

			case 3:
				Jdbc.inputCount(conn);
				break;

			case 4:
				Jdbc.deleteCount(conn);
				break;

			case 5:
				Jdbc.calcCount(conn);
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
}
