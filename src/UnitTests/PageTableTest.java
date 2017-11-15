package UnitTests;

import MemoryManagement.Page;
import MemoryManagement.PageTable;

// Page Table Structure

public class PageTableTest {
	
	public static boolean Insert_ReturnsValue_InsertsIntoPageTable(){
		PageTable pageTable = new PageTable();
		Page page = new Page(new byte[4096]);
		int logicalAdd = pageTable.insert(page);
		
		if (logicalAdd != -1 && pageTable.get(logicalAdd)) {
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public static void main(String[] args){
		
		System.out.println("True = good False = bad");
		boolean yes = Insert_ReturnsValue_InsertsIntoPageTable();
		System.out.println(yes);
	}
}
