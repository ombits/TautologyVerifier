import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class TautologyVerifier {

    public static void main(String[] args){

        String sentence =  args[0]; //"(a & (!b | b)) | (!a & (!b | b))";
        char[] ch= findUniqueCharacter(sentence);
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();

        for(int j=0; j< ch.length ; j++){
            map.put(ch[j],j);
        }
        int size = ch.length;

        int numRows = (int)Math.pow(2, size);
        boolean[][] bools = new boolean[numRows][size];
        booleanGeneration(bools);


        for(int k =0 ; k< bools.length; k++){

             if(!parse(bools, k, map, sentence)){
                 System.out.println("False");
                 return;
             }
        }

        System.out.println("True");
   }

    private static boolean parse(boolean[][] bools , int i, HashMap<Character, Integer> map, String sentence){
        Stack<Boolean> st1 = new Stack<Boolean>();
        Stack<Character> st2 = new Stack<Character>();
        char[] ch = sentence.toCharArray();
        int flag = 0;
        for (int j = 0; j < ch.length; j++) {

            if(ch[j] == '('){
               //nothing
            }
          else if(ch[j] == ' '){
                flag++ ;
            }
            else if(ch[j] == '&' || ch[j] == '|')
                st2.push(ch[j]);

            else if(Character.isLetter(ch[j])){
                if(st1.size() > 0 && flag == 0){
                    char opr = st2.pop();
                    operation(st1,st1.pop(), bools[i][map.get(ch[j])],opr);
                }
                else
                    st1.push(bools[i][map.get(ch[j])]);
            }


            else if(ch[j] =='!' && Character.isLetter(ch[j+1])){
                if(st1.size() > 0  && flag == 0){
                    char opr = st2.pop();
                    operation(st1,st1.pop(), !bools[i][map.get(ch[j+1])],opr);
                }
                else
                    st1.push(!bools[i][map.get(ch[j+1])]);
                j++;
            }
            else if(ch[j] == ')'){
                char opr = st2.pop();
                operation(st1,st1.pop(), st1.pop(),opr);
                flag-- ;
            }
        }

        if(st1.size() >1){
            char opr = st2.pop();
            operation(st1,st1.pop(), st1.pop(),opr);
        }


        return st1.pop();
    }

    private static char[] findUniqueCharacter(String sentence){

        if(sentence == null || sentence.isEmpty())
            return null;

        Set<Character> set = new HashSet<Character>();
        char[] chars = sentence.toCharArray();

        for (char c : chars) {
            if(Character.isLetter(c)) {
               if(!set.contains(c)){
                   set.add(c);
               }
            }
        }

        char[] cha = new char[set.size()];
        int i =0;
        for(char ch : set){
            cha[i++] = ch;
        }

        return cha;

    }

    public static void booleanGeneration(boolean[][] bools){


        for(int i = 0;i<bools.length;i++)
        {
            for(int j = 0; j < bools[i].length; j++)
            {
                int val = bools.length * j + i;
                int ret = (1 & (val >>> j));
                bools[i][j] = ret != 0;
              }
        }
    }

    private static void operation(Stack<Boolean> st1, boolean b1 , boolean b2, char opr){

        if(opr == '&')
            st1.push(b1 & b2);
        else
            st1.push(b1 | b2);
    }

}

