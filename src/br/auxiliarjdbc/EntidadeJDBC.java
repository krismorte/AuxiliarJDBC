/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc;

import br.auxiliarjdbc.util.ExcecaoDao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author krisnamourtscf
 */
public abstract class EntidadeJDBC<T> {

    private HashMap<Integer, Object> campos = new HashMap();
    private Integer indiceCampo = 1;
    private Integer comandoEscolhido;
    private HashMap<Integer, String> comandosSQL = new HashMap();
    private Connection conexao;
    private String nomeClasse;
    private boolean exibeStackTrace;
    private ExcecaoJDBC excecao;

    public EntidadeJDBC(String nomeClasse) {
        this.nomeClasse = nomeClasse;
        this.exibeStackTrace = false;
    }

    public abstract void incluir(T model) throws ExcecaoDao;

    public abstract void atualizar(T model) throws ExcecaoDao;

    public abstract void remover() throws ExcecaoDao;

    public abstract T consultar() throws ExcecaoDao;

    public abstract List<T> listar() throws ExcecaoDao;

    public void adicionaComandoSQL(Integer identificador, String consultaSQL) {
        comandosSQL.put(identificador, consultaSQL);
    }

    public void escolherComandoSQL(Integer identificador) {
        comandoEscolhido = identificador;
    }

    public String getComandoSQL() {
        return comandosSQL.get(comandoEscolhido);
    }

    private void adicionaCampo(Object valor) {
        if (valor instanceof java.util.Date) {
            valor = new Date(((java.util.Date) valor).getTime());
        } else if (valor instanceof java.util.Calendar) {
            java.util.Date dataTmp = ((java.util.Calendar) valor).getTime();
            valor = new Date(dataTmp.getTime());
        }
        campos.put(indiceCampo, valor);
        indiceCampo++;
    }

    public void adicionaCampos(Object... valor) {
        for (Object o : valor) {
            adicionaCampo(o);
        }
    }

    public void removeCampos() {
        campos = new HashMap();
        indiceCampo = 1;
    }

    public void preencheComando(PreparedStatement comando) throws ExcecaoDao {
        try {
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    public void preencheComando(PreparedStatement comando, Object... valores) throws ExcecaoDao {
        try {
            adicionaCampos(valores);
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    private void preencheValores(PreparedStatement cmd) throws SQLException {
        if (campos.isEmpty()) {
            return;
        }
        for (int i = 1; i < indiceCampo; i++) {
            cmd.setObject(i, campos.get(i));
        }
    }

    public void executaComando() throws ExcecaoDao {
        Executor exec = new ExecutorSQL(this);
        exec.execucaoModificacao();
    }

     public ResultSet executaConsulta() throws ExcecaoDao,SQLException {
        Executor exec = new ExecutorSQL(this);
        return exec.execucaoConsulta();
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

    /**
     * @return the conexao
     */
    public Connection getConexao() {
        return conexao;
    }

    /**
     * @param conexao the conexao to set
     */
    public void setConexao(Connection conexao) {
        this.conexao = conexao;
    }

    /**
     * @return the nomeClasse
     */
    public String getNomeClasse() {
        return nomeClasse;
    }

    /**
     * @return the exibeStackTrace
     */
    public boolean isExibeStackTrace() {
        return exibeStackTrace;
    }

    /**
     * @param exibeStackTrace the exibeStackTrace to set
     */
    public void setExibeStackTrace(boolean exibeStackTrace) {
        this.exibeStackTrace = exibeStackTrace;
    }

    /**
     * @return the excecao
     */
    public ExcecaoJDBC getExcecao() {
        return excecao;
    }

    /**
     * @param excecao the excecao to set
     */
    public void setExcecao(ExcecaoJDBC excecao) {
        this.excecao = excecao;
    }

    
    
}
