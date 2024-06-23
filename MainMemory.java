
public class MainMemory
{

	private static Word[] memory = new Word[1024];
	
	
	/**
     * Reads a word from the memory at the specified address.
     *
     * @param address The memory address to read from.
     * @return The word read from the memory.
     * @throws IllegalArgumentException if the memory address is invalid.
     */
	public static Word read(Word address)
	{
		//Processor.clockCycle += 300;
		int index = (int) address.getUnsigned(); // Get the unsigned value of the address
		
		if (index >= 0 && index < memory.length)
		{
			if(memory[index] == null)
			{
				memory[index] = new Word(0);
			}
		
			Word readMem = new Word();
        	readMem.copy((memory[index]));
        	
            return readMem; // Return a new Word object
        } 
        else 
        {
            throw new IllegalArgumentException("----- Invalid memory address " + index + " != " + address.getSigned() + " -----");
        }
		
	}
	
	public static Word[] readBlock(Word startAddress, Word endAddress) 
	{
		//Processor.clockCycle += 300;
		// Get the unsigned value of the start address
	    int startIndex = (int) startAddress.getUnsigned(); 
	    // Get the unsigned value of the end address
	    int endIndex = (int) endAddress.getUnsigned(); 
	    
	    if (startIndex >= 0 && endIndex >= startIndex && endIndex < memory.length)
	    {
	        int blockSize = endIndex - startIndex + 1; // Calculate the size of the block
	        Word[] block = new Word[blockSize];
	        
	        for (int i = 0; i < blockSize; i++) 
	        {
	            if (memory[startIndex + i] == null)
	            {
	                memory[startIndex + i] = new Word(0);
	            }
	            block[i] = new Word();
	            block[i].copy(memory[startIndex + i]);
	        }
	        
	        return block;
	    } 
	    
	    else
	    {
	        throw new IllegalArgumentException("Invalid memory addresses or block size exceeds memory");
	    }
	}

	
	/**
     * Writes a word to the memory at the specified address.
     *
     * @param address The memory address to write to.
     * @param value   The value to be written to memory.
     * @throws IllegalArgumentException if the memory address is invalid.
     */
	public static void write(Word address, Word value)
	{
		//Processor.clockCycle += 300;
			int index = (int) address.getUnsigned(); // Get the unsigned value of the address
			
			if (index >= 0 && index < memory.length)
			{
				
				if(memory[index] == null)
				{
					memory[index] = new Word(0);
				}
				
					memory[index].copy(value);
					
				}
			
			else 
			{
	            throw new IllegalArgumentException("----- Invalid memory address "+index+"-----");
	        }
			
		
	}
	
	/**
     * Loads data into the memory.
     *
     * @param data An array of binary strings representing data to be loaded into memory.
     * @throws IllegalArgumentException if the data size exceeds memory capacity.
     */
	public static void load(String[] data)
	{
		Processor.clockCycle += 300;
		int dataSize = data.length;
	       
	       if (dataSize > memory.length) 
	       {
	    	   throw new IllegalArgumentException("----- Data size exceeds memory capacity -----");
	       }
		
		for (int i = 0; i < data.length && i < memory.length; i++)
		{
            Word word = new Word();
            
            for (int j = 0; j < 32 && j < data[i].length(); j++) 
            {
                char bitChar = data[i].charAt(j);
                boolean bit = bitChar == '1';
                
                word.setBit(j, new Bit(bit));
                
            }
            memory[i] = word;
        }
	}

	

	
}
