import java.util.LinkedList;

/**
 * The network simulator class for testing packet congestion.
 */
public class NetworkSimulator {
	private LinkedList<Packet> packetQueue;
	private double tickRatio;
	private double ticks;
	private PacketGenerator generator;
	private PacketServer server;
	
	/**
	 * Create a network simulator
	 * @param ratio - The ratio of time per tick
	 */
	public NetworkSimulator(double ratio){
		packetQueue = new LinkedList<Packet>();
		generator = new PacketGenerator(packetQueue);
		server = new PacketServer(packetQueue);
		tickRatio = ratio;
	}
	
	/**
	 * Start the network simulation.
	 * @param simulationTime - the time to run the simulation for in seconds 
	 * @param lambda - average packets generation rate in packet per seconds
	 * @param L - the size of the packet in bits
	 * @param C - the link speed of the server in bit per second
	 * @param K - the maximum limit to the queue (-1 = infinity)
	 */
	public void discreteEventSimulator(double simulationTime, double lambda, double L, double C, int K){
		ticks = simulationTime/tickRatio;
		
		// Reset the environment and record the parameters
		Reporter.RecordParameters(tickRatio, ticks, lambda, L, C, K);
		generator.setup(lambda, K, tickRatio);
		server.setup(L, C, tickRatio);
		packetQueue.clear();
		
		// Run the simulation
		for(double i=0;i<ticks;i++){
			// Check if a new packet arrived?
			Boolean lostPacket = generator.arrival(i);
			
			// Check if the server has sent a packet
			Packet sentPacket = server.service(i);
			
			// Update the reports the results of last tick
			Reporter.Update(i, packetQueue.size(), sentPacket, lostPacket); 
		}
	}
}
