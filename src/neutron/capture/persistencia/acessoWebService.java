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
import java.util.ArrayList;
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

    public RetornoUploadFile UploadFile(String ChaveAcesso, String Base64File) throws IOException {
        p = new Proxy();
        String resultado;
        String q = "jsonRecebePagina";
        String b64 = encodeFileToBase64Binary(Base64File);
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
        lista[4].nomeCampo = "Base64File";
        lista[4].valorCampo = b64;
        lista[5].nomeCampo = "FinalPagina";
        lista[5].valorCampo = "True";
        lista[6].nomeCampo = "InicioPagina";
        lista[6].valorCampo = "True";

        resultado = p.getResultPut(q, ChaveAcesso, lista);
        resultado = resultado.substring(61);
        resultado = resultado.substring(0, resultado.length() - 1);
        Gson gson = new Gson();
        RetornoUploadFile re = gson.fromJson(resultado, RetornoUploadFile.class);
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
