import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides methods for parsing a list of tokens into machine instructions
 * and storing the binary instructions into a specified file.
 */
public class Parser
{
	
	/**
	 * Parses a list of tokens into machine instructions and stores the binary instructions into the specified file.
	 * 
	 * @param tokens      The list of tokens to parse.
	 * @param outputFile  The file where the binary instructions will be stored.
	 * @throws IOException If an I/O error occurs while writing to the output file.
	 */
    public static void parse(List<Token> tokens, File outputFile) throws IOException 
    {
    	List<String> machineInstructions = new ArrayList<>();
    	//int numNewLines = countNewLines(tokens);
    	//String[] machineInstructions = new String[numNewLines];
    	
        int index = 0;
        
        while (index < tokens.size())
        {
            Token token = tokens.get(index);
            switch (token.getType()) 
            {
                case MATH:
                    machineInstructions.add(parseMath(tokens, index));
                    index = findNextNewline(tokens, index);                    
                    break;
                case SHIFT:
                	machineInstructions.add(parseMath(tokens, index));
                    index = findNextNewline(tokens, index);  
                    break;
                case COPY:
                	machineInstructions.add(parseCopy(tokens, index));
                    index = findNextNewline(tokens, index);  
                    break;
                case BRANCH:
                	machineInstructions.add(parseBranch(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case JUMP:
                	machineInstructions.add(parseJump(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case CALL:
                	machineInstructions.add(parseCall(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case PUSH:
                	machineInstructions.add(parsePush(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case LOAD:
                	machineInstructions.add(parseLoad(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case RETURN:
                	machineInstructions.add("00000000000000000000000010000"); // RETURN instruction
                    index = findNextNewline(tokens, index);
                    break;
                case STORE:
                	machineInstructions.add(parseStore(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case POP:
                	machineInstructions.add(parsePop(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case PEEK:
                	machineInstructions.add(parsePeek(tokens, index));
                    index = findNextNewline(tokens, index);
                    break;
                case HALT:
                	machineInstructions.add("00000000000000000000000000000000"); //HALT instruction
                    index = findNextNewline(tokens, index);
                	break;
                case NEWLINE:
                    index++; // Skip the newline token
                    break;
                default:
                    // Skip other tokens
                    index++;
                    break;
            }
        }
        
        String[] binaryInstuctions = machineInstructions.toArray(new String[machineInstructions.size()]);
        
        System.out.println("\nParsing tokens complete.....\n"
        		+ "Binary being stored into file:\n"
        		+ Arrays.toString(binaryInstuctions) + "\n"
        				+ outputFile + "\n");
        
        try (FileWriter writer = new FileWriter(outputFile)) 
        {
            for (int i = 0; i < machineInstructions.size(); i++) 
            {
                writer.write(machineInstructions.get(i));
                
                if (i < machineInstructions.size() - 1)
                {
                	// Add newline unless it's the last instruction
                    writer.write("\n");
                }
            }
        }
    	 
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }

    /**
     * Parses a COPY operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the COPY operation and its operands.
     * @param index  The index of the COPY operation token in the tokens list.
     * @return The binary representation of the COPY operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseCopy(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	
    	String binaryNumber;
        
        Token.TokenType operation = tokens.get(index).getType();
                
        if(operation == Token.TokenType.COPY)
        {       
        	if(tokens.get(index + 1).getType() == Token.TokenType.REGISTER)
            {
                register = Integer.parseInt(tokens.get(index + 1).getValue().substring(1));
                binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                        
                if(tokens.get(index + 2).getType() == Token.TokenType.NUMBER)
                {
                	number = Integer.parseInt(tokens.get(index + 2).getValue());
                	binaryNumber = String.format("%18s", Integer.toBinaryString(number)).replace(' ', '0');
                     
                    return binaryNumber + "0000" + binaryDRegister + tokens.get(index).getValue() + "01";
                }
                else
                {
                    throw new RuntimeException("EXCPETED NUMBER IN PARSE COPY"
                                + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());    
                }
            }
            else
            {
                throw new RuntimeException("EXCPETED REGISTER IN PARSE COPY"
                        + "/nTYPE:" + tokens.get(index + 1).getType() + " VALUE:" + tokens.get(index + 1).getValue());
            }
        }
        
        throw new IllegalArgumentException("Invalid copy operation: " + operation);
    }
    
    /**
     * Parses a math operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the math operation and its operands.
     * @param index  The index of the math operation token in the tokens list.
     * @return The binary representation of the math operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseMath(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	
    	String binaryNumber;
        
        Token.TokenType operation = tokens.get(index + 1).getType();
                
        if(operation == Token.TokenType.DESTONLY)
        {             
            if(tokens.get(index + 2).getType() == Token.TokenType.NUMBER)
            {
                number = Integer.parseInt(tokens.get(index + 2).getValue());
                binaryNumber = String.format("%18s", Integer.toBinaryString(number)).replace(' ', '0');
                     
                if(tokens.get(index + 3).getType() == Token.TokenType.REGISTER)
                {
                    register = Integer.parseInt(tokens.get(index + 3).getValue().substring(1));
                    binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                            
                    return binaryNumber + tokens.get(index + 1).getValue() + binaryDRegister + tokens.get(index).getValue() + "01";
                }
                else
                {
                    throw new RuntimeException("EXCPETED REGISTER IN PARSE MATH DESTONLY"
                                + "/nTYPE:" + tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 3).getValue());    
                }
            }
            else
            {
                throw new RuntimeException("EXCPETED NUMBER IN PARSE MATH DESTONLY"
                        + "/nTYPE:" + tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 3).getValue());
            }
        }


        else if (operation == Token.TokenType.AND || operation == Token.TokenType.OR ||
                operation == Token.TokenType.XOR || operation == Token.TokenType.NOT ||
                operation == Token.TokenType.LEFT || operation == Token.TokenType.RIGHT ||
                operation == Token.TokenType.ADD || operation == Token.TokenType.SUBTRACT ||
                operation == Token.TokenType.MULTIPLY) {
         return parseBinaryOperation(tokens, index, operation);
     }
        throw new IllegalArgumentException("Invalid math operation: " + operation);
    }

    /**
     * Parses a BRANCH operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the BRANCH operation and its operands.
     * @param index  The index of the BRANCH operation token in the tokens list.
     * @return The binary representation of the BRANCH operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseBranch(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	String binarySRegister;
    	String binaryS2Register;

    	String binaryNumber;
    	
    	Token operation = tokens.get(index + 1);
        
    	 if(operation.getType() == Token.TokenType.REGISTER)
         {
    		 if(tokens.get(index + 2).getType() == Token.TokenType.REGISTER)
    		 {
	             register = Integer.parseInt(tokens.get(index + 2).getValue().substring(1));
	             binarySRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
	             
	             if(tokens.get(index + 3).getType() == Token.TokenType.EQ ||
	                tokens.get(index + 3).getType() == Token.TokenType.NEQ|| 
	                tokens.get(index + 3).getType() == Token.TokenType.GE ||
	                tokens.get(index + 3).getType() == Token.TokenType.GT ||
	                tokens.get(index + 3).getType() == Token.TokenType.LE ||
	                tokens.get(index + 3).getType() == Token.TokenType.LT) 
	                {
	                 if(tokens.get(index + 4).getType() == Token.TokenType.REGISTER)
	                 {
	                     register = Integer.parseInt(tokens.get(index + 4).getValue().substring(1));
	                     binaryS2Register = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
	                     
	                     if(tokens.get(index + 5).getType() == Token.TokenType.NUMBER)
	                     {
	                         number = Integer.parseInt(tokens.get(index + 5).getValue());
	                         binaryNumber = String.format("%13s", Integer.toBinaryString(number)).replace(' ', '0');
	                         
	                         return binaryNumber + binarySRegister + tokens.get(index + 3).getValue() + binaryS2Register + tokens.get(index).getValue() + "11";
	                     }
	                     
	                     else if(tokens.get(index + 5).getType() == Token.TokenType.REGISTER)
	                     {
	                         register = Integer.parseInt(tokens.get(index + 5).getValue().substring(1));
	                         binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
	                         
	                         if(tokens.get(index + 6).getType() == Token.TokenType.NUMBER)
	                         {
	                         	number = Integer.parseInt(tokens.get(index + 6).getValue());
	                         	binaryNumber = String.format("%8s", Integer.toBinaryString(number)).replace(' ', '0');
	                         
	                         	return binaryNumber + binarySRegister + binaryS2Register + tokens.get(index + 3).getValue() + binaryDRegister + tokens.get(index).getValue() + "10";
	                         }
	                         else
	                         {
	                         	throw new RuntimeException("EXCPETED NUMBER IN PARSE BRANCH"
	                                     + "/nTYPE: " + tokens.get(index + 6).getType() + " VALUE:" + tokens.get(index + 6).getValue());    
	                         }
	                     }
	                     else
	                     {
	                     	throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE BRANCH"
	                                 + "/nTYPE:" + tokens.get(index + 5).getType() + " VALUE:" + tokens.get(index + 5).getValue());    
	                     }
	                 }
	                 else
	                 {
	                     throw new RuntimeException("EXCPETED REGISTER IN PARSE BRANCH"
	                                 + "/nTYPE:" + tokens.get(index + 4).getType() + " VALUE:" + tokens.get(index + 4).getValue());    
	                 }
	                }
	             else  
	             {
	             	throw new RuntimeException("EXCPETED BOOL IN PARSE BRANCH"
	                         + "/nTYPE:" + tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 3).getValue());    
	             }
	
    		 }
    		 else
    		 {
    			 throw new RuntimeException("EXCPETED REGISTER IN PARSE BRANCH"
	                     + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());    
    		 }
         }
         else
         {
             throw new RuntimeException("EXCPETED REGISTER IN PARSE BRANCH "
                         + "TYPE: " + operation.getType() + index +" VALUE: " + operation.getValue());    
         }
     }          

    /**
     * Parses a JUMP operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the JUMP operation and its operands.
     * @param index  The index of the JUMP operation token in the tokens list.
     * @return The binary representation of the JUMP operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseJump(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	String binaryNumber;

    	if(tokens.get(index + 1).getType() == Token.TokenType.REGISTER)
        {
            register = Integer.parseInt(tokens.get(index + 1).getValue().substring(1));
            binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
            
            if(tokens.get(index + 2).getType() == Token.TokenType.NUMBER)
            {
            	number = Integer.parseInt(tokens.get(index + 2).getValue());
            	binaryNumber = String.format("%18s", Integer.toBinaryString(number)).replace(' ', '0');
            
            	return binaryNumber + "0000" + binaryDRegister + tokens.get(index).getValue() + "10";
            }
            else
            {
            	throw new RuntimeException("EXCPETED NUMBER IN PARSE JUMP"
                        + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());    
            }
        }
    	
    	else if(tokens.get(index + 1).getType() == Token.TokenType.NUMBER)
        {
    		number = Integer.parseInt(tokens.get(index + 1).getValue());
        	binaryNumber = String.format("%27s", Integer.toBinaryString(number)).replace(' ', '0');
        
        	return binaryNumber + tokens.get(index).getValue() + "10";
        }
    	else
    	{
    		throw new RuntimeException("EXCPETED NUMBER/REGISTER IN PARSE JUMP"
    				+ "/nTYPE:" + tokens.get(index + 1).getType() + " VALUE:" + tokens.get(index + 1).getValue());    
    	}
    }
    
    /**
     * Parses a CALL operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the CALL operation and its operands.
     * @param index  The index of the CALL operation token in the tokens list.
     * @return The binary representation of the CALL operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseCall(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	String binarySRegister;
    	String binaryS2Register;

    	String binaryNumber;
    	
    	Token operation = tokens.get(index + 1);
        
    	 if(operation.getType() == Token.TokenType.REGISTER)
         {
    		 if(tokens.get(index + 2).getType() == Token.TokenType.REGISTER)
    		 {
	             register = Integer.parseInt(tokens.get(index + 2).getValue().substring(1));
	             binarySRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
	             
	             if(tokens.get(index + 3).getType() == Token.TokenType.EQ ||
	                tokens.get(index + 3).getType() == Token.TokenType.NEQ|| 
	                tokens.get(index + 3).getType() == Token.TokenType.GE ||
	                tokens.get(index + 3).getType() == Token.TokenType.GT ||
	                tokens.get(index + 3).getType() == Token.TokenType.LE ||
	                tokens.get(index + 3).getType() == Token.TokenType.LT) 
	                {
	                 if(tokens.get(index + 4).getType() == Token.TokenType.REGISTER)
	                 {
	                     register = Integer.parseInt(tokens.get(index + 4).getValue().substring(1));
	                     binaryS2Register = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
	                     
	                     if(tokens.get(index + 5).getType() == Token.TokenType.NUMBER)
	                     {
	                         number = Integer.parseInt(tokens.get(index + 5).getValue());
	                         binaryNumber = String.format("%13s", Integer.toBinaryString(number)).replace(' ', '0');
	                         
	                         return binaryNumber + binarySRegister + tokens.get(index + 3).getValue() + binaryS2Register + tokens.get(index).getValue() + "11";
	                     }
	                     
	                     else if(tokens.get(index + 5).getType() == Token.TokenType.REGISTER)
	                     {
	                         register = Integer.parseInt(tokens.get(index + 5).getValue().substring(1));
	                         binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
	                         
	                         if(tokens.get(index + 6).getType() == Token.TokenType.NUMBER)
	                         {
	                         	number = Integer.parseInt(tokens.get(index + 6).getValue());
	                         	binaryNumber = String.format("%8s", Integer.toBinaryString(number)).replace(' ', '0');
	                         
	                         	return binaryNumber + binarySRegister + binaryS2Register + tokens.get(index + 3).getValue() + binaryDRegister + tokens.get(index).getValue() + "10";
	                         }
	                         else
	                         {
	                         	throw new RuntimeException("EXCPETED NUMBER IN PARSE CALL"
	                                     + "/nTYPE:" + tokens.get(index + 6).getType() + " VALUE:" + tokens.get(index + 6).getValue());    
	                         }
	                     }
	                     else
	                     {
	                     	throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE CALL"
	                                 + "/nTYPE:" + tokens.get(index + 5).getType() + " VALUE:" + tokens.get(index + 5).getValue());    
	                     }
	                 }
	                 else
	                 {
	                     throw new RuntimeException("EXCPETED REGISTER IN PARSE CALL"
	                                 + "/nTYPE:" + tokens.get(index + 4).getType() + " VALUE:" + tokens.get(index + 4).getValue());    
	                 }
	                }
	             else  
	             {
	             	throw new RuntimeException("EXCPETED BOOL IN PARSE CALL"
	                         + "/nTYPE:" + tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 3).getValue());    
	             }
	
    		 }
    		 else
    		 {
    			 throw new RuntimeException("EXCPETED REGISTER IN PARSE CALL"
	                     + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());    
    		 }
         }
         else
         {
             throw new RuntimeException("EXCPETED REGISTER IN PARSE CALL"
                         + "/nTYPE:" + operation.getType() + " VALUE:" + operation.getValue());    
         }
     }          
    	
    /**
     * Parses a PUSH operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the PUSH operation and its operands.
     * @param index  The index of the PUSH operation token in the tokens list.
     * @return The binary representation of the PUSH operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parsePush(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	String binarySRegister;
    	String binaryS2Register;

    	String binaryNumber;
    	
    	Token operation = tokens.get(index + 1);
        
    	 if(operation.getType() == Token.TokenType.REGISTER)
         {
    		 register = Integer.parseInt(tokens.get(index + 1).getValue().substring(1));
             binarySRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
             
             if (tokens.get(index+2).getType() == Token.TokenType.AND || tokens.get(index+2).getType() == Token.TokenType.OR ||
            		 tokens.get(index+2).getType() == Token.TokenType.XOR || tokens.get(index+2).getType() == Token.TokenType.NOT ||
            		 tokens.get(index+2).getType() == Token.TokenType.LEFT || tokens.get(index+2).getType() == Token.TokenType.RIGHT ||
            		 tokens.get(index+2).getType() == Token.TokenType.ADD || tokens.get(index+2).getType() == Token.TokenType.SUBTRACT ||
            		 tokens.get(index+2).getType() == Token.TokenType.MULTIPLY)
             {
	             if(tokens.get(index+3).getType() == Token.TokenType.REGISTER)
	             {
	            	 register = Integer.parseInt(tokens.get(index + 3).getValue().substring(1));
	                 binaryS2Register = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
	                 
	                 if(tokens.get(index+4).getType() == Token.TokenType.REGISTER)
	                 {
		            	 register = Integer.parseInt(tokens.get(index + 4).getValue().substring(1));
		                 binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
		                 
		                 return "00000000" + binarySRegister + binaryS2Register + tokens.get(index + 2).getValue() + binaryDRegister + tokens.get(index).getValue() + "10";
	                 }
	                 else
	                 {
		                 return "0000000000000" + binaryS2Register + tokens.get(index + 2).getValue() + binarySRegister + tokens.get(index).getValue() + "11";

	                 }
	             }
	             else if(tokens.get(index+3).getType() == Token.TokenType.NUMBER)
	             {
	            	 number = Integer.parseInt(tokens.get(index+3).getValue());
	            	 binaryNumber = String.format("%18s", Integer.toBinaryString(number)).replace(' ', '0');
	            	 
	            	 return binaryNumber + tokens.get(index + 2).getValue() + binarySRegister + tokens.get(index).getValue() + "01";
	             }
             }
             
             else if(tokens.get(index+2).getType() == Token.TokenType.NUMBER)
             {
            	 number = Integer.parseInt(tokens.get(index+2).getValue());
            	 binaryNumber = String.format("%18s", Integer.toBinaryString(number)).replace(' ', '0');
	           			
            	 return binaryNumber + "0000" + binarySRegister + "01001";
	           
             }
             else
             {
            	 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE PUSH"
	                         + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());    
             }
	             
	         
             
         }    	 
    	 else if(operation.getType() == Token.TokenType.NUMBER)
    	 {
    		 	number = Integer.parseInt(operation.getValue());
          		binaryNumber = String.format("%27s", Integer.toBinaryString(number)).replace(' ', '0');
          
          	return binaryNumber + "01000";
    	 }
    	 
    	 else
    	 {
    		 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE PUSH"
                     + "/nTYPE:" + operation.getType() + " VALUE:" + operation.getValue());    
    	 }
         throw new IllegalArgumentException("Invalid PUSH operation: " + operation);
    }

    /**
     * Parses a LOAD operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the LOAD operation and its operands.
     * @param index  The index of the LOAD operation token in the tokens list.
     * @return The binary representation of the LOAD operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseLoad(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	String binarySRegister;
    	String binaryS2Register;

    	String binaryNumber;
    	
    	Token operation = tokens.get(index + 1);
        
    	 if(operation.getType() == Token.TokenType.REGISTER)
         {
    		 register = Integer.parseInt(tokens.get(index + 1).getValue().substring(1));
             binarySRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
             
             if(tokens.get(index + 2).getType() == Token.TokenType.REGISTER)
             {
            	 
            	 register = Integer.parseInt(tokens.get(index + 2).getValue().substring(1));
                 binaryS2Register = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                 
            	 if(tokens.get(index + 3).getType() == Token.TokenType.REGISTER)
                 {
            		 register = Integer.parseInt(tokens.get(index + 3).getValue().substring(1));
                     binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                     
                     return "00000000" + binarySRegister + binaryS2Register + "0000" + binaryDRegister + tokens.get(index).getValue() + "10";
                 }
            	 else
            	 {
            		 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE LOAD"
                             + "/nTYPE:" + tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 3).getValue());  
            	 }
             }
             else if(tokens.get(index + 2).getType() == Token.TokenType.NUMBER)
             {
            	 number = Integer.parseInt(tokens.get(index + 2).getValue());
        		 binaryNumber = String.format("%13s", Integer.toBinaryString(number)).replace(' ', '0');
        		 
        		 if(tokens.get(index + 3).getType() == Token.TokenType.REGISTER)
        		 {
        			 register = Integer.parseInt(tokens.get(index + 3).getValue().substring(1));
                     binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                     
                     return binaryNumber + binarySRegister + "0000" + binaryDRegister + tokens.get(index).getValue() + "11";
                     
        		 }
             }
             else
             {
            	 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE LOAD"
                         + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());  
             }
         }
    	 else if(operation.getType() == Token.TokenType.NUMBER)
    	 {
    		 number = Integer.parseInt(operation.getValue());
    		 binaryNumber = String.format("%18s", Integer.toBinaryString(number)).replace(' ', '0');
    		 
    		 if(tokens.get(index + 2).getType() == Token.TokenType.REGISTER)
    		 {
    			 register = Integer.parseInt(tokens.get(index + 2).getValue().substring(1));
                 binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                 
    			 return binaryNumber + "0000" + binaryDRegister + tokens.get(index).getValue() + "01";
    		 }
    		 else 
    		 {
    			 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE LOAD"
                         + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());  
    		 }
       
    	 }
    	 else
    	 {
    		 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE LOAD"
                     + "/nTYPE:" + operation.getType() + " VALUE:" + operation.getValue());  
    	 }
         throw new IllegalArgumentException("Invalid LOAD operation: " + operation);
    }

    /**
     * Parses a STORE operation token into its corresponding machine instruction.
     * 
     * @param tokens The list of tokens containing the STORE operation and its operands.
     * @param index  The index of the STORE operation token in the tokens list.
     * @return The binary representation of the STORE operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseStore(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	String binarySRegister;
    	String binaryS2Register;

    	String binaryNumber;
    	
    	Token operation = tokens.get(index + 1);
        
    	 if(operation.getType() == Token.TokenType.REGISTER)
         {
    		 register = Integer.parseInt(tokens.get(index + 1).getValue().substring(1));
             binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
             
             if(tokens.get(index + 2).getType() == Token.TokenType.REGISTER)
             {
            	 register = Integer.parseInt(tokens.get(index +2).getValue().substring(1));
                 binarySRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                 
                 if(tokens.get(index + 3).getType() == Token.TokenType.REGISTER)
                 {
                	 register = Integer.parseInt(tokens.get(index +3).getValue().substring(1));
                     binaryS2Register = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                     
                     return "00000000" + binarySRegister + binaryS2Register + "0000" + binaryDRegister + tokens.get(index).getValue() + "10";
                 }
                 else 
                 {
                	 throw new RuntimeException("EXCPETED REGISTER IN PARSE LOAD"
                             + "/nTYPE:" + tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 2).getValue());  
                 }
             }
             else if(tokens.get(index + 2).getType() == Token.TokenType.NUMBER)
             {            	 
            	 if(tokens.get(index + 3).getType() == Token.TokenType.REGISTER)
                 {
                	 register = Integer.parseInt(tokens.get(index +3).getValue().substring(1));
                     binarySRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                     
               		 number = Integer.parseInt(tokens.get(index + 2).getValue());
            		 binaryNumber = String.format("%13s", Integer.toBinaryString(number)).replace(' ', '0');
            		 
                     return binaryNumber + binarySRegister + "0000" + binaryDRegister + tokens.get(index).getValue() + "11";
                 }
            	 else
            	 {
            		 number = Integer.parseInt(tokens.get(index + 2).getValue());
            		 binaryNumber = String.format("%18s", Integer.toBinaryString(number)).replace(' ', '0');
            		 
            		 return binaryNumber + "0000" + binaryDRegister  + tokens.get(index).getValue() + "01";
            	 }
             }
             else
             {
            	 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE LOAD"
                         + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());
             }
         }
    	 else
    	 {
    		 throw new RuntimeException("EXCPETED REGISTER IN PARSE LOAD"
                     + "/nTYPE:" + operation.getType() + " VALUE:" + operation.getValue());  
    	 }
    }

    /**
     * Parses the POP operation.
     *
     * @param tokens The list of tokens to parse.
     * @param index  The index of the current token.
     * @return The binary representation of the POP operation.
     * @throws RuntimeException If the expected register is not found.
     */
    private static String parsePop(List<Token> tokens, int index)
    {
    	int register = 0;

    	String binaryDRegister;
       	Token operation = tokens.get(index + 1);
       	
    	if(operation.getType() == Token.TokenType.REGISTER)
    	{
    		register = Integer.parseInt(tokens.get(index + 1).getValue().substring(1));
            binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
            
            return "000000000000000000" + "0000" + binaryDRegister + tokens.get(index).getValue() + "01";
    	}
    	else 
    	{
    		throw new RuntimeException("EXCPETED REGISTER IN PARSE POP"
                    + "/nTYPE:" + operation.getType() + " VALUE:" + operation.getValue());  
    	}
    }

    /**
     * Parses the PEEK operation.
     *
     * @param tokens The list of tokens to parse.
     * @param index  The index of the current token.
     * @return The binary representation of the PEEK operation.
     * @throws RuntimeException If the expected register or number is not found.
     */
    private static String parsePeek(List<Token> tokens, int index) 
    {
    	int number = 0;
    	int register = 0;

    	String binaryDRegister;
    	String binarySRegister;
    	String binaryS2Register;

    	String binaryNumber;
    	
    	Token operation = tokens.get(index + 1);
    	
    	if(operation.getType() == Token.TokenType.REGISTER)
    	{
    		register = Integer.parseInt(tokens.get(index + 1).getValue().substring(1));
            binaryDRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
            
            if(tokens.get(index + 2).getType() == Token.TokenType.REGISTER)
            {
           	 register = Integer.parseInt(tokens.get(index +2).getValue().substring(1));
                binarySRegister = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                
                if(tokens.get(index + 3).getType() == Token.TokenType.REGISTER)
                {
               	 register = Integer.parseInt(tokens.get(index +3).getValue().substring(1));
                    binaryS2Register = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');
                    
                    return "00000000" + binarySRegister + binaryS2Register + "0000" + binaryDRegister + tokens.get(index).getValue() + "10";
                }
                else if(tokens.get(index + 3).getType() == Token.TokenType.NUMBER)
                {
              		 number = Integer.parseInt(tokens.get(index + 3).getValue());
              		 binaryNumber = String.format("%13s", Integer.toBinaryString(number)).replace(' ', '0');
           		 
                    return binaryNumber + binarySRegister + "0000" + binaryDRegister + tokens.get(index).getValue() + "11";
                }
                else
                {
               	 throw new RuntimeException("EXCPETED REGISTER/NUMBER IN PARSE PEEK"
                            + "/nTYPE:" + tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 2).getValue());  
                }
            }
            else
            {
            	throw new RuntimeException("EXCPETED REGISTER IN PARSE PEEK"
                        + "/nTYPE:" + tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());  
            }
    	}
    	else 
    	{
    		throw new RuntimeException("EXCPETED REGISTER IN PARSE PEEK"
                    + "/nTYPE:" + operation.getType() + " VALUE:" + operation.getValue());  
    	}
    }
    
    /**
     * Parses a binary arithmetic operation token into its corresponding machine instruction.
     *
     * @param tokens    The list of tokens containing the binary arithmetic operation and its operands.
     * @param index     The index of the binary arithmetic operation token in the tokens list.
     * @param operation The type of binary arithmetic operation.
     * @return The binary representation of the binary arithmetic operation.
     * @throws RuntimeException if the expected tokens are not found or have incorrect types or values.
     */
    private static String parseBinaryOperation(List<Token> tokens, int index, Token.TokenType operation) 
    {
        String binarySRegister;
        String binaryS2Register;
        String binaryDRegister;

        if (tokens.get(index + 2).getType() == Token.TokenType.REGISTER)
        {
            int register = Integer.parseInt(tokens.get(index + 2).getValue().substring(1));
            binaryS2Register = String.format("%5s", Integer.toBinaryString(register)).replace(' ', '0');

            if (tokens.get(index + 3).getType() == Token.TokenType.REGISTER) 
            {
                int registerS = Integer.parseInt(tokens.get(index + 3).getValue().substring(1));
                binarySRegister = String.format("%5s", Integer.toBinaryString(registerS)).replace(' ', '0');

                if (tokens.get(index + 4).getType() == Token.TokenType.REGISTER)
                {
                    int registerD = Integer.parseInt(tokens.get(index + 4).getValue().substring(1));
                    binaryDRegister = String.format("%5s", Integer.toBinaryString(registerD)).replace(' ', '0');

                    return "00000000" + binarySRegister + binaryS2Register + tokens.get(index + 1).getValue() +
                            binaryDRegister + "00010";
                }
                else 
                {
                    binaryDRegister = binarySRegister;
                    return "0000000000000" + binaryS2Register + tokens.get(index + 1).getValue() +
                           binaryDRegister + "00011";
                }
            } 
            else 
            {
                throw new RuntimeException("EXPECTED REGISTER IN PARSE MATH: " +
                                           tokens.get(index + 3).getType() + " VALUE:" + tokens.get(index + 3).getValue());
            }
        } 
        else 
        {
            throw new RuntimeException("EXPECTED REGISTER IN PARSE MATH: " +
                                       tokens.get(index + 2).getType() + " VALUE:" + tokens.get(index + 2).getValue());
        }
    }

    /**
     * Finds the index of the next newline token in the list of tokens starting from the given index.
     *
     * @param tokens     The list of tokens to search for the next newline token.
     * @param startIndex The starting index from which to search for the next newline token.
     * @return The index of the next newline token or the index at the end of tokens if no newline token is found.
     */
    private static int findNextNewline(List<Token> tokens, int startIndex)
    {
        int index = startIndex;
        
        while (index < tokens.size()) 
        {
            if (tokens.get(index).getType() == Token.TokenType.NEWLINE) 
            {
            	// Skip the newline token
                return index + 1; 
            }
            index++;
        }
        // Reached the end of tokens
        return index; 
    }
}
