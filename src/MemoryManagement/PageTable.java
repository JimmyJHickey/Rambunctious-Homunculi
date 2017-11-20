package MemoryManagement;

 // Page Table Structure

public class PageTable {
	
	private boolean[] pageTable ;
	
	public PageTable(int availableMem){ // in MB
		
		int kbmem = availableMem * 1000; //convert from MB to KB
		pageTable = new boolean[kbmem/4]; //divide by frame size
		
		for (int i = 0; i < pageTable.length; i++){
			pageTable[i] = false;
		}
	}
	
	public Page insert() {
		//TODO: map logical address to physical address
		
		for (int i = 0; i < pageTable.length; i++){
			
			if (!pageTable[i]){
				pageTable[i]= true;
				Page page = new Page();
				page.pageNumber = i;
				return page;
			}
		}
		
		
	}// insert
	
	public boolean get(int pageNumber){
		return pageTable[pageNumber];
	}//get
}
