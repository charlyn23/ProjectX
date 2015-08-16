package charlyn23.c4q.nyc.projectx;

/**
 * Created by charlynbuchanan on 8/8/15.
 */
public class ProjectXUser {
    private String username;
    private String password;
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ProjectXUser(String username, String password, String email) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public ProjectXUser(){
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}

