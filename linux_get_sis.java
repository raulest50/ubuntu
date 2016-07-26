
package linuxidenttets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root, esteban
 */
public class LinuxIdentTets {

    public static void main(String[] args) {
        try{
            String dmire = eComand("sudo dmidecode"); // manera de obtener algunos datos de hardware en linux
            String proc = ReadFile(File.separator+"proc"+File.separator+"cpuinfo"); // manera de obtener lagunos datos del procesador en linux
            String ff = TrDMI(dmire)+System.getProperty("java.home")+TrProc(proc)+System.getProperty("user.dir");
            System.out.println(ff);
            
        } catch(IOException | InterruptedException e){
            System.out.println("Exception");
        }
    }
    
    public static String GetThisSysUbun(){
        String ff="";
        try{
            String dmire = eComand("sudo dmidecode"); // manera de obtener algunos datos de hardware en linux
            String proc = ReadFile(File.separator+"proc"+File.separator+"cpuinfo"); // manera de obtener lagunos datos del procesador en linux
            ff = TrDMI(dmire)+System.getProperty("java.home")+TrProc(proc)+System.getProperty("user.dir");
            
        } catch(IOException | InterruptedException e){
            System.out.println("Exception");
        }
        return ff;
    }
    
    
    public static String eComand(String comando) 
            throws IOException, InterruptedException{
        
        Process p;
        
        StringBuilder output = new StringBuilder();
        
        p = Runtime.getRuntime().exec(comando);
        p.waitFor();
        
        BufferedReader reader = 
                new BufferedReader(new InputStreamReader(p.getInputStream()));
        
        String line="";
        
        while((line = reader.readLine())!= null){
            output.append(line+"\n");
        }
        
        return output.toString();
    }
    
    /**
     * ejecuta un comando en la shell de linux o en cmd  de windows
     * pero retorna solo las lineas de respuesta de la shell que conntienen la
     * palabra "contains".
     * @param comando
     * @param contains
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
    public static String eComand(String comando, String contains) 
            throws IOException, InterruptedException{
        
        Process p;
        
        StringBuilder output = new StringBuilder();
        
        p = Runtime.getRuntime().exec(comando);
        p.waitFor();
        
        BufferedReader reader = 
                new BufferedReader(new InputStreamReader(p.getInputStream()));
        
        String line="";
        
        while((line = reader.readLine())!= null){
            if(line.contains(contains)){
                line = line.replace("  ", "");
                line = line.replace(" ", "");
                output.append(line+"\n");
            }
        }
        
        return output.toString();
    }
    
    
    /**
     * permite leer un archivo de texto.
     * @param dir
     * @return 
     */
    public static String ReadFile(String dir){
        BufferedReader br = null;
        String r = "";
        try{
            String sCurrentLine;
            br = new BufferedReader(new FileReader(dir));
            while((sCurrentLine = br.readLine())!= null){
                r+=sCurrentLine+"\n";
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if(br != null) try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(LinuxIdentTets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return r;
    }
    
    /**
     * arregla el string de la respuesta de dmidecode para que sea mas apropiado
     * para la labor de lienciamiento, se quitan redundancias de caracteres 
     * entre otros.
     * @param dmi
     * @return 
     */
    public static String TrDMI(String dmi){
        String r="";
        
        String uuid, serial, manufacturer, sku, vendor;
        uuid = ExtLineCont(dmi, "UUID:").replace("UUID:", "");
        serial = ExtLineCont(dmi, "Serial Number:").replace("Serial Number:", "");
        manufacturer = ExtLineCont(dmi, "Manufacturer:").replace("Manufacturer:", "");
        sku = ExtLineCont(dmi, "SKU Number:").replace("SKU Number:", "");
        vendor = ExtLineCont(dmi, "Vendor:").replace("Vendor:", "");
        
        r=uuid+serial+manufacturer+sku+vendor;
        r=r.replace("	", "");
        r=r.replace(" ", "");
        r=r.replace("-", "");
        return r;
    }
    
    
    /**
     * toma la respuesta de leer el archivo proc y acomoda el string
     * para que sea mas a propiado para la labor de licenciamiento.
     * @param proc
     * @return 
     */
    public static String TrProc(String proc){
        String r = "";
        
        String vendor_id, cpufam, model, mhz, mips;
        
        vendor_id = ExtLineCont(proc, "vendor");
        cpufam = ExtLineCont(proc, "cpu").replace("\t", "");
        model = ExtLineCont(proc, "model");
        mhz = ExtLineCont(proc, "Mhz");
        mips = ExtLineCont(proc, "mips");
        
        r=strip2punt(vendor_id)+strip2punt(cpufam)+strip2punt(model)+strip2punt(mhz)+strip2punt(mips);
        
        return r;
    }
    
    /**
     * funcion auxiliar  usada por TRPROC, lo que hace es partir
     * un string dado usando el caracter :, y descarta los impares, correspon
     * dientes a caracteres redundantes
     * @return 
     */
    public static String strip2punt(String s){
        String r = "";
        String[] aux = s.split(":");
        for(int i=1; i<aux.length; i=i+2){
            r+=aux[i];
        }
        return r;
    }
    
    /**
     * extrae la linea que contiene la palabra x
     * y la retorna sin modificacion
     */
    public static String ExtLineCont(String txt, String x){
        String r="";
        String[] Lines = txt.split("\n");//se hace un array donde cada indice es una linea
        for(int k=0; k<Lines.length; k++){// se barre cada linea buscando la coincidencia
            if(Lines[k].contains(x)){
                r+=Lines[k];// se concatena la linea que contiene x a r
            }
        }
        return r;
    }
    
}
