package zendesk.ticket.viewer.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import zendesk.ticket.viewer.api.Communicator;
import zendesk.ticket.viewer.util.Constants;

/**
 * @author Shalakha
 *
 */
public class CommunicatorImpl implements Communicator {

	CloseableHttpClient httpClient;
	CredentialsProvider credsProvider;
	Logger logger = null;
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<JSONObject> fetchAllTickets() {
		CloseableHttpResponse resp = null;
		try {
			String url = "https://shalakha.zendesk.com/api/v2/tickets.json";			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
			ArrayList<JSONObject> listOfJsonObjs = new ArrayList<JSONObject>();
			while(url != null){
				HttpUriRequest reqAllTickets = new HttpGet(url);
				reqAllTickets.setHeader("Content-Type","application/json");
				resp = httpClient.execute(reqAllTickets);			
				jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
				if(jsonObject != null){
					System.out.println("jsonObject.get(tickets) : " + jsonObject.get("tickets"));
					listOfJsonObjs.addAll((ArrayList<JSONObject>)jsonObject.get("tickets"));
				}
				if(resp.getStatusLine().getStatusCode() == 200)
					logger.log(Level.INFO, "Request to fetch all tickets executed successfully.");
				else{
					logger.log(Level.SEVERE, "Error in fetching tickets." + resp.getStatusLine());
					System.out.println("Unable to fetch tickets for your account. Please contact the system administrator.");
				}
				url = (String) jsonObject.get("next_page");
				resp.close();
			}
			return listOfJsonObjs;
		} catch (IOException | UnsupportedOperationException | ParseException e) {
			logger.log(Level.SEVERE,"Error in fetching all tickets. " + e.getMessage());
			System.out.println("Error: Something isn't right. Please check your internet connection and try again.");
		} finally {
			try {
				if(resp != null)
					resp.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE,"Error in closing response for fetching all tickets. " + e.getMessage());
				System.out.println("Error: Something went wrong. Please check TicketViewer.log for details.");
			}
		}
		return null;
	}

	@Override
	public JSONObject fetchTicketById(int ticketId) {
		HttpUriRequest reqSingleTicket = new HttpGet("https://shalakha.zendesk.com/api/v2/tickets/"+ ticketId +".json");
		reqSingleTicket.setHeader("Content-Type","application/json");
		CloseableHttpResponse resp = null;
		try {
			resp = httpClient.execute(reqSingleTicket);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
			jsonObject = (JSONObject)jsonParser.parse(
					new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
			logger.log(Level.INFO,"Request to fetch ticket by ID executed successfully. Fetch ticket id: " + ticketId);
			return jsonObject;
		} catch (IOException | UnsupportedOperationException | ParseException e) {
			logger.log(Level.SEVERE, "Error in fetching ticket with id : " + ticketId + "  " + e.getMessage());
			System.out.println("Error: Something isn't right. Please check your internet connection and try again.");
		}finally {
			try {
				resp.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error in closing response for ticket id : " + ticketId + "  " + e.getMessage());
				System.out.println("Error: Something went wrong. Please check TicketViewer.log for details.");
			}
		}
		return null;
	}

	@Override
	public void initialize() {
		logger = Logger.getLogger(CommunicatorImpl.class.getPackage().getName());
		try {
			FileHandler fh = new FileHandler("TicketViewer.log", false);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			Logger parentLogger = Logger.getLogger(""); 
			Handler[] handlers = parentLogger.getHandlers();  
			for (Handler handler : handlers) {
				parentLogger.removeHandler(handler);
			}
		} catch (SecurityException e) {
			System.out.println("Something went wrong with initializing logger. Please try again.");
		} catch (IOException e) {
			System.out.println("Something went wrong with initializing logger. Please try again.");
		}
		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(Constants.username,Constants.password));
		httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build();
		logger.log(Level.INFO, "Communicator initialized successfully.");
	}

	@Override
	public void cleanup() {
		try {
			httpClient.close();
			logger.log(Level.INFO, "Http client closed. Exiting now.");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error in closing http client : " + e.getMessage());
			System.out.println("Error: Something went wrong. Please check TicketViewer.log for details.");
		}
	}

}
