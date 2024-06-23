import java.util.Arrays;

public class Processor
{
	 /** Program Counter (PC) register. */
	private Word PC;
	private Word PCHolder;
	 /** Stack Pointer (SP) register. */
    private Word  SP = new Word(1024);; 
    /** Current instruction being executed. */
    private Word currentInstruction;
    /** Flag indicating if the processor is halted. */
    private Bit halted;
    
    private ALU alu = new ALU();
    
    Word opcode = new Word();
    Word registerS = new Word();
    Word registerS2 = new Word();
    Word registerD = new Word();
    Word immediateValue = new Word();
    
    Bit [] functionBits = new Bit[4];
    Bit [] registerDbits = new Bit[5];
    Bit [] registerSbits = new Bit[5];
    Bit [] registerS2bits = new Bit[5];
    
    private Word[] registers;
    private InstructionCache instructionCache;
    private L2Cache l2Cache;
    private Word[] cache;
    public static int clockCycle = 0;
    
    /**
     * Constructs a new Processor object.
     * Initializes the PC, SP, currentInstruction, and halted.
     */
    public Processor() 
    {
        PC = new Word(0);
        PCHolder = new Word(0);
        currentInstruction = new Word();
        halted = new Bit();
        halted.set();
        
        registers = new Word[32];
        for (int i = 0; i < 32; i++)
        {
            registers[i] = new Word();
        }
        
        l2Cache = new L2Cache();
        instructionCache = new InstructionCache(l2Cache);
    }
    
    /**
     * Runs the processor.
     * Continuously executes fetch-decode-execute-store cycle until halted.
     */
    public void run() 
    {
    	
    	while(halted.getValue())
    		{
    			fetch();
    			decode();
           		execute();
            	store();
            	//clockCycle++;
    		}
    	System.out.println("******HALTED******");
    	System.out.println("Total clock cycles: " + clockCycle);
    }
    
    /**
     * Fetches the next instruction from memory and increments the program counter.
     */
    private void fetch()
    {
    	System.out.println("PC: " + PC.getSigned());
    	PCHolder.copy(PC);
    	currentInstruction.copy(instructionCache.read(PCHolder));
    	System.out.println(currentInstruction);
    	
    	PC.increment();
    }
    
