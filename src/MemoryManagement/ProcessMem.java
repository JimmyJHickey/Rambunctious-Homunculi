package MemoryManagement;

import Process.Process;;

public class ProcessMem {
	
	private PageTable pageTable;
	
	public void initializeMem(int memSize){
		pageTable = new PageTable(memSize);
	}
	
	public Page[] save(Process process){ // returns list of pages or empty list if no memory space
		
		int pageAmt = (process.memory * 1000)/4;
		if (pageTable.memSpaceCheck(pageAmt)){
			Page[] page = new Page[pageAmt];
			
			for (int i = 0; i < pageAmt; i++){
				page[i].pageNumber = pageTable.insert();
			}
			
			return page;
		}else{
			return new Page[0];
		}
		
	}
	
}
