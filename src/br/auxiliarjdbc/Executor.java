/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc;

import br.auxiliarjdbc.util.ExcecaoDao;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author krismorte
 */
public interface Executor {

    public ResultSet execucaoConsulta() throws ExcecaoDao,SQLException;

    public void execucaoModificacao() throws ExcecaoDao;

    public void lancaExcecao(boolean exibeStackTrace, Exception exception) throws ExcecaoDao;
    
}
