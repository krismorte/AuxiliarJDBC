/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc.util;

import br.auxiliarjdbc.ExcecaoJDBC;

/**
 *
 * @author krismorte
 */
public class ExcecaoUtil implements ExcecaoJDBC {

    @Override
    public void lancaExcecao(boolean exibeStackTrace, Exception exception) throws ExcecaoDao {
        if (exibeStackTrace) {
            exception.printStackTrace();
        }
        throw new ExcecaoDao(getNomeClasse() + ": " + exception.getMessage());
    }

    @Override
    public String getNomeClasse() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
