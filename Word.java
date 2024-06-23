/**
 * The {@code Word} class represents a 32-bit word composed of individual {@link Bit} instances.
 * It provides methods for bitwise operations and conversions between signed and unsigned integer values.
 */
public class Word 
{

    /**
     * An array of 32 {@link Bit} instances representing the bits of the word.
     */
    private Bit[] bits;

    /**
     * Constructs a new {@code Word} object with all bits initialized to false.
     */
    public Word()
    {
        bits = new Bit[32];
        
        for (int i = 0; i < 32; i++)
        {
            bits[i] = new Bit();
        }
    }
    
    public Word(int value) 
    {
        this();
        set(value);
    }


    

	/**
     * Gets a new {@link Bit} instance with the same value as the bit at index i.
     *
     * @param i The index of the bit to retrieve.
     * @return A new {@link Bit} instance with the same value as the specified bit.
     */
    public Bit getBit(int i) 
    {
        return new Bit(bits[i].getValue());
    }

    /**
     * Sets the value of the bit at index i to the value of the provided {@link Bit}.
     *
     * @param i     The index of the bit to set.
     * @param value The {@link Bit} containing the new value.
     */
    public void setBit(int i, Bit value) 
    {
        bits[i].set(value.getValue());
    }

    /**
     * Performs bitwise AND operation with another {@code Word} and returns a new {@code Word} with the result.
     *
     * @param other The other {@code Word} to perform AND operation with.
     * @return A new {@code Word} representing the result of the AND operation.
     */
    public Word and(Word other)
    {
        Word result = new Word();
        
        for (int i = 0; i < 32; i++) 
        {
            result.setBit(i, bits[i].and(other.getBit(i)));
        }
        
        return result;
    }

    /**
     * Performs bitwise OR operation with another {@code Word} and returns a new {@code Word} with the result.
     *
     * @param other The other {@code Word} to perform OR operation with.
     * @return A new {@code Word} representing the result of the OR operation.
     */
    public Word or(Word other)
    {
        Word result = new Word();
        
        for (int i = 0; i < 32; i++) 
        {
            result.setBit(i, bits[i].or(other.getBit(i)));
        }
        
        return result;
    }

    /**
     * Performs bitwise XOR operation with another {@code Word} and returns a new {@code Word} with the result.
     *
     * @param other The other {@code Word} to perform XOR operation with.
     * @return A new {@code Word} representing the result of the XOR operation.
     */
    public Word xor(Word other)
    {
        Word result = new Word();
        
        for (int i = 0; i < 32; i++) 
        {
            result.setBit(i, bits[i].xor(other.getBit(i)));
        }
        
        return result;
    }

    /**
     * Performs bitwise NOT operation on the current {@code Word} and returns a new {@code Word} with the result.
     *
     * @return A new {@code Word} representing the result of the NOT operation.
     */
    public Word not() 
    {
        Word result = new Word();
        
        for (int i = 0; i < 32; i++) 
        {
            result.setBit(i, bits[i].not());
        }
        
        return result;
    }

    /**
     * Performs right shift on the current {@code Word} by the specified amount of bits and returns a new {@code Word}.
     *
     * @param amount The number of bits to shift.
     * @return A new {@code Word} representing the result of the right shift operation.
     */
    public Word rightShift(int amount) {
        Word result = new Word();

        // Iterate over the bits array from right to left
        for (int i = 31; i >= amount; i--) {
            // Shift bits to the right by 'amount' positions
            result.setBit(i, getBit(i - amount));
        }

        // Fill the remaining bits with 0
        for (int i = 0; i < amount; i++) {
            result.setBit(i, 0);
        }

        return result;
    }

    /**
     * Sets the value of the bit at the specified index.
     *
     * @param index The index of the bit to set.
     * @param value The value to set the bit to (0 or 1).
     */
    public void setBit(int index, int value) {
        if (index >= 0 && index < 32 && (value == 0 || value == 1)) {
            bits[index].set(value == 1);
        }
    }
    
