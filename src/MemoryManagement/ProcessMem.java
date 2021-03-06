package MemoryManagement;

import java.util.ArrayList;

import Process.Process;;

public class ProcessMem {
	
	private PageTable pageTable;
	
	public void initializeMem(int memSize){
		memSize = (memSize)/4;
		pageTable = new PageTable(memSize);
	}
	
	public int totalMemorySize(){
		return (pageTable.totalMem()* 4);
	}
	
	public int memAvailable(){
		int pages = pageTable.availableMem();
		return (pages * 4); //return available memory in KB
	}
	
	public Process load(Process process){ // returns list of pages or empty list if no memory space
		
		int pageAmt = (process.memory / 1024) / 4;
		if (pageAmt <= pageTable.availableMem()){
			ArrayList<Page> pageList = new ArrayList<Page>();
			
			for (int i = 0; i < pageAmt; i++){
				Page page = new Page();
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
		for(Page p:process.pages){
			pageTable.remove(p.pageNumber);
		}
	}
	
}
