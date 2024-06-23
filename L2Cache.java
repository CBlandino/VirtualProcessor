import java.util.Arrays;

public class L2Cache {
    private static final int CACHE_SIZE = 32;
    private static final int GROUP_SIZE = 8;
    private static final int GROUP_COUNT = CACHE_SIZE / GROUP_SIZE;
    private static final int FILL_COST = 350;
    private static final int ACCESS_COST = 20;
    private static final int FILL_INSTRUCTION_CACHE_COST = 50;
    private Word[][] cache;
    private boolean[] cacheOccupied; // Indicates if a group in cache is occupied

    public L2Cache() 
    {
        cache = new Word[GROUP_COUNT][GROUP_SIZE];
        cacheOccupied = new boolean[GROUP_COUNT];
        Arrays.fill(cacheOccupied, false);
    }

    public Word read(Word address) 
    {
        int groupIndex = (int) (address.getUnsigned() / GROUP_SIZE);

        Processor.clockCycle += ACCESS_COST;
        
        if(groupIndex >= GROUP_COUNT)
        {
        	cacheOccupied[groupIndex % GROUP_COUNT] = false;
        }
        
        if (groupIndex >= 0 && groupIndex < GROUP_COUNT && cache[groupIndex % GROUP_COUNT] != null && cacheOccupied[groupIndex])
        {
            System.out.println("L2 Cache hit");
            return cache[groupIndex][(int) (address.getUnsigned() % GROUP_SIZE)];
        } 
        else if(cacheOccupied[groupIndex % GROUP_COUNT])
        {
        	System.out.println("L2 Cache hit");
            return cache[groupIndex % GROUP_COUNT][(int) (address.getUnsigned() % GROUP_SIZE)];
        }
        else 
        {
            System.out.println("L2 Cache miss");
            Processor.clockCycle += FILL_COST;
            return fillCache(address);
        }
    }

    private Word fillCache(Word address)
    {
        int groupIndex = (int) (address.getUnsigned() / GROUP_SIZE);
        
        if(groupIndex >= GROUP_COUNT)
        {
        	groupIndex = address.getSigned() % GROUP_COUNT;
        }
        
        for (int i = 0; i < GROUP_SIZE; i++) 
        {
            cache[groupIndex][i] = MainMemory.read(address);
            // Increment the address
            address.increment();
        }
        cacheOccupied[groupIndex] = true;
        return cache[groupIndex][0];
    }

    public Word copyToInstructionCache(Word address) 
    {
        int groupIndex = (int) (address.getUnsigned() / GROUP_SIZE);
        Word firstInstruction = null;
        if (groupIndex >= 0 && groupIndex < GROUP_COUNT && cacheOccupied[groupIndex]) 
        {
            System.out.println("Copying from L2 to Instruction Cache");
            Processor.clockCycle += FILL_INSTRUCTION_CACHE_COST;
            firstInstruction = cache[groupIndex][0];
           
            InstructionCache.fillCacheFromL2(cache[groupIndex]);
        }
        return firstInstruction;
    }
    


    public void write(Word address, Word data)
    {
    	int groupIndex = (int) (address.getUnsigned() / GROUP_SIZE);
    	// Write the data to the corresponding location in the cache
    	cache[groupIndex][(int) (address.getUnsigned() % GROUP_SIZE)] = data;
    	// Write-through to main memory (assuming MainMemory.write is a write-through operation)
        MainMemory.write(address, data);
        // Increment clock cycles for both read and write operations
        Processor.clockCycle += 50;
    }
        
        


}
