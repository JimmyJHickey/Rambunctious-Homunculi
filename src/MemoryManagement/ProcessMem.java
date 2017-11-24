package MemoryManagement;

import Process.Process;;

public class ProcessMem {
	
	private PageTable pageTable;
	
	public void initializeMem(int memSize){
		pageTable = new PageTable(memSize);
	}
	
	public int memAvailable(){
		int pages = pageTable.availableMem();
		return (pages * 4)/1000; //return available memory in MB
	}
	
	public Page[] load(Process process){ // returns list of pages or empty list if no memory space
		
		int pageAmt = (process.memory * 1000)/4;
		if (pageAmt <= pageTable.availableMem()){
			Page[] page = new Page[pageAmt];
			
			for (int i = 0; i < pageAmt; i++){
				page[i].pageNumber = pageTable.insert();
			}
			
			return page;
		}else{
			return new Page[0];
		}
		
	}
	
	public void unload(Process process){
		
	}
	
}
