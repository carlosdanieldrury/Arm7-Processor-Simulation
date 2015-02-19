
package trabarq2;


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



public class Funcoes {
    
    private static int regAux = 13;
    private static int regData = 14;
    private static int carry = 0;
    
    
    public static int setCarry(int rm, int rn, int operation){
        String srm = Interpretador.readReg(rm);
        String srn = Interpretador.readReg(rn);
       // switch ( operation ){
            //case 1: // Soma
                    if ((srm.substring(0, 0).equals("1")) && (srn.substring(0, 0).equals("1")))
                        carry = 1;
                    else carry = 0;
                   // break;
                    
            
       // }
        
        
        
        return carry;
    }
    
    
    // Funcao addi
    public static String adds(String simm, String srs, String srt) {
        int rs = Integer.parseInt(srs);
        int rt = Integer.parseInt(srt);
        int imm = Integer.parseInt(simm);
        String resultString = null;
        
        srs = Interpretador.readReg(rs);
        rs = utils.bin2dec(srs);

        int result = rs + imm;

        simm = utils.dec2bin(result);
        simm = utils.to32bits(simm);

        Interpretador.save2reg(rt, simm);

        resultString = simm;
        Interpretador.save2reg(regAux, resultString);
        return resultString;
        // Grava no banco de Registros
        //Window.setRegister(simm, rt + 2);
  
    }

    // Funcao add e sub
    public static String addSub(String srs, String srt, String srd, boolean SUB) {
        int rs = Integer.parseInt(srs);
        int rt = Integer.parseInt(srt);
        int rd = Integer.parseInt(srd);
        String resultString = "";

        srs = Interpretador.readReg(rs);
        srt = Interpretador.readReg(rt);

        rs = utils.bin2dec(srs);
        rt = utils.bin2dec(srt);
        
        int result;
        

        if (!SUB) {
            result = rs + rt;
           // setCarry(rs,rt,1);
        } else {
            result = rs - rt;
        }

        srd = utils.dec2bin(result);
        srd = utils.to32bits(srd);
        
        resultString = srd;

        Interpretador.save2reg(rd, resultString);
        Interpretador.save2reg(regAux, resultString);
        return resultString;

        //Window.setRegister(srd, rd + 2);

    }
    
    
    
    // Funcao add e sub
    public static String adcSbcRsc(String srs, String srt, String srd, int instrucao) {
        int rs = Integer.parseInt(srs);
        int rt = Integer.parseInt(srt);
        int rd = Integer.parseInt(srd);
        
        String resultString = "";
        srs = Interpretador.readReg(rs);
        srt = Interpretador.readReg(rt);
        
        rs = utils.bin2dec(srs);
        rt = utils.bin2dec(srt);

        
        int result = 0;
        

        if (instrucao == 0) {
            result = rs + rt + carry;
        } 
        if (instrucao == 1) {
            result = rs - rt + carry - 1;
        }
        if (instrucao == 2){
            result = rt - rs + carry - 1;
        }

        srd = utils.dec2bin(result);
        srd = utils.to32bits(srd);
        
        resultString = srd;

        Interpretador.save2reg(rd, srd);
        Interpretador.save2reg(regAux, resultString);
        return resultString;


    }

    
    // And ou OR ou Nor
    public static String andOrEor(String srs, String srt, String srd, int func) {
        int rs = Integer.parseInt(srs);
        int rt = Integer.parseInt(srt);
        int rd = Integer.parseInt(srd);

        srs = Interpretador.readReg(rs);
        srt = Interpretador.readReg(rt);

        String result = "";

        switch (func) {
            case 0:
                for (int i = 0; i < 32; i++) {
                    if ( (srs.charAt(i) == '1') && (srt.charAt(i) == '1') ) {
                        result = result + "1";
                    } else {
                        result = result + "0";
                    }
                }

                break;
            case 1:
                for (int i = 0; i < 32; i++) {
                    if ( (srs.charAt(i) == '0') && (srt.charAt(i) == '0') ) {
                        result = result + "0";
                    } else {
                        result = result + "1";
                    }
                }

                break;
            case 2:
                for (int i = 0; i < 32; i++) {
                    if ( ((srs.charAt(i) == '0') && (srt.charAt(i) == '0')) || ((srs.charAt(i) == '1') && (srt.charAt(i) == '1')) ) {
                        result = result + "1";
                    } else {
                        result = result + "0";
                    }

                }

                break;
        }

        
        Interpretador.save2reg(rd, result);
        Interpretador.save2reg(regAux, result);
        return result;
        //Window.setRegister(result, rd + 2);
  
    }

