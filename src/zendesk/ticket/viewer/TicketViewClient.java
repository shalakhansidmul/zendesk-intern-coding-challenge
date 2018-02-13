package zendesk.ticket.viewer;

import zendesk.ticket.viewer.api.TicketViewer;
import zendesk.ticket.viewer.factory.TicketViewerFactory;

/**
 * @author Shalakha
 *
 */
public class TicketViewClient {
	public static void main(String[] args) {
		int option = 0;
		TicketViewer ticketViewerCli = TicketViewerFactory.getViewer();
		System.out.println("Welcome to Zendesk ticket client.");
		while(option!=3){
			System.out.println("------------ MENU ------------");
			System.out.println("1. View all tickets.");
			System.out.println("2. View ticket by id.");
			System.out.println("3. Exit");
			System.out.println("------------------------------");
			System.out.println("Enter your choice: ");
			option = ticketViewerCli.getUserInput();
			switch(option){
			case 1:
				ticketViewerCli.viewAllTickets();break;
			case 2: 
				System.out.println("Please enter ticket id: ");
				int ticketId = ticketViewerCli.getUserInput();
				ticketViewerCli.viewTicketById(ticketId);break;
			case 3:
				ticketViewerCli.exit();
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
		System.out.println("Thank you for using Zendesk Ticket View Client.");
		return;
	}
}
