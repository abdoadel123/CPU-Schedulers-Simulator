import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SRTF {
	ArrayList<Process> processes = new ArrayList<>();
	ArrayList<Process> sequance = new ArrayList<>();
	ArrayList<Process> readyQueu = new ArrayList<>();
	int minIndex = 0, t = 0, minBrust = Integer.MAX_VALUE;
	float avgWaitingTime;
	float avgTurnaroundTime;
	int context;
	public SRTF() {
		this.processes = null;
	}

	public SRTF(ArrayList<Process> p,int context) {
		this.processes = p;
		this.context=context;
	}

	public void swap(int i, int j) {
		Process temp = processes.get(i);
		processes.set(i, processes.get(j));
		processes.set(j, temp);
	}
	
	public void readyQueSort() {
		Collections.sort(readyQueu, new Comparator<Process>() {
			@Override
			public int compare(Process p1, Process p2) {
				return (p1.burstTime - p2.burstTime);
			}
		});
	}

	public void run() {
		int totalWaiting = 0;
		int totalTurnaround = 0;
		int numOfProcesses = processes.size();
		Process currentProcess = null;
		for (int i = 0; i < processes.size(); i++) {
			for (int j = i + 1; j < processes.size(); j++) {
				if (processes.get(j).arrivalTime == processes.get(i).arrivalTime
						&& processes.get(j).burstTime < processes.get(i).burstTime) {
					swap(i, j);
				}
			}
		}
		while (numOfProcesses != 0) {
			if (processes.size() > 0 && processes.get(0).arrivalTime <= t) {
				readyQueu.add(processes.get(0));
				processes.remove(0);
				readyQueSort();
			}
			if (currentProcess == null) {
				currentProcess = readyQueu.get(0);
				sequance.add(currentProcess);
				readyQueu.remove(0);
				System.out.println("Proccess  "+currentProcess.processName+"Start at "+t);
			} else {
				if (currentProcess.burstTime == 0) {
					numOfProcesses--;
					if(numOfProcesses!=0)
						t+=context;
					currentProcess.setTurnaroundTime(t-currentProcess.arrivalTime);
					currentProcess.setWaitingTime(currentProcess.turnaroundTime-currentProcess.servaceTime);
					if (numOfProcesses == 0)
						break;
					currentProcess = readyQueu.get(0);
					System.out.println("Switching to "+currentProcess.processName+" at "+t);
					readyQueu.remove(0);
					sequance.add(currentProcess);
				}
				else if (readyQueu.size() > 0 && readyQueu.get(0).burstTime < currentProcess.burstTime) {
					t+=context;
					readyQueu.add(currentProcess);
					currentProcess = readyQueu.get(0);
					System.out.println("Switching to "+currentProcess.processName+" at "+t);
					readyQueu.remove(0);
					readyQueSort();
					sequance.add(currentProcess);
				}

			}
			currentProcess.burstTime--;
			t++;
		}
		System.out.print("\nProccesses Sequance:  ");
		for (int i = 0; i < sequance.size(); i++) {
			System.out.print(sequance.get(i).processName);
			if(i!=sequance.size()-1)
				System.out.print(" , ");
		}
		
		Set<Process> newSet=new HashSet<>(sequance);
		sequance.clear();
		sequance.addAll(newSet);
		
		Collections.sort(sequance, new Comparator<Process>() {
			@Override
			public int compare(Process p1, Process p2) {
				return (p1.processName.compareTo(p2.processName));
			}
		});
		
		for (int i = 0; i < sequance.size(); i++) {
			totalWaiting += sequance.get(i).waitingTime;
			totalTurnaround+=sequance.get(i).turnaroundTime;
		}
		

		System.out.println("\n\nProccessName  WaitingTime TurnaroundTime");
		for (int i = 0; i < sequance.size(); i++) {
			System.out.println("    "+sequance.get(i).processName
					+ "           " + sequance.get(i).waitingTime
					+ "           "+ sequance.get(i).turnaroundTime);
		}
		avgWaitingTime = (float) totalWaiting / sequance.size();
		avgTurnaroundTime = (float) totalTurnaround / sequance.size();
		System.out.println("\nThe Average Waiting Time: " + avgWaitingTime);
		System.out.println("The Average Turnaround Time: " + avgTurnaroundTime);
	}
}