package zendesk.ticket.viewer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.simple.JSONObject;

import zendesk.ticket.viewer.api.Communicator;
import zendesk.ticket.viewer.api.TicketViewer;
import zendesk.ticket.viewer.factory.TicketViewerFactory;
import zendesk.ticket.viewer.util.Constants;

/**
 * @author Shalakha
 *
 */
public class TicketViewerImpl implements TicketViewer{
	Communicator comm ;
	Scanner sc;
	Logger logger = null;
	public TicketViewerImpl(){
		logger = Logger.getLogger(this.getClass().getPackage().getName());
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
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Something went wrong with initializing logger. Please try again.");
		}
		sc = new Scanner(System.in);
		comm = TicketViewerFactory.getCommunicator();
	}
	@Override
	public void viewAllTickets(){
		ArrayList<JSONObject> jsonObject = comm.fetchAllTickets();
		if(jsonObject!=null){
			display(jsonObject);			
		}
		else{
			System.out.println("No data found. Please check log file for errors if any.");
		}
	}

	@Override
	public void viewTicketById(int ticketId){
		JSONObject jsonObject = comm.fetchTicketById(ticketId);
		if(jsonObject!=null)
			printSingleTicketInDetail(jsonObject);
		else{
			System.out.println("No data found. Please check log file for errors if any.");
		}
	}
	@Override
	public void exit(){
		comm.cleanup();
	}
	@Override
	public int getUserInput() {	
		return sc.nextInt();
	}
	private void display(ArrayList<JSONObject> ticketsToDisplay){
		
		if(ticketsToDisplay == null){
			System.out.println("Error: Could not fetch tickets. Please try again.");
			System.exit(0);
		}
		int ticketCount = ticketsToDisplay.size();
		System.out.println("ticketCount : " + ticketCount);
		int totalPages = ticketCount/Constants.MAX_TICKETS_PER_PAGE;
		if(ticketCount%Constants.MAX_TICKETS_PER_PAGE > 0){
			totalPages++;
		}
		int option = 0;
		int currentPage = 1;
		int i = 0;
		boolean display = true;
		while(option != 4 && currentPage <= totalPages){
			if(display == true){
				while(i < ticketsToDisplay.size() && i< currentPage * Constants.MAX_TICKETS_PER_PAGE){
					printTicket((JSONObject) ticketsToDisplay.get(i));
					i++;
				}
				System.out.println("\nPage " + currentPage + " of " + totalPages);
			}
			printTicketViewOptions();
			option = getUserInput();
			switch(option){
			case 1:
				if(currentPage == totalPages){
					System.out.println("All of " + ticketCount + " tickets displayed. Wrapping back to page 1.");
					currentPage = 1;
					i = 0;
				}else{
					currentPage++;
				}
				display = true;
				continue;
			case 2:
				if(i > Constants.MAX_TICKETS_PER_PAGE){
					i = i - (2*Constants.MAX_TICKETS_PER_PAGE);
					currentPage--;
				}else{
					i = 0;
				}
				display = true;
				break;
			case 3:
				System.out.println("Please enter ticket id: ");
				int selectedTicket = getUserInput();
				if(selectedTicket-1 < 1 || selectedTicket - 1 > ticketCount){
					System.out.println("Ticket with id: " + selectedTicket + " not found. Please try again." );
					break;
				}
				printSingleTicketInDetail((JSONObject) ticketsToDisplay.get(selectedTicket-1));
				display = false;
				break;
			case 4:
				break;
			default:
				display = false;
				System.out.println("Invalid option. Please trye again.");
			}
		}
	}

	private void printSingleTicketInDetail(JSONObject ticktToPrint) {
		if(ticktToPrint.containsKey("ticket"))
			ticktToPrint = (JSONObject) ticktToPrint.get("ticket");
		if(ticktToPrint.get("id") == null){
			System.out.println("Ticket with specified ID not found. Please try again.");
			return;
		}
		System.out.println(" ------------------------- Displaying selected ticket ------------------------- ");
		System.out.println("\nTICKET ID: " + ticktToPrint.get("id") + "\nSTATUS: " + "[" + ticktToPrint.get("status") + "]" +				
				"\nOPENED BY: " + ticktToPrint.get("requester_id") +
				"\nUPDATED ON: " + ticktToPrint.get("updated_at"));
		System.out.println("SUBJECT: '" + ticktToPrint.get("subject") + "'" );
		System.out.println("DESCRIPTION: " + ticktToPrint.get("description"));
		System.out.println("\n--------------------------------------- End ------------------------------------");
	}

	private void printTicketViewOptions() {
		System.out.println("------------ MENU ------------");
		System.out.println("\n1. Next \n2. Previous \n3. Select a ticket \n4. Exit to previous menu.\nEnter your choice: ");
		System.out.println("------------------------------");
	}

	private void printTicket(JSONObject ticktToPrint){
		Date date = new Date();
		String id = String.valueOf(ticktToPrint.get("id"));
		try {
			date = Constants.DATE.parse((String) ticktToPrint.get("updated_at"));
			System.out.println("[" + ticktToPrint.get("status") + "]" +
					" Ticket " + id + 
					" subject '" + ticktToPrint.get("subject") + "'" +
					" opened by " + ticktToPrint.get("requester_id") +
					" updated " + date.toString());
		} catch (java.text.ParseException e) {
			logger.log(Level.INFO, "Error in printing ticket with ID: " + id + e.getMessage());
			System.out.println("Error: Something went wrong. Please check TicketViewer.log for details.");
		}
	}
}
