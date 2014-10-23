package neutron.capture.persistencia;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

//import org.apache.commons.codec.binary.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Jersey REST client generated for REST resource:_1 [rest/search/query=p53]<br>
 * USAGE:
 * <pre>
 *        Proxy client = new Proxy();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Marcos Arruda; Gustavo Baladão; Jonatha Daguerre
 */
public class Proxy {

    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://192.168.0.205/WcfContent/WcfContent.svc/CONTENT";
    public boolean webSericeOnLine;

    public Proxy() {
        webSericeOnLine = false;
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("");
    }

    /**
     * @param q representa a query do Rest
     * @return retorna uma String com resultado
     */
    public String getResultGet(String q) throws UniformInterfaceException {
        ClientResponse clientResponse = webResource.path(java.text.MessageFormat.format("/{0}", new Object[]{q})).get(ClientResponse.class);
        return clientResponse.getEntity(String.class);
    }

    public String getResultPut(String q, String ChaveAcesso, ParametrosProxy listaCampos[]) {

        String http = java.text.MessageFormat.format("{1}/{0}", q, BASE_URI);

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(http);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Host", "content.softwareneutron.com");
            //urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("ChaveAcesso", ChaveAcesso);
            for (ParametrosProxy parametro : listaCampos) {
                if (!parametro.nomeCampo.equals("")) {
                    jsonParam.put(parametro.nomeCampo, parametro.valorCampo);
                }
            }

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonParam.toString());
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                br.close();
                return sb.toString();
            } else {
                return urlConnection.getResponseMessage();
            }
        } catch (MalformedURLException e) {
        } catch (IOException | JSONException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return "";
    }

    public void close() {
        client.destroy();
    }
}
