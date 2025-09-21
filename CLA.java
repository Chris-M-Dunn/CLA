import java.util.Scanner;

public class CLA {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
    
        System.out.print("Enter operand1 (binary): ");
        String operand1 = scnr.nextLine();

        System.out.print("Enter operand2 (binary): ");
        String operand2 = scnr.nextLine();

        scnr.close();

        // Remove the whitespaces
        operand1 = operand1.replaceAll(" ", "");
        operand2 = operand2.replaceAll(" ", "");
        String c0 = "0";

        String[] generates = generate(operand1, operand2);
        String[] propogates = propogate(operand1, operand2);
        String[] superPropogates = superPropogate(propogates);
        String[] superGenerates = superGenerate(generates, propogates);
        String carryOut15 = carryout(superGenerates, superPropogates, c0);

        System.out.println("\noperand1: " + formatWithSpaces(operand1));
        System.out.println("operand2: " + formatWithSpaces(operand2) + "\n");

        String g_i = String.join("", generates);
        String p_i = String.join("", propogates);

        System.out.print("g_i: " + formatWithSpaces(g_i));
        System.out.print("\np_i: " + formatWithSpaces(p_i));

        System.out.println("\n");
        int index = 3;
        for (int i=0; i<superPropogates.length; i++) {
            System.out.println("P" + index + " = " + superPropogates[i]);
            index -= 1;
        }

        System.out.println();
        for (int i=0; i<superGenerates.length; i++) {
            System.out.println("G" + i + " = " + superGenerates[i]);
        }