    // Funcao mult
    public static String mul(String SRsDec, String SRtDec, String SRdDec) {
        int rs = Integer.parseInt(SRsDec);
        int rt = Integer.parseInt(SRtDec);
        int rd = Integer.parseInt(SRdDec);
        String resultString;

        SRsDec = Interpretador.readReg(rs);
        SRtDec = Interpretador.readReg(rt);

        rs = utils.bin2dec(SRsDec);
        rt = utils.bin2dec(SRtDec);

        int result = rs * rt;
        resultString = utils.dec2bin(result);

        resultString = utils.to32bits(resultString);
        
        //SRdDec = utils.to64bits(SRtDec);

        //SRsDec = SRtDec.substring(0, 32);
        //SRtDec = SRtDec.substring(32, 64);

       // Interpretador.setRegHI(SRsDec);
       // Interpretador.setRegLO(SRtDec);
         Interpretador.save2reg(rd, resultString);
        
        //Window.setRegister(SRsDec, 1);
        //Window.setRegister(SRtDec, 2);
        
        Interpretador.save2reg(regAux, resultString);
        return resultString;
      
    }
    
    

    // LW ou SW
    public static String ldrStr(String SRsDec, String SRtDec, String Simm, boolean SW) {
        int rs = Integer.parseInt(SRsDec);
        int rt = Integer.parseInt(SRtDec);
        int imm = Integer.parseInt(Simm);
        String resultString = "";

        SRsDec = Interpretador.readReg(rs);

        rs = utils.bin2dec(SRsDec);

        if (!SW) {
            // load
            SRtDec = Interpretador.readMem(rs + imm, false);

            Interpretador.save2reg(rt, SRtDec);
            Interpretador.save2reg(regAux, SRtDec);

            Window.setRegister(SRtDec, rt + 2);
           
    
            
        } else {
            SRtDec = Interpretador.readReg(rt);

            Interpretador.save2mem(rs + imm, SRtDec);
 
            
            //Window.setMemoryStatus("'" + SRtDec + "' salvo no endereço " + String.valueOf(rs + imm));
            resultString = ("'" + SRtDec + "' salvo no endereço " + String.valueOf(rs + imm));
            
        }
        return resultString;
    }

    

    // Beq ou Bne ou Bge
    public static String beqBneBge(String SRsDec, String Simm, int func) {
        System.out.println("Branch");
        String SRtDec;
        int rs = Integer.parseInt(SRsDec);
        int rt = regData;
        int imm = Integer.parseInt(Simm);
        String resultString = "";

        SRsDec = Interpretador.readReg(rs);
        SRtDec = Interpretador.readReg(rt);

        switch (func) {
            case 0:
                if (SRsDec.equals(SRtDec)) {
                    System.out.println("Entrei no BEQ");
                    resultString = utils.dec2bin(Interpretador.getCurrentInstruction() - 1 + imm);
                    Interpretador.setCurrentInstruction(Interpretador.getCurrentInstruction() - 1 + imm);
                }

                break;
            case 1:
                if (!SRsDec.equals(SRtDec)) {
                    resultString = utils.dec2bin(Interpretador.getCurrentInstruction() - 1 + imm);
                    Interpretador.setCurrentInstruction(Interpretador.getCurrentInstruction() - 1 + imm);
         
                } 

                break;
            case 2:
                rs = utils.bin2dec(SRsDec);
                rt = utils.bin2dec(SRtDec);

                if (rs > rt) {
                    resultString = utils.dec2bin(Interpretador.getCurrentInstruction() - 1 + imm);
                    Interpretador.setCurrentInstruction(Interpretador.getCurrentInstruction() - 1 + imm);

       
                } else if (rs == rt) {
                    resultString = utils.dec2bin(Interpretador.getCurrentInstruction() - 1 + imm);
                    Interpretador.setCurrentInstruction(Interpretador.getCurrentInstruction() - 1 + imm);

         
                } else {
                   
           
                }

                break;
        }
        resultString = utils.to32bits(resultString);
        Interpretador.save2reg(regAux, resultString);
        return resultString;
     
    }
    
    
    // cmp
    public static String cmpCmn(String srs, String srt, boolean cmn){
        int rs = Integer.parseInt(srs);
        int rt = Integer.parseInt(srt);
        //int rd = Integer.parseInt(srd);
        String resultString;
        resultString = null;


        srs = Interpretador.readReg(rs);
        srt = Interpretador.readReg(rt);

        System.out.println("  Leu os regs");
        
        rs = utils.bin2dec(srs);
        rt = utils.bin2dec(srt);
        
        int result;

        if (!cmn)
            result = rs - rt;
        else
            result = rs + rt;

        resultString = utils.dec2bin(result);
        resultString = utils.to32bits(resultString);
        
        System.out.println(" Result e :"+result);
        System.out.println(" ResultString :" + resultString);
        
        Interpretador.save2reg(regData, resultString);
        return resultString;

       
  
    }
    
    
    // mov
    public static String mov(String srn, String srm){
        int rn = Integer.parseInt(srn);
        int rm = Integer.parseInt(srm);
        String result = "";
        
        result = Interpretador.readReg(rm);
        
        Interpretador.save2reg(rn, result);
        
        return result;
        
    }
   
      
   

    // Jump ou Jump and link
    public static void bBl(String Simm, boolean bl) {
        int imm = Integer.parseInt(Simm);

        if (bl) {
            String sra = utils.to32bits(utils.dec2bin(Interpretador.getCurrentInstruction()));

            Interpretador.save2reg(16, sra);

            Window.setRegister(sra, regAux);
        }

        Interpretador.setCurrentInstruction(imm);
   
  
    }

}