/*
 * Trabalho 1 - Arquitetura de Computadores 2 
 * 
 * Simulador do ARM7
 * 
 * Alunos: Carlos Daniel Drury
 *	   Frederico
 *         Gabriel Almeida Miranda
 *         Leonardo Almeida de Araújo
 *         Juliano
 *
 * Implementação na linguagem JAVA
 * IDE utilizada para execução e compilação: NetBeans 7.3
 * Plataformas testadas: Windows 7 e Ubuntu 12.10
 *
 */

package trabarq2;

public class utils {
    // private static final String ZEROS = "0000000000000000000000000000000000000000000000000000000000000000";
     private static final String ZEROS = "00000000000000000000000000000000";

    public static String getZEROS(int num) {
        return ZEROS.substring(0, num);
    }

    public static String dec2bin(int num) {
        return Integer.toBinaryString(num);
    }
    

    public static String dec2bin(String snum) {
        int num = Integer.parseInt(snum);

        return Integer.toBinaryString(num);
    }

    public static int bin2dec(String num) {
        return Integer.parseInt(num, 2);
    }

    public static String to64bits(String num) {
        return ZEROS.substring(0, 64 - num.length()).concat(num);
    }

    public static String to32bits(String num) {
        return ZEROS.substring(0, 32 - num.length()).concat(num);
    }

    public static String to26bits(String num) {
        return ZEROS.substring(0, 26 - num.length()).concat(num);
    }
    
    public static String to24bits(String num) {
        return ZEROS.substring(0, 24 - num.length()).concat(num);
    }
    
    public static String to12bits(String num) {
        return ZEROS.substring(0, 12 - num.length()).concat(num);
    }

    public static String to16bits(String addr) {
        return ZEROS.substring(0, 16 - addr.length()).concat(addr);
    }

    public static String to5bits(String addr) {
        return ZEROS.substring(0, 5 - addr.length()).concat(addr);
    }
    
    public static String to4bits(String addr) {
        return ZEROS.substring(0, 4 - addr.length()).concat(addr);
    }
    
    public static String to3bits(String addr) {
        String result = ZEROS.substring(0, 3 - addr.length()).concat(addr);
        return result;
    }
    
    public static String to8bits(String addr) {
        return ZEROS.substring(0, 8 - addr.length()).concat(addr);
    }

    public static String remove$(String ent) {
        return ent.replace("$", "");
    }
    
    public static String calcShift4bits(String snum){
        int num = Integer.parseInt(snum);
        String shift = "0000";
        if (num > 256){
            if (num < 512)
                shift = "0001";
            else if (num < 1024)
                shift = "0010";
            else if (num < 2048)
                shift = "0011";
            else if (num < 4096)
                shift = "0100";
            else if (num < 8192)
                shift = "0101";
            else if (num < 16384)
                shift = "0110";
            else if (num < 32768)
                shift = "0111";
            else if (num < 65536)
                shift = "1000";
            else if (num < 131072)
                shift = "1001";
            else if (num < 262144)
                shift = "1010";
            else if (num < 524288)
                shift = "1011";
            else if (num < 1048576)
                shift = "1100";
            else if (num < 2097152)
                shift = "1101";
            else if (num < 4194304)
                shift = "1110";
            else if (num < 8388608)
                shift = "1111";
        }
        
      
        return shift;
    }
    
    
    
    public static String calcShift8bits(String snum){
        int num = Integer.parseInt(snum);
        String shiftType = "100"; // rotacao aritmetica a direita
        String shift = "00000000";
        if (num > 256){
            if (num < 512)
                shift = "00001";
            else if (num < 1024)
                shift = "00010";
            else if (num < 2048)
                shift = "00011";
            else if (num < 4096)
                shift = "00100";
            else if (num < 8192)
                shift = "00101";
            else if (num < 16384)
                shift = "00110";
            else if (num < 32768)
                shift = "00111";
            else if (num < 65536)
                shift = "01000";
            else if (num < 131072)
                shift = "01001";
            else if (num < 262144)
                shift = "01010";
            else if (num < 524288)
                shift = "01011";
            else if (num < 1048576)
                shift = "01100";
            else if (num < 2097152)
                shift = "01101";
            else if (num < 4194304)
                shift = "01110";
            else if (num < 8388608)
                shift = "01111";
            else if (num < 16777216)
                shift = "10000";
            else if (num < 33554432)
                shift = "10001";
            else if (num < 67108864)
                shift = "10010";
            else if (num < 134217728)
                shift = "10011";
            else if (num < 268435456)
                shift = "10100";
            else if (num < 536870912)
                shift = "10101";
            else if (num < 1073741824)
                shift = "10110";
//            else if (num < 2147483648)
//                shift = "10111";
//            else if (num < 4294967296)
//                shift = "11000";
//            else if (num < 16777216)
//                shift = "11001";
//            else if (num < 16777216)
//                shift = "11010";
//            else if (num < 16777216)
//                shift = "11011";
//            else if (num < 16777216)
//                shift = "11100";
//            else if (num < 16777216)
//                shift = "11101";
//            else if (num < 16777216)
//                shift = "11110";
//            else if (num < 16777216)
//                shift = "11111";
      
            
        }
        
      
        return shift + shiftType;
    }
    
    
 
    
}