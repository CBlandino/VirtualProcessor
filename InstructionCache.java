import java.util.HashMap;
import java.util.Map;

public class InstructionCache {
    private static final int CACHE_SIZE = 8;
    private static final int ACCESS_COST = 10;
    private static final int FILL_FROM_L2_COST = 50;
    private static Map<Word, Word> addressMap;
    private static Word[] cache;
    private L2Cache l2Cache;

    private InstructionCache instance;

    public InstructionCache(L2Cache l2Cache) 
    {
        addressMap = new HashMap<>();
        cache = new Word[CACHE_SIZE];
        this.l2Cache = l2Cache;
    }

    public Word read(Word address)
    {
        int index = (int) address.getUnsigned();

        if (index >= 0 && index < CACHE_SIZE && cache[index] != null)
        {
            System.out.println("Instruction Cache hit");
            Processor.clockCycle += ACCESS_COST;
            return cache[index];
        } 
        else 
        {
            System.out.println("Instruction Cache miss");            
            Word firstInstruction = l2Cache.copyToInstructionCache(address);
            
            if (firstInstruction != null)
            {
                return firstInstruction;
            } 
            else 
            {
                return l2Cache.read(address);
            }
        }
    }

    public static void fillCacheFromL2(Word[] instructions)
    {
        for (int i = 0; i < CACHE_SIZE; i++)
        {
            cache[i] = instructions[i];
            //addressMap.put(address, instructions[i]);
        }
    }
}
