import java.util.LinkedList;

/**
 * Generates packets and the exponential arrival rate
 */
public class PacketGenerator {

	private LinkedList<Packet> packetQueue;	// Reference to the packet queue
	private double nextArrivalTick;			// Next tick when packet will arrival
	private int packetID;					// Record the ID of the packet
	private double lambda;					// The arrival rate in packets per second
	private int queueLimit;					// The limit of the queue size
	private double tickRatio;				// The ratio of time to ticks

	/**
	 * Create a new packet generator.
	 * @param queue - Reference to the packet queue
	 */
	public PacketGenerator(LinkedList<Packet> queue)
	{
		packetQueue = queue;
	}

	/**
	 * Process if an arrival has occurred this tick.
	 * @param ticks - The current tick in the simulation.
	 * @return True if packet lost this tick otherwise false.
	 */
	public Boolean arrival(double ticks){
		Boolean lostPacket = false;

		// Is it time to send an arrival tick?
		if(ticks >= nextArrivalTick) {
			Packet packet = new Packet(packetID++,ticks);

			// Add packet to queue unless the queue is full then drop it
			if (packetQueue.size() <= queueLimit || queueLimit == -1) {
				packetQueue.add(packet);
			} else {
				lostPacket = true;
			}

			// Calculate when the next packet should arrive
			double U = Math.random();
			double nextTime = (-1 / lambda) * Math.log(1 - U);
			double nextTick = nextTime / tickRatio;
			nextArrivalTick = ticks + nextTick;
		}

		return lostPacket;
	}

	/**
	 * Gets the next tick that a packet is due to arrive
	 * @return  The tick that a packet will arrive next
	 */
	public double getNextArrivalTick() {
		return nextArrivalTick;
	}

	/**
	 * Set up a new generator based on the simulation parameters.
	 * @param arrivalRate - The packet arrival rate in packets per second
	 * @param limit - The size limit to how many packets can be stored
	 * @param ratio - The ratio of time to ticks
	 */
	public void setup(double arrivalRate, int limit, double ratio)
	{
		nextArrivalTick = 0;
		queueLimit = limit;
		lambda = arrivalRate;
		tickRatio = ratio;
	}
}