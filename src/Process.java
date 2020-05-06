public class Process implements Comparable<Process>{
	String processName;
	int arrivalTime;
	int burstTime;
	int priorityNumber;
	int waitingTime;
	int turnaroundTime;
	int agFactor;
	int servaceTime;
	
	public Process(){
		this.processName="";
		//this.processColor="";
		this.arrivalTime=-1;
		this.burstTime=0;
		this.priorityNumber=0;
		this.waitingTime=0;
		this.turnaroundTime=0;
		this.agFactor=0;
	}
	
	public Process(String pN,int aT,int bT,int prioN){
		this.processName=pN;
		//this.processColor=pC;
		this.arrivalTime=aT;
		this.burstTime=bT;
		this.priorityNumber=prioN;
		this.waitingTime=0;
		this.turnaroundTime=0;
		this.servaceTime=bT;
		this.agFactor=this.burstTime+this.arrivalTime+this.priorityNumber;
	}
	
	public void setWaitingTime(int w){
		this.waitingTime=w;
	}
	
	public void setTurnaroundTime(int turnTime){
		this.turnaroundTime=turnTime;
	}

	@Override
	public int compareTo(Process o) {
		return (this.arrivalTime-o.arrivalTime);
	}
}
