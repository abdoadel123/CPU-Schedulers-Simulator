import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	static ArrayList<Process> processes = new ArrayList<>();
	
	static Scanner input = new Scanner(System.in);

	static String processName;
	static int arrivalTime;
	static int burstTime;
	static int priorityNumber;
	static int quantum;
	static int context;
	static int choice = 0;

	public static void ProccessesDate() {
		processes=new ArrayList<Process>();
		System.out.println("Enter Number Of Processes:");
		int processesNum = input.nextInt();
		for (int i = 0; i < processesNum; i++) {
			System.out.println("\nEnter Process " + (i + 1) + " Info:");
			System.out.print("Process Name: ");
			processName = input.next();
			System.out.print("Process Arrival Time: ");
			arrivalTime = input.nextInt();
			System.out.print("Process Brust Time: ");
			burstTime = input.nextInt();
			System.out.print("Process Priorety Number: ");
			priorityNumber = input.nextInt();
			Process p = new Process(processName, arrivalTime, burstTime, priorityNumber);
			processes.add(p);
		}
	}

	public static void main(String[] args) {
	    ArrayList<Process> newProcesses = new ArrayList<>();
		System.out.println(
				"                                                                             CPU Schedulers Simulator\n");
		System.out.println(
				"                                                                             ------------------------\n\n");
		ProccessesDate();
		do {
			newProcesses=new ArrayList<>();
			for (Process p:processes) {
				Process pr=new Process(p.processName,p.arrivalTime,p.burstTime,p.priorityNumber);
				newProcesses.add(pr);
			}
			//System.out.print("Ok "+processes.size());
			Collections.sort(newProcesses);
			System.out.println("\n\n"
			        + "1.Non-Preemptive Shortest- Job First (SJF)\n"
					+ "2.Shortest- Remaining Time First (SRTF)\n"
					+ "3.Non-preemptive Priority Scheduling\n"
					+ "4.AG Scheduling\n"
					+ "5.Enter New Proccesses\n"
					+ "6.Exit");
			
			System.out.print("\nEnter your Choice: ");
			choice = input.nextInt();
			switch (choice) {
			
			case 1:
				SJF sjf = new SJF(newProcesses);
				sjf.run();
				break;

			case 3:
				Priorety pr = new Priorety(newProcesses);
				pr.run();
				break;
				
			case 2:
				System.out.println("Enter Context Time:");
				context = input.nextInt();
				SRTF srtf = new SRTF(newProcesses, context);
				srtf.run();
				break;
				
			case 4:
				System.out.println("Enter Quantum:");
				quantum = input.nextInt();
				AG ag = new AG(newProcesses, quantum);
				ag.run();
				break;
				
			case 5:
				for(int i=0;i<100;i++)
					System.out.println();
				System.out.println(
						"                                                                             CPU Schedulers Simulator\n");
				System.out.println(
						"                                                                             ------------------------\n\n");
				ProccessesDate();
				break;
				
			case 6:
				System.out.println("Thanks for Using Our Program :D");
				System.exit(0);
			}
		} while (choice != 6);
	}

}