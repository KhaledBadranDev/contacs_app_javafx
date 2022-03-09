public class Test{
    
    public static void main(String[] args) {
        String email = "s@s";

        if (email.length() < 3 || email.charAt(0) == '@' || email.charAt(email.length()-1) == '@'){
            System.out.println("not valid");
            return;                   
        }

        for(int i = 1; i < email.length()-1; i++) {
            
            if (email.charAt(i) == '@'){
                System.out.println("valid");
                return;   
            }
        }  

        System.out.println("not valid");
    }
}