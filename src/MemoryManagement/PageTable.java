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
	
	public int totalMem(){
		return pageTable.length;
	}
	
	public int availableMem(){
		int availablePages = 0;
		for(int j = 0; j < pageTable.length; j++){
			if (!pageTable[j]){
				availablePages++;
			}
		}
		return availablePages;
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
	
	public void remove(int i) {
		pageTable[i]= false;
	}// remove
	
	public boolean get(int pageNumber){
		return pageTable[pageNumber];
	}//get
}
