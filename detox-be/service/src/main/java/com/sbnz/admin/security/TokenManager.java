package com.sbnz.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class TokenManager {

    @Autowired
    TokenProvider tokenProvider;

    HashSet<String> invalidTokens = new HashSet<>();

    public boolean isTokenValid(String token) {
        return !invalidTokens.contains(token);
    }

    public void invalidateToken(String token) {
        invalidTokens.add(token);
//        System.out.println("Invalidating token: " + token);
//        prettyPrintAllTokens();
//        System.out.println("--------------------");
    }

    // delete expired tokens every 5 minutes
    // delete invalid tokens every 5 minutes
    @Scheduled(fixedRate = 1 * 10 * 1000)
    public void deleteExpiredTokens() {
        // iterate over all tokens and delete expired ones
//        System.out.println("Scheduled JOB: Deleting expired tokens");
        invalidTokens = invalidTokens.stream().filter(token -> {
            try {
                tokenProvider.validateToken(token, TokenType.ACCESS);
                return true;
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toCollection(HashSet::new));
//        prettyPrintAllTokens();
//        System.out.println("--------------------");
    }

    private void prettyPrintAllTokens() {
        System.out.println("Invalid tokens (" + invalidTokens.size() + ") :");
        for (String token : invalidTokens) {
            System.out.println(token);
        }
    }

}
