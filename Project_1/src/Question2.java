import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for question 2 simulations
 */
public class Question2 {

	// Create a new simulator
	public NetworkSimulator sim = new NetworkSimulator();
	
	/**
	 * Create a new simulator for test suite
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Create a new simulator
		sim = new NetworkSimulator();
	}
	
	/**
	 * Run simulations for no queue size
	 */
	@Test
	public void Run() 
	{
		double M;			// Number of times to run simulation 
		double lambda;		// Packet arrival rate
		double L = 2000;	// Packet length is 2000 bits
		double C = 1e+6;	// Service speed is 1Mbps
		double rho;			// Utilization of the queue
		
		// Simulation for different simulation 
		for (rho = 0.2; rho <= 0.9; rho += 0.1)
		{
			// Calculate the arrival rate
			lambda = rho * ( C / L);
			
			// Run M simulations
			for(M = 0; M < 10; M++)
			{
				// Simulate for 20 minutes
				sim.discreteEventSimulator(1200, lambda, L, C, -1);
				
				// Record the results of the test
				Reporter.Report("Q2.csv");
			}
		}
	}
}
