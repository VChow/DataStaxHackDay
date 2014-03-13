package com.kudu.models;

public class User {
	String username, password, email, location, bio;

    public String getUsername() {
    	return username; 
    }
    
    public String getPassword() { 
    	return password; 
    }
    
    public String getEmail() { 
    	return email; 
    }
    
    public String getLocation() { 
    	return location; 
    }
    
    public String getBio() { 
    	return bio; 
    }
    
    //==========================================================
    
    public void setUsername(String username) { 
    	this.username = username; 
    }
    
    public void setPassword(String password) { 
    	this.password = password; 
    }
    
    public void setEmail(String email) { 
    	this.email = email; 
    }
    
    public void setLocation(String location) { 
    	this.location = location; 
    }
    
    public void setbio(String bio) { 
    	this.bio = bio; 
    }
}
