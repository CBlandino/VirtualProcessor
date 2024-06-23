import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test 
{
	/**
	 * Please look in the terminal for the keywords, binary and size of the instructions
	 * ive double checked the binary with my own tests i just dont know how you want me to assertequals 20+
	 * lines of binary
	 * @throws IOException 
	 */
	@Test
	public void parselextest() throws IOException
	{
		MainMemory mainmem = new MainMemory();
		File inputFile = new File("src/input.txt");
        File outputFile = new File("src/output.txt");

        if (!outputFile.exists()) 
        {
            outputFile.createNewFile();
        }
        
        try
        {
            new Lexer(inputFile, outputFile);
            System.out.println("Output written to " + outputFile.getAbsolutePath());

            // Read output file into a string array
            System.out.println("\nLoading Output File into String[]......");
            List<String> outputLines = new ArrayList<>();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(outputFile)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    outputLines.add(line);
                }
            }

            // Convert list to array
            String[] outputArray = outputLines.toArray(new String[outputLines.size()]);
            
            // Print the contents of the string array
            System.out.println("Reading contents of newly added String[]......\n");
            for (String line : outputArray)
            {
                System.out.println(line);
            }
            
            System.out.println("\nLoading contents of newly added String[] into memory......");
            MainMemory.load(outputArray);
            System.out.println("Complete");
            Processor processor = new Processor();
            processor.run();
            System.out.println("Completed running assembly");
            
            
            
        } 
        
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    
	}

