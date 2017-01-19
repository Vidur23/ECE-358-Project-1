import java.util.LinkedList;

/**
 * The network simulator class for testing packet congestion.
 */
public class NetworkSimulator {
	private LinkedList<Packet> packetQueue;	// The packet queue of the server
	private double tickRatio;				// The time per tick
	private double ticks;					// The total number of ticks to run the simulation
	private PacketGenerator generator;		// Monitors the packet arrival rate
	private PacketServer server;			// Monitors the packet service time

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
	public void discreteEventSimulator(double simulationTime, double lambda, 
			double L, double C, int K){
		
		// Reset the environment and record the parameters
		ticks = simulationTime/tickRatio;
		Reporter.RecordParameters(tickRatio, ticks, lambda, L, C, K);
		generator.setup(lambda, K, tickRatio);
		server.setup(L, C, tickRatio);
		packetQueue.clear();

		// Run the simulation
		double i = 0;
		while (i <= ticks) {
            // Record queue size before the 
            int queueSize =  packetQueue.size();
        
			// Check if a new packet arrived?
			Boolean lostPacket = generator.arrival(i);

			// Check if the server has sent a packet
			Packet sentPacket = server.service(i);

			// Update the reports the results of last tick
			Reporter.Update(i, queueSize, sentPacket, lostPacket); 

			// Advance to the next event: packet arrival or packet serviced
			double nextArrival = generator.getNextArrivalTick();
			double nextService = server.getNextServiceTick();
			double nextTick = nextArrival;

			// If the server is not idle than check if the service is done before next arrival
			if (!server.isIdle() && nextTick > nextService)	{
				nextTick = nextService;
			}

			// Update the reporter of the results of last tick for the metrics
			if (nextTick > ticks) {
				Reporter.Update(nextTick, packetQueue.size(), null, false);
			}

			// Advance to the next event time
			i = nextTick;
		}
	}
}