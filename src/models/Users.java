package models;

public class Users {
 
    private int id;
    private String name;
    private String username;
    private String password;
    private String role; // "ADMIN", "ENTRY_OPERATOR", "EXIT_OPERATOR"
 
    public Users(int id, String name, String username, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
 
    public int getId()          { return id; }
    public String getName()     { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
 
    // No setId() on purpose — ID must never change
    public void setName(String name)         { this.name = name; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role)         { this.role = role; }
 
    public String toCSV() {
        return id + "," + name + "," + username + "," + password + "," + role;
    }

    public void display() {
        System.out.println("ID: " + id + " | Name: " + name
                         + " | Username: " + username + " | Role: " + role);
    }
}