    /**
     * Performs left shift on the current {@code Word} by the specified amount of bits and returns a new {@code Word}.
     *
     * @param amount The number of bits to shift.
     * @return A new {@code Word} representing the result of the left shift operation.
     */
    public Word leftShift(int amount) {
        Word result = new Word();
        int size = 32;

        for (int i = 0; i < size - amount; i++) {
            // Shift bits to the left by 'amount' positions
            result.setBit(i, getBit(i + amount));
        }

        // Fill the remaining bits with 0
        for (int i = size - amount; i < size; i++) {
            result.setBit(i, 0);
        }

        return result;
    }
    
    /**
     * Performs right shift on the current {@code Word} by the specified amount of bits,
     * followed by reversing the bits, and returns a new {@code Word}.
     *
     * @param amount The number of bits to shift.
     * @return A new {@code Word} representing the result of the operation.
     */
    public void reverseBits() 
    {
    	
    	
        for (int i = 0; i < 16; i++) {
            // Swap bits at positions i and (31 - i)
            Bit temp = getBit(i);
            setBit(i, getBit(31 - i));
            setBit(31 - i, temp);
        }
    }

    
    public Word getLowest5BitsLeftShift(int amount)
    {
        Word result = leftShift(amount);
        Word mask = new Word(0b11111); // Represents the lowest 5 bits
        return result.and(mask);
    }

    public Word getLowest5BitsRightShift(int amount) 
    {
        Word result = rightShift(amount);
        Word mask = new Word(0b11111); // Represents the lowest 5 bits
        return result.and(mask);
    }
    
    /**
     * Returns a comma-separated string of 't' and 'f' representing the bits in the {@code Word}.
     *
     * @return A string representation of the {@code Word}.
     */
    public String toString() 
    {
        StringBuilder stringBuilder = new StringBuilder();
        
        for (int i = 0; i < 32; i++) 
        {
            stringBuilder.append(bits[i].toString());
            
        }
        
        return stringBuilder.toString();
    }

    /**
     * Converts the unsigned binary representation of the {@code Word} to a long integer.
     *
     * @return The unsigned value of the {@code Word} as a long integer.
     */
    public long getUnsigned() 
    {
        long result = 0;

        for (int i = 0; i < 32; i++) 
        {
            result = (result << 1) + (bits[i].getValue() ? 1 : 0);
        }

        return result;
    }

    /**
     * Converts the signed binary representation of the {@code Word} to an integer.
     *
     * @return The signed value of the {@code Word} as an integer.
     */
    public int getSigned()
    {
        int result = 0;

        for (int i = 0; i < 32; i++) 
        {
            result = (result << 1) + (bits[i].getValue() ? 1 : 0);
        }

        return result;
    }

    /**
     * Copies the values of the bits from another {@code Word} into this one.
     *
     * @param other The {@code Word} from which to copy the values.
     */
    public void copy(Word other) 
    {
        for (int i = 0; i < 32; i++) 
        {
            bits[i].set(other.getBit(i).getValue());
        }
    }

    /**
     * Sets the value of the bits of this {@code Word} based on the provided integer value.
     *
     * @param value The integer value to set.
     */
    public void set(int value) 
    {
 
        for (int i = 31; i >= 0; i--)
        {
            bits[i].set((value & 1) == 1);
            value >>>= 1;
        }
    }
    
    /**
     * Adds two bits and returns the sum along with the carry.
     *
     * @param a    The first bit.
     * @param b    The second bit.
     * @param cin  The carry input.
     * @param cout The carry output.
     * @return The sum of a, b, and cin.
     */
    private Bit add2(Bit a, Bit b, Bit cin, Bit[] cout)
    {
        Bit sum = a.xor(b).xor(cin);
        cout[0] = a.and(b).or(a.and(cin)).or(b.and(cin));
        return sum;
    }
    
    public Word add2(Word a, Word b) 
    {
        Word sum = new Word();
        Bit carry = new Bit(false);

        for (int i = 31; i >= 0; i--) 
        {
            Bit sumBit = add2(a.getBit(i), b.getBit(i), carry, new Bit[1]);
            sum.setBit(i, sumBit);
            carry = a.getBit(i).and(b.getBit(i)).or(a.getBit(i).and(carry)).or(b.getBit(i).and(carry));
        }

        return sum;
    }

