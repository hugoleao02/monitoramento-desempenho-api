package com.projeto.monitoramento_desempenho_api.enums;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getName(){
        return role;
    }
}
