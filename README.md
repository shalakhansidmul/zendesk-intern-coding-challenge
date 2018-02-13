# zendesk-intern-challenge

	Zendesk Ticket View Client
	---------------------------------------------------------------------------------------------------------
Pre-requisites:

	1. JRE : version 1.8.0_144 or higher.
  
Running the client:

    Open the zendeskticketcleint folder.
    Open a command prompt (preferably in Administrator mode)
    Either save the run_client.txt as a '.bat' file  and run it 
    or 
    copy it's contents and paste them into the command prompt.
    
    
This file does the following:

	1. makes a directory 'bin' in the current directory.
	2. compiles the code with the required libraries in classpath
	3. runs the TicketViewClient.
  
In case of Java errors like 'Main class not found' etc. Please ensure that the version of Java is as mentioned in the pre-requisites.

Usage Instrcutions:

    Displaying All Tickets:
      1.Run the program as mentioned in previous section.
      2. You will see a menu as follows:

          Welcome to Zendesk ticket client.
          1. View all tickets.
          2. View ticket by id.
          3. Exit
          Enter your choice:

      3. Select option 1.
      4. Max 25 records will be displayed per page.
      5. After every page, you will see a menu as follows:

            1. Next
            2. Previous
            3. Select a ticket
            4. Exit to previous menu.
            Enter your choice:

        6. You can navigate through pages by choosing the appropriate options.
        7. You can display a particular ticket in detail. using option 3.
        8. You can skip to previous menu using option 4.

    Displaying a particular ticket (by ID):
      1.Run the program as mentioned in previous section.
      2. You will see a menu as follows:

        Welcome to Zendesk ticket client.
        1. View all tickets.
        2. View ticket by id.
        3. Exit
        Enter your choice:

      3. Select option 2.
      4. Enter ticket id of your choice.
      5. Ticket, if exists and fetched without issues, will be displayed in detail.

	
