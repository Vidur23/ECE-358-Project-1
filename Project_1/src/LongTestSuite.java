import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Run some longer tests (don't run unless you want to wait)
 */
public class LongTestSuite 
{
	// Set for a 20 minute simulation
	public double simulationTime = 1200;
	
	// Set a tick time of 100 ns
	public double tickRatio = 1e-7;
	
	// Create a new simulator
	public NetworkSimulator sim = new NetworkSimulator(tickRatio);
	
	/**
	 * Run a longer test of the simulator
	 */
	@Test
	public void Run() {
		int length = 2000;
		double serviceSpeed = 1e+6;
		
		// Run for different queue sizes
		int[] queueSize = {-1, 10, 25, 50};
		for (int K : queueSize)
		{
			for(double lambda = 100; lambda <= 1000; lambda += 100)
			{
				// Run the simulation
				sim.discreteEventSimulator(simulationTime, lambda, length, serviceSpeed, K);
				
				// Record the results of the test
				Reporter.Report("LongTestSuite.csv");
				
				// TODO Assert outputs of test
			}
		}
	}
}
