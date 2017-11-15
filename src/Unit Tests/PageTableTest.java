package UnitTests;

 // Page Table Structure

public class PageTableTest {
	
	public static boolean Insert_ReturnsValue_InsertsIntoPageTable(){
		PageTable pageTable = new PageTable();
		Page page = new Page(new byte[4096]);
		int logicalAdd = PageTable.insert(Page);
		
		if (logicalAdd != -1 && PageTable.get(logicalAdd)) {
			return true
		}
		else{
			return false
		}
	}
	
	
	public static void main(String[] args){
		
		bool yes = Insert_ReturnsValue_InsertsIntoPageTable()
		System.out.println(yes);
	}
}
