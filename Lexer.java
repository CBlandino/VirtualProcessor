import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer 
{
	public static List<Token> tokens;
	
	public Lexer(File inputFile, File outputFile) throws IOException
	{
		tokens = new ArrayList<>();

		lex(inputFile, outputFile);
	}
	
	/**
	 * Tokenizes the content of the input file and passes the tokens to the parser.
	 *
	 * @param inputFile  The input file to tokenize.
	 * @param outputFile The output file to store the parsed instructions.
	 * @throws IOException If an I/O error occurs.
	 */
    public static void lex(File inputFile, File outputFile) throws IOException
    {
      
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) 
        {
            String line;
            
            while ((line = br.readLine()) != null)
            {
                tokens.addAll(lexLine(line));
                tokens.add(new Token(Token.TokenType.NEWLINE));
            }
        }
        
        System.out.print("Tokenizing Complete... Passing tokens and filepath to parser :\n" + tokens + "\n" + outputFile + "\n");
        
        Parser.parse(tokens,outputFile);
    }

    /**
     * Tokenizes a single line of code.
     *
     * @param line The line of code to tokenize.
     * @return A list of tokens representing the line of code.
     */
    private static List<Token> lexLine(String line) 
    {
        List<Token> tokens = new ArrayList<>();
        String[] words = line.split("\\s+");
        
        for (String word : words) 
        {
        	
        	if (word.startsWith(";"))
            {
                break; // Exit the loop as the rest of the line is a comment
            }
        	
            tokens.add(getToken(word));
            
        }
        
        return tokens;
    }

    /**
     * Retrieves the token corresponding to a given word.
     *
     * @param word The word to get the token for.
     * @return The token corresponding to the word.
     * @throws IllegalArgumentException If the word is invalid and cannot be tokenized.
     */
    private static Token getToken(String word) 
    {
        switch (word.toUpperCase()) 
        {
            case "MATH":
                return new Token(Token.TokenType.MATH, "000");
            case "DESTONLY":
            	return new Token(Token.TokenType.DESTONLY, "0000");
            case "ADD":
                return new Token(Token.TokenType.ADD, "1110");
            case "SUBTRACT":
                return new Token(Token.TokenType.SUBTRACT, "1111");
            case "MULTIPLY":
                return new Token(Token.TokenType.MULTIPLY, "0111");
            case "AND":
                return new Token(Token.TokenType.AND, "1000");
            case "OR":
                return new Token(Token.TokenType.OR, "1001");
            case "NOT":
                return new Token(Token.TokenType.NOT, "1011");
            case "XOR":
                return new Token(Token.TokenType.XOR, "1010");
            case "COPY":
                return new Token(Token.TokenType.COPY, "000");
            case "HALT":
                return new Token(Token.TokenType.HALT);
            case "BRANCH":
                return new Token(Token.TokenType.BRANCH, "001");
            case "JUMP":
                return new Token(Token.TokenType.JUMP, "001");
            case "CALL":
                return new Token(Token.TokenType.CALL, "010");
            case "PUSH":
                return new Token(Token.TokenType.PUSH, "011");
            case "LOAD":
                return new Token(Token.TokenType.LOAD, "100");
            case "RETURN":
                return new Token(Token.TokenType.RETURN, "100");
            case "STORE":
                return new Token(Token.TokenType.STORE, "101");
            case "PEEK":
                return new Token(Token.TokenType.PEEK, "110");
            case "POP":
                return new Token(Token.TokenType.POP, "110");
            case "INTERRUPT":
                return new Token(Token.TokenType.INTERRUPT, "110");
            case "EQ":
                return new Token(Token.TokenType.EQ, "0000");
            case "NEQ":
                return new Token(Token.TokenType.NEQ, "0001");
            case "GT":
                return new Token(Token.TokenType.GT, "0100");
            case "LT":
                return new Token(Token.TokenType.LT, "0010");
            case "GE":
                return new Token(Token.TokenType.GE, "0011");
            case "LE":
                return new Token(Token.TokenType.LE, "0101");
            case "SHIFT":
                return new Token(Token.TokenType.SHIFT, "000");
            case "LEFT":
                return new Token(Token.TokenType.LEFT, "1100");
            case "RIGHT":
                return new Token(Token.TokenType.RIGHT, "1101");
            default:
                if (word.matches("-?\\d+")) 
                {
                    return new Token(Token.TokenType.NUMBER, word);
                } 
                
                else if (word.matches("R\\d+")) 
                {
                    return new Token(Token.TokenType.REGISTER, word);
                }
                else if (word.matches("\\d+R")) 
                {
                    String numericPart = word.substring(0, word.length() - 1);
                    return new Token(Token.TokenType.REGISTER, numericPart);
                }
                
                else 
                {
                    throw new IllegalArgumentException("Invalid token: " + word);
                }
        }
    }
}
