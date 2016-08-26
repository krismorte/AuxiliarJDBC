/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc;

import br.auxiliarjdbc.util.ExcecaoDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author krisnamourtscf
 */
public abstract class EntidadeDao<T> {

    private HashMap campos;
    private Integer indiceCampo = 1;
    private String comandoEscolhido;
    private HashMap comandosSQL = new HashMap<String, String>();

    public abstract void incluir(T model) throws ExcecaoDao;

    
    
    public abstract void atualizar(T model) throws ExcecaoDao;

    public abstract void remover() throws ExcecaoDao;

    public abstract T consultar() throws ExcecaoDao;

    public abstract List<T> listar() throws ExcecaoDao;

    public void adicionaComandoSQL(String nomeConsulta, String consultaSQL) {
        comandosSQL.put(nomeConsulta, consultaSQL);
    }

    public void setComandoSQL(String nomeConsulta) {
        comandoEscolhido = nomeConsulta;
    }

    public String getComandoSQL() {
        return comandosSQL.get(comandoEscolhido).toString();
    }

    private void adicionaCampo(Object valor) {
        campos.put(indiceCampo, valor);
        indiceCampo++;
    }

    public void adicionaCampos(Object... valor) {
        for (Object o : valor) {
            adicionaCampo(o);
        }
    }

    public void removeCampos() {
        campos = new HashMap<Integer, Object>();
        indiceCampo = 1;
    }

    public void preencheComando(PreparedStatement comando) throws ExcecaoDao {
        try {
            finalizaComando(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    public void preencheComando(PreparedStatement comando, Object... valores) throws ExcecaoDao {
        try {
            adicionaCampos(valores);
            finalizaComando(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    private void finalizaComando(PreparedStatement cmd) throws SQLException {
        for (int i = 1; i < indiceCampo; i++) {
            cmd.setObject(i, campos.get(i));
        }
    }

    public void lancaExcecao(Exception ex) throws ExcecaoDao {
        throw new ExcecaoDao(this.getClass().getName() + ": " + ex.getMessage());
    }

    public void finalizaConexaoComSeguranca(Connection con, PreparedStatement cmd) throws ExcecaoDao {
        try {
            if (con != null) {
                con.close();
            }
            if (cmd != null) {
                cmd.close();
            }
        } catch (Exception ex) {
            throw new ExcecaoDao(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

}
