/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc.util;

import java.sql.SQLException;

/**
 *
 * @author krisnamourtscf
 */
public class ExcecaoDao extends SQLException {

    private String message;

    public ExcecaoDao() {
        super();
    }

    public ExcecaoDao(String msg) {
        super();
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;//"Não existe letra B em sua frase";
    }
}
