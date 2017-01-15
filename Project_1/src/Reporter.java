import java.io.*;
import java.text.*;
import java.util.Date;

/**
 * The reporter records the events of the simulation a reports them to a CSV file
 */
public class Reporter {
	private static double tickRatio;		// The ratio of time per tick
	private static double totalTicks;		// The total number of ticks in the simulation
	private static double lastTick;			// The last tick that was recorded
	private static double sumQueueSize;		// The sum of queue size of each tick
	private static double sumSojournTime;	// The sum of sojourn time of each packet
	private static double sumIdle;			// The sum of queue of ticks when the queue was empty
	private static double sumLoss;			// The sum of loss packets
	private static double sumPacketsRx;		// The sum of total packets received
	private static double sumPacketsTx;		// The sum of total packets transmitted
	private static double rho;				// The network utilization ratio
	private static double lambda;			// The arrival rate of the packets
	private static double L;				// The size of the packets in bits
	private static double C;				// The service speed of the link in Mbps
	private static double K;				// The limit of the queue (-1 = infinity)
	private static double EN;				// The average sojourn time
	private static double ET;				// The average sojourn time of a packet
	private static double P_IDLE;			// The percentage idle time of the queue
	private static double P_LOSS;			// The percentage of lost packets
	
	/**
	 * Reset the reporters records
	 */
	public static void reset()
	{
		lastTick = -1;
		sumQueueSize = 0;
		sumSojournTime = 0;
		sumIdle = 0;
		sumLoss = 0;
		sumPacketsRx = 0;
		sumPacketsTx = 0;
		rho = 0;
		lambda = 0;
		L = 0;
		C = 0;
		K = 0;
		EN = 0;
		P_IDLE = 0;
		ET = 0;
		P_LOSS = 0;
	}
	
	/**
	 * Calculate the network utilization (rho) and record parameters it for reporting 
	 * @param ratio - The ratio of time per tick
	 * @param ticks - The total number of ticks for simulation
	 * @param arrivalRate - The arrival rate of the packets
	 * @param length - The size of the packets in bits
	 * @param serviceSpeed - The service speed of the link in Mbps
	 * @param queueLimit - The limit of the queue (-1 = infinity)
	 * @return rho - The network utilization ratio
	 */
	public static double RecordParameters(double ratio, double ticks, double arrivalRate, double length, double serviceSpeed, int queueLimit)
	{
		reset();
		tickRatio = ratio;
		totalTicks = ticks;
		lambda = arrivalRate;
		L = length;
		C = serviceSpeed;
		K = (double)queueLimit;
		rho = L * ( lambda / C);
		return rho;
	}
	
	/**
	 * Update the report of what has occurred between events
	 * @param tick - The number of ticks since the beginning of the simulation
	 * @param queueSize - The number of packets in the queue 
	 * @param rxPacket - The last packet sent
	 * @param lostPacket -  If true then a packet was lost last tick
	 */
	public static void Update(double tick, int queueSize, Packet rxPacket, Boolean lostPacket)
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
		if(rxPacket != null){
			sumSojournTime += rxPacket.getSojournTime();
			sumPacketsRx++;
			sumPacketsTx++;
		}
		
		// If a packet was lost last tick then record it
		if(lostPacket) 
		{
			sumLoss++;
			sumPacketsTx++;
		}
					
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
				writer.write("now,ticks,Tx,Rx,lost,lambda,K,rho,E[N],E[T],P_IDLE,P_LOSS\n");
			}
			
			// Find the averages and ratios for simulations
			EN = sumQueueSize / totalTicks;
			P_IDLE = sumIdle / totalTicks;
			
			// Prevent divide by zero exception if no packets where sent
			if (sumPacketsRx != 0) ET = (sumSojournTime * tickRatio) / sumPacketsRx;
			if (sumPacketsTx != 0) P_LOSS = sumLoss / sumPacketsTx;
			
			// Get the current time of writing
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String now = dateFormat.format(new Date());
			
			// Write each parameter and output to match header
			String CSVFormat = "%1s,%2$f,%3$f,%4$f,%5$.3f,%6$f,%7$f,%8$f,%9$e,%10$f,%11$f,%12$f\n";
			String CSVWrite = String.format(CSVFormat, now, totalTicks, sumPacketsTx, sumPacketsRx, sumLoss, lambda, K, rho, EN, ET, P_IDLE, P_LOSS);
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
