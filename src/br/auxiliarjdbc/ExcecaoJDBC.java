/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc;

import br.auxiliarjdbc.util.ExcecaoDao;

/**
 *
 * @author krismorte
 */
public interface ExcecaoJDBC {
    
    public void lancaExcecao(boolean exibeStackTrace, Exception exception) throws ExcecaoDao;
    public String getNomeClasse();
}
