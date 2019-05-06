package backend;

import java.util.HashMap;

public class Security {
    private HashMap<Character, Character> lib;
    
    public Security() {
        this.lib = new HashMap();
        char norm[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0' ,'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char key[] =  {'9', '2', '4', '3', '7', '1', '8', '5', '6', '0', 'c', 'a', 'h', 'i', 'x', 'y', 'm', 's', 't', 'z', 'b', 'd', 'p', 'r', 'e', 'f', 'v', 'u', 'g', 'j', 'l', 'k', 'o', 'n', 'q', 'w'};
        // 1 2 3 4 5 6 7 8 9 0 a b c d e f g h i j k l m n o p q r s t u v w x y z
        // 9 2 4 3 7 1 8 5 6 0 c a h i x y m s t z b d p r e f v u g j l k o n q w
        
        int len = norm.length;
        for(int i=0; i<len; i++){
            this.lib.put(norm[i], (i%2 == 0)? Character.toUpperCase(key[i]) : key[i]);
            this.lib.put(Character.toUpperCase(norm[i]), (i%2 == 1)? Character.toUpperCase(key[i]) : key[i]);
        }
    }
    
    public String getHashed (String password) {
        String hashed = "";
        try {
            int passwordLength = password.length();
            char currentChar;
            for(int i=0; i<passwordLength; i++){
                currentChar = password.charAt(i);
                if(this.lib.containsKey(currentChar)){
                    hashed += this.lib.get(currentChar);
                }else{
                    hashed += currentChar;
                }
            }        
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return hashed;
    }
}
