package mysql;

import java.sql.*;

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

	public static void outputCurrCount(Connection conn, int year, int month) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT date, counter FROM electro.data_counter WHERE YEAR(date) = ? AND MONTH(date) = ?");
		pstmt.setInt(1, year);
		pstmt.setInt(2, month);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.println("Показания счетчика - " + rs.getString("counter"));
		}

		rs.close();
		pstmt.close();
		conn.close();
	}

	public static void inputCount(Connection conn, int counter) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO electro.data_counter (date, counter) VALUES (current_date(), ?)");
		pstmt.setInt(1, counter);
		pstmt.executeUpdate();
		System.out.println("Done.");

		pstmt.close();
		conn.close();
	}

	public static void deleteCount(Connection conn, String data_del) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM electro.data_counter WHERE date = ?");
		pstmt.setString(1, data_del);
		pstmt.executeUpdate();
		System.out.println("Done.");

		pstmt.close();
		conn.close();
	}

	public static void calcCount(Connection conn, int m) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("SET @mon=" + m);
		stmt.executeUpdate(
				"SET @var1=(SELECT counter FROM electro.data_counter WHERE YEAR(date)= 2016 AND MONTH(date) = @mon-1)");
		stmt.executeUpdate(
				"SET @var2=(SELECT counter FROM electro.data_counter WHERE YEAR(date)= 2016 AND MONTH(date) = @mon)");
		ResultSet rs = stmt.executeQuery("SELECT @var2-@var1 AS Diff");
		while (rs.next()) {
			int diff = rs.getInt("Diff");
			double res = (57 + (diff - 100) * 0.99);
			System.out.println(
					"Разность показаний за " + m + " месяц : " + diff + " Квт/ч.  Нужно заплатить - " + res + " грн.");
		}

		rs.close();
		stmt.close();
		conn.close();
	}
}
