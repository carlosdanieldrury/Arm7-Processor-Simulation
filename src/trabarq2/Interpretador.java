
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


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;


public class Interpretador {
    
     private static ArrayList instructions = new ArrayList();
     private static String opCode = null;
     private static String rs = null;
     private static String rn = null;
    private static String rt = null;
    private static String rd = null;
    private static String rm = null;
    private static String immediate = null;
    private static String rest = null;
    private static String cond = null;
    private static String immOp = null;
    private static String Ssetcond = null;
    private static String shift = null;
    private static String prePost = null;
    private static String upDown = null;
    private static String byteWord = null;
    private static String writeBack = null; 
    private static String loadStore = null;
    private static Queue result = new LinkedList<String>();
     private static boolean continueExecuting = true;
     private static Pattern pattern = Pattern.compile("\\s+");
     private static Matcher matcher = null;
     private static String matcherOut = null;
     private static String[] matcherArrayOut = null;
     private static String[] IDArray;
     private static int currentInstruction = 0;
    private static int currentIntPC = 0;
    private static int i = 0;
    private static String[] memoryData = new String[262144];
    private static String[] registers = new String[32];
    private static File arquivo = null;
    private static String instrucaoIF = null;
    private static String instrucaoID = null;
    private static String instrucaoIExe = null;

    private static Queue opMemory = new LinkedList<String>();
    private static Queue regDestiny = new LinkedList<Integer>();
    private static int cont = 0;
    
     private static HashMap<String, Integer> hm = new HashMap<String, Integer>();
     
     
     // Cada instrucao tem um numero HASH
     private static void fillHm() {
        hm.put("add", 0);
        hm.put("adds", 1);
        hm.put("sub", 2);
        hm.put("and", 3);
        hm.put("orr", 4);
        hm.put("eor", 5);
        hm.put("mul", 6);
        hm.put("ldr", 7);
        hm.put("str", 8);
        hm.put("tsteq", 9);
        hm.put("teqeq", 10);
        hm.put("beq", 11);
        hm.put("bne", 12);
        hm.put("bge", 13);
        hm.put("adc", 14);
        hm.put("b", 15);
        hm.put("bleq", 16);
        hm.put("cmneq", 17);
        hm.put("sbc",18);
        hm.put("rsc", 19);
        hm.put("rsb", 20);
        hm.put("cmpeq", 21);
        hm.put("mov", 22);
    }

    
     // Retorna o valor da String IntrucaoIF que está sendo executada.
    public static String getInstrucaoIF() {
        return instrucaoIF;
    }

    // Seta o valor da String que indica instrução atual na View
    public static void setInstrucaoIF(String instrucaoIF) {
        Interpretador.instrucaoIF = instrucaoIF;
    }

   
     
     
     // retorna um padrão da instrução buscada, ha um tratamento de caracteres
     public static String[] getArrayFromInstruction() {
        matcherOut = matcherOut.replace(",", "");
        matcherOut = matcherOut.replace("[", "");
        matcherOut = matcherOut.replace("]", "");
        matcherOut = matcherOut.replace("#", "");
        matcherArrayOut = matcherOut.split("\\s");

        ++currentInstruction;

        for (i = 0; i < matcherArrayOut.length; i++) {
            matcherArrayOut[i] = utils.remove$(matcherArrayOut[i]);
        }

        return matcherArrayOut;
    }
    
    
     // retorna o numero da instrucao atual
     public static int getCurrentInstruction() {
        return currentInstruction;
    }
     
     // seta qual a instrucao que deve ser executada
     public static void setCurrentInstruction(int num) {
        currentInstruction = num;
        System.out.println((String) instructions.get(num));
       // setInstrucaoIF((String) instructions.get(num));
    }

