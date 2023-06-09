package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Asiakas;

public class Dao {
	
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Myynti.sqlite";
	
	private Connection yhdista() {	// KytketÃ¤Ã¤n yhteysobjekti tietokantaan
		Connection con = null;
		String path = System.getProperty("catalina.base");
		// path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); // Eclipsessa
		//System.out.println(path); //TÃ¤stÃ¤ nÃ¤et mihin kansioon laitat tietokanta-tiedostosi
		path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
		String url = "jdbc:sqlite:" + path + db;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			System.out.println("Yhteys avattu.");
		} catch (Exception e) {
			System.out.println("Yhteyden avaus epÃ¤onnistui.");
			e.printStackTrace();
		}
		return con;
	}
	
	private void sulje() {		// Katkaistaan yhteys tietokantaan
		if (stmtPrep != null) {
			try {
				stmtPrep.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
				System.out.println("Yhteys suljettu.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<Asiakas> getAllItems() {
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();	// Uusi tyhjÃ¤ arrayList
		sql = "SELECT * FROM asiakkaat ORDER BY asiakas_id DESC"; //Suurin id tulee ensimmÃ¤isenÃ¤
		try {
			con = yhdista();	// Palauttaa yhteyden tietokantaan
			if (con != null) { // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql); // Ajaa sql kyselyn tietokantaan
				rs = stmtPrep.executeQuery();	// rs = resultset. Antaa kyselyn tulokset tietokannasta
				if (rs != null) { // jos kysely onnistui
					while (rs.next()) { // KÃ¤ydÃ¤Ã¤n lÃ¤pi resultsetin sisÃ¤ltÃ¶
						// rs aloittaa lÃ¤pikÃ¤ymisen ensimmÃ¤istÃ¤ edeltÃ¤vÃ¤ltÃ¤ riviltÃ¤, joka on tyhjÃ¤
						Asiakas asiakas = new Asiakas(); // Uusi asiakas objekti
						asiakas.setAsiakas_id(rs.getInt(1));	// siirrytÃ¤Ã¤n ensimmÃ¤iseen riviin ...
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));
						asiakas.setPuhelin(rs.getString(4));
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);	// Asiakas lisÃ¤tÃ¤Ã¤n asiakkaat arrayListiin
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();	// Kertoo jos tuli jotain ongelmia.
		} finally {
			sulje();	// Sulkee yhteyden
		}
		return asiakkaat;
	}
	
	public ArrayList<Asiakas> getAllItems(String searchStr) {
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();	// Uusi tyhjÃ¤ arrayList
		sql = "SELECT * FROM asiakkaat WHERE etunimi LIKE ? or sukunimi LIKE ? or puhelin LIKE ? or sposti LIKE ? ORDER BY asiakas_id DESC"; //Suurin id tulee ensimmÃ¤isenÃ¤
		try {
			con = yhdista();	// Palauttaa yhteyden tietokantaan
			if (con != null) { // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql); // Ajaa sql kyselyn tietokantaan
				stmtPrep.setString(1, "%" + searchStr + "%");
				stmtPrep.setString(2, "%" + searchStr + "%");
				stmtPrep.setString(3, "%" + searchStr + "%");
				stmtPrep.setString(4, "%" + searchStr + "%");
				rs = stmtPrep.executeQuery();	// rs = resultset. Antaa kyselyn tulokset tietokannasta
				if (rs != null) { // jos kysely onnistui
					while (rs.next()) { // KÃ¤ydÃ¤Ã¤n lÃ¤pi resultsetin sisÃ¤ltÃ¶
						// rs aloittaa lÃ¤pikÃ¤ymisen ensimmÃ¤istÃ¤ edeltÃ¤vÃ¤ltÃ¤ riviltÃ¤, joka on tyhjÃ¤
						Asiakas asiakas = new Asiakas(); // Uusi asiakas objekti
						asiakas.setAsiakas_id(rs.getInt(1));	// siirrytÃ¤Ã¤n ensimmÃ¤iseen riviin ...
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));
						asiakas.setPuhelin(rs.getString(4));
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);	// Asiakas lisÃ¤tÃ¤Ã¤n asiakkaat arrayListiin
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();	// Kertoo jos tuli jotain ongelmia.
		} finally {
			sulje();	// Sulkee yhteyden
		}
		return asiakkaat;
	}
	
	public boolean addItem(Asiakas asiakas) {
		boolean paluuArvo = true;
		sql = "INSERT INTO asiakkaat(etunimi, sukunimi, puhelin, sposti)VALUES(?,?,?,?)";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setString(1, asiakas.getEtunimi());
			stmtPrep.setString(2, asiakas.getSukunimi());
			stmtPrep.setString(3, asiakas.getPuhelin());
			stmtPrep.setString(4, asiakas.getSposti());
			stmtPrep.executeUpdate();		
		} catch (Exception e) {
			paluuArvo=false;
			e.printStackTrace();
		} finally {
			sulje();
		}
		return paluuArvo;
	}
	
	public boolean removeItem(int asiakas_id) { // Oikeassa elämässä tiedot ensisijaisesti merkitään poistetuksi.
		boolean paluuArvo = true;
		sql = "DELETE FROM asiakkaat WHERE asiakas_id=?";
		try {
			con = yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setInt(1, asiakas_id);
			stmtPrep.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		} finally {
			sulje();
		}
		return paluuArvo;
	}
	
	public Asiakas getItem(int asiakas_id) {
		Asiakas asiakas = null;
		sql = "SELECT * FROM asiakkaat WHERE asiakas_id=?";       
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setInt(1, asiakas_id);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ //jos kysely tuotti dataa, eli rekNo on k�yt�ss�
        			rs.next();
        			asiakas = new Asiakas();        			
        			asiakas.setAsiakas_id(rs.getInt(1));
        			asiakas.setEtunimi(rs.getString(2));
        			asiakas.setSukunimi(rs.getString(3));
        			asiakas.setPuhelin(rs.getString(4));
        			asiakas.setSposti(rs.getString(5));      			      			
				}        		
			}			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sulje();
		}		
		return asiakas;		
	}
	
	public boolean changeItem(Asiakas asiakas){
		boolean paluuArvo=true;
		sql="UPDATE asiakkaat SET etunimi=?, sukunimi=?, puhelin=?, sposti=? WHERE asiakas_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setString(1, asiakas.getEtunimi());
			stmtPrep.setString(2, asiakas.getSukunimi());
			stmtPrep.setString(3, asiakas.getPuhelin());
			stmtPrep.setString(4, asiakas.getSposti());
			stmtPrep.setInt(5, asiakas.getAsiakas_id());
			stmtPrep.executeUpdate();	        
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		} finally {
			sulje();
		}				
		return paluuArvo;
	}
	
	public String findUser(String uid, String pwd) {
		String nimi = null;
		sql="SELECT * FROM asiakkaat WHERE sposti=? AND salasana=?";						  
		try {
			con = yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setString(1, uid);
				stmtPrep.setString(2, pwd);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ //jos kysely tuotti dataa, eli asiakas l�ytyi
        			rs.next();
        			nimi = rs.getString("etunimi")+ " " +rs.getString("sukunimi");     			      			
				}        		
			}			        
		} catch (Exception e) {				
			e.printStackTrace();			
		} finally {
			sulje();
		}				
		return nimi;
	}

}
