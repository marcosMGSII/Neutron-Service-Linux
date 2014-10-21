/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neutron.capture.persistencia;

import com.google.gson.Gson;
import neutron.capture.negocio.RetornoUploadFile;


/**
 *
 * @author Marcos Arruda
 */
public class acessoWebService {

    private boolean webSericeOnLine;
    private Proxy p;

    public boolean isWebSericeOnLine() {
        return webSericeOnLine;
    }

    public void setWebSericeOnLine(boolean webSericeOnLine) {
        this.webSericeOnLine = webSericeOnLine;
    }

    public acessoWebService() {        
        this.webSericeOnLine = false;
    }
    
    public RetornoUploadFile UploadFile(String ChaveAcesso, String Base64File) {
        p = new Proxy();
        String resultado;       
        String q = "jsonRecebePagina";
        resultado = p.getResultTeste(q,Base64File,ChaveAcesso);        
        resultado = resultado.substring(61);
        resultado = resultado.substring(0, resultado.length() - 1);
        Gson gson = new Gson();
        RetornoUploadFile re = gson.fromJson(resultado, RetornoUploadFile.class);
        return re;
    }
}
