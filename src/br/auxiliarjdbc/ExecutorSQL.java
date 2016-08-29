/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc;

import br.auxiliarjdbc.util.ExcecaoDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author krismorte
 */
public class ExecutorSQL implements ExcecaoJDBC, Executor {

    private EntidadeJDBC jdbc;

    public ExecutorSQL(EntidadeJDBC jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void execucaoModificacao() throws ExcecaoDao {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            String comandoSQL = jdbc.getComandoSQL();
            conexao = jdbc.getConexao();

            comando = conexao.prepareStatement(comandoSQL);
            jdbc.preencheComando(comando);
            comando.executeUpdate();
        } catch (Exception e) {
            if (jdbc.getExcecao() != null) {
                jdbc.getExcecao().lancaExcecao(jdbc.isExibeStackTrace(), e);
            } else {
                lancaExcecao(jdbc.isExibeStackTrace(), e);
            }
        } finally {
            jdbc.finalizaConexaoComSeguranca(conexao, comando);
        }
    }

    @Override
    public ResultSet execucaoConsulta() throws ExcecaoDao, SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        //try {
        String comandoSQL = jdbc.getComandoSQL();
        conexao = jdbc.getConexao();

        comando = conexao.prepareStatement(comandoSQL);
        jdbc.preencheComando(comando);
        return comando.executeQuery();
        /*} catch (Exception e) {
         lancaExcecao(jdbc.isExibeStackTrace(), e);
         return null;
         } finally {
         jdbc.finalizaConexaoComSeguranca(conexao, comando);
         }*/
    }

    @Override
    public String getNomeClasse() {
        return jdbc.getNomeClasse();
    }

    @Override
    public void lancaExcecao(boolean exibeStackTrace, Exception exception) throws ExcecaoDao {
        if (exibeStackTrace) {
            exception.printStackTrace();
        }
        throw new ExcecaoDao(getNomeClasse() + ": " + exception.getMessage());
    }

}
