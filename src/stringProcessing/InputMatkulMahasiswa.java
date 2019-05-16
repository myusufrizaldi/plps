package stringProcessing;

public class InputMatkulMahasiswa {
    
    private int[][] matrix;
    
    public InputMatkulMahasiswa() {
        this.matrix = new int[12][95];
        
        //init error
        for(int i=0; i<12; i++){
            for(int j=0; j<95; j++){
                matrix[i][j] = 11;
            }
        }
        
        //angka 0-9
        for(int j=16; j<26; j++){
            matrix[0][j] = 1;
            matrix[1][j] = 1;
            matrix[2][j] = 3;
            matrix[3][j] = 3;
            matrix[4][j] = 5;
            matrix[5][j] = 5;
            matrix[6][j] = 7;
            //matrix[5][j] = 6;
        }
        
        //A-Z
        for(int j=33; j<59; j++){
            matrix[2][j] = 3;
            matrix[3][j] = 3;
            matrix[4][j] = 5;
            matrix[5][j] = 5;
        }
        
        //a-z
        for(int j=65; j<91; j++){
            matrix[2][j] = 3;
            matrix[3][j] = 3;
            matrix[4][j] = 5;
            matrix[5][j] = 5;
        }
        
        //simbol yang mungkin masuk nama matkul
        for(int j=1; j<16; j++){
            matrix[4][j] = 5;// &
            matrix[5][j] = 5;
        }
        
        //spasi
        matrix[1][0] = 2;
        matrix[3][0] = 4;
        matrix[4][0] = 5;
        matrix[5][0] = 5;
        matrix[7][0] = 8;
        
        //tab
        matrix[5][94] = 6;
        matrix[1][94] = 2;
        matrix[3][94] = 4;
        matrix[7][94] = 8;
        
        //nilai
        for(int j=33; j<38; j++){
            matrix[8][j] = 9;
        }
        matrix[9][34] = 10;
        matrix[9][35] = 10;
    }
    
    public InputMatkulMahasiswaJSON extract(String line) {
        int currentState = 0;
        int lastValidState = 0;
        int ascii;
        String nomor = "";
        String idMatkul = "";
        String nilai = "";
        for(int i=0; i<line.length(); i++){
            ascii = (int)line.charAt(i) - 32;
            if(ascii == -23) ascii = 94;
            
            if((currentState == 0 || currentState == 1) && ascii >= 16 && ascii < 26){
                nomor += line.charAt(i);
            }else if(currentState == 2 || currentState == 3){
                idMatkul += line.charAt(i);
            }else if(currentState == 8 || currentState == 9){
                nilai += line.charAt(i);
            }
            
            System.out.print((char)line.charAt(i) + "~" + currentState + "#");
            
            currentState = this.matrix[currentState][ascii];
            if(currentState < 11) lastValidState = currentState;
        }
        
        boolean accepted = (currentState == 9 || currentState == 10);
        InputMatkulMahasiswaJSON json = new InputMatkulMahasiswaJSON(accepted, lastValidState, nomor, idMatkul, nilai);
        
        System.out.println("");
        return json;
    }
    
}
