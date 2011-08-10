package ro.radcom.ldir.ldirbackendwebjsf2.tools;

/**
 * 
 * @author Iurie Potorac
 *
 */
public class DataValidation {

	private static String nameExp = "[a-zA-z]+([ '-][a-zA-Z]+)*";
	

	   
	   public static boolean validateName( String firstName )
	   {
		  firstName = firstName.trim(); 
	      return firstName.matches(nameExp);
	   } 
	   
//	   public static boolean validateLastName( String lastName )
//	   {
//		  lastName =lastName.trim();
//	      return lastName.matches( nameExp );
//	   } 

	   public static void main( String[] args )
	   {
//	       System.out.println(validateFirstName("ricAndrei-dare"));
//	       System.out.println(validateLastName("asda as asasa sfee"));
	   }
}
