/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neutron.capture.persistencia;

import com.google.gson.Gson;
import com.sun.jersey.core.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import neutron.capture.negocio.RetornoUploadFile;
import static neutron.capture.persistencia.Proxy.getProp;

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

    public RetornoUploadFile UploadFile(String ChaveAcesso, String Base64File) throws IOException {
        int TamanhoPacote = Integer.parseInt(getProp().getProperty("prop.client.tamanho.pacote"));
        if (TamanhoPacote > 0) {
            TamanhoPacote = TamanhoPacote * 1024; //Converte para bytes
            TamanhoPacote = TamanhoPacote * 8; //Converte para bites
            TamanhoPacote = TamanhoPacote / 7; //Recupera a quantidade máxima de caracteres
        }
        p = new Proxy();
        String resultado;
        String q = "jsonRecebePagina";
        ParametrosProxy lista[] = new ParametrosProxy[7];
        lista[0] = new ParametrosProxy();
        lista[1] = new ParametrosProxy();
        lista[2] = new ParametrosProxy();
        lista[3] = new ParametrosProxy();
        lista[4] = new ParametrosProxy();
        lista[5] = new ParametrosProxy();
        lista[6] = new ParametrosProxy();
        lista[0].nomeCampo = "idTipoDocumental";
        lista[0].valorCampo = "36";
        lista[1].nomeCampo = "IdDoc";
        lista[1].valorCampo = "9";
        lista[2].nomeCampo = "NumeroPagina";
        lista[2].valorCampo = "1";
        lista[3].nomeCampo = "Extensao";
        lista[3].valorCampo = "TIF";

        String b64 = encodeFileToBase64Binary(Base64File);
        Gson gson = new Gson();
        RetornoUploadFile re = null;
        int TamanhBase64 = b64.length();
        if (TamanhBase64 > TamanhoPacote) {            
            for (int i = TamanhoPacote; i < TamanhBase64 + TamanhoPacote; i += TamanhoPacote) {
                int tempInicio = i - TamanhoPacote;
                if (i > TamanhBase64) {
                    lista[5].nomeCampo = "FinalPagina";
                    lista[5].valorCampo = "TRUE";
                    lista[4].valorCampo = b64.substring(tempInicio);
                } else {
                    lista[5].nomeCampo = "FinalPagina";
                    lista[5].valorCampo = "FALSE";                    
                    lista[4].valorCampo = b64.substring(tempInicio, TamanhoPacote);
                }
                if (i == TamanhoPacote) {
                    lista[6].nomeCampo = "InicioPagina";
                    lista[6].valorCampo = "TRUE";
                } else {
                    lista[6].nomeCampo = "InicioPagina";
                    lista[6].valorCampo = "FALSE";
                }
                lista[4].nomeCampo = "Base64File";                                
                resultado = p.getResultPut(q, ChaveAcesso, lista);
                resultado = resultado.substring(26).trim();
                resultado = resultado.substring(0, resultado.length() - 1);
                re = gson.fromJson(resultado, RetornoUploadFile.class);
            }
        } else {
            lista[5].nomeCampo = "FinalPagina";
            lista[5].valorCampo = "TRUE";
            lista[6].nomeCampo = "InicioPagina";
            lista[6].valorCampo = "TRUE";
            lista[4].nomeCampo = "Base64File";
            lista[4].valorCampo = b64;
            resultado = p.getResultPut(q, ChaveAcesso, lista);
            resultado = resultado.substring(27).trim();
            resultado = resultado.substring(0, resultado.length() - 1);
            re = gson.fromJson(resultado, RetornoUploadFile.class);
        }

        return re;
    }

    private String encodeFileToBase64Binary(String fileName)
            throws IOException {
        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encode(bytes);
        String encodedString = new String(encoded);
        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        byte[] bytes;
        try (InputStream is = new FileInputStream(file)) {
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }
            bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Não foi possivel ler o arquivo por completo " + file.getName());
            }
        }
        return bytes;
    }
}