//	public static int clockcycles = 0;
//	
//    @Test
//    public void testDec()
//    {
//    	Word five = new Word(5);
//    	five.decrement();
//    	
//    	assertEquals(4, five.getSigned());
//    }
//	
//	@Test
//	public void testBool()
//	{
//		Word five = new Word(5);
//		Word three = new Word(3);
//		
//					//false, 3 > 5	
//		 assertEquals(false, three.isGreaterThan(five));
//					//true, 5 > 3	
//		 assertEquals(true, five.isGreaterThan(three));
//					//true, 3 < 5	
//		 assertEquals(true, three.lessThan(five));
//					//true, 5 < 3	
//		 assertEquals(false, five.lessThan(three));
//		 
//		 
//		 			//true, 5 != 3	
//		 assertEquals(true, five.notEqual(three));
//		 			//false, 5 != 5	
//		 assertEquals(false, five.notEqual(five));
//		 			//true, 5 == 5	
//		 assertEquals(true, five.equals(five));
//		 			//false, 5 == 5	
//		 assertEquals(false, five.notEqual(five));
//		 
//		 
//		 			//true, 5 >= 5
//		 assertEquals(true, five.greaterThanOrEqual(five));
//					//true, 5 >= 3
//		 assertEquals(true, five.greaterThanOrEqual(three));
//					//true, 3 >= 5
//		 assertEquals(false, three.greaterThanOrEqual(five));
//		 
//		 
//					//true, 3 <= 5
//		 assertEquals(true, three.lessThanOrEqual(five));
//					//true, 3 <= 3
//		 assertEquals(true, three.lessThanOrEqual(three));
//					//false, 5 <= 3
//		 assertEquals(false, five.lessThanOrEqual(three));
//		 
//	}
//	
//	@Test
//    public void testMath() 
//	{
//        Processor processor = new Processor();
//        
//        String[] load = new String[5];
//        
//          load[0] = "00000000000000010111100000100001";
//					// MATH DestOnly 5, R1
//          
//          load[1] = "00000000000010000111100001000010"; // MATH ADD R1 R1 R2
//          		  
//          load[2] = "00000000000000001011100001000011"; // MATH ADD R2 R2
//          
//          load[3] = "00000000000100000111100001100010"; // MATH ADD R2 R1 R3
//          
//          load[4] = "00000000000001000010000100000000"; // HALT	
//          
//        // Load program into memory
//        MainMemory.load(load);
//
//        processor.run();
//        clockcycles += processor.clockCycle;
//        // Verify that R3 contains 25
//        Word[] registers = processor.getRegisters();
//        int test = 25;
//        assertEquals(test, registers[3].getSigned());
//        
//        //clockcycles += processor.clockCycle;
//    }
//	
//    @Test
//    public void testTrueBranch_11()
//    {
//        Processor processor = new Processor();
//        
//        String[] loadTest_11 = new String[6];
//        loadTest_11[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        
//        loadTest_11[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//        
//        loadTest_11[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//        
//        loadTest_11[3] = "00000000000010000101010001000111"; // BRANCH 2R R1 LE R2 1
//        
//        loadTest_11[4] = "00000000000000010111100001000001"; // MATH DestOnly 5, R2
//        
//        loadTest_11[5] = "00000000000001000010000100000000"; // HALT
//        
//        MainMemory.load(loadTest_11);
//        
//        processor.run();
//        clockcycles += processor.clockCycle;
//        
//        // Verify that R2 contains 5 after the call
//        Word[] registers = processor.getRegisters();
//        System.out.println(registers[2].getSigned());
//        
//        int expectedResult = 10;
//        assertEquals(expectedResult, registers[2].getSigned());
//    }
//    
//    @Test
//    public void testTrueBranch_10()
//    {
//    	Processor processor = new Processor();
//    	
//        String[] loadTest_10 = new String[6];
//        loadTest_10[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        
//        loadTest_10[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//        
//        loadTest_10[2] = "00000000000000101011100001100001"; // MATH DestOnly 10, R3
//        
//        loadTest_10[3] = "00000001000110000101010001000110"; // BRANCH 2R R1 LE R3 R2 1
//        
//        loadTest_10[4] = "00000000000000010111100001000001"; // MATH DestOnly 5, R2
//        
//        loadTest_10[5] = "00000000000001000010000100000000"; // HALT
//        
//        MainMemory.load(loadTest_10);
//        
//        processor.run();
//        clockcycles += processor.clockCycle;
//        
//        Word[] registers = processor.getRegisters();
//        System.out.println(registers[2].getSigned());
//        
//        int expectedResult = 10;
//        assertEquals(expectedResult, registers[2].getSigned());
//    }
//    
//    @Test
//    public void testFalseBranch_11()
//    {
//        Processor processor = new Processor();
//        
//        String[] load = new String[6];
//        load[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        
//        load[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//        
//        load[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//        
//        load[3] = "00000000000010000101000001000111"; // Branch 2R R1 GT R2 4
//        
//        load[4] = "00000000000100000111100001000010"; // MATH ADD R2 R1 R2
//        
//        load[5] = "00000000000001000010000100000000"; // HALT
//        
//        MainMemory.load(load);
//        
//        processor.run();
//        clockcycles += processor.clockCycle;
//        
//        // Verify that R2 contains 15 after the call
//        Word[] registers = processor.getRegisters();
//        System.out.println(registers[2].getSigned());
//        
//        int expectedResult = 15;
//        assertEquals(expectedResult, registers[2].getSigned());
//    }
//        
//    @Test
//    public void testFalseBranch_10()
//    {
//    	Processor processor = new Processor();
//    	
//        String[] loadTest_10 = new String[6];
//        loadTest_10[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        
//        loadTest_10[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//        
//        loadTest_10[2] = "00000000000000101011100001100001"; // MATH DestOnly 10, R3
//        
//        loadTest_10[3] = "00000001000010001101010001000110"; // BRANCH 2R R3 LE R1 1
//        
//        loadTest_10[4] = "00000000000000010111100001000001"; // MATH DestOnly 5, R2
//        
//        loadTest_10[5] = "00000000000001000010000100000000"; // HALT
//        
//        MainMemory.load(loadTest_10);
//        
//        processor.run();
//        clockcycles += processor.clockCycle;
//        
//        Word[] registers = processor.getRegisters();
//        System.out.println(registers[2].getSigned());
//        
//        int expectedResult = 5;
//        assertEquals(expectedResult, registers[2].getSigned());
//    }
//    
//    @Test
//    public void testBranch_01()
//    {
//    	Processor processor = new Processor();
//    	 
//        String[] loadTest_01 = new String[6];
//        
//        loadTest_01[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        
//        loadTest_01[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//        
//        loadTest_01[2] = "00000000000000101011100001100001"; // MATH DestOnly 10, R3
//        
//        loadTest_01[3] = "00000000000000000111100001000101"; // BRANCH 2R JUMP 1
//        
//        loadTest_01[4] = "00000000000000010111100001000001"; // MATH DestOnly 5, R2
//        
//        loadTest_01[5] = "00000000000001000010000100000000"; // HALT
//        
//        MainMemory.load(loadTest_01);
//        
//        processor.run();
//        clockcycles += processor.clockCycle;
//        
//        Word[] registers = processor.getRegisters();
//        System.out.println(registers[2].getSigned());
//        
//        int expectedResult = 10;
//        assertEquals(expectedResult, registers[2].getSigned());
//        
//    }
//    
//    
//    /**
//     * NOT WORKING AS INTENDED
//     */
//    @Test
//    public void testBranch_00()
//    {
//    	Processor processor = new Processor();
//    	 
//        String[] loadTest_01 = new String[6];
//        
//        loadTest_01[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        
//        loadTest_01[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//        
//        loadTest_01[2] = "00000000000000101011100001100001"; // MATH DestOnly 10, R3
//        
//        loadTest_01[3] = "00000000000000000000000010000100"; // BRANCH 2R JUMP TO 4
//        
//        loadTest_01[4] = "00000000000000010111100001000001"; // MATH DestOnly 5, R2
//        
//        loadTest_01[5] = "00000000000001000010000100000000"; // HALT
//        
//        MainMemory.load(loadTest_01);
//        
//        processor.run();
//        clockcycles += processor.clockCycle;
//        
//        Word[] registers = processor.getRegisters();
//        System.out.println(registers[2].getSigned());
//        
//        int expectedResult = 5;
//        assertEquals(expectedResult, registers[2].getSigned());
//        
//    }
//
//    @Test
//	public void testTrueCall_POP()
//	{
//	    Processor processor = new Processor();
//	    
//	    String[] loadTest_11 = new String[7];
//	    loadTest_11[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//	    
//	    loadTest_11[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//	    
//	    loadTest_11[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//	    
//	    loadTest_11[3] = "0000000000001"+"00001"+"0010"+"00010"+"01011"; // CALL 2R R1 LT R2 1
//	    
//	    loadTest_11[4] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//	    
//	    loadTest_11[5] = "000000000000000000000000010"+"11001"; //POP (PC 5)
//	    
//	    loadTest_11[6] = "00000000000001000010000100000000"; // HALT
//	    
//	    MainMemory.load(loadTest_11);
//	    
//	    processor.run();
//	    clockcycles += processor.clockCycle;
//	    
//	    // Verify that R2 contains the pushed PC after the call
//	    Word[] registers = processor.getRegisters();
//	    System.out.println(registers[2].getSigned());
//	    
//	    int expectedResult = 4;
//	    assertEquals(expectedResult, registers[2].getSigned());
//	}
//    
//    @Test
//   	public void testFalseCall_Load_Branch()
//   	{
//   	    Processor processor = new Processor();
//   	    
//   	    String[] loadTest_11 = new String[7];
//   	    loadTest_11[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//   	    
//   	    loadTest_11[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//   	    
//   	    loadTest_11[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//   	    
//   	    loadTest_11[3] = "0000000000001"+"00001"+"0010"+"00010"+"01011"; // CALL 2R R1 LT R2 1
//   	    
//   	    loadTest_11[4] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//   	    
//   	    loadTest_11[5] = "000000000000000000000000010"+"11001"; //POP (PC 5)
//   	    
//   	    loadTest_11[6] = "00000000000001000010000100000000"; // HALT
//   	    
//   	    MainMemory.load(loadTest_11);
//   	    
//   	    processor.run();
//   	    clockcycles += processor.clockCycle;
//   	    
//   	    // Verify that R2 contains the pushed PC after the call
//   	    Word[] registers = processor.getRegisters();
//   	    System.out.println(registers[2].getSigned());
//   	    
//   	    int expectedResult = 4;
//   	    assertEquals(expectedResult, registers[2].getSigned());
//   	}
//    
//    @Test
//   	public void testPush_Return_11()
//   	{
//   	    Processor processor = new Processor();
//   	    
//   	    String[] loadTest_11 = new String[7];
//   	    loadTest_11[0] = "00000000000000001111100000100001"; // MATH DestOnly 3, R1
//   	    
//   	    loadTest_11[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//   	    
//   	    loadTest_11[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//   	    
//   	    loadTest_11[3] = "0000000000001"+"00001"+"1111"+"00010"+"01111"; // PUSH 2R(7), R2(10) SUBTRACT R1(3)
//   	    
//   	    loadTest_11[4] = "000000000000000000000000010"+"10000"; //RETURN (PC 7));
//   	    
//   	    loadTest_11[5] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//   	    
//   	    loadTest_11[6] = "00000000000001000010000100000000"; // HALT
//   	    
//   	    MainMemory.load(loadTest_11);
//   	    
//   	    processor.run();
//   	    clockcycles += processor.clockCycle;
//   	    
//   	    // Verify that R2 contains the pushed PC after the call
//   	    Word[] registers = processor.getRegisters();
//   	    System.out.println(registers[2].getSigned());
//   	    
//   	    int expectedResult = 7;
//   	    assertEquals(expectedResult, registers[2].getSigned());
//   	}
//    
//    @Test
//   	public void testPush_Return_01()
//   	{
//   	    Processor processor = new Processor();
//   	    
//   	    String[] loadTest_01 = new String[7];
//   	    loadTest_01[0] = "00000000000000101011100000100001"; // MATH DestOnly 10, R1
//   	    
//   	    loadTest_01[1] = "00000000000000011111100001000001"; // MATH DestOnly 3, R2
//   	    
//   	    loadTest_01[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//   	    
//   	    loadTest_01[3] = "000000000000000100"+"1110"+"00010"+"01101"; // PUSH 2R(7), R2(3) ADD imm(4)
//   	    
//   	    loadTest_01[4] = "000000000000000000000000010"+"10000"; //RETURN (PC 7);
//   	    
//   	    loadTest_01[5] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//   	    
//   	    loadTest_01[6] = "00000000000001000010000100000000"; // HALT
//   	    
//   	    MainMemory.load(loadTest_01);
//   	    
//   	    processor.run();
//   	    clockcycles += processor.clockCycle;
//   	    
//   	    // Verify that R2 contains the pushed PC after the call
//   	    Word[] registers = processor.getRegisters();
//   	    System.out.println(registers[2].getSigned());
//   	    
//   	    int expectedResult = 7;
//   	    assertEquals(expectedResult, registers[2].getSigned());
//   	}
//    
//    @Test
//   	public void testPush_Return_10()
//   	{
//   	    Processor processor = new Processor();
//   	    
//   	    String[] loadTest_10 = new String[7];
//   	    loadTest_10[0] = "00000000000000001111100000100001"; // MATH DestOnly 10, R1
//   	    
//   	    loadTest_10[1] = "00000000000000011111100001000001"; // MATH DestOnly 3, R2
//   	    
//   	    loadTest_10[2] = "00000000000000010011100001100001"; // MATH DestOnly 5, R3
//   	    
//   	    loadTest_10[3] = "00000000"+"00011"+"00001"+"1110"+"00001"+"01110"; // PUSH 2R(10), R1(3) ADD R2(4)
//   	    
//   	    loadTest_10[4] = "000000000000000000000000010"+"10000"; //RETURN (PC 7);
//   	    
//   	    loadTest_10[5] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//   	    
//   	    loadTest_10[6] = "00000000000001000010000100000000"; // HALT
//   	    
//   	    MainMemory.load(loadTest_10);
//   	    
//   	    processor.run();
//   	    clockcycles += processor.clockCycle;
//   	    
//   	    // Verify that R2 contains the pushed PC after the call
//   	    Word[] registers = processor.getRegisters();
//   	    System.out.println(registers[2].getSigned());
//   	    
//   	    int expectedResult = 7;
//   	    assertEquals(expectedResult, registers[2].getSigned());
//   	}
//    
//    @Test
//   	public void testLoad_01()
//   	{
//   	    Processor processor = new Processor();
//   	    
//   	    String[] loadTest_01 = new String[9];
//   	    loadTest_01[0] = "00000000000000001111100000100001"; // MATH DestOnly 3, R1
//   	    
//   	    loadTest_01[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//   	    
//   	    loadTest_01[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//   	    
//   	    loadTest_01[3] = "0000000000011"+"00001"+"0010"+"00010"+"01011"; // CALL 2R R1 LT R2 3 (PC 4 + lines skipped(3))
//   	    
//   	    loadTest_01[4] = "0000000000001"+"00001"+"1111"+"00010"+"01111"; // PUSH 2R(7), R2(10) SUBTRACT R1(3)
//   	    
//   	    loadTest_01[5] = "00000000"+"00011"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R3
//   	    
//   	    loadTest_01[6] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//   	    
//   	    loadTest_01[7] = "00000000111111010100000001010001"; // Load PC 
//   	    
//   	    loadTest_01[8] = "00000000000001000010000100000000"; // HALT
//   	    
//   	    MainMemory.load(loadTest_01);
//   	    
//   	    processor.run();
//   	    clockcycles += processor.clockCycle;
//   	    
//   	    // Verify that R2 contains the pushed PC after the call
//   	    Word[] registers = processor.getRegisters();
//   	    System.out.println(registers[2].getSigned());
//   	    
//   	    int expectedResult = 4;
//   	    assertEquals(expectedResult, registers[2].getSigned());
//   	}
//    
//    @Test
//   	public void testLoad_10()
//   	{
//   	    Processor processor = new Processor();
//   	    
//   	    String[] loadTest_11 = new String[9];
//   	    loadTest_11[0] = "00000000100000000011100000100001"; // MATH DestOnly 3, R1
//   	    
//   	    loadTest_11[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//   	    
//   	    loadTest_11[2] = "00000000011111111111100001100001"; // MATH DestOnly 5, R3
//   	    
//   	    loadTest_11[3] = "0000000000011"+"00010"+"0010"+"00001"+"01011"; // CALL 2R R1 LT R2 3 (PC 4 + lines skipped(3))
//   	    
//   	    loadTest_11[4] = "0000000000001"+"00001"+"1111"+"00010"+"01111"; // PUSH 2R(7), R2(10) SUBTRACT R1(3)
//   	    
//   	    loadTest_11[5] = "00000000"+"00011"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R3
//   	    
//   	    loadTest_11[6] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//   	    
//   	    loadTest_11[7] = "00011111"+"00011"+"00001"+"0000"+"00010"+"10010"; // Load PC 
//   	    
//   	    loadTest_11[8] = "00000000000001000010000100000000"; // HALT
//   	    
//   	    MainMemory.load(loadTest_11);
//   	    
//   	    processor.run();
//   	    clockcycles += processor.clockCycle;
//   	    
//   	    // Verify that R2 contains the pushed PC after the call
//   	    Word[] registers = processor.getRegisters();
//   	    System.out.println(registers[2].getSigned());
//   	    
//   	    int expectedResult = 4;
//   	    assertEquals(expectedResult, registers[2].getSigned());
//   	}
//    
//    @Test
//   	public void testLoad_11()
//   	{
//   	    Processor processor = new Processor();
//   	    
//   	    String[] loadTest_11 = new String[9];
//   	    loadTest_11[0] = "00000000000000001111100000100001"; // MATH DestOnly 3, R1
//   	    
//   	    loadTest_11[1] = "00000000000000101011100001000001"; // MATH DestOnly 10, R2
//   	    
//   	    loadTest_11[2] = "00000000000000010111100001100001"; // MATH DestOnly 5, R3
//   	    
//   	    loadTest_11[3] = "0000000000011"+"00001"+"0010"+"00010"+"01011"; // CALL 2R R1 LT R2 3 (PC 4 + lines skipped(3))
//   	    
//   	    loadTest_11[4] = "0000000000001"+"00001"+"1111"+"00010"+"01111"; // PUSH 2R(7), R2(10) SUBTRACT R1(3)
//   	    
//   	    loadTest_11[5] = "00000000"+"00011"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R3
//   	    
//   	    loadTest_11[6] = "00000000"+"00010"+"00001"+"1110"+"00010"+"00010"; // MATH ADD R2 R1 R2
//   	    
//   	    loadTest_11[7] = "0001111111100"+"00001"+"0000"+"00010"+"10011"; // Load PC 
//   	    
//   	    loadTest_11[8] = "00000000000001000010000100000000"; // HALT
//   	    
//   	    MainMemory.load(loadTest_11);
//   	    
//   	    processor.run();
//   	    clockcycles += processor.clockCycle;
//   	    
//   	    // Verify that R2 contains the pushed PC after the call
//   	    Word[] registers = processor.getRegisters();
//   	    System.out.println(registers[2].getSigned());
//   	    
//   	    int expectedResult = 4;
//   	    assertEquals(expectedResult, registers[2].getSigned());
//   	    
//   	}
//    
//    @Test
//    public void ClockCycles()
//    {
//    	 assertEquals(5, 30);
//    	 
//    }
    
