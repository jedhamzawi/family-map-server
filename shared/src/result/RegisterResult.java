package result;

/**
 * Result of register request
 */
public class RegisterResult {
    /**
     * Generated authtoken
     */
    private String authtoken;
    /**
     * User's username
     */
    private String username;
    /**
     * User's unique personID
     */
    private String personID;
    /**
     * Message generated on error
     */
    private String message;
    /**
     * Status of request
     */
    boolean success;

    /**
     * Create new RegisterResult with the following data members
     * @param authtoken Resulting auth token
     * @param username Associated username
     * @param personID Associated person ID
     * @param success Success status
     * @param message Generated error message if not successful
     */
    public RegisterResult(String authtoken, String username, String personID, boolean success,
                          String message) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.message = message;
        this.success = success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
