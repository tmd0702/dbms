package Utils;

import java.util.regex.Pattern;
public class Validator {
    public Validator() {

    }

    public static boolean patternMatches(String text, String regexPattern) {
        if (text == null) return false;
        return Pattern.compile(regexPattern)
                .matcher(text)
                .matches();
    }
    public static boolean validatePassword(String password) {
//        min length is 8 and max length is 25
//        at least include a digit number,
//        at least an upcase and a lowcase letter
//        at least a special characters
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,25}$";

        return patternMatches(password, regex);
    }
    public static boolean validateUsername(String username) {
//        Username consists of alphanumeric characters (a-zA-Z0-9), lowercase, or uppercase.
//        Username allowed of the dot (.), underscore (_), and hyphen (-).
//        The dot (.), underscore (_), or hyphen (-) must not be the first or last character.
//        The dot (.), underscore (_), or hyphen (-) does not appear consecutively, e.g., java..regex
//        The number of characters must be between 5 and 25.
        String regex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";

        return patternMatches(username, regex);
    }
    public static boolean validateEmail(String email) {
//        This regular expression is provided by the OWASP validation regex repository to check the email validation:
        String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        return patternMatches(email, regex);
    }
}
