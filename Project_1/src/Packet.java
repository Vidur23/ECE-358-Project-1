
public class Packet {
	private int id;
	private int arrivalTime;
	private int serviceTime;
	private int queueDelayTime;

	public int getQueueDelayTime() {
		return queueDelayTime;
	}

	public void setQueueDelayTime(int queueDelayTime) {
		this.queueDelayTime = queueDelayTime;
	}

	public Packet(int pid, int arrTime, int serTime){
		id = pid;
		arrivalTime = arrTime;
		serviceTime = serTime;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public int getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public int getSojournTime(){
		return this.getServiceTime() + this.getQueueDelayTime() - this.getArrivalTime();
	}
}
