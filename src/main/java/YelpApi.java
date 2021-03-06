

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

/**
 * Servlet implementation class SearchServlet
 * This servler handles
 * communication with the yelpApi.
 */
public class YelpApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YelpApi() {
        super();
        // TODO Auto-generated constructor stub
    }
    /*
     * This helper function creates an http request to the yelp api
     * to get a list of restaurants based off query, and size.
     */
    public String getYelpApiResults(String query, String size) throws IOException {
    	Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    	//Url to reach the yelp api
		String url = "https://api.yelp.com/v3/businesses/search?location=801%20Childs%20Way,%20Los%20Angeles,%20CA%2090089&term="+ query + "&sort_by=distance&limit=50";
		
		//Creating the http request.
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer vA1w5vh_CQ8cDc_ro1pu9E_7fHDhQqeUUfn5f7sNKEE9ib3akOutyXVU4o9SpMGPk11y_B7hv9zTAiytKRknZpP6g9s1Klnc7TnpQqlnDIPs1qx3pPy-qRpKfNp1XHYx");
		int responseCode = con.getResponseCode();
		
		//If the response was good
		if(responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();
			while((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
			}
			in.close();
			//Create a list of restaurants
			JsonObject json = new Gson().fromJson(resp.toString(), JsonObject.class);
			Type listType = new TypeToken<ArrayList<Restaurant>>() {
		    }.getType();
			ArrayList<Restaurant> restaurants = gson.fromJson(json.getAsJsonArray("businesses"), listType);
			//Return a list of restaurants
			return gson.toJson(restaurants);
		}
		return "Failed to reach Yelp";
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
	    
		String query = request.getParameter("query");
		String size = request.getParameter("size");
		
		
		response.getWriter().print(getYelpApiResults(query, "50"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
