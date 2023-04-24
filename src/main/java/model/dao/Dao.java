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
	
	private Connection yhdista() {	// Kytketään yhteysobjekti tietokantaan
		Connection con = null;
		String path = System.getProperty("catalina.base");
		path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); // Eclipsessa
		//System.out.println(path); //Tästä näet mihin kansioon laitat tietokanta-tiedostosi
		// path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
		String url = "jdbc:sqlite:" + path + db;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			System.out.println("Yhteys avattu.");
		} catch (Exception e) {
			System.out.println("Yhteyden avaus epäonnistui.");
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
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();	// Uusi tyhjä arrayList
		sql = "SELECT * FROM asiakkaat ORDER BY asiakas_id DESC"; //Suurin id tulee ensimmäisenä
		try {
			con = yhdista();	// Palauttaa yhteyden tietokantaan
			if (con != null) { // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql); // Ajaa sql kyselyn tietokantaan
				rs = stmtPrep.executeQuery();	// rs = resultset. Antaa kyselyn tulokset tietokannasta
				if (rs != null) { // jos kysely onnistui
					while (rs.next()) { // Käydään läpi resultsetin sisältö
						// rs aloittaa läpikäymisen ensimmäistä edeltävältä riviltä, joka on tyhjä
						Asiakas asiakas = new Asiakas(); // Uusi asiakas objekti
						asiakas.setAsiakas_id(rs.getInt(1));	// siirrytään ensimmäiseen riviin ...
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));
						asiakas.setPuhelin(rs.getString(4));
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);	// Asiakas lisätään asiakkaat arrayListiin
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
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();	// Uusi tyhjä arrayList
		sql = "SELECT * FROM asiakkaat WHERE etunimi LIKE ? or sukunimi LIKE ? or puhelin LIKE ? or sposti LIKE ? ORDER BY asiakas_id DESC"; //Suurin id tulee ensimmäisenä
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
					while (rs.next()) { // Käydään läpi resultsetin sisältö
						// rs aloittaa läpikäymisen ensimmäistä edeltävältä riviltä, joka on tyhjä
						Asiakas asiakas = new Asiakas(); // Uusi asiakas objekti
						asiakas.setAsiakas_id(rs.getInt(1));	// siirrytään ensimmäiseen riviin ...
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));
						asiakas.setPuhelin(rs.getString(4));
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);	// Asiakas lisätään asiakkaat arrayListiin
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

}
