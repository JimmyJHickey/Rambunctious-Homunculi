package MemoryManagement;

import java.util.ArrayList;

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
	
	public Process load(Process process){ // returns list of pages or empty list if no memory space
		
		int pageAmt = (process.memory * 1000)/4;
		if (pageAmt <= pageTable.availableMem()){
			ArrayList<Page> pageList = new ArrayList<Page>();
			Page page = new Page();
			
			for (int i = 0; i < pageAmt; i++){
				page.pageNumber = pageTable.insert();
				pageList.add(page);
			}
			
			process.pages = pageList;
			return process;
		}else{
			return process;
		}
		
	}
	
	public void unload(Process process){
		
		int pageAmt = (process.memory * 1000)/4;
		
		for (int i = 0; i < pageAmt; i++){
			Page page = process.pages.get(i);
			pageTable.remove(page.pageNumber);
		}
	}
	
}