     // Pega a proxima instrucao
    public static String getNextInstruction() {
        if (currentInstruction >= instructions.size()) {
            //currentInstruction = 0;
            instrucaoIF = "NOP";
            Window.setInstrucaoAtualBin("");

            alertDialog("Este arquivo já foi totalmente executado ou a instrução" +
                    " referenciada não existe.\nClique em 'Ok' para reexecutar.");
   
        }
        // padroniza a instrucao e divide depois em arrays
        matcher = pattern.matcher(instructions.get(currentInstruction).toString());
        
        
        if (matcher.find()) {
            matcherOut = matcher.replaceAll(" ");
            matcherOut = matcherOut.trim();
        }

        return matcherOut;
    }
    
    private static void errorDialog(String message) {
        JOptionPane.showConfirmDialog(null, message,
                "Erro", -1, 0);
        JOptionPane.showConfirmDialog(null, "Execução interrompida.", "Alerta", -1, 2);
        clearAll();
        //Window.clean();
        continueExecuting = false;
    }
    
    
    // Associa com o arquivo que o usuário escolheu na view
     public static void setArquivo(File arq) {
        arquivo = arq;

        fillHm();
    }
    
    // Tambem associa com o arquivo que o usuário escolheu.
    public static String getFileContents() {
        try{
            FileInputStream fstream = new FileInputStream(arquivo.getCanonicalPath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = null;
            String out = "";

            while ((strLine = br.readLine()) != null) {
                out = out + strLine + "\n";
                instructions.add(strLine);
            }

            in.close();

            return out;
        } catch (Exception e){
            System.out.println(e.toString());

            errorDialog("Erro ao tentar ler o arquivo.\nContate o administrador do sistema.");
            //Window.clean();
            return null;
        }
    }



    private static void alertDialog(String message) {
        JOptionPane.showConfirmDialog(null, message, "Alerta", -1, 2);
    }

    // Limpa a memoria
    private static void clearMemory() {
        i = 0;

        for (i = 0; i < 65536; i++) {
            memoryData[i] = null;
        }
    }

    // limpa o banco de registradores
    private static void clearRegisters() {
        i = 0;

        for (i = 0; i < 32; i++) {
            registers[i] = null;
        }
    }
    
    // limpa memoria e banco de registradores
     public static void clearAll() {
        continueExecuting = true;
        currentInstruction = 0;

        instructions.clear();
        
        Window.instrucaoAtual("");

        clearMemory();
        clearRegisters();
        instrucaoIF = null;
        instrucaoID = null;
        instrucaoIExe = null;
        result.clear();
        regDestiny.clear();
        cont = 0;
        Window.clean();
    }
     
     
     // metodo para salvar na memoria, utiliza o parametro para endereco e o dado para ser salvo
     public static void save2mem(int addr, String data) {
        if (addr > -1 && addr < 262144) {
            if (data.length() == 32) {
                addr = addr / 4;
                addr = addr * 4;

                memoryData[addr] = data.substring(0, 8);
                memoryData[addr + 1] = data.substring(8, 16);
                memoryData[addr + 2] = data.substring(16, 24);
                memoryData[addr + 3] = data.substring(24, 32);
            } else if (data.length() == 8) {
                memoryData[addr] = data;
            } else {
                errorDialog("Tamanho dos dados é diferente de 32bits e de 8bits.");
            }
        } else {
            errorDialog("O endereço de memória '" + addr + "' é inválido.");
        }
    }

     // Metodo para salvar o dado no banco de registradores
    public static void save2reg(int addr, String data) {
        //--addr;

        if (addr > -1 && addr < 33) {
            if (data.length() == 32) {
                registers[addr] = data;
            } else {
                errorDialog("Tamanho dos dados é diferente de 32bits e de 8bits.");
            }
        } else {
            errorDialog("O endereço de registrador '" + addr + "' é inválido.");
        }
    }

    // fazer leitura da memória
    public static String readMem(int addr, boolean Byte) {
        if (!Byte) {
            addr = addr / 4;
            addr = addr * 4;

            if (addr > -1 && addr < 262144) {
                if (memoryData[addr] == null) {
                    return "00000000000000000000000000000000";
                } else {
                    return memoryData[addr] +
                            memoryData[addr + 1] +
                            memoryData[addr + 2] +
                            memoryData[addr + 3];
                }
            } else {
                errorDialog("O endereço de memória '" + addr + "' é inválido.");

                return null;
            }
        } else {
            if (addr > -1 && addr < 262144) {
                if (memoryData[addr] == null) {
                    return "00000000";
                } else {
                    return memoryData[addr];
                }
            } else {
                errorDialog("O endereço de memória '" + addr + "' é inválido.");

                return null;
            }
        }
    }

    // leitura do dado do registrador que e passado como parametro
    public static String readReg(int addr) {
        //--addr;

        if (addr == 0) {
            return "00000000000000000000000000000000";
        } else if (addr > -1 && addr < 33) {
            if (registers[addr] == null) {
                return "00000000000000000000000000000000";
            } else {
                return registers[addr];
            }
        } else {
            errorDialog("O endereço de registrador '" + addr + "' é inválido.");

            return null;
        }
    }

     
     // roda a instruçao e completa as tabelas ID, EXE,MEM e WB
     public static void RunInstruction() throws Exception {
         Interpretador.save2reg(0, "00000000000000000000000000000000");
         Window.setRegister("00000000000000000000000000000000", 0);
        instrucaoIExe = instrucaoID;
        instrucaoID = instrucaoIF;
        
        
        Window.setIDecoder(instrucaoID);
        Window.setIExec(instrucaoIExe);
        
        Window.setLabelDecode(instrucaoID);
        Window.setLabelExecute(instrucaoIExe);
         
         if (continueExecuting) {
            
            // Busca a instrução
            if (currentInstruction >= instructions.size()){
                instrucaoIF = "NOP";
                Window.instrucaoAtual(instrucaoIF);
                Window.setLabelFetch(instrucaoIF);
                Window.setInstrucaoAtualBin("");
            }
            else {
                instrucaoIF = getNextInstruction();
                Window.setLabelFetch(instrucaoIF);
           
            // Seta na View a instrução Atual
            Window.instrucaoAtual(instrucaoIF);
            // IDARRAY recebe a instrução já dividida em um array de 4 partes dependendo do tipo de instrucao
            // parte 1 = instrucao
            // parte 2 = Rs
            // parte 3 = Rt
            // parte 4 = Rd ou imediato
            IDArray = getArrayFromInstruction();

            // PC + 4
            currentIntPC = (currentInstruction - 1) * 4;
            // Mostra na view o valor do PC
            Window.setPc(currentIntPC);
            // Coloca o valor do PC no banco de registradores
            Window.setRegister(utils.to32bits(utils.dec2bin(currentIntPC)), 15);

            // Procura qual o nome da instrução no case através do Hash da instrução
            switch (hm.get(IDArray[0])) {
                case 0:
                    // add
                    cond = "1110";
                    opCode = "0100";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    //shift = "000000000";
                    shift = utils.calcShift8bits(IDArray[1]);
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +   rs + rd + shift + rt );
     
               
                    
                    result.add(Funcoes.addSub(IDArray[2], IDArray[3], IDArray[1], false));
                    regDestiny.add(Integer.parseInt(IDArray[1]));

                    break;
                case 1:
                    // adds
                    cond = "1110";
                    opCode = "0100";
                    Ssetcond = "0";
                    immOp = "1"; // 1 pois o segundo operando e um valor imediato.
                    rt = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    immediate = utils.to8bits(utils.dec2bin(IDArray[3]));

                    shift = utils.calcShift4bits(IDArray[3]);
                    
                    Window.setInstrucaoAtualBin(cond + opCode + Ssetcond + immOp +rs + rt + shift + immediate);
                                       
 
  
                    result.add(Funcoes.adds(IDArray[3], IDArray[2], IDArray[1]));
                    regDestiny.add(Integer.parseInt(IDArray[1]));

                    break;
                case 2:
                    // sub
                    cond = "1110";
                    opCode = "0010";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +   rs + rd + shift + rt );
                    
                    
  
   
                    result.add(Funcoes.addSub(IDArray[2], IDArray[3], IDArray[1], true));
                    regDestiny.add(Integer.parseInt(IDArray[1]));

                    break;
                case 3:
                    // and
                    cond = "1110";
                    opCode = "0000";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond + rs + rd  + shift + rt );
 

                    result.add(Funcoes.andOrEor(IDArray[2], IDArray[3], IDArray[1], 0));
                    regDestiny.add(Integer.parseInt(IDArray[1]));

                    break;
                case 4:
                    // orr
                    
                    cond = "1110";
                    opCode = "1100";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    shift = "000000000";
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond + rs + rd  + shift + rt );
 
                    result.add(Funcoes.andOrEor(IDArray[2], IDArray[3], IDArray[1], 1));
                    regDestiny.add(Integer.parseInt(IDArray[1]));

                    break;
                case 5:
                    // eor
                    cond = "1110";
                    opCode = "1100";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    shift = "000000000";
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rn = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rm = utils.to4bits(utils.dec2bin(IDArray[3]));
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond + rn + rd  + shift + rm );
  
   
                    result.add(Funcoes.andOrEor(IDArray[2], IDArray[3], IDArray[1], 2));
                    regDestiny.add(Integer.parseInt(IDArray[1]));

