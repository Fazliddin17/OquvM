package com.example.demo.helper;

import com.example.demo.db.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {
    public static User getCurrentUser(){
        try {
            return (User) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}
