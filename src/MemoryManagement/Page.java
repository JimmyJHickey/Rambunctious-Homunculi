package MemoryManagement;

	//page structure

public class Page {
	private int size = 255;
	public int pageNumber;
	public byte [] data = new byte [size];
	
	public Page (byte [] inData){
		data = inData;
		//TODO: assign pageNumber
		//TODO: save as frame
		//TODO: map to frame
	}
}
