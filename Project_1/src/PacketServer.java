import java.util.LinkedList;

/**
 * Services packets a constant rate.
 */
public class PacketServer {
	private LinkedList<Packet> packetQueue;	// Reference to the packet queue
	private Packet packetBuffer;			// A spot to store the packet while it serviced
	private double nextServiceTick;			// Next tick when packet will be served
	private double serviceTicks;			// The amount of ticks to service a packet
	
	/**
	 * Create a new packet server. 
	 * @param queue - Reference to the packet queue.
	 */
	public PacketServer(LinkedList<Packet> queue)
	{
		packetQueue = queue;
	}
	
	/**
	 * Is the queue idle?
	 * @return True if both queue and buffer are empty, false otherwise.
	 */
	public Boolean isIdle()
	{
		return packetQueue.size() == 0 && packetBuffer == null;
	}
	
	/**
	 * Process the server to see if a new packet can be served
	 * @param ticks - The current tick in the simulation.
	 * @return The packet that is ready to be sent.
	 */
	public Packet service(double ticks){
		// Remember if a packet is sent
		Packet sendPacket = null;
		
		// If there is a packet in the buffer and service time is done then send packet
		if(packetBuffer != null && ticks >= nextServiceTick){
			sendPacket = packetBuffer;
			sendPacket.setServiceTime(ticks);
			packetBuffer = null;
		}
		
		// If the queue is not empty and there is room in the buffer then service the packet
		if(!packetQueue.isEmpty() && packetBuffer == null){
			packetBuffer = packetQueue.remove();
			packetBuffer.setQueueDelayTime(ticks);
			nextServiceTick = ticks + serviceTicks;
		}
	
		return sendPacket;
	}
	
	/**
	 * Gets the next tick that a packet is served
	 * @return The tick that a packet will be served
	 */
	public double getNextServiceTick() {
		return nextServiceTick;
	}
	
	/**
	 * Setup a new generator based on the simulation parameters.
	 * @param length - The length of the packet in bits
	 * @param linkSpeed - The link speed in bit per second
	 * @param ratio - The ratio of time to ticks 
	 */
	public void setup(double length, double linkSpeed, double ratio)
	{
		// Reset server
		packetBuffer = null;
		nextServiceTick = 0;
		
		// Calculate how long it takes to service a packet
		double serviceTime = (length / linkSpeed);
		serviceTicks =  serviceTime / ratio;
	}
}
