package MemoryManagement;

import Process.Process;;

public class ProcessMem {
	
	private PageTable pageTable;
	
	public void initializeMem(int memSize){
		pageTable = new PageTable(memSize);
	}
	
	public Page[] save(Process process){
		
		int pageAmt = (process.memory * 1000)/4;
		Page[] page = new Page[pageAmt];
		
		for (int i = 0; i < pageAmt; i++){
			page[i] = pageTable.insert();
		}
		
		return page;
	}
	
}
