public class Test {
    public static void main(String[] args) {
        basicTests();
        truncatedHashTest();
    }

    // Make sure truncated hashes don't validate.
    public static void truncatedHashTest() {
        String userString = "password!";
        String goodHash = "";
        String badHash = "";
        int badHashLength = 0;
        boolean badResult = false;

        try {
            goodHash = PasswordHash.createHash(userString);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        badHashLength = goodHash.length();

        do {
            badHashLength -= 1;
            badHash = goodHash.substring(0, badHashLength);

            try {
                badResult = PasswordHash.validatePassword(userString, badHash);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }

            if (badResult != false) {
                System.err.println("Truncated hash test: FAIL " +
                    "(At hash length of " +
                    badHashLength + ")"
                    );
                System.exit(1);
            }

        // The loop goes on until it is two characters away from the last : it
        // finds. This is because the PBKDF2 function requires a hash that's at
        // least 2 characters long.
        } while (badHash.charAt(badHashLength - 3) != ':');

        if (badResult == false) {
            System.out.println("Truncated hash test: pass");
        }
    }

    /**
     * Tests the basic functionality of the PasswordHash class
     *
     * @param   args        ignored
     */
    public static void basicTests()
    {
        try
        {
            // Print out 10 hashes
            for(int i = 0; i < 10; i++)
                System.out.println(PasswordHash.createHash("p\r\nassw0Rd!"));

            // Test password validation
            boolean failure = false;
            System.out.println("Running tests...");
            for(int i = 0; i < 10; i++)
            {
                String password = ""+i;
                String hash = PasswordHash.createHash(password);
                String secondHash = PasswordHash.createHash(password);
                if(hash.equals(secondHash)) {
                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
                    failure = true;
                }
                String wrongPassword = ""+(i+1);
                if(PasswordHash.validatePassword(wrongPassword, hash)) {
                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
                    failure = true;
                }
                if(!PasswordHash.validatePassword(password, hash)) {
                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                    failure = true;
                }
            }
            if(failure)
                System.out.println("TESTS FAILED!");
            else
                System.out.println("TESTS PASSED!");
        }
        catch(Exception ex)
        {
            System.out.println("ERROR: " + ex);
        }
    }
}
