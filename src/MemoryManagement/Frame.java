package MemoryManagement;

	//frame structure

public class Frame {
	private int size = 4096;
	private int frameNumber;
	public byte [] data = new byte [size];
	
	public Frame (byte [] inData){
		data = inData;
		//TODO: assignFrameNumber
	}
}
