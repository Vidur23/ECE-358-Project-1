import java.util.LinkedList;

public class NetworkSimulatorComplete {
	private  LinkedList<Packet> packetQueue;
	private final double TICKTIME = 0.001;
	private int bufferSize;
	private int packetSize;
	private double serviceRate;
	private double lambda;
	private double totalSojournTime;
	private int idleTime;
	private int sumBufferSize;
	private int packetLoss;
	private int packetNum;
	private double packetServiceTime;
	
	
	public NetworkSimulatorComplete(double lambda, int packetSize,
			double serviceRate, int bufferSize){
		this.lambda = lambda;
		this.packetSize = packetSize;
		this.serviceRate = serviceRate;
		this.bufferSize = bufferSize;
		
	    packetQueue = new LinkedList<Packet>();
		
		totalSojournTime = 0;
		idleTime = 0;
		sumBufferSize = 0;
		packetLoss = 0;
		packetNum = 0;
		packetServiceTime =(this.packetSize/this.serviceRate)/TICKTIME;
	}
	
	private double arrivalTimeGenerator(){
		double U = Math.random();
		return (-1/lambda)*(Math.log(1-U));
	}
	
	public void discreteEventSimulator(double simulationTime){
		double ticks = simulationTime/TICKTIME;
		double nextPacketArrivalTime = 0;
		
		for(int i = 0; i < ticks; ++i){
			// First service the queue and then accept new packets to prevent
			// unnecessary packet loss. For e.g. when the queue is full and at the
			// same tick both a new packet is generated and an existing packet is
			// serviced, then if the arrival method is first executed then, there
			// will be an unnecessary packet loss.
			packetService(i);
			nextPacketArrivalTime = packetArrival(i,nextPacketArrivalTime);
			this.sumBufferSize += packetQueue.size();
		}
	}
	
	private double packetArrival(int tickTime, double packetArrivalTime){
		if((double)tickTime >= packetArrivalTime){
			// A newly generated packet is arriving. Increment packetNum
			this.packetNum++;
			// Create new packet
			Packet packet = new Packet(this.packetNum, tickTime, (int)Math.ceil(tickTime + this.packetServiceTime));
			// Add packet only if buffer is not full
			if(this.bufferSize != -1 && this.bufferSize > packetQueue.size()){
				packetQueue.add(packet);
			}
			// Buffer was full and the packet is lost
			else
				this.packetLoss++;
			// Return arrival time of next packet
			return packetArrivalTime + arrivalTimeGenerator();
		}
		
		else
			return packetArrivalTime;
	}
	
	private void packetService(int tickTime){
		// if the packet queue is not empty
		if(!packetQueue.isEmpty()){
			Packet packet = packetQueue.peek();
			if((double)tickTime >= packet.getServiceTime()){
				packet.setQueueDelayTime(tickTime - packet.getServiceTime());
				totalSojournTime += packet.getSojournTime() * TICKTIME;
				packetQueue.remove(); // service the packet and remove from queue
			}
		}
		// Queue is empty and so the server is idle
		else
			this.idleTime++;
	}
	
	public double getAverageSojournTime(){
		return this.totalSojournTime/this.packetNum;
	}
	
	public double getIdleProportion(int totalTicks){
		return this.idleTime/totalTicks;
	}
	
	public double getAverageBufferSize(int totalTicks){
		return this.sumBufferSize/totalTicks;
	}
	
	public double getPacketLossProportion(int totalTicks){
		return this.packetLoss/this.packetNum;
	}
	
}
