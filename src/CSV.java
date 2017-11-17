import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Thanks to https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/

/*
 * Process Structure
 * Process Name, Number, Arrival Time, Priority, niceness, Memory Required (bytes), Bursts
 * P1,           5,      2,            99,       0,        64,                      <Bursts>
 * 
 * Burst Structure
 * Type, Time
 * CPU,  10, IO 12, CPU 3, IO 3, CPU 7
 * 
 * 
 * Critical Section Structure
 * CS, Name, Time
 * CS, d1,   13
 * 
 */

public class CSV {
	
	final private static int NAME_INDEX = 0;
	final private static int NUMBER_INDEX = 1;
	final private static int ARRIVAL_TIME_INDEX = 2;
	final private static int PRIORITY_INDEX = 3;
	final private static int NICENESS_INDEX = 4;
	final private static int MEMORY_INDEX = 5;
	final private static int BURST_START_INDEX = 6;
	
	final private static int CPU_IO_BURST_SIZE = 2;
	final private static int CPU_IO_TIME_OFFSET = 1;
	
	final private static int CS_BURST_SIZE = 3;
	final private static int CS_NAME_OFFSET = 1;
	final private static int CS_TIME_OFFSET = 2;
	
	private static String csvFile = "data/processes.csv";
	private static BufferedReader br = null;
	private static String line = "";
	private static String splitChar = ",";
	private static String processInfo = "";
	
	public static ArrayList<Process> parseProcessCSV(String filepath) 
	{
		ArrayList<Process> returnList = new ArrayList<Process>();
		Process buildProcess = null;
		
		boolean build_cpuBurst = true;
		boolean build_criticalSection = false;
		char build_lock = '~';
		int build_length = 0;
		
		try 
		{
			br = new BufferedReader(new FileReader(csvFile));
			
			while((line = br.readLine()) != null ) 
			{
				System.out.printf("%s\n", line);
				processInfo = "";
				String[] processArr = line.split(splitChar);
				
				buildProcess = new Process();
				buildProcess.name = processArr[NAME_INDEX];
				buildProcess.number = Integer.parseInt(processArr[NUMBER_INDEX]);
				buildProcess.arrivalTime = Integer.parseInt(processArr[ARRIVAL_TIME_INDEX]);
				buildProcess.priority = Integer.parseInt(processArr[PRIORITY_INDEX]);	
				buildProcess.niceness = Integer.parseInt(processArr[NICENESS_INDEX]);
				buildProcess.memory = Integer.parseInt(processArr[MEMORY_INDEX]);
				
				processInfo += String.format("Name: %s ", buildProcess.name);
				processInfo += String.format("Number: %d ", buildProcess.number);
				processInfo += String.format("Arrival Time: %d ", buildProcess.arrivalTime);
				processInfo += String.format("Priority: %d ", buildProcess.priority);
				processInfo += String.format("Niceness: %d ", buildProcess.niceness);
				processInfo += String.format("Memory: %d\n", buildProcess.memory);
				
				int i = BURST_START_INDEX;
				String var = "";
				
				// Could do while i < length of array ? --Why not both?
				innerWhile:
				while( i < processArr.length && (var = processArr[i]) != null) 
				{	
					switch (var) 
					{
						case "CPU": 							
							build_cpuBurst = true;
							build_criticalSection = false;
							build_lock = '~';
							build_length = Integer.parseInt(processArr[i + CPU_IO_TIME_OFFSET]);	
							
							processInfo += String.format("CPU Burst: %d\n", build_length);
							
							i += CPU_IO_BURST_SIZE;
							break;
							
						case "IO": 
							build_cpuBurst = false;
							build_criticalSection = false;
							build_lock = '~';
							build_length = Integer.parseInt(processArr[i + CPU_IO_TIME_OFFSET]);
							
							processInfo += String.format("IO Burst: %d\n", build_length);
							
							i += CPU_IO_BURST_SIZE;
							break;
							
						case "CS":
							build_cpuBurst = true;
							build_criticalSection = true;
							build_lock = processArr[i + CS_NAME_OFFSET].charAt(0);
							build_length = Integer.parseInt(processArr[i + CS_TIME_OFFSET]);
							
							processInfo += String.format("Critical Section: %d\n", build_length);
							
							i += CS_BURST_SIZE;
							break;
							
						default:
							processInfo += " If you are here then something went horribly wrong.";
							break innerWhile;
					} // end switch	
					
					buildProcess.bursts.add(new Burst(build_cpuBurst, build_criticalSection, build_lock, build_length));
				} // end inner while
				
				System.out.printf("%s\n", processInfo);
				returnList.add(buildProcess);
			}// end outer while
			
		} 
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if (br != null) 
			{
				try 
				{
					br.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
	
		return returnList;
	}
	
	
	
	public static void main(String[] args)
	{

		parseProcessCSV(csvFile);
		
		/*
		try {
			br = new BufferedReader(new FileReader(csvFile));
			
			while((line = br.readLine()) != null ) {
				String[] processArr = line.split(splitChar);
				
				processInfo += "Name: " + processArr[0];
				processInfo += " Size: " + processArr[1];
				
				int i = 2;
				String var = "";
				
				// Could do while i < length of array ?
				while( (var = processArr[i]) != null) {
					switch (var) {
					case "CPU": processInfo += " CPU Burst: " + processArr[i+1];
						i +=2;
						break;
					case "IO": processInfo += " IO Burst: " + processArr[i+1];
						i +=2;
						break;
					case "CS": processInfo += " Critical Section " + processArr[i+1] + ": " + processArr[i+2];
						i+=3;
						break;
					default:
						processInfo += " If you are here then something went horribly wrong.";
					}	
				}
			}
			System.out.println(processInfo);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		*/
	}
}
