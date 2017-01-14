import java.io.*;
import java.text.*;
import java.util.Date;

/**
 * The reporter records the events of the simulation a reports them to a CSV file
 */
public class Reporter {
	private static double totalTicks;		// The total number of ticks in the simulation
	private static double lastTick;			// The last tick that was recorded
	private static double sumQueueSize;		// The sum of queue size of each tick
	private static double sumSojournTime;	// The sum of sojourn time of each packet
	private static double sumIdle;			// The sum of queue of ticks when the queue was empty
	private static double sumLoss;			// The sum of loss packets
	private static double sumPackets;		// The sum of total packets sent
	private static double rho;				// The network utilization ratio
	private static double lambda;			// The arrival rate of the packets
	private static double L;				// The size of the packets in bits
	private static double C;				// The service speed of the link in Mbps
	private static double K;				// The limit of the queue (-1 = infinity)
	
	/**
	 * Reset the reporters records
	 */
	public static void Reset()
	{
		lastTick = -1;
		sumQueueSize = 0;
		sumSojournTime = 0;
		sumIdle = 0;
		sumLoss = 0;
		sumPackets = 0;
		rho = 0;
		lambda = 0;
		L = 0;
		C = 0;
		K = 0;
	}
	
	/**
	 * Calculate the network utilization (rho) and record parameters it for reporting 
	 * @param ticks - The total number of ticks for simulation
	 * @param arrivalRate - The arrival rate of the packets
	 * @param length - The size of the packets in bits
	 * @param serviceSpeed - The service speed of the link in Mbps
	 * @param queueLimit - The limit of the queue (-1 = infinity)
	 * @return rho - The network utilization ratio
	 */
	public static double RecordParameters(double ticks, double arrivalRate, double length, double serviceSpeed, double queueLimit)
	{
		Reset();
		totalTicks = ticks;
		lambda = arrivalRate;
		L = length;
		C = serviceSpeed;
		K = queueLimit;
		rho = L * ( lambda / C);
		return rho;
	}
	
	/**
	 * Update the report of what has occurred between events
	 * @param tick - the number of ticks since the beginning of the simulation
	 * @param queueSize - the number of packets in the queue 
	 * @param lastPacket - the last packet sent
	 * @param lostPacket -  if true then a packet was lost last tick
	 */
	public static void Update(double tick, int queueSize, Packet lastPacket, Boolean lostPacket)
	{
		// Calculate tick delta since last update
		// The time delta is used to multiply the data by skipped time
        // (assumes values do not change in between events)
		double tickDelta = tick - lastTick;
		
		// If queue empty record as idle otherwise record queue size
		if (queueSize == 0)
		{
			// Record that the queue has empty for delta ticks
			sumIdle += tickDelta;
		}
		else
		{
			// Record how big was the queue was since last update
			sumQueueSize += queueSize * tickDelta;
		}
		
		// Record the last packet's sojourn time if the packet was sent this tick
		if(lastPacket != null){
			sumSojournTime += lastPacket.getSojournTime();
			sumPackets++;
		}
		
		// If a packet was lost last tick then record it
		if(lostPacket) sumLoss++;
					
		// Remember the last tick value
		lastTick = tick;
	}
	
	/**
	 * Report on the simulation to the passed CSV file.
	 * @param filename - The name of the file to write the report to.
	 */
	public static void Report(String filename)
	{
		FileWriter writer = null;
		
		try {
			// Check if file exists and create if not does not
			File file = new File(filename);
			Boolean fileExists = file.exists();
			if (!fileExists) {
				file.createNewFile();
			}
			
			// Create the file writer
			writer = new FileWriter(file, true); 
			
			// If the file was created then write the header
			if (!fileExists) {
				writer.write("now,ticks,lambda,L,C,K,rho,E[N],E[T],P_IDLE,P_LOSS\n");
			}
			
			// Find the averages and ratios for simulations
			double EN = sumQueueSize / totalTicks;
			double P_IDLE = sumIdle / totalTicks;
			double ET = 0;
			double P_LOSS = 0;
			
			// Prevent divide by zero exception if no packets where sent
			if (sumPackets != 0)
			{
				ET = sumSojournTime / sumPackets;
				P_LOSS = sumLoss / sumPackets;
			}
			
			// Get the current time of writing
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String now = dateFormat.format(new Date());
			
			// Write each parameter and output to match header
			String CSVFormat = "%1s,%2$.0f,%3$.3f,%4$.3f,%5$.3f,%6$.3f,%7$.3f,%8$.3f,%9$.3f,%10$.3f,%11$.3f\n";
			String CSVWrite = String.format(CSVFormat, now, totalTicks, lambda, L, C, K, rho, EN, ET, P_IDLE, P_LOSS);
			writer.write(CSVWrite);
		} catch (IOException e) {
			System.out.print("Exception: failed to write to file " + filename);
			e.printStackTrace();
		}
		
		// try to close the file writer if opened
		try {
			if (writer != null)
			{
				writer.flush();
				writer.close();	
			}
		} catch (IOException e) {
			System.out.print(" Exception: failed to close file " + filename);
		}
	}
}
