package MiCOMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *
 * @author erich
 */
public class Poster {
    
    public String defautlWeb = "http://raulest50.pythonanywhere.com/post_test";
    
    /**
     * recibe comolparametros una lista de los nombres de los parametros y otra lista con los
     * valores de los paramteros, tambien un String con la direccion de la aplicacion web
     * que respondera el post.
     * @param param_name
     * @param param_val
     * @return
     */
    public String HacerPost(ArrayList<String> param_name, ArrayList<String> param_val, String Web){
        String param_str = build_param_str(param_name, param_val);
        String acu="";
        try{
            URL url = new URL(Web);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            BufferedWriter out=
                    new BufferedWriter(new OutputStreamWriter(
                            conn.getOutputStream()));
            out.write(param_str);
            out.flush();
            out.close();
            BufferedReader in =
                    new BufferedReader( new InputStreamReader(
                            conn.getInputStream()));
            String respuesta;
            
            while ( (respuesta = in.readLine()) != null){
                acu+= respuesta;
                //System.out.println(respuesta);
            }
            in.close();
        }
        catch(MalformedURLException e){
        }
        catch(IOException e){
        }
        
        return acu;
    }
    
    /**
     * metodo que convierte 2 listas nombre del parametro y el valor del parametro
     * en el formato String requerido por el metodo para hacer post de esta clase.
     * @param param_name
     * @param param_val
     * @return 
     */
    public String build_param_str(ArrayList<String> param_name, ArrayList<String> param_val){
        String r="";
        for (int k=0; k<param_name.size(); k++){
            r += param_name.get(k)+"="+param_val.get(k)+"&";
        }
        r += param_name.get(param_name.size()-1)+"="+param_val.get(param_name.size()-1);
        return r;
    }
}