                    break;
                case 6:
                    // mul
                    cond = "1110";
                    opCode = "0000000";
                     Ssetcond = "0";

                    shift = "000000000";
                    rn = "0000"; // o rn na multiplicacao e ignorado e setado para 0.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    rest = "1001";

                    Window.setInstrucaoAtualBin(cond + opCode + Ssetcond + rd + rn +rs + rest + rt);
                    
 
                   result.add(Funcoes.mul(IDArray[2], IDArray[3], IDArray[1]));
                   regDestiny.add(Integer.parseInt(IDArray[1]));

                    break;
               
                case 7:
                    // ldr
                    cond = "1110";
                    rest = "01";
                    immOp= "0"; // valor imediato para o deslocamento
                    prePost = "1"; // 1 para pre , 0 para post com relacao a indexacao
                    upDown = "1"; // 1 para up e 0 para down com relacao a base
                    byteWord = "0"; // 1 para byte , 0 para word
                    writeBack = "1"; // 0 para nao writeback, 1 para escrever no endereco base
                    loadStore = "1"; // 0 para store , 1 para load
                    
                    rn = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    immediate = utils.to12bits(utils.dec2bin(IDArray[3]));

                    Window.setInstrucaoAtualBin(cond + rest + immOp + prePost + upDown + byteWord + writeBack + loadStore + rn + rd + immediate);

                                
                    Funcoes.ldrStr(IDArray[3], IDArray[2], IDArray[1], false);

