import java.util.ArrayList;

public class SJF {
	ArrayList<Process> processes = new ArrayList<>();
	int lastCompTime = 0;
	float avgWaitingTime;
	float avgTurnaroundTime;

	public SJF() {
		this.processes = null;
	}

	public SJF(ArrayList<Process> p) {
		this.processes = p;
	}

	public void swap(int i, int j) {
		Process temp = processes.get(i);
		processes.set(i, processes.get(j));
		processes.set(j, temp);
	}

	public void run() {
		int totalWaiting = 0;
		int totalTurnaround = 0;
		for (int i = 0; i < processes.size(); i++) {
			for (int j = i + 1; j < processes.size(); j++) {
				if (processes.get(j).arrivalTime == processes.get(i).arrivalTime
						&& processes.get(j).burstTime < processes.get(i).burstTime) {
					swap(i, j);
				}
			}
		}
		for (int i = 0; i < processes.size(); i++) {

			for (int j = i + 1; j < processes.size(); j++) {
				if (processes.get(j).burstTime < processes.get(i).burstTime
						&& processes.get(j).arrivalTime <= lastCompTime) {
					swap(i, j);
				}
			}
			lastCompTime += processes.get(i).burstTime;
			processes.get(i).setTurnaroundTime(lastCompTime - processes.get(i).arrivalTime);
			processes.get(i).setWaitingTime(processes.get(i).turnaroundTime - processes.get(i).burstTime);
			totalWaiting += processes.get(i).waitingTime;
			totalTurnaround += processes.get(i).turnaroundTime;
		}
		System.out.print("\nProccesses Sequance:  ");
		for (int i = 0; i < processes.size(); i++) {
			System.out.print(processes.get(i).processName);
			if(i!=processes.size()-1)
				System.out.print(" , ");
		}
		
		System.out.println("\n\nProccessName  WaitingTime TurnaroundTime");
		for (int i = 0; i < processes.size(); i++) {
			System.out.println("    "+processes.get(i).processName
					+ "           " + processes.get(i).waitingTime
					+ "           "+ processes.get(i).turnaroundTime);
		}
		avgWaitingTime = (float) totalWaiting / processes.size();
		avgTurnaroundTime = (float) totalTurnaround / processes.size();
		System.out.println("\nThe Average Waiting Time: " + avgWaitingTime);
		System.out.println("The Average Turnaround Time: " + avgTurnaroundTime);
	}
}