    /**
     * Decodes the current instruction.
     */
    private void decode() 
    {
        // Extract the last 2 bits of the instruction to determine the instruction format
        opcode = currentInstruction.getLowest5BitsRightShift(0);
        System.out.println("\nOPCODE : " + currentInstruction.getLowest5BitsRightShift(0));

        // Determine instruction format based on the opcode
        
        // No R: get the immediate value from the instruction
        if(opcode.getBit(31).getValue() == false && opcode.getBit(30).getValue() == false)
        {
        	immediateValue = currentInstruction.getLowest5BitsRightShift(5);
        	System.out.println("DECODE '00' COMPLETE");
        }
        
        // Destination Only: Get the Rd value from the registers and the immediate value from the instruction
        else if(opcode.getBit(30).getValue() == false && opcode.getBit(31).getValue() == true)
        {
        	registerD = currentInstruction.getLowest5BitsRightShift(5);
        	
        	 registerDbits[0] = registerD.getBit(31);
             registerDbits[1] = registerD.getBit(30);
    		 registerDbits[2] = registerD.getBit(29);
    		 registerDbits[3] = registerD.getBit(28);
    		 registerDbits[4] = registerD.getBit(27);
    		 
    		 functionBits[0] = currentInstruction.getBit(21);
    		 functionBits[1] = currentInstruction.getBit(20);
    		 functionBits[2] = currentInstruction.getBit(19);
    		 functionBits[3] = currentInstruction.getBit(18);
        	
            immediateValue = currentInstruction.rightShift(14);
            System.out.println("DECODE '01' COMPLETE");
        }

     // 2R: Get the Rs and Rd values from the registers and the immediate value from the instruction
        else if(opcode.getBit(30).getValue() == true && opcode.getBit(31).getValue() == true)
        {
            registerS = currentInstruction.getLowest5BitsRightShift(14);
            
            registerSbits[0] = registerS.getBit(31);
   		 	registerSbits[1] = registerS.getBit(30);
   		 	registerSbits[2] = registerS.getBit(29);
   		 	registerSbits[3] = registerS.getBit(28);
   		 	registerSbits[4] = registerS.getBit(27);
            
            registerD = currentInstruction.getLowest5BitsRightShift(5);
            
            registerDbits[0] = registerD.getBit(31);
            registerDbits[1] = registerD.getBit(30);
   		 	registerDbits[2] = registerD.getBit(29);
   		 	registerDbits[3] = registerD.getBit(28);
   		 	registerDbits[4] = registerD.getBit(27);
            
   		 	functionBits[0] = currentInstruction.getBit(21);
   		 	functionBits[1] = currentInstruction.getBit(20);
   		 	functionBits[2] = currentInstruction.getBit(19);
		 	functionBits[3] = currentInstruction.getBit(18);
            
            immediateValue = currentInstruction.rightShift(19);
            System.out.println("DECODE '11' COMPLETE");
        }
        
        // 3R: Get the Rs1, Rs2 and Rd values from the registers and the immediate value from the instruction
        else if(opcode.getBit(30).getValue() == true && opcode.getBit(31).getValue() == false)
        {
        	registerS = currentInstruction.getLowest5BitsRightShift(14);
        	
        	registerSbits[0] = registerS.getBit(31);
   		 	registerSbits[1] = registerS.getBit(30);
   		 	registerSbits[2] = registerS.getBit(29);
   		 	registerSbits[3] = registerS.getBit(28);
   		 	registerSbits[4] = registerS.getBit(27);
        	
            registerS2 = currentInstruction.getLowest5BitsRightShift(19);
            
            registerS2bits[0] = registerS2.getBit(31);
            registerS2bits[1] = registerS2.getBit(30);
   		 	registerS2bits[2] = registerS2.getBit(29);
   		 	registerS2bits[3] = registerS2.getBit(28);
   		 	registerS2bits[4] = registerS2.getBit(27);
            
            registerD = currentInstruction.getLowest5BitsRightShift(5);
            
            registerDbits[0] = registerD.getBit(31);
            registerDbits[1] = registerD.getBit(30);
   		 	registerDbits[2] = registerD.getBit(29);
   		 	registerDbits[3] = registerD.getBit(28);
   		 	registerDbits[4] = registerD.getBit(27);
            
            functionBits[0] = currentInstruction.getBit(21);
            functionBits[1] = currentInstruction.getBit(20);
            functionBits[2] = currentInstruction.getBit(19);
   		 	functionBits[3] = currentInstruction.getBit(18);
            
            immediateValue = currentInstruction.rightShift(24);
            System.out.println("DECODE '10' COMPLETE");
        }
   
        else
        {
        	throw new RuntimeException("----- Invalid opcode in processor -----");
        }
    }

    /**
     * Executes the current instruction.
     */
    private void execute() 
    {
    	System.out.println("\nexecute started");
    	
    	System.out.println("execute opcode " +  opcode + "\n"); 

         // If all bits of the opcode are 0, halt the processor
    	/** OPCODE (00) **/
    	if (opcode.getBit(30).getValue() == false && opcode.getBit(31).getValue() == false)
    	{
    		// If opcode is MATH (000), execute ALU operation
    		if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false )
    		{
    			//HALT
    			System.out.println("HALTING");
    			//clockCycle = clockCycle/10;
        		halted.toggle(); 
        		return;
    		}
    		
    		// If opcode is Branch (001), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true )
    		{
    			//JUMP
    			System.out.println("BRANCH -> JUMP TO");
    			PC.copy((immediateValue));
    		}
    		// If opcode is Call (010), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{ 			
    			System.out.println("BRANCH -> JUMP TO");
    			
    	        SP.decrement(); // Decrement stack pointer
    	        
    	        // Push the current PC onto the stack
    	        MainMemory.write(SP, PC);
    	        
    	        PC.copy(immediateValue);	
    		}
	       	 
    		// If opcode is Push (011), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == true)
    		{
    			//UNUSED
    		}
	       	 
    		// If opcode is Load (100), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false)
    		{
    			System.out.println("||||||||||||" + PC.getUnsigned() + " " + SP.getUnsigned() + "||||||||||||||||");
    			PC.copy(MainMemory.read(SP)); // Increment stack pointer and read value
    			SP.increment();
    		}
	       	 
    		// If opcode is Store (101), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
    		{
    			//UNUSED
    		}
    		
