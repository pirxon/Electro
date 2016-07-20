package mysql;

import java.sql.*;
import java.util.Scanner;

public class Jdbc {

	public static void showTable(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM electro.data_counter");
		while (rs.next()) {
			System.out.println(
					"Дата(YYYY-MM-DD): " + rs.getString("date") + "  Показания счетчика: " + rs.getString("counter"));
		}
		
		rs.close();
		stmt.close();
	    conn.close();
	}

	public static void outputCurrCount(Connection conn) throws SQLException {
		System.out.print("Введите год (в формате YYYY): ");
		Scanner sc = new Scanner(System.in);
		int year = sc.nextInt();
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT date, counter FROM electro.data_counter WHERE YEAR(date) = ? AND MONTH(date) = ?");
		pstmt.setInt(1, year);
		System.out.print("Введите месяц (число 1-12): ");
		int month = sc.nextInt();
		pstmt.setInt(2, month);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.println("Показания счетчика - " + rs.getString("counter"));
		}
		
		rs.close();
		sc.close();
	    pstmt.close();
	    conn.close();
	}

	public static void inputCount(Connection conn) throws SQLException {
		System.out.print("Введите показания электросчетчика: ");
		Scanner sc = new Scanner(System.in);
		int counter = sc.nextInt();
		PreparedStatement pstmt = conn
				.prepareStatement("INSERT INTO electro.data_counter (date, counter) VALUES (current_date(), ?)");
		pstmt.setInt(1, counter);
		pstmt.executeUpdate();
		System.out.println("Done.");
		
		sc.close();
	    pstmt.close();
	    conn.close();
	}

	public static void deleteCount(Connection conn) throws SQLException {
		System.out.print("Удаление показаний, введите точную дату (YYYY-MM-DD): ");
		Scanner sc = new Scanner(System.in);
		String data_del = sc.nextLine();
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM electro.data_counter WHERE date = ?");
		pstmt.setString(1, data_del);
		pstmt.executeUpdate();
		System.out.println("Done.");
		
		sc.close();
	    pstmt.close();
	    conn.close();
	}
	
	public static void calcCount(Connection conn) throws SQLException {
		System.out.print("Введите месяц текущего года (1-12): ");
		Scanner sc = new Scanner(System.in);
		int m = sc.nextInt();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("SET @mon="+m);
		stmt.executeUpdate("SET @var1=(SELECT counter FROM electro.data_counter WHERE YEAR(date)= 2016 AND MONTH(date) = @mon-1)");
		stmt.executeUpdate("SET @var2=(SELECT counter FROM electro.data_counter WHERE YEAR(date)= 2016 AND MONTH(date) = @mon)");
	    ResultSet rs = stmt.executeQuery("SELECT @var2-@var1 AS Diff");
	    while(rs.next()) {
	       	int diff = rs.getInt("Diff");
	       	double res = (57 + (diff-100)*0.99);
	       	System.out.println("Разность показаний за "+m+" месяц : "+diff+" Квт/ч.  Нужно заплатить - "+res+" грн.");
	    }
	
	    sc.close();
	    rs.close();
	    stmt.close();
	    conn.close();
	}
}
