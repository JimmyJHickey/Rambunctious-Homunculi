package MemoryManagement;

	//page structure

public class Page {
	private int size = 4096; //4kb
	public int pageNumber;
	public byte [] data = new byte [size];
	
	public Page (){
		//TODO: assign pageNumber
	}
}
