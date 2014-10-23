/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neutron.capture.negocio;

/**
 *
 * @author max
 */
public class RetornoUploadFile {
    private boolean Erro;
    private String Mensagem;
    private String dataHora;

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isErro() {
        return Erro;
    }

    public void setErro(boolean Erro) {
        this.Erro = Erro;
    }

    public String getMensagem() {
        return Mensagem;
    }

    public void setMensagem(String Mensagem) {
        this.Mensagem = Mensagem;
    }
}
