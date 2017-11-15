package MemoryManagement;

 // Page Table Structure

public class PageTable {
	
	private boolean[] pageTable = new boolean[4096];
	
	public PageTable(){
		for (int i = 0; i < pageTable.length; i++){
			pageTable[i] = false;
		}
	}
	
	
	public int insert(Page page) {
		//TODO: map logical address to physical address
		
		for (int i = 0; i < pageTable.length; i++){
			
			if (!pageTable[i]){
				pageTable[i]= true;
				return i;
			}
		}
		
		return -1;
		
	}// insert
	
	public boolean get(int pageNumber){
		return pageTable[pageNumber];
	}//get
}
