package mysql;

	import java.sql.*;
	import java.util.Scanner;

	public class Main implements DB_properties{

		public static void main(String[] args) throws SQLException, ClassNotFoundException {
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
		            Statement stat1 = conn.createStatement();
					ResultSet rs1 = stat1.executeQuery("SELECT * FROM electro.data_counter");
					while(rs1.next()) {
						System.out.println("Дата(YYYY-MM-DD): " + rs1.getString("date") + "  Показания счетчика: " + rs1.getString("counter"));
					}
					rs1.close();
					stat1.close();
					conn.close();
					break;

				case 2:
					System.out.print("Введите год (в формате YYYY): ");
					Scanner sc2 = new Scanner(System.in);
					int year = sc2.nextInt();
					PreparedStatement  pstat2 = conn.prepareStatement("SELECT date, counter FROM electro.data_counter WHERE YEAR(date) = ? AND MONTH(date) = ?");
					pstat2.setInt(1, year);
					System.out.print("Введите месяц (число 1-12): ");
					int month = sc2.nextInt();
					pstat2.setInt(2, month);
					ResultSet rs2 = pstat2.executeQuery();
					while(rs2.next()) {
						System.out.println("Показания счетчика - " + rs2.getString("counter"));
					}
					rs2.close();
					pstat2.close();
					conn.close();
					sc2.close();
					break;
				
				case 3:
					System.out.print("Введите показания электросчетчика: ");
					Scanner sc3 = new Scanner(System.in);
					int counter = sc3.nextInt();
					PreparedStatement  pstat3 = conn.prepareStatement("INSERT INTO electro.data_counter (date, counter) VALUES (current_date(), ?)");
					pstat3.setInt(1, counter);
					pstat3.executeUpdate();
					pstat3.close();
					conn.close();
					sc3.close();
					break;
			
				case 4:
					System.out.print("Удаление показаний, введите точную дату (YYYY-MM-DD): ");
					Scanner sc4= new Scanner(System.in);
					String data_del = sc4.nextLine();
					PreparedStatement  pstat4 = conn.prepareStatement("DELETE FROM electro.data_counter WHERE date = ?");
			        pstat4.setString(1, data_del);
		            pstat4.executeUpdate();
					pstat4.close();
					conn.close();
					sc4.close();
					break;
					
				case 5:
					System.out.print("Введите месяц текущего года (1-12): ");
					Scanner sc5 = new Scanner(System.in);
					int m = sc5.nextInt();
					Statement stat5 = conn.createStatement();
					stat5.executeUpdate("SET @mon="+m);
					stat5.executeUpdate("SET @var1=(SELECT counter FROM electro.data_counter WHERE YEAR(date)= 2016 AND MONTH(date) = @mon-1)");
					stat5.executeUpdate("SET @var2=(SELECT counter FROM electro.data_counter WHERE YEAR(date)= 2016 AND MONTH(date) = @mon)");
		            ResultSet rs5 = stat5.executeQuery("SELECT @var2-@var1 AS Diff");
		            while(rs5.next()) {
		            	int diff = rs5.getInt("Diff");
		            	double res = (57 + (diff-100)*0.99);
		            	System.out.println("Разность показаний за "+m+" месяц : "+diff+" Квт/ч.  Нужно заплатить - "+res+" грн.");
		            }
		            rs5.close();
		           	stat5.close();
	        		conn.close();
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
	        
	        

			


	
	
		
