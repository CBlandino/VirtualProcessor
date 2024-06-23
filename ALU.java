
public class ALU 
{
	public Word op1;
	public Word op2;
	public Word result;
	//public String opCode;
	
	public ALU() 
	{
		op1 = new Word();
		op2 = new Word();
		result = new Word();
	}

	public boolean doBOP(Bit[] operation)
	{
		
		StringBuilder sb = new StringBuilder();

		for (int i = operation.length - 1; i >= 0; i--)
		{
		    Bit bit = operation[i];
		    
		    sb.append(bit.getValue() ? '1' : '0');
		    System.out.println("|||||| ALU BOP BIT = " + bit + "||||||");
		}
		
		String opCode = sb.toString();
		
		switch(opCode)
		{
		case "0000":
		{
			System.out.println("|||||| BOP EQUALS (EQ) CALLED ||||||");
			Processor.clockCycle +=2;
			return op1.equals(op2);
		}
		
		case "0001":
		{
			System.out.println("|||||| BOP NOT EQUALS (NEQ) CALLED ||||||");
			Processor.clockCycle +=2;
			return op1.notEqual(op2);
		}
		
		case "0010":
		{
			System.out.println("|||||| BOP LESS THAN (LT) CALLED ||||||");
			Processor.clockCycle +=2;
			return op1.lessThan(op2);
		}
			
		case "0011":
		{
			System.out.println("|||||| BOP GREATER THAN OR EQUALS (GE) CALLED ||||||");
			Processor.clockCycle +=2;
			return op1.greaterThanOrEqual(op2);
		}
		
		case "0100":
		{
			System.out.println("|||||| BOP GREATER THAN (GT) CALLED ||||||");
			Processor.clockCycle +=2;
			return op1.isGreaterThan(op2);
		}
		
		case "0101":
		{
			System.out.println("|||||| BOP LESS THAN OR EQUALS (LE) CALLED ||||||");
			Processor.clockCycle +=2;
			return op1.lessThanOrEqual(op2);
		}
		
		default:
			System.out.println("---- Invalid operation in alu.BOP ----");
		}
		
		System.out.println("---- Invalid operation in alu.BOP ----");
		return false;
}
				
	
	public void doOperation(Bit[] operation)
	{
		StringBuilder sb = new StringBuilder();

		for (int i = operation.length - 1; i >= 0; i--)
		{
		    Bit bit = operation[i];
		    
		    sb.append(bit.getValue() ? 't' : 'f');
		    System.out.println("|||||| ALU OP BIT = " + bit + "||||||");
		    sb.append(" ");
		}

		String opCode = sb.toString();

	        System.out.println("|||||| ALU COMPLETETED OP = " + opCode + " ||||||");
		switch (opCode)
		{
		case "t f f f ":
			System.out.println("|||||| ALU AND CALLED ||||||");
			result = op1.and(op2);
			Processor.clockCycle +=2;
			break;
		case "t f f t ":
			System.out.println("|||||| ALU OR CALLED ||||||");
			result = op1.or(op2);
			Processor.clockCycle +=2;
			break;
		case "t f t f ":
			System.out.println("|||||| ALU XOR CALLED ||||||");
			result = op1.xor(op2);
			Processor.clockCycle +=2;
			break;
		case "t f t t ":
			System.out.println("|||||| ALU NOT CALLED ||||||");
			result = op1.not();
			Processor.clockCycle +=2;
			break;
		case "t t f f ":
			System.out.println("|||||| ALU LEFT5LOWBIT CALLED ||||||");
			result = op1.getLowest5BitsLeftShift(op2.getSigned());
			Processor.clockCycle +=2;
			break;
		case "t t f t ":
			System.out.println("|||||| ALU RIGHT5LOWBIT CALLED ||||||");
			result = op1.getLowest5BitsRightShift(op2.getSigned());
			Processor.clockCycle +=2;
			break;
		case "t t t f ":
			System.out.println("|||||| ALU ADD CALLED ||||||");
			result = result.add(op1, op2);
			Processor.clockCycle +=2;
			break;
		case "t t t t ":
			System.out.println("|||||| ALU SUBTRACT CALLED ||||||");
			result = result.subtract(op1, op2);
			Processor.clockCycle +=2;
			break;
		case "f t t t ":
			System.out.println("|||||| ALU MULTIPLY CALLED ||||||");
			result = result.multiply(op1, op2);
			Processor.clockCycle +=10;
			break;
		default:
			System.out.println("---- Invalid operation in alu ----");
		}
	}

}