    /**
     * Adds four Word objects using a multi-stage adder.
     * 
     * @param a1 The first Word object for the first stage addition.
     * @param b1 The second Word object for the first stage addition.
     * @param a2 The first Word object for the second stage addition.
     * @param b2 The second Word object for the second stage addition.
     * @return The Word object representing the sum of the four input Word objects.
     */
    public Word add4(Word a1, Word b1, Word a2, Word b2) 
    {
        // Initialize a Word object to store the sum
        Word sum = new Word();
        
        // Initialize a Bit object to represent the carry
        Bit carry = new Bit(false);
        
        // Initialize an array to store intermediate carries for each stage
        Bit[] intermediateCarries = new Bit[3];
        intermediateCarries[0] = new Bit(false);
        intermediateCarries[1] = new Bit(false);
        intermediateCarries[2] = new Bit(false);

        // Iterate through each bit from the most significant to the least significant
        for (int i = 31; i >= 0; i--) {
            // First stage adder
            Bit sumBit1 = a1.getBit(i).xor(b1.getBit(i)).xor(carry);
            carry = (a1.getBit(i).and(b1.getBit(i))).or((a1.getBit(i).xor(b1.getBit(i))).and(carry));

            // Second stage adder
            Bit sumBit2 = a2.getBit(i).xor(b2.getBit(i)).xor(intermediateCarries[0]);
            intermediateCarries[0] = (a2.getBit(i).and(b2.getBit(i))).or((a2.getBit(i).xor(b2.getBit(i))).and(intermediateCarries[0]));

            // Third stage adder
            Bit sumBit3 = sumBit1.xor(sumBit2).xor(intermediateCarries[1]);
            intermediateCarries[1] = (sumBit1.and(sumBit2)).or((sumBit1.xor(sumBit2)).and(intermediateCarries[1]));

            // Final stage adder
            Bit sumBit = sumBit3.xor(carry).xor(intermediateCarries[2]);
            intermediateCarries[2] = (sumBit3.and(carry)).or((sumBit3.xor(carry)).and(intermediateCarries[2]));

            // Set the sum bit at the current position
            sum.setBit(i, sumBit);
        }

        // Return the sum
        return sum;
    }

    
    /**
     * Adds the first instance of a new Word to the Word that calls this method 
     * and sets the result in this Word.
     *
     * @param by The second Word to add.
     * @return The sum of op1 and op2.
     */
    public Word add(Word by) 
    {
        Word result = new Word();
        Bit[] carry = new Bit[1];
        carry[0] = new Bit(false);

        for (int i = 31; i >= 0; i--)
        {
            Bit sum = add2(this.getBit(i), by.getBit(i), carry[0], carry);
            result.setBit(i, sum);
        }

        return result;
    }
    
    /**
     * Adds two Word instances and sets the result in this Word.
     *
     * @param op1 The first Word to add.
     * @param op2 The second Word to add.
     * @return The sum of op1 and op2.
     */
    public Word add(Word op1, Word op2)
    {
        Word result = new Word();
        Bit[] carry = new Bit[1];
        carry[0] = new Bit(false);

        for (int i = 31; i >= 0; i--) 
        {
            Bit sum = add2(op1.getBit(i), op2.getBit(i), carry[0], carry);
            result.setBit(i, sum);
        }

        return result;
    }

    /**
     * Subtracts two Word instances and sets the result in this Word.
     *
     * @param op1 The Word to subtract from.
     * @param op2 The Word to subtract.
     * @return The difference of op1 and op2.
     */
    public Word subtract(Word op1, Word op2)
    {
    	Word result = new Word();
        Word negOp2 = op2.not();
        //negOp2.add(new Word(1)); // two's complement negation
        
        return  result.add(op1,negOp2.add(new Word(1)));
    }

    
    
    /**
     * Multiplies two Word objects using binary multiplication.
     * 
     * @param a The first Word object, representing the multiplicand.
     * @param b The second Word object, representing the multiplier.
     * @return The Word object representing the result of the multiplication.
     */
    public Word multiply(Word a, Word b) {
        // Create a new Word object to store the result
        Word result = new Word();
        
        // Create a Word object representing the value 0
        Word zero = new Word(); 
        
        // Loop through each bit in the multiplier (b)
        for (int i = 31; i >= 0; i--) {
            // Check if the current bit in the multiplier is 1
            if (b.getBit(i).getValue()) {
                // If the current bit in the multiplier is 1,
                // add the multiplicand (a) shifted by the appropriate amount to the result
                result = add(result, a.leftShift(31 - i));
            }
        }
        
        // Return the result of the multiplication
        return result;
    }
    
