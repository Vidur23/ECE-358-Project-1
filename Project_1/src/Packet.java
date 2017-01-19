/**
 * Simulate a Internet packet.
 */
public class Packet {
	private int id;					// The packet id number
	private double arrivalTime;		// The tick when the packet arrived
	private double serviceTime;		// The tick when the service finished
	private double queueDelayTime;	// The delay of waiting in the queue

	/**
	 * Create a new packet.
	 * @param pid - The unique packet id
	 * @param arrTime - The tick when the packet arrived
	 */
	public Packet(int pid, double arrTime){
		id = pid;
		arrivalTime = arrTime;
	}
	
	/**
	 * Get the tick when the packet arrived.
	 * @return When the packet arrived.
	 */
	public double getArrivalTime() {
		return arrivalTime;
	}
	
	/**
	 * Set the tick when the packet arrived.
	 * @param arrivalTime - The tick when the packet arrived. 
	 */
	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	/**
	 * Get the tick when the packet got serviced.
	 * @return  When the packet serviced.
	 */
	public double getServiceTime() {
		return serviceTime;
	}
	
	/**
	 * Set the tick when the packet got serviced.
	 * @param serviceTime - The tick when the packet got serviced.
	 */
	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
		this.queueDelayTime = serviceTime - this.arrivalTime;
	}
	
	/**
	 * The unique Id of the packet.
	 * @return Returns the packet ID number
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * Calculate the sojourn time of the packet.
	 * @return How long the packet took from the arrival to departure.
	 */
	public double getSojournTime(){
		return this.serviceTime - this.arrivalTime;
	}
	
	/**
	 * Get the tick when the packet got out of the queue.
	 * @return  When the packet got out of the queue.
	 */
	public double getQueueDelayTime() {
		return queueDelayTime;
	}
	
	/**
	 * Set the tick packet got out of the queue.
	 * @param queueTime - The tick when the packet got out of the queue.
	 */
	public void setQueueDelayTime(double queueTime) {
		this.queueDelayTime = queueTime;
	}
}