//    @Test
//    public void testStore() {
//        Processor processor = new Processor();
//        String[] load = new String[3];
//        load[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        load[1] = "00000000000000011000100100000001"; // LOAD R1 R2 1
//        load[2] = "00000000000000010000100000000010"; // STORE R1 R0 2
//        
//        MainMemory.load(load);
//        processor.run();
//        
//        // Verify that memory location 2 contains 5 after storing from R1
//        Word memory = MainMemory.read(new Word(2));
//        int expectedResult = 5;
//        assertEquals(expectedResult, memory.getSigned());
//    }
//    
//    @Test
//    public void testPeek() {
//        Processor processor = new Processor();
//        String[] load = new String[4];
//        load[0] = "00000000000000010111100000100001"; // MATH DestOnly 5, R1
//        load[1] = "00000000000000011000100100000001"; // LOAD R1 R2 1
//        load[2] = "00000000000000010000100000000010"; // STORE R1 R0 2
//        load[3] = "00000000000001010100000000000100"; // PEEK R5 R0 4
//        
//        MainMemory.load(load);
//        processor.run();
//        
//        // Verify that the peeked value from the stack is 5
//        Word[] registers = processor.getRegisters();
//        int expectedResult = 5;
//        assertEquals(expectedResult, registers[5].getSigned());
//    } 
   
}

