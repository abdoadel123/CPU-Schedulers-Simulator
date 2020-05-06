import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AG {
	ArrayList<Process> processes = new ArrayList<>();
	ArrayList<Process> sequance = new ArrayList<>();
	ArrayList<Process> readyQueu = new ArrayList<>();
	Map<String, Integer> quantumList = new HashMap<String, Integer>();
	int quantum;
	float avgWaitingTime;
	float avgTurnaroundTime;

	public AG() {
		processes = null;
		sequance = null;
		readyQueu = null;
		quantum = 0;
	}

	public AG(ArrayList<Process> p, int quantum) {
		this.processes = p;
		this.quantum = quantum;
		for (Process process : processes) {
			quantumList.put(process.processName, quantum);
		}
	}

	public void swap(int i, int j) {
		Process temp = processes.get(i);
		processes.set(i, processes.get(j));
		processes.set(j, temp);
	}

	public int minFactor() {
		int index = 0;
		int minAG = Integer.MAX_VALUE;
		for (int i = 0; i < readyQueu.size(); i++) {
			if (readyQueu.get(i).agFactor < minAG) {
				index = i;
				minAG = readyQueu.get(i).agFactor;
			}
		}
		return index;
	}

	public String printQuantum() {
		String printStr = "Quantum (";
		for (Map.Entry<String, Integer> pair : quantumList.entrySet()) {
			printStr += pair.getValue() + ",";
		}
		printStr = printStr.substring(0, printStr.length() - 1) + ") -> Ceil(50%) = (";
		for (Map.Entry<String, Integer> pair : quantumList.entrySet()) {
			printStr += (int) Math.ceil((double) pair.getValue() / 2) + ",";
		}
		printStr = printStr.substring(0, printStr.length() - 1) + ") ";
		return printStr;
	}

	public void run() {
		int totalWaiting = 0;
		int totalTurnaround = 0;
		int t = 0, counter = 0;
		int numOfProcesses = processes.size();
		boolean preemptive = false;
		Process currentProcess = null;
		for (int i = 0; i < processes.size(); i++)
			for (int j = i + 1; j < processes.size(); j++)
				if (processes.get(j).arrivalTime == processes.get(i).arrivalTime
						&& processes.get(j).agFactor < processes.get(i).agFactor)
					swap(i, j);

		while (numOfProcesses != 0) {
			String prtStr;
			if (processes.size() > 0 && processes.get(0).arrivalTime == t) {
				readyQueu.add(processes.get(0));
				processes.remove(0);
			}
			if (currentProcess == null && readyQueu.size() > 0) {
				currentProcess = readyQueu.get(0);
				sequance.add(currentProcess);
				readyQueu.remove(0);
				quantum = quantumList.get(currentProcess.processName);
				counter = 0;
				prtStr = printQuantum() + currentProcess.processName + " Running";
				System.out.println(prtStr);
			} else if (currentProcess == null && readyQueu.size() <= 0) {
				t++;
				continue;
			} else {
				int index = minFactor();
				if (currentProcess.burstTime == 0) {
					quantumList.replace(currentProcess.processName, 0);
					numOfProcesses--;
					currentProcess.setTurnaroundTime(t - currentProcess.arrivalTime);
					currentProcess.setWaitingTime(currentProcess.turnaroundTime - currentProcess.servaceTime);
					if (numOfProcesses == 0)
						break;
					if (readyQueu.size() > 0) {
						currentProcess = readyQueu.get(0);
						quantum = quantumList.get(currentProcess.processName);
						readyQueu.remove(0);
						sequance.add(currentProcess);
						counter = 0;
						prtStr = printQuantum() + currentProcess.processName + " Running";
						System.out.println(prtStr);
					}
					else {
						t++;
						continue;
					}

				} else if (readyQueu.size() > 0 && preemptive && counter < quantum
						&& readyQueu.get(index).agFactor < currentProcess.agFactor) {
					quantum += quantum - counter;
					quantumList.replace(currentProcess.processName, quantum);
					readyQueu.add(currentProcess);
					currentProcess = readyQueu.get(index);
					quantum = quantumList.get(currentProcess.processName);
					readyQueu.remove(index);
					sequance.add(currentProcess);
					counter = 0;
					preemptive = false;
					prtStr = printQuantum() + currentProcess.processName + " Running";
					System.out.println(prtStr);
				} else if (counter == quantum && currentProcess.burstTime != 0) {
					double quantumMean = 0;
					for (Map.Entry<String, Integer> pair : quantumList.entrySet()) {
						quantumMean += pair.getValue();
					}
					quantumMean = quantumMean / quantumList.size();
					quantum += Math.ceil(0.1 * (quantumMean));
					quantumList.replace(currentProcess.processName, quantum);
					readyQueu.add(currentProcess);
					currentProcess = readyQueu.get(0);
					quantum = quantumList.get(currentProcess.processName);
					readyQueu.remove(0);
					sequance.add(currentProcess);
					counter = 0;
					preemptive = false;
					prtStr = printQuantum() + currentProcess.processName + " Running";
					System.out.println(prtStr);
				}
			}
			counter++;
			if (counter == Math.ceil((double) quantum / 2))
				preemptive = true;
			currentProcess.burstTime--;
			t++;
		}
		System.out.println("Quantum (0,0,0,0)");
		System.out.print("\n\nProccesses Sequance:  ");
		for (int i = 0; i < sequance.size(); i++) {
			System.out.print(sequance.get(i).processName);
			if (i != sequance.size() - 1)
				System.out.print(" , ");
		}

		Set<Process> newSet = new HashSet<>(sequance);
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
			totalTurnaround += sequance.get(i).turnaroundTime;
		}

		System.out.println("\n\nProccessName  WaitingTime TurnaroundTime");
		for (int i = 0; i < sequance.size(); i++) {
			System.out.println("    " + sequance.get(i).processName + "           " + sequance.get(i).waitingTime
					+ "           " + sequance.get(i).turnaroundTime);
		}
		avgWaitingTime = (float) totalWaiting / sequance.size();
		avgTurnaroundTime = (float) totalTurnaround / sequance.size();
		System.out.println("\nThe Average Waiting Time: " + avgWaitingTime);
		System.out.println("The Average Turnaround Time: " + avgTurnaroundTime);

	}
}