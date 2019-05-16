package stringProcessing;

public class InputDatabaseMatkul {
    
    private int[][] matrix;
    
    public InputDatabaseMatkul() {
        this.matrix = new int[19][95];
        
        //init error
        for(int i=0; i<12; i++){
            for(int j=0; j<95; j++){
                matrix[i][j] = 18;
            }
        }
        
        //angka 0-9
        for(int j=16; j<26; j++){
            matrix[0][j] = 1;
            matrix[1][j] = 1;
            matrix[2][j] = 3;
            matrix[3][j] = 3;
            matrix[4][j] = 5;
            matrix[6][j] = 7;
            matrix[7][j] = 7;
            matrix[10][j] = 11;
            matrix[11][j] = 11;
            matrix[12][j] = 13;
            matrix[13][j] = 13;
            matrix[14][j] = 15;
            matrix[15][j] = 15;
            matrix[16][j] = 17;
            matrix[17][j] = 17;
        }
        matrix[8][16] = 9;
        matrix[8][17] = 9;
        
        //A-Z
        for(int j=33; j<59; j++){
            matrix[0][j] = 1;
            matrix[1][j] = 1;
            matrix[2][j] = 3;
            matrix[3][j] = 3;
        }
        
        //a-z
        for(int j=65; j<91; j++){
            matrix[0][j] = 1;
            matrix[1][j] = 1;
            matrix[2][j] = 3;
            matrix[3][j] = 3;
        }
        
        //simbol yang mungkin masuk nama matkul
        for(int j=1; j<16; j++){
            matrix[2][j] = 3;// &
            matrix[3][j] = 3;
        }
        
        //spasi
        matrix[2][0] = 3;
        matrix[3][0] = 3;
        
        //tab
        matrix[1][94] = 2;
        matrix[3][94] = 4;
        matrix[5][94] = 6;
        matrix[7][94] = 8;
        matrix[9][94] = 10;
        matrix[11][94] = 12;
        matrix[13][94] = 14;
        matrix[15][94] = 16;
    }
    
    public InputDatabaseMatkulJSON extract(String line) {
        int currentState = 0;
        int lastValidState = 0;
        int ascii;
        String idMatkul = "";
        String namaMatkul = "";
        String sks = "";
        String semester = "";
        boolean wajib = false;
        String prioritas = "";
        String logicPointRate = "";
        String mathPointRate = "";
        String memoryPointRate = "";
        
        for(int i=0; i<line.length(); i++){
            ascii = (int)line.charAt(i) - 32;
            if(ascii == -23) ascii = 94;
            
            if((currentState == 0 || currentState == 1) && line.charAt(i) != '\t'){
                idMatkul += line.charAt(i);
            }else if((currentState == 2 || currentState == 3) && line.charAt(i) != '\t'){
                namaMatkul += line.charAt(i);
            }else if(currentState == 4){
                sks += line.charAt(i);
            }else if(currentState == 6 || currentState == 7 && line.charAt(i) != 9){
                semester += line.charAt(i);
            }else if(currentState == 8){
                wajib = (line.charAt(i) == '1');
            }else if(currentState == 10 || currentState == 11){
                prioritas += line.charAt(i);
            }else if(currentState == 12 || currentState == 13){
                logicPointRate += line.charAt(i);
            }else if(currentState == 14 || currentState == 15){
                mathPointRate += line.charAt(i);
            }else if(currentState == 16 || currentState == 17){
                memoryPointRate += line.charAt(i);
            }
            
            //System.out.print((char)line.charAt(i) + "~" + currentState + "#");
            
            currentState = this.matrix[currentState][ascii];
            if(currentState < 18) lastValidState = currentState;
        }
        
        boolean accepted = (currentState == 16 || currentState == 17);
        InputDatabaseMatkulJSON json = new InputDatabaseMatkulJSON(accepted, lastValidState, idMatkul, namaMatkul, sks, semester, wajib, prioritas, logicPointRate, mathPointRate, memoryPointRate);
        
        System.out.println(semester);
        
        System.out.println(json.getLastValidState());
        return json;
    }
    
}
