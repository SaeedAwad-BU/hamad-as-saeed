package com.example.demo;

/**
 * UserSession class to track the currently logged-in user and their role.
 * This is used for role-based access control throughout the application.
 */
public class UserSession {
    private static UserSession instance;
    private user currentUser;
    
    private UserSession() {
        // Private constructor for singleton pattern
    }
    
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setCurrentUser(user user) {
        this.currentUser = user;
    }
    
    public user getCurrentUser() {
        return currentUser;
    }
    
    public String getCurrentUserRole() {
        if (currentUser == null) {
            return null;
        }
        return currentUser.getRule();
    }
    
    public boolean isAdmin() {
        String role = getCurrentUserRole();
        if (role == null) {
            return false;
        }
        // Check for both "admin" and "Administrator" roles
        return role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("Administrator");
    }
    
    public void clearSession() {
        this.currentUser = null;
    }
}
