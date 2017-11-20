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
	public boolean memSpaceCheck(int pageAmt){
		for(int j = 0; j < pageTable.length; j++){
			if (pageTable[j] = false){
				pageAmt --;
			}
		}
		
		if (pageAmt <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public int insert() {
		
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
