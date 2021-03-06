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

    @Override
    public void execucaoModificacao(EntidadeJDBC jdbc) throws ExcecaoDao {
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
                jdbc.getExcecao().lancaExcecao(e);
            } else {
                lancaExcecao(e);
            }
        } finally {
            jdbc.finalizaConexaoComSeguranca(conexao, comando);
        }
    }

    @Override
    public ResultSet execucaoConsulta(EntidadeJDBC jdbc) throws ExcecaoDao, SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        String comandoSQL = jdbc.getComandoSQL();
        conexao = jdbc.getConexao();

        comando = conexao.prepareStatement(comandoSQL);
        jdbc.preencheComando(comando);
        return comando.executeQuery();
    }

    @Override
    public void lancaExcecao(Exception exception) throws ExcecaoDao {
        throw new ExcecaoDao(ExecutorSQL.class.getName() + ": " + exception.getMessage());
    }

}