        System.out.println("\nCarryOut_15 = " + carryOut15);
    }

    /** 
    @param bitString1 the first bit string
    @param bitString2 the second bit string
    @return Returns the generate (logical OR) of the two bit strings
    */
    public static String[] generate(String bitString1, String bitString2) {
        String[] result = new String[bitString1.length()];

        for (int i=bitString1.length()-1; i>=0; i--) {
            if (bitString1.charAt(i) == '1' && bitString2.charAt(i) == '1') {
                result[i] = "1";
            } else {
                result[i] = "0";
            }
        }

        return result;
    }

    /**
    @param bitString1 the first bit string
    @param bitString2 the second bit string
    @return Returns the propogates (logical AND) of the two bit strings 
    */
    public static String[] propogate(String bitString1, String bitString2) {
        String[] result = new String[bitString1.length()];

        for (int i=bitString1.length()-1; i>=0; i--) {
            if (bitString1.charAt(i) == '1' || bitString2.charAt(i) == '1') {
                result[i] = "1";
            } else {
                result[i] = "0";
            }
        }

        return result;
    }

    /** 
    @param propogates An array of propogates
    @return Returns an array of the super propogates given the propogates
    */
    public static String[] superPropogate(String[] propogates) {
        String[] result = new String[propogates.length / 4];
        String[] lowerLevelPropogates = new String[propogates.length / 4];
        
        // Turn the String[] of the propogates into a String
        // Then, turn that String into groups of 4
        String StringOfPropogates = String.join("", propogates);
        int endIndex = 4;
        for (int i=0; i<lowerLevelPropogates.length; i++) {
            lowerLevelPropogates[i] = StringOfPropogates.substring(i*4, endIndex);
            endIndex += 4;
        }
        
        // Calculate the super propogates
        for (int i=0; i<lowerLevelPropogates.length; i++) {
            if (lowerLevelPropogates[i].contains("1111")) {
                result[i] = "1";
            } else {
                result[i] = "0";
            }   
        }
         
        return result;
    }

    /**
    Table to help me keep the generates and propogates straight
    g[0] = g15; g[1] = g14; g[2] = g13; g[3] = g12;
    g[4] = g11; g[5] = g10; g[6] = g9; g[7] = g8;
    g[8] = g7; g[9] = g6; g[10] = g5; g[11] = g4;
    g[12] = g3; g[13] = g2; g[14] = g1; g[15] = g0

    p[0] = p15; p[1] = p14; p[2] = p13; p[3] = p12;
    p[4] = p11; p[5] = p10; p[6] = p9; p[7] = p8;
    p[8] = p7; p[9] = p6; p[10] = p5; p[11] = p4;
    p[12] = p3; p[13] = p2; p[14] = p1; p[15] = p0

    @param generates Array of generates 
    @param propogates Array of propogates
    @return Returns an array of the super generates given the generates and propogates
    */
    public static String[] superGenerate(String[] generates, String[] propogates) {
        String[] result = new String[generates.length / 4];

        String firstAndBlock = "";
        String secondAndBlock = "";
        String thirdAndBlock = "";

        int index = 15;
        for (int i=0; i<result.length; i++) {
            firstAndBlock = and(propogates[index-3], generates[index-2]);
            secondAndBlock = and(propogates[index-3], propogates[index-2], generates[index-1]);
            thirdAndBlock = and(propogates[index-3], propogates[index-2], propogates[index-1], generates[index]);
            result[i] = or(generates[index-3], firstAndBlock, secondAndBlock, thirdAndBlock);
            index -= 4;
        }

        return result;
    }

    /**
    Table to help me keep the superGenerates and superPropogates straight
    G[0] = G0, G[1] = G1; G[2] = G2; G[3] = G3
    P[0] = P3; P[1] = P2; P[2] = P1; P[3] = P0;
      
    @param superGenerates Array of super generates
    @param superPropogates Array of super propogates
    @param c0
    @return Returns CarryOut_15 of the given super generates and super propogates
    */
    public static String carryout(String[] superGenerates, String[] superPropogates, String c0) {
        String result = "";

        String firstAndBlock = and(superPropogates[0], superGenerates[2]);
        String secondAndBlock = and(superPropogates[0], superPropogates[1], superGenerates[1]);
        String thirdAndBlock = and(superPropogates[0], superPropogates[1], superPropogates[2], superGenerates[0]);
        String fourthAndBlock = and(superPropogates[0], superPropogates[1], superPropogates[2], superPropogates[3], c0);

        result = or(superGenerates[3], firstAndBlock, secondAndBlock, thirdAndBlock, fourthAndBlock);

        return result;
    }

    /**
    * Returns the LOGICAL AND of 2 bits
    */
    public static String and(String bitOne, String bitTwo) {
        if (bitOne == "1" && bitTwo == "1") {
            return "1";
        } else {
            return "0";
        }
    }

    /**
    * Returns the LOGICAL AND of 3 bits
    */
    public static String and(String bitOne, String bitTwo, String bitThree) {
        if (bitOne == "1" && bitTwo == "1" && bitThree == "1") {
            return "1";
        } else {
            return "0";
        }
    }

    /**
    * Returns the LOGICAL AND of 4 bits
    */
    public static String and(String bitOne, String bitTwo, String bitThree, String bitFour) {
        if (bitOne == "1" && bitTwo == "1" && bitThree == "1" && bitFour == "1") {
            return "1";
        } else {
            return "0";
        }
    }

    /**
    * Returns the LOGICAL AND of 5 bits
    */
    public static String and(String bitOne, String bitTwo, String bitThree, String bitFour, String bitFive) {
        if (bitOne == "1" && bitTwo == "1" && bitThree == "1" && bitFour == "1" && bitFive == "1") {
            return "1";
        } else {
            return "0";
        }
    }

    /**
    * Returns the LOGICAL OR of 4 bits
    */
    public static String or(String bitOne, String bitTwo, String bitThree, String bitFour) {
        if (bitOne == "1" || bitTwo == "1" || bitThree == "1" || bitFour == "1") {
            return "1";
        } else {
            return "0";
        }
    }

    /**
    * Returns the LOGICAL OR of 5 bits
    */
    public static String or(String bitOne, String bitTwo, String bitThree, String bitFour, String bitFive) {
        if (bitOne == "1" || bitTwo == "1" || bitThree == "1" || bitFour == "1" || bitFive == "1") {
            return "1";
        } else {
            return "0";
        }
    }

    /**
    * A helper method to format the Strings properly for the output
    */
    public static String formatWithSpaces(String bitString) {
        StringBuilder formattedString = new StringBuilder();

        for (int i=0; i<bitString.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formattedString.append(" ");
            }
            formattedString.append(bitString.charAt(i));
        }

        return formattedString.toString();
    }
}