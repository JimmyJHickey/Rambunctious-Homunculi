package MemoryManagement;

 // Page Table Structure

public class PageTable {
	private int[] pageTable = new int[255]; //TODO: give this a non-arbitrary number
	
	public void insert (Page page) {
		//TODO: map logical address to physical address
	}
	
	public int get(int pageNumber){
		return pageTable[pageNumber];
	}
}
