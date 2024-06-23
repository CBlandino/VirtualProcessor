public class Token 
{
    private TokenType type;
    private String value;
    
    public enum TokenType
    {
        MATH, ADD, SUBTRACT, MULTIPLY, AND, OR, NOT, XOR, COPY, HALT, BRANCH, JUMP, CALL, PUSH, LOAD,
        RETURN, STORE, PEEK, POP, INTERRUPT, EQ, NEQ, GT, LT, GE, LE,
        SHIFT, LEFT, RIGHT, REGISTER, NUMBER, NEWLINE, DESTONLY
    }


    public Token(TokenType type)
    {
        this.type = type;
        this.value = null;
    }

    public Token(TokenType type, String value) 
    {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() 
    {
        return type;
    }

    public String getValue() 
    {
        return value;
    }

    @Override
    public String toString() 
    {
        if (value != null) 
        {
            return type + "(" + value + ")";
        } 
        else 
        {
            return type.toString();
        }
    }
}
