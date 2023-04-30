package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Asiakas;
import model.dao.Dao;

@WebServlet("/myynti/*")
public class Myynti extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Myynti() {
    	System.out.println("Myynti.Myynti()");
    }

    // Tietojen hakeminen
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doGet()");
		String hakusana = request.getParameter("hakusana");
		System.out.println(hakusana);
		Dao dao = new Dao();
		ArrayList<Asiakas> asiakkaat = dao.getAllItems();
		String strJSON="";
		if(hakusana!=null) {//Jos kutsun mukana tuli hakusana
			if(!hakusana.equals("")) {//Jos hakusana ei ole tyhj‰
				asiakkaat = dao.getAllItems(hakusana); //Haetaan kaikki hakusanan mukaiset autot							
			}else {
				asiakkaat = dao.getAllItems(); //Haetaan kaikki autot
			}
			strJSON = new Gson().toJson(asiakkaat);	
		}
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
	}

	// Tietojen lis‰‰minen
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doPost()");
		//Luetaan JSON-tiedot POST-pyynnˆn bodysta ja luodaan niiden perusteella uusi auto
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		Asiakas asiakas = new Gson().fromJson(strJSONInput, Asiakas.class);	
		System.out.println(asiakas);
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(dao.addItem(asiakas)) {
			out.println("{\"response\":1}");  //Asiakkaan lis‰‰minen onnistui {"response":1}
		}else {
			out.println("{\"response\":0}");  //Asiakkaan lis‰‰minen ep‰onnistui {"response":0}
		}
	}

	// Tietojen muuttaminen
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doPut()");
	}

	// Tietojen poistaminen
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doDelete()");
		int asiakas_id = Integer.parseInt(request.getParameter("asiakas_id"));
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(dao.removeItem(asiakas_id)) {
			out.println("{\"response\":1}");  //Asiakkaan poistaminen onnistui {"response":1}
		}else {
			out.println("{\"response\":0}");  //Asiakkaan pistaminen ep‰onnistui {"response":0}
		}
	}

}
