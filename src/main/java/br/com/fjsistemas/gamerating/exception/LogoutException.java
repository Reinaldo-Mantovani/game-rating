package br.com.fjsistemas.gamerating.exception;

import jakarta.servlet.ServletException;

public class LogoutException extends Throwable {
    public LogoutException(String erroAoFazerLogout, ServletException e) {
    }
}