                    break;
                case 8:
                    // str
                    cond = "1110";
                    rest = "01";
                    immOp= "0"; // valor imediato para o deslocamento
                    prePost = "1"; // 1 para pre , 0 para post com relacao a indexacao
                    upDown = "1"; // 1 para up e 0 para down com relacao a base
                    byteWord = "0"; // 1 para byte , 0 para word
                    writeBack = "1"; // 0 para nao writeback, 1 para escrever no endereco base
                    loadStore = "0"; // 0 para store , 1 para load
                    
                    rn = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    immediate = utils.to12bits(utils.dec2bin(IDArray[3]));

                    Window.setInstrucaoAtualBin(cond + rest + immOp + prePost + upDown + byteWord + writeBack + loadStore + rn + rd + immediate);


       
                    opMemory.add(Funcoes.ldrStr(IDArray[3], IDArray[2], IDArray[1], true));

                    break;
                case 9:
                    // tsteq
                    cond = "0000";
                    opCode = "1000";
                    Ssetcond = "1";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(14));
                    rs = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[2]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond + rs + rd  + shift + rt );
 

                    result.add(Funcoes.andOrEor(IDArray[1], IDArray[2], "14", 0));
                    regDestiny.add(14);

                    break;
                case 10:
                     // teqeq
                    cond = "0000";
                    opCode = "1001";
                    Ssetcond = "1";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(14));
                    rs = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[2]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond + rs + rd  + shift + rt );
 

                    result.add(Funcoes.andOrEor(IDArray[1], IDArray[2], "14", 2));
                    regDestiny.add(14);
                    

                    break;
                case 11:
                    // beq

                    cond = "0000";
                    rest = "1010";
                    
                    immediate = utils.to24bits(utils.dec2bin(IDArray[1]));


                    Window.setInstrucaoAtualBin(cond + rest + immediate);

                   rs = "0";           

      
                    rd = Funcoes.beqBneBge(rs, IDArray[1], 0);
                    if (!rd.equals("")){
                        result.add(rd);
                        regDestiny.add(15);
                    }

                    break;
                case 12:
                    // bne
                    cond = "0001";
                    rest = "1010";
                    
                    immediate = utils.to24bits(utils.dec2bin(IDArray[1]));


                    Window.setInstrucaoAtualBin(cond + rest + immediate);

                    //rn = Interpretador.readReg(14);
                    rs = "0";           

      
                    rd = Funcoes.beqBneBge(rs, IDArray[1], 1);
                    if (!rd.equals("")){
                        result.add(rd);
                        regDestiny.add(15);
                    }
                    
                    break;
                case 13:
                    // bge 
                    cond = "1010";
                    rest = "1010";
                    
                    immediate = utils.to24bits(utils.dec2bin(IDArray[1]));


                    Window.setInstrucaoAtualBin(cond + rest + immediate);

                    //rn = Interpretador.readReg(14);
                    rs = "0";           

      
                    rd = Funcoes.beqBneBge(rs, IDArray[1], 2);
                    if (!rd.equals("")){
                        result.add(rd);
                        regDestiny.add(15);
                    }

                    break;
                case 14:
                    // adc = op1 + op2 + carry
                    cond = "1110";
                    opCode = "0100";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +   rs + rd + shift + rt );
 
                    
                    result.add(Funcoes.adcSbcRsc(IDArray[2], IDArray[3], IDArray[1], 0));
                    regDestiny.add(Integer.parseInt(IDArray[1]));
                    
                    

                    break;
                case 15:
                    // b
                    cond = "1110";
                    rest = "1010";
                    immediate = utils.to24bits(utils.dec2bin(IDArray[1]));

                    Window.setInstrucaoAtualBin(cond + rest + immediate);

       
                    Funcoes.bBl(IDArray[1], false);

                    break;
                case 16:
                     // bleq
                    cond = "0000";
                    rest = "1011";
                    immediate = utils.to24bits(utils.dec2bin(IDArray[1]));

                    Window.setInstrucaoAtualBin(cond + rest + immediate);

       
                    Funcoes.bBl(IDArray[1], true);

                    break;
                case 17:
                    // cmn eq
                    cond = "0000";
                    opCode = "1011";
                    Ssetcond = "1";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = "1110"; // R14 guarda os dados.
                    rs = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[2]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond + rs + rd  + shift + rt );
 

                    result.add(Funcoes.cmpCmn(IDArray[1], IDArray[2], true));
                    regDestiny.add(14);
                    
                    break;
                 case 18:
                   // sbc = op1 - op2 + carry - 1
                    cond = "1110";
                    opCode = "0100";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rn = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rm = utils.to4bits(utils.dec2bin(IDArray[3]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +   rn + rd + shift + rm );

               
                    
                    result.add(Funcoes.adcSbcRsc(IDArray[2], IDArray[3], IDArray[1], 1));
                    regDestiny.add(Integer.parseInt(IDArray[1]));
                     
                     
                     
                    break;
                 case 19:
                    // rsc = op2 - op1 + carry - 1
                    cond = "1110";
                    opCode = "0100";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +   rs + rd + shift + rt );
 
                    
               
                    
                    result.add(Funcoes.adcSbcRsc(IDArray[2], IDArray[3], IDArray[1], 3));
                    regDestiny.add(Integer.parseInt(IDArray[1]));
                     
                     
                    break;
                     
                     
                 case 20:
                     // rsb
                    // op2 - op1
                    cond = "1110";
                    opCode = "0011";
                    Ssetcond = "0";
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rs = utils.to4bits(utils.dec2bin(IDArray[2]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[3]));
                    shift = "000000000";
                    rest = "00";

                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +   rs + rd + shift + rt );
                    
  

   
                    result.add(Funcoes.addSub(IDArray[3], IDArray[2], IDArray[1], true)); // trocamos somente a ordem
                    regDestiny.add(Integer.parseInt(IDArray[1]));
                                       
                     break;
                  
                 case 21:
                     
                     //cmp eq
                     // condicao e op1 - op2
                    cond = "0000"; // EQ
                    opCode = "1010";
                    Ssetcond = "1"; // ha uma condicao
                    immOp = "0"; // 0 pois o segundo operando e um registrador.
                    rd = "1110"; // R14 guarda os dados.
                    rn = utils.to4bits(utils.dec2bin(IDArray[1]));
                    rt = utils.to4bits(utils.dec2bin(IDArray[2]));
                    shift = "000000000";
                    rest = "00";

                    int reg = 14;
                    Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +  rn + rd + shift + rt );


   
                    result.add(Funcoes.cmpCmn(IDArray[1], IDArray[2], false));
                    regDestiny.add(reg);

                     
                     
                    break;
                 case 22:
                     // mov 
                     // move o operando 2 para o operando 1
                     cond = "1110";
                     opCode = "1101";
                     Ssetcond = "0";
                     immOp = "0";
                     rd = "0000";
                     rn = utils.to4bits(utils.dec2bin(IDArray[1]));;
                     rm = utils.to4bits(utils.dec2bin(IDArray[2]));;
                     shift = "000000000";
                     rest = "00";
                     
                     
                     Window.setInstrucaoAtualBin(cond + rest + immOp + opCode + Ssetcond +  rn + rd + shift + rm );

                    result.add(Funcoes.mov(IDArray[1], IDArray[2]));
                    regDestiny.add(Integer.parseInt(IDArray[1]));
                     
                     
                     break;
                     
                 default:
                     System.out.println("Instrução não encontrada");
                     Window.setInstrucaoAtualBin("Instrução não encontrada");
                             
                     
            } 
            
           }
        } else {
            alertDialog("A execução já foi previamente interrompida.\nFeche o"
                    + " aquivo e abra-o novamente.");
            Window.clean();
        }
     
        
//        if ((instrucaoIExe == null) || (instrucaoIExe.equals(""))){
//            System.out.println("Sem Instrucao");
//        } else {
//             Window.setRegister(result.get(cont), regDestiny.get(cont));
//             Window.setRegister(result.get(cont), 14);
//             Interpretador.save2reg(regDestiny.get(cont), result.get(cont));
//             cont++;
 //        }
         
         
         //if ((instrucaoIExe == null) || (instrucaoIExe.equals(""))){
         if (currentInstruction < 3){
            System.out.println("Sem Instrucao");
         } else {
             if (!result.isEmpty()){
                 Window.setRegister((String)result.remove(), (Integer)regDestiny.remove());
                 //Window.setRegister((String)result.remove(), 14);
                 //Interpretador.save2reg(regDestiny.get(cont), result.get(cont));
                 
                 if (!opMemory.isEmpty()){
                     Window.setMemoryStatus((String) opMemory.remove());
                 }
             }
         }
     }
     
    
}