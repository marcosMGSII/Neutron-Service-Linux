/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neutron.capture.service;

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
        acessoWebService ac = new acessoWebService();
        System.out.println(ac.UploadFile( "85d520fe-4570-4109-9f20-b835cf92aefd", "/home/max/Neutron Capture/digitalizando/img_emulacao/00000002.TIF"));
    }
    
}
