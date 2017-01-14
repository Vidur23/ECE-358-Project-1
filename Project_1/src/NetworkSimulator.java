import java.util.LinkedList;

public class NetworkSimulator {
	// FROM ECLIPSE
	private LinkedList<Packet> packetQueue;
	private static final double TICKTIME = 0.001;
	private static int packetID;
	private static double totalSojournTime;
	private static int idleTime;
	private static int sumBufferSize;
	private static double lambda;
	private static int packetLength;
	public NetworkSimulator(){
		packetQueue = new LinkedList<Packet>();
		packetID = 0;
		totalSojournTime = 0;
		idleTime = 0;
		sumBufferSize = 0;
	}
	
	private double arrivalTimeGenerator(double lambda){
		double U = Math.random();
		return (-1/lambda)*(Math.log(1-U));
	}
	
	/**
	 * Start the network simulation.
	 * @param simulationTime - the time to run the simulation for in seconds 
	 * @param lambda - average packets generation rate in packet per seconds
	 * @param L - the size of the packet in bits
	 * @param C - the link speed of the server in bit per second
	 * @param K - the maximum limit to the queue (-1 = infinity)
	 */
	public void discreteEventSimulator(double simulationTime, double lambda, double L, double C, double K){
		double ticks = simulationTime/TICKTIME;
		double nextPacketArrivalTime = 0;
		
		// Reset the reporter and record the parameters
		Reporter.RecordParameters(ticks, lambda, L, C, K);
		
		for(int i=0;i<ticks;i++){
			nextPacketArrivalTime = arrival(i,nextPacketArrivalTime,lambda);
			service(i);
			updateStatistics();
			
			// Update the reports the results of last tick
			Reporter.Update(i, packetQueue.size(), null, false); 
		}
	}
	
	private double arrival(int tickTime,double packetArrivalTime, double lambda){
		if((double)tickTime >= packetArrivalTime){
			Packet packet = new Packet(packetID++,tickTime,tickTime + 4);
			packetQueue.add(packet);
			return packetArrivalTime + arrivalTimeGenerator(lambda);
		}
		
		return packetArrivalTime;
	}
	
	private void service(int tickTime){
		if(!packetQueue.isEmpty()){
		Packet packet = packetQueue.peek();
		if((double)tickTime >= packet.getServiceTime()){
			packet.setQueueDelayTime(tickTime - packet.getServiceTime());
			totalSojournTime += packet.getSojournTime() * TICKTIME;
			packetQueue.remove();
		}

		}
	}
	
	public double getAverageSojournTime(){
		return totalSojournTime/packetID;
	}
	
	public double getIdleProportion(int totalTicks){
		return idleTime/totalTicks;
	}

	private void updateStatistics(){
		if(packetQueue.isEmpty())
			idleTime++;
		else
			sumBufferSize += packetQueue.size();
	}
	
	public double getAverageBufferSize(int totalTicks){
		return sumBufferSize/totalTicks;
	}
}
