import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for questions 4 simulations
 */
public class Question4 {

	public NetworkSimulator sim = new NetworkSimulator();
	
	/**
	 * Run simulations for queue sizes K = [10, 25, 50]
	 */
	@Test
	public void Run() 
	{
		simQueueLimit(10);
		simQueueLimit(25);
		simQueueLimit(50);
	}
	
	
	/**
	 * Run simulations with a queue limit
	 * @param K - the limit size of the queue
	 */
	public void simQueueLimit(int K)
	{
		double M;			// Number of times to run simulation 
		double lambda;		// Packet arrival rate
		double L = 2000;	// Packet length is 2000 bits
		double C = 1e+6;	// Service speed is 1Mbps
		double rho;			// Utilization of the queue
		
		// Simulation for different simulation 
		for (rho = 0.5; rho <= 1.5; rho += 0.1)
		{
			// Calculate the arrival rate
			lambda = rho * ( C / L);
			
			// Run N simulations
			for(M = 0; M <10; M++)
			{
				// Simulate for 20 minutes
				sim.discreteEventSimulator(1200, lambda, L, C, K);
				
				// TODO Record the results of the test
				// sim.Report()
			}
		}
	}
}