    		// If opcode is Pop/Interupt (110), execute ALU operation
       	 	else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
       	 	{
       	 		//Unimplemented
       	 	}
	       	 
    	}
	    		

    	/** OPCODE (11) **/
    	else if (opcode.getBit(30).getValue() == true && opcode.getBit(31).getValue() == true)
    	{ 
        	 // If opcode is Math (000), execute ALU operation
        	 if (opcode.getBit(29).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(27).getValue() == false)
        	 {
        		 System.out.println("Starting opcode... \nMATH (000)\n");
        		         		 
        		 // Set OP1 and OP2 on the ALU appropriately
        		 alu.op1.copy(registers[decodeRd(registerDbits)]);
				 alu.op2.copy(registers[decodeRd(registerSbits)]);
             
        		 alu.doOperation(functionBits);
             
        	 }
        	 
        	 // If opcode is Branch (001), execute ALU operation
        	 else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true )
        	 {
        		 alu.op2.copy(registers[decodeRd(registerDbits)]);
				 alu.op1.copy(registers[decodeRd(registerSbits)]);
        		 
        		 if(alu.doBOP(functionBits)) 
        		 {
        			 PC.copy(PC.add(immediateValue));
        		 }
        	 }
        	 
        	 // If opcode is Call (010), execute ALU operation
        	 else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
        	 {
        		System.out.println("Starting opcode... \nCALL (010)\n");
        		
        		alu.op2.copy(registers[decodeRd(registerDbits)]);
				alu.op1.copy(registers[decodeRd(registerSbits)]);     	        
        	 }
        	 
        	 // If opcode is Push (011), execute ALU operation
        	 else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == true)
        	 {
        		 System.out.println("Starting opcode... \nPUSH (011)\n");
        		 
        		 alu.op1.copy(registers[decodeRd(registerDbits)]);
				 alu.op2.copy(registers[decodeRd(registerSbits)]);
				 alu.doOperation(functionBits);
        	 }
        	 
        	 // If opcode is Load (100), execute ALU operation
        	 else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false)
        	 {
        		 System.out.println("Starting opcode... \nLOAD (100)\n");
        	 }
        	 
        	 // If opcode is Store (101), execute ALU operation
        	 else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
        	 {
        		 System.out.println("Starting opcode... \nSTORE (101)\n");
        		 
        		 alu.op1.copy(registers[decodeRd(registerDbits)]);
				 alu.op2.copy(immediateValue);	 
        	 }
        	 
        	 // If opcode is Pop/Interupt (110), execute ALU operation
        	 else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
        	 {
        		 System.out.println("Starting opcode... \nPOP (110)\n");
        	 }
        	 
         }
    	
    	/** OPCODE (10) **/
    	else if (opcode.getBit(30).getValue() == true && opcode.getBit(31).getValue() == false)
    	{ 
    		System.out.println("\nStarting opcode (10)\n");
    		
    		// If opcode is Math (000), execute ALU operation
    		if (opcode.getBit(29).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(27).getValue() == false)
    		{
    			System.out.println("Starting opcode... \nMATH (000)\n");
        	 
    			// Set OP1 and OP2 on the ALU appropriately
    			alu.op1.copy(registers[decodeRd(registerSbits)]);
    			alu.op2.copy(registers[decodeRd(registerS2bits)]);
        		 
    			alu.doOperation(functionBits);
    		}
    		
    		// If opcode is Branch (001), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true )
    		{
    			System.out.println("Starting opcode... \nBRANCH (001)\n");
    			
    			alu.op2.copy(registers[decodeRd(registerS2bits)]);
    			alu.op1.copy(registers[decodeRd(registerSbits)]);
       		 
    			if(alu.doBOP(functionBits)) 
    			{	
    				PC.copy(PC.add(immediateValue));
    			}
    		}
        	 
    		// If opcode is Call (010), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
    			System.out.println("Starting opcode... \nCALL (010)\n");
    			
    			alu.op2.copy(registers[decodeRd(registerS2bits)]);
    			alu.op1.copy(registers[decodeRd(registerSbits)]);
    		}
        	 
    		// If opcode is Push (011), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == true)
    		{
    			System.out.println("Starting opcode... \nPUSH (011)\n");
    			
    			alu.op1.copy(registers[decodeRd(registerSbits)]);
				alu.op2.copy(registers[decodeRd(registerS2bits)]);
				alu.doOperation(functionBits);
				 
    		}
        	 
    		// If opcode is Load (100), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false)
    		{
    			System.out.println("Starting opcode... \nLOAD (100)\n");
    			
    		}
        	 
    		// If opcode is Store (101), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
    		{
    			System.out.println("Starting opcode... \nSTORE (101)\n");
    			
    			alu.op1.copy(registers[decodeRd(registerDbits)]);
    			alu.op2.copy(registers[decodeRd(registerSbits)]);
       		 
    		} 
    		
    		// If opcode is Pop/Interupt (110), execute ALU operation
       	 	else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
       	 	{
       	 		System.out.println("Starting opcode... \nPOP (110)\n");
       	 		
       	 	}
    	}
    	
    	/** OPCODE (01) **/
    	else if (opcode.getBit(30).getValue() == false && opcode.getBit(31).getValue() == true)
    	{
    		System.out.println("\nStarting opcode(01)\n");
    		
    		// If opcode is Branch (001), execute ALU operation
    		if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true )
    		{
    			System.out.println("Starting opcode... \nBRANCH -> JUMP (001)\n");
    		}
       	 
    		// If opcode is Call (010), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
    			System.out.println("Starting opcode... \nCALL (010)\n");	
    		}
       	 
    		// If opcode is Push (011), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == true)
    		{
    			System.out.println("Starting opcode... \nPUSH (011)\n");
    			
    			alu.op1.copy(registers[decodeRd(registerDbits)]);
    			
				alu.op2.copy(immediateValue);
				
				alu.doOperation(functionBits);
    		}
       	 
    		// If opcode is Load (100), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false)
    		{
    			System.out.println("Starting opcode... \nLOAD (100)\n");
 
    			//System.out.println("****************"+MainMemory.read(registers[decodeRd(registerDbits)].add(immediateValue)).getSigned());
    			//System.out.println("**********"+registers[decodeRd(registerDbits)].add(immediateValue).getSigned());	
    		}
       	 
    		// If opcode is Store (101), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
    		{
    			System.out.println("Starting opcode... \nSTORE (101)\n");	
    		}
    			
    		// If opcode is Pop/Interupt (110), execute ALU operation
       	 	else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
       	 	{
       	 		System.out.println("Starting opcode... \nPOP (110)\n");
       	 	}
         
    		//Math (000), dest only
    		return;
         }
     }

    /**
     * Stores the result of the executed instruction.
     */
    private void store() 
    {      		 
    	// Depending on the instruction format, update registers accordingly
    	/** OPCODE 01 */
    	if (opcode.getBit(30).getValue() == false && opcode.getBit(31).getValue() == true)
    	{
    		// If opcode is Math (000), execute ALU operation
    		if (opcode.getBit(29).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(27).getValue() == false)
    		{
    			// Destination Only: Update Rd register
    			registers[decodeRd(registerDbits)].copy(immediateValue);
    			System.out.println("\nDestination Only: Update Rd register. Rd = " + registerD + ", Immediate Value = " + immediateValue + "\n");
    		}
    		
    		// If opcode is Call (001), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
    		{
    			PC.copy(PC.add(immediateValue));
    		}
    		
    		// If opcode is Call (010), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
    			
    			PC.copy(registers[decodeRd(registerDbits)].add(immediateValue));
    			
    			SP.decrement(); // Decrement stack pointer
    			
    	        MainMemory.write(SP, PC); // Push the current PC onto the stack
    	        Processor.clockCycle += 50;
    		}
    		
    		// If opcode is Push (011), execute ALU operation
       	 	else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == true)
       	 	{
       	 		SP.decrement(); // Decrement stack pointer
			
       	 		// Push alu.result onto the stack
       	 		MainMemory.write(SP, alu.result);
       	 		Processor.clockCycle += 50;
       	 	}
    		
    		// If opcode is Load (100), execute ALU operation
       	 	else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false)
       	 	{
       	 		// Load value from memory into register
    			registers[decodeRd(registerDbits)].copy(MainMemory.read(registers[decodeRd(registerDbits)].add(immediateValue)));
    			 Processor.clockCycle += 50;
       	 	}
    		
    		// If opcode is Store (101), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
    		{
    			System.out.println("STORING VALUE: " +registers[decodeRd(registerDbits)]);
    			MainMemory.write(registers[decodeRd(registerDbits)], immediateValue);
    			 Processor.clockCycle += 50;
    		}
    		
    		// If opcode is Pop/Interrupt (110), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
       	 		registers[decodeRd(registerDbits)].copy(MainMemory.read(SP)); // Increment stack pointer and read value
       	 		SP.increment();
       	 	 Processor.clockCycle += 50;
    		}
    	}
    	    
    	/** OPCODE 11 */
    	else if (opcode.getBit(30).getValue() == true && opcode.getBit(31).getValue() == true)
    	{
    		// If opcode is Math (000), execute ALU operation
    		if (opcode.getBit(29).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(27).getValue() == false)
    		{
    			// 2R: Update Rs and Rd registers
    			registers[decodeRd(registerDbits)].copy(alu.result);
    			System.out.println("2R: Update Rs and Rd registers. Rs = " + registerS + ", Rd = " + registerD + ", Immediate Value = " + immediateValue + "\n");
    		}
    		
    		// If opcode is Call (010), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
    			
    			 Processor.clockCycle += 50;
    			SP.decrement(); // Decrement stack pointer
    			
    			// Push the current PC onto the stack
    	        MainMemory.write(SP, PC);
    	        
    	        if(alu.doBOP(functionBits)) 
     			{	
     	        	PC.copy(PC.add(immediateValue));
     			}
    		}
    		
    		// If opcode is Push (011), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == true)
    		{
    			 Processor.clockCycle += 50;
    			SP.decrement(); // Decrement stack pointer
    			
    			 MainMemory.write(SP, alu.result);
    		}
    		
    		// If opcode is Load (100), execute ALU operation
       	 	else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false)
       	 	{
       	 	 Processor.clockCycle += 50;
       	 		//System.out.println("**************"+registers[decodeRd(registerSbits)].add(immediateValue));
       	 		// Load value from memory into register
       	 	registerSbits[0] = registerS.getBit(31);
   		 	registerSbits[1] = registerS.getBit(30);
   		 	registerSbits[2] = registerS.getBit(29);
   		 	registerSbits[3] = registerS.getBit(28);
   		 	registerSbits[4] = registerS.getBit(27);
       	 		Word holder = registers[decodeRd(registerSbits)].add(immediateValue);
       	 		System.out.println(registers[decodeRd(registerSbits)] + "\n" + Arrays.toString(registerSbits));
       	 		registers[decodeRd(registerDbits)].copy(MainMemory.read(holder));
       	 	}
    		
    		// If opcode is Store (101), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
    		{
    			 Processor.clockCycle += 50;
    			MainMemory.write(alu.result, registers[decodeRd(registerSbits)]);
    		}
    		
    		// If opcode is Pop/Interrupt (110), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
    			 Processor.clockCycle += 50;
    			registers[decodeRd(registerDbits)].copy(MainMemory.read(SP.subtract(SP, registers[decodeRd(registerSbits)].add(immediateValue))));
    		}
    	}
    	    
    	/** OPCODE 10 */
    	else if (opcode.getBit(30).getValue() == true && opcode.getBit(31).getValue() == false)
    	{
    		// If opcode is Math (000), execute ALU operation
    		if (opcode.getBit(29).getValue() == false && opcode.getBit(28).getValue() == false && opcode.getBit(27).getValue() == false)
    		{
    			// 3R: Update Rs1, Rs2, and Rd registers
    			registers[decodeRd(registerDbits)].copy(alu.result);
    			System.out.println("3R: Update Rs and Rd registers. Rs = " + registerS + ", Rd = " + registerD + ", Rs2 = " + registerS2 +", Immediate Value = " + immediateValue + "\n");
    		}
    		// If opcode is Call (010), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
    			 Processor.clockCycle += 50;
    			SP.decrement(); // Decrement stack pointer

    			// Push the current PC onto the stack
    	        MainMemory.write(SP, PC);
    	        
    	        if(alu.doBOP(functionBits)) 
    			{	
    	        	PC.copy(registerD.add(immediateValue));
    			}
    		}
    		// If opcode is Push (011), execute ALU operation
    		else if(opcode.getBit(27).getValue() == false && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == true)
       	 	{
    			 Processor.clockCycle += 50;
    			SP.decrement(); // Decrement stack pointer

    			MainMemory.write(SP, alu.result);
       	 	}
    		// If opcode is Load (100), execute ALU operation
       	 	else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == false)
       	 	{
       	 	
       	 	 Processor.clockCycle += 50;
       	 		// Load value from memory into register
    			registers[decodeRd(registerDbits)].copy(MainMemory.read(registers[decodeRd(registerSbits)].add(registers[decodeRd(registerS2bits)])));
       	 	}
    		// If opcode is Store (101), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == false && opcode.getBit(29).getValue() == true)
    		{
    			 Processor.clockCycle += 50;
    			MainMemory.write(alu.result, registers[decodeRd(registerS2bits)]);
    		}
    		
    		// If opcode is Pop/Interrupt (110), execute ALU operation
    		else if(opcode.getBit(27).getValue() == true && opcode.getBit(28).getValue() == true && opcode.getBit(29).getValue() == false)
    		{
    			 Processor.clockCycle += 50;
    			registers[decodeRd(registerDbits)].copy(MainMemory.read(SP.subtract(SP, registers[decodeRd(registerSbits)].add(registers[decodeRd(registerS2bits)]))));
    		}
    	}
    }
    

  //testing use
    public Word[] getRegisters()
    {
        return registers;
    }
    
    //https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.reddit.com%2Fr%2FNingen%2Fcomments%2F15wf0ye%2Fwhat_would_you_do_if_hes_was_your_school_librarian%2F&psig=AOvVaw1XUitWRXhRgrdz3aQHQxnL&ust=1710184144265000&source=images&cd=vfe&opi=89978449&ved=0CBMQjRxqFwoTCPjer8ay6oQDFQAAAAAdAAAAABAE
    private int decodeRd(Bit[] operation)
    {
    	StringBuilder sb = new StringBuilder();

    	for (int i = operation.length - 1; i >= 0; i--)
    	{
    		
    	    Bit bit = operation[i];

    	    sb.append(bit.getValue() ? '1' : '0');
    	    System.out.println("***** PROCESOR RD BIT = " + bit + "*****");
    	}

    	String opCode = sb.toString();

    	switch(opCode) 
    	{
    	    case "00001":
    	    	System.out.println("\nRD ENTRY 1");
    	        return 1;
    	        
    	    case "00010":
    	    	System.out.println("\nRD ENTRY 2");
    	        return 2;
    	        
    	    case "00011":
    	    	System.out.println("\nRD ENTRY 3");
    	        return 3;
    	        
    	    case "00100":
    	    	System.out.println("\nRD ENTRY 4");
    	        return 4;
    	        
    	    case "00101":
    	    	System.out.println("\nRD ENTRY 5");
    	        return 5;
    	        
    	    case "00110":
    	    	//youre bold if you think im writing a sysoutprint for every number this is a mess
    	        return 6;
    	        
    	    case "00111":
    	        return 7;
    	        
    	    case "01000":
    	        return 8;
    	        
    	    case "01001":
    	        return 9;
    	        
    	    case "01010":
    	        return 10;
    	        
    	    case "01011":
    	        return 11;
    	        
    	    case "01100":
    	        return 12;
    	        
    	    case "01101":
    	        return 13;
    	        
    	    case "01110":
    	        return 14;
    	        
    	    case "01111":
    	        return 15;
    	        
    	    case "10000":
    	        return 16;
    	        
    	    case "10001":
    	        return 17;
    	        
    	    case "10010":
    	        return 18;
    	        
    	    case "10011":
    	        return 19;
    	        
    	    case "10100":
    	        return 20;
    	        
    	    case "10101":
    	        return 21;
    	        
    	    case "10110":
    	        return 22;
    	        
    	    case "10111":
    	        return 23;
    	        
    	    case "11000":
    	        return 24;
    	        
    	    case "11001":
    	        return 25;
    	        
    	    case "11010":
    	        return 26;
    	        
    	    case "11011":
    	        return 27;
    	        
    	    case "11100":
    	        return 28;
    	        
    	    case "11101":
    	        return 29;
    	        
    	    case "11110":
    	        return 30;
    	        
    	    case "11111":
    	        return 31;
    	        
    	    default:
    	        return -1;
    	}

		
		
    }
}