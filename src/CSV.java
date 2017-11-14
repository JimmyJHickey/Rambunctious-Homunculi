import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Thanks to https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/

/*
 * Process Structure
 * Process Name, Memory Required, Bursts
 * P1, 64MB, Bursts
 * 
 * Burst Structure
 * Type, Time
 * CPU, 10, IO 12, CPU 3, IO 3, CPU 7
 * 
 * 
 * Critical Section Structure
 * CS, Name, Time
 * CS, d1, 13
 * 
 */

public class CSV {
	public static void main(String[] args) {

		String csvFile = "processes.csv";
		BufferedReader br = null;
		String line = "";
		String splitChar = ",";
		String processInfo = "";
		
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
		
	}
}
