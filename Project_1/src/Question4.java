import org.junit.Test;

/**
 * Unit tests for questions 4 simulations
 */
public class Question4 {
	// The length time that simulation will run for in seconds
	public double simTime = 600;
	
	// Create a new simulator
	public NetworkSimulator sim = new NetworkSimulator(1);
	
	/**
	 * Run simulations for queue sizes K = [10, 25, 50]
	 */
	@Test
	public void Run() 
	{
		double M;			// Number of times to run simulation 
		double lambda;		// Packet arrival rate
		double L = 2000;	// Packet length is 2000 bits
		double C = 1e+6;	// Service speed is 1 Mbps
		double rho;			// Utilization of the queue
		
		// Run for different queue sizes
		int[] queueSize = {10, 25, 50};
		for (int K : queueSize)
		{
			// Simulation for different simulation 
			for (rho = 0.5; rho < 1.55; rho += 0.1)
			{
				// Calculate the arrival rate
				lambda = rho * ( C / L);
				
				System.out.print("M, simTime, lambda, L, C, K\n");
				
				// Run N simulations
				for(M = 0; M <10; M++)
				{
					// Simulate for 20 minutes
					sim.discreteEventSimulator(simTime, lambda, L, C, K);
					
					// Record the results of the test
					Reporter.Report("Q4.csv");
					
					String simFormat = "%1$f, %2$f, %3$f, %4$f, %5$f, %6$d\n";
					String simResults = String.format(simFormat, M, simTime, lambda, L, C, K);
					System.out.print(simResults);
				}
			}
		}
	}
}