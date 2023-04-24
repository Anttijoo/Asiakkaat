package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doGet()");
		String hakusana = request.getParameter("hakusana");
		System.out.println(hakusana);
		Dao dao = new Dao();
		ArrayList<Asiakas> asiakkaat = dao.getAllItems();
		String strJSON="";
		if(hakusana!=null) {//Jos kutsun mukana tuli hakusana
			if(!hakusana.equals("")) {//Jos hakusana ei ole tyhjä
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doPost()");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doPut()");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynti.doDelete()");
	}

}
