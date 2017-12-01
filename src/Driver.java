
public class Driver 
{

	public static void main(String[] args) 
	{
		Kernel kernel = new Kernel();
		
		kernel.runKernel("data/processes1.csv", 512 * 1024, 2, 10);

	}

}
