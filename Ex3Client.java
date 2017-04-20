import java.net.*;
import java.io.*;
import java.util.*;

public class Ex3Client{

    public static void main(String[] args) throws Exception{

        try(Socket socket = new Socket("codebank.xyz",38102)){
            System.out.println("Connected to Server.");
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);

            int fh,sh,numVal;
            fh = is.read();
            sh = is.read();
            numVal = 16*fh+sh;
            byte[] bArray = new byte[numVal];

            System.out.printf("Reading %d byte(s).",numVal);
            int data;
            for(int i = 0; i < numVal;i++){
                fh = is.read();
                sh = is.read();
                data = 16*fh+sh;
                bArray[i] = (byte) data;
                if(i%10==0){
                    System.out.println("\t\t");
                }
                System.out.print((Integer.toHexString(fh)).toUpperCase() + (Integer.toHexString(sh)).toUpperCase());
            }
            System.out.println("\ndone");
            


        } catch(Exception e){

        }

    }

}