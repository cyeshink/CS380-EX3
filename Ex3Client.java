import java.io.*;
import java.util.*;
import java.net.*;

public class Ex3Client {

    public static void main(String[] args) throws Exception {

        try (Socket socket = new Socket("codebank.xyz", 38103)) {
            System.out.println("Connected to Server.");
            InputStream is = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            int numVal = is.read();
            System.out.println("Reading " + numVal + " bytes.");
            byte[] bArray = new byte[numVal];
            System.out.print("Data Received:");

            String byteString;
            for (int i = 0; i < numVal; i++) {
                byteString = "";
                if(i%10==0){
                    System.out.print("\n    ");
                }
                int data = is.read();
                byteString += String.format("%02X",data);                
                System.out.print(byteString);
                bArray[i] = (byte) data;
            }

            System.out.println();
            short cksum = checksum(bArray);
            System.out.println("Checksum Calculated: 0x" + String.format("%04X", cksum & 0xFFFF));

            byte[] toServer = new byte[2];
            toServer[0] = (byte) ((cksum & 0xFF00) >>> 8);
            toServer[1] = (byte) ((cksum & 0x00FF));
            out.write(toServer);

            int response = is.read();
            if (response == 1){
                System.out.println("Response Good.");
            }
            else{
                System.out.println("Response Bad.");
            }
            System.out.println("Disconnected from Server.");

        } catch (Exception e) {
        }
    }


    public static short checksum(byte[] b) {
        int l = b.length;
        int i = 0;
        long total = 0;
        long sum = 0;

        while (l > 1) {
            sum = sum + ((b[i] << 8 & 0xFF00) | ((b[i + 1]) & 0x00FF));
            i = i + 2;
            l = l - 2;
            if ((sum & 0xFFFF0000) > 0) {
                sum = sum & 0xFFFF;
                sum++;
            }
        }
        if (l > 0) {
            sum += b[i] << 8 & 0xFF00;
            if ((sum & 0xFFFF0000) > 0) {
                sum = sum & 0xFFFF;
                sum++;
            }
        }
        total = (~((sum & 0xFFFF) + (sum >> 16))) & 0xFFFF;
        return (short) total;
    }
}