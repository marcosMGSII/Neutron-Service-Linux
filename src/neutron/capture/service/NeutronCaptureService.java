/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neutron.capture.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import neutron.capture.persistencia.acessoWebService;

/**
 *
 * @author max
 */
public class NeutronCaptureService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        armazenaDocumentos();
    }

    public static void armazenaDocumentos() {
        acessoWebService ac = new acessoWebService();

        String[] dirTiposDocumentais = getSubPastas(getPastaConcluidos());
        for (String idTipoDocumental : dirTiposDocumentais) {
            String pastaRaiz = getPastaConcluidos();
            String[] dirProcessos = getSubPastas(pastaRaiz + idTipoDocumental);
            pastaRaiz = pastaRaiz + idTipoDocumental + File.separatorChar;
            for (String idProcesso : dirProcessos) {
                String[] dirDocumentos = getSubPastas(pastaRaiz + idProcesso);
                pastaRaiz = pastaRaiz + idProcesso + File.separatorChar;
                for (String docArmazenar : dirDocumentos) {
                    String[] dirSequencia = getSubPastas(pastaRaiz + docArmazenar);
                    pastaRaiz = pastaRaiz + docArmazenar + File.separatorChar;
                    for (String seqDigitalizacao : dirSequencia) {
                        File xmlIndices = new File(pastaRaiz + seqDigitalizacao + File.separatorChar + "indice.xml");
                        if (xmlIndices.exists()) {
                            String[] arqImagens = getArquivos(pastaRaiz + seqDigitalizacao);
                            pastaRaiz = pastaRaiz + seqDigitalizacao + File.separatorChar;
                            for (String arquivoArmazenar : arqImagens) {
                                try {
                                    File arqInfo = new File(pastaRaiz + arquivoArmazenar);
                                    if (!arqInfo.getName().equals("indice.xml")) {
                                        ac.UploadFile("85d520fe-4570-4109-9f20-b835cf92aefd", arqInfo.getAbsolutePath(), idTipoDocumental, org.apache.commons.io.FilenameUtils.removeExtension(arqInfo.getName()));
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(NeutronCaptureService.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }
                    }

                }
            }

        }

    }

    private static String[] getSubPastas(String pastaRaiz) {
        File file = new File(pastaRaiz);
        String[] diretorios = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return diretorios;
    }

    private static String[] getArquivos(String pastaRaiz) {
        File file = new File(pastaRaiz);
        String[] arquivos = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        return arquivos;
    }

    private static String getPastaConcluidos() {
        String retorno;
        String sep = File.separator;
        StringBuilder s = new StringBuilder();
        s.append(System.getProperty("user.home"));
        s.append(sep);
        s.append("Neutron Capture");
        s.append(sep);
        s.append("concluidos");
        s.append(sep);
        File file = new File(s.toString());
        int totalPastas = 0;
        if (file.exists()) {
            String[] directories = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });
            totalPastas = directories.length + 1;
        }
        if (totalPastas > 0) {
            retorno = s.toString();
        } else {
            retorno = "";
        }
        return retorno;
    }

}
