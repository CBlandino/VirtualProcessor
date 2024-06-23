/**
 * The {@code Bit} class represents a single bit with methods to manipulate and perform logical operations on it.
 * The value of the bit is either true (t) or false (f).
 */
public class Bit {

    /**
     * The value of the bit.
     */
    private boolean bit;

    /**
     * Constructs a {@code Bit} object with the default value set to false.
     */
    public Bit() 
    {
        this.bit = false; // Default bit
    }

    /**
     * Constructs a {@code Bit} object with the specified initial value.
     *
     * @param bit The initial value of the bit.
     */
    public Bit(boolean bit)
    {
        this.bit = bit;
    }

    /**
     * Sets the value of the bit to the specified boolean.
     *
     * @param bit The new value of the bit.
     */
    public void set(boolean bit) 
    {
        this.bit = bit;
    }

    /**
     * Toggles the value of the bit from true to false or false to true.
     */
    public void toggle() 
    {
        this.bit = !this.bit;
    }

    /**
     * Sets the value of the bit to true.
     */
    public void set() 
    {
        this.bit = true;
    }

    /**
     * Sets the value of the bit to false.
     */
    public void clear() 
    {
        this.bit = false;
    }

    /**
     * Gets the current value of the bit.
     *
     * @return The value of the bit.
     */
    public boolean getValue() 
    {
        return this.bit;
    }

    /**
     * Performs logical AND operation with another bit and returns a new bit with the result.
     *
     * @param other The other bit to perform AND operation with.
     * @return A new bit representing the result of the AND operation.
     */
    public Bit and(Bit other) 
    {
        Bit result = new Bit();
        
        if (this.bit) 
        {
            result.set(other.getValue());
        } 
        else
        {
        	result.clear();
        }
        return result;
    }

    /**
     * Performs logical OR operation with another bit and returns a new bit with the result.
     *
     * @param other The other bit to perform OR operation with.
     * @return A new bit representing the result of the OR operation.
     */
    public Bit or(Bit other)
    {
        Bit result = new Bit();
        
        if (this.bit) 
        {
            result.set(true);
        } 
        else 
        {
            result.set(other.getValue());
        }
        return result;
    }

    /**
     * Performs logical XOR operation with another bit and returns a new bit with the result.
     *
     * @param other The other bit to perform XOR operation with.
     * @return A new bit representing the result of the XOR operation.
     */
    public Bit xor(Bit other) 
    {
        Bit result = new Bit();
        
        if (this.bit != other.getValue()) 
        {
            result.set(true);
        } 
        
        return result;
    }

    /**
     * Performs logical NOT operation on the existing bit and returns a new bit with the result.
     *
     * @return A new bit representing the result of the NOT operation.
     */
    public Bit not() {
        Bit result = new Bit();
        
        result.set(!this.bit);
        return result;
    }

    /**
     * Returns a string representation of the bit. "t" for true, "f" for false.
     *
     * @return A string representation of the bit.
     */
    public String toString() 
    {
        return this.bit ?
        		"1" : "0";
    }
}