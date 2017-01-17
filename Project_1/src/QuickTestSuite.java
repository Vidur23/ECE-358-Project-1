import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Run some shorter tests
 */
public class QuickTestSuite 
{
	// Set for a minute simulation
	public double simTime = 1200;
	
	// Set a short tick time of 10 us
	public double tickRatio = 1e-5;
	
	// Create a new simulator
	public NetworkSimulator sim = new NetworkSimulator(tickRatio);
	
	/**
	 * Run a quick test of the simulator
	 */
	@Test
	public void Run() {
		int length = 2000;
		double serviceSpeed = 1e+6;
		
		// Run for different queue sizes
		int[] queueSize = {-1, 10, 25, 50};
		for (int K : queueSize)
		{
			System.out.print("simTime, lambda, L, C, K\n");
			
			for(double lambda = 100; lambda <= 1000; lambda += 100)
			{
				// Run the simulation
				sim.discreteEventSimulator(simTime, lambda, length, serviceSpeed, K);
				
				// Record the results of the test
				Reporter.Report("QuickTestSuite.csv");
				
				// TODO Assert outputs of test
				
				// Output results to console
				String simFormat = "%1$f, %2$f, %3$d, %4$f, %5$d\n";
				String simResults = String.format(simFormat, simTime, lambda, length, serviceSpeed, K);
				System.out.print(simResults);
			}
		}
	}
}
