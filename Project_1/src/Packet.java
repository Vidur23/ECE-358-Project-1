// Packet Class
public class Packet {
	private int id;
	private double arrivalTime;
	private double serviceTime;
	private double queueDelayTime;

	public Packet(int pid, double arrTime){
		id = pid;
		arrivalTime = arrTime;
	}
	
	public double getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public double getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
		this.queueDelayTime = serviceTime - this.arrivalTime;
	}
	
	public double getSojournTime(){
		return this.serviceTime - this.arrivalTime;
	}
	
	public double getQueueDelayTime() {
		return queueDelayTime;
	}
	public void setQueueDelayTime(double queueTime) {
		this.queueDelayTime = queueTime;
	}
}
