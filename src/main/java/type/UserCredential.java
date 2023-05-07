package type;

public class UserCredential {
    private String email;
    private String password;
    public UserCredential() {
    }

    public UserCredential(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public static UserCredential getCredentials(User user){
        return new UserCredential(user.getEmail(),user.getPassword());
    }

}
