package zendesk.ticket.viewer.api;

/**
 * @author Shalakha
 *
 */
public interface TicketViewer {
	public void viewAllTickets();
	public void viewTicketById(int ticketId);
	public void exit();
	public int getUserInput();
}
