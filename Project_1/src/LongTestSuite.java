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
	
	// Create a new simulator
	public NetworkSimulator sim = new NetworkSimulator();
	
	/**
	 * Quick test with no queue slow, at a slow arrival rate
	 */
	@Test
	public void noQueueSlow() {
		double lambda = 250;
		int length = 2000;
		double serviceSpeed = 1e+6;
		
		// Run the simulation
		sim.discreteEventSimulator(simulationTime, lambda, length, serviceSpeed, -1);
		
		// Record the results of the test
		Reporter.Report("LongTestSuite.csv");
		
		// TODO Assert outputs of test
	}
	
	/**
	 * Quick test with no queue, at a fast arrival rate
	 */
	@Test
	public void noQueueFast() {
		double lambda = 750;
		int length = 2000;
		double serviceSpeed = 1e+6;
		
		// Run the simulation
		sim.discreteEventSimulator(simulationTime, lambda, length, serviceSpeed, -1);
		
		// Record the results of the test
		Reporter.Report("LongTestSuite.csv");
		
		// TODO Assert outputs of test
	}

	/**
	 * Quick test with a queue limit, at a slow arrival rate
	 */
	@Test
	public void queue50Slow() {
		double lambda = 250;
		int length = 2000;
		double serviceSpeed = 1e+6;
		int queueLimit = 50;
		
		// Run the simulation
		sim.discreteEventSimulator(simulationTime, lambda, length, serviceSpeed, queueLimit);
		
		// Record the results of the test
		Reporter.Report("LongTestSuite.csv");
		
		// TODO Assert outputs of test
	}
	
	/**
	 * Quick test with a queue limit, at a fast arrival rate
	 */
	@Test
	public void queue50Fast() {
		double lambda = 750;
		int length = 2000;
		double serviceSpeed = 1e+6;
		int queueLimit = 50;
		
		// Run the simulation
		sim.discreteEventSimulator(simulationTime, lambda, length, serviceSpeed, queueLimit);
		
		// Record the results of the test
		Reporter.Report("LongTestSuite.csv");
		
		// TODO Assert outputs of test
	}
}