    /**
     * Increments the value represented by the word.
     * Performs a binary addition of 1 to the word.
     */
    public void increment()
    {
    	Bit carry = new Bit();
    	carry.set();
    	
        for (int i = bits.length - 1; i >= 0; i--) 
        {
            Bit currentBit = bits[i];
            bits[i] = currentBit.xor(carry);
            carry = currentBit.and(carry);
        }
    }
    
    /**
     * Decrements the value represented by the word.
     * Performs a bitwise subtraction of 1 from the word.
     */
    public void decrement()
    {
        // Initialize carry to true
        Bit carry = new Bit(true);

        // Iterate over each bit starting from the least significant bit
        for (int i = bits.length - 1; i >= 0; i--)
        {
            // XOR the current bit with carry
            Bit currentBit = bits[i].xor(carry);
            bits[i] = currentBit;

            // If the current bit was 1 and carry was 1, then carry remains true, otherwise, carry becomes false
            carry = bits[i].and(carry);
        }
    }





    
    /**
     * Checks if this Word is equal to the provided Word by comparing individual bits.
     *
     * @param other The Word to compare against.
     * @return True if this Word is equal to the provided Word, false otherwise.
     */
    public boolean equals(Word other)
    {
        // Iterate over each bit starting from the most significant bit
        for (int i = 0; i < 32; i++) 
        {
            // Get the bits at the same position in both words
            Bit bitThis = this.getBit(i);
            Bit bitOther = other.getBit(i);

            // If any bit is different, the words are not equal
            if (bitThis.getValue() != bitOther.getValue())
            {
                return false;
            }
        }
        // All bits are equal, hence the words are equal
        return true;
    }

    /**
     * Checks if this Word is greater than the provided Word by comparing individual bits.
     *
     * @param other The Word to compare against.
     * @return True if this Word is greater than the provided Word, false otherwise.
     */
    public boolean isGreaterThan(Word other)
    {
        // Iterate over each bit starting from the most significant bit
        for (int i = 0; i < 32; i++)
        {
            // Get the bits at the same position in both words
            Bit bitThis = this.getBit(i);
            Bit bitOther = other.getBit(i);

            // Compare the bits
            if (bitThis.getValue() && !bitOther.getValue())
            {
                // If the current bit of this word is 1 and the corresponding bit of the other word is 0,
                // this word is greater
                return true;
            } 
            else if (!bitThis.getValue() && bitOther.getValue()) 
            {
                // If the current bit of this word is 0 and the corresponding bit of the other word is 1,
                // this word is not greater
                return false;
            }
            // If the bits are equal, continue comparing the next bit
        }
        // If all bits are equal, the words are equal, hence this word is not greater
        return false;
    }

    
    /**
     * Checks if this Word is not equal to the provided Word by comparing individual bits.
     *
     * @param other The Word to compare against.
     * @return True if this Word is not equal to the provided Word, false otherwise.
     */
    public boolean notEqual(Word other) 
    {
        // Return the opposite of equals
        return !this.equals(other);
    }

    /**
     * Checks if this Word is less than the provided Word by comparing individual bits.
     *
     * @param other The Word to compare against.
     * @return True if this Word is less than the provided Word, false otherwise.
     */
    public boolean lessThan(Word other)
    {
        // If this word is not equal to the other word, check if it's not greater than the other word
        return !this.equals(other) && !this.isGreaterThan(other);
    }

    /**
     * Checks if this Word is greater than or equal to the provided Word by comparing individual bits.
     *
     * @param other The Word to compare against.
     * @return True if this Word is greater than or equal to the provided Word, false otherwise.
     */
    public boolean greaterThanOrEqual(Word other)
    {
        // Return the opposite of lessThan
        return !this.lessThan(other);
    }

    /**
     * Checks if this Word is less than or equal to the provided Word by comparing individual bits.
     *
     * @param other The Word to compare against.
     * @return True if this Word is less than or equal to the provided Word, false otherwise.
     */
    public boolean lessThanOrEqual(Word other) 
    {
        // Return the opposite of isGreaterThan
        return !this.isGreaterThan(other);
    }

    


}