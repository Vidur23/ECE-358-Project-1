import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for questions 4 simulations
 */
public class Question4 {

	// Set a tick time of 1 us
	public double tickRatio = 1e-6;
	
	// Create a new simulator
	public NetworkSimulator sim = new NetworkSimulator(tickRatio);
	
	/**
	 * Run simulations for queue sizes K = [10, 25, 50]
	 */
	@Test
	public void Run() 
	{
		double M;			// Number of times to run simulation 
		double lambda;		// Packet arrival rate
		double L = 2000;	// Packet length is 2000 bits
		double C = 1e+6;	// Service speed is 1Mbps
		double rho;			// Utilization of the queue
		
		// Run for different queue sizes
		int[] queueSize = {10, 25, 50};
		for (int K : queueSize)
		{
			// Simulation for different simulation 
			for (rho = 0.5; rho <= 1.5; rho += 0.1)
			{
				// Calculate the arrival rate
				lambda = rho * ( C / L);
				
				// Run N simulations
				for(M = 0; M <10; M++)
				{
					// Simulate for 20 minutes
					sim.discreteEventSimulator(600, lambda, L, C, K);
					
					// Record the results of the test
					Reporter.Report("Q4.csv");
				}
			}
		}
	}
}
