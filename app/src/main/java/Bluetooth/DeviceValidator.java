package Bluetooth;

import android.bluetooth.BluetoothDevice;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by andrea on 01/10/14.
 */
public class DeviceValidator {

    // Se transforma el valor del nombre en su valor de numeros.
    /**
     * Para la encriptacion se multiplica el numero por la constante y luego se devuelve a su forma de
     * texto
     *
     * Para la desencripcion se DIVIDE el valor del numero por el numero
     * */

    public DeviceValidator() {

    }
    private final static String ALGORITM = "Blowfish";
    private final static String KEY = "26d6a3a4abc59c1f20a721ad3f90aefd83d93c763c1af218a9b8c18c";


    /**
     * Retorna si un dispositivo esta usando la aplicacion o no.
     * Para definir si un dispositivo esta usando la aplicacion la desencripcion del nombre
     * de este tiene que ser un integer
     * @param device
     * @return
     */
    public boolean isValidDevice(BluetoothDevice device){
        String realName = decrypt(device.getName());
        if(this.isInteger(realName)){
            return true;
        }
        else{
            return false;
        }
    }

    public String encrypt(String dato){
        String st = "";
        try {

            st = bytesToHex(encrypt(dato.getBytes()));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return st;
    }

    public String decrypt(String dato){
        String pop = "";
        try {
            pop = decrypt(this.hexStringToByteArray(dato));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return pop;
    }
    private byte[] encrypt(byte[] data) throws GeneralSecurityException {

        SecretKey secret_key = new SecretKeySpec(KEY.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        return cipher.doFinal(data);
    }

    private String decrypt(byte[] encryptedText) throws GeneralSecurityException {

        SecretKey secret_key = new SecretKeySpec(KEY.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.DECRYPT_MODE, secret_key);

        byte[] decrypted = cipher.doFinal(encryptedText);

        return new String(decrypted);
    }

    /**
     * devuelve si un string contiene SOLAMENTE ints
     * @param integerString
     * @return
     */
    private boolean isInteger(String integerString){

        try{
            Integer.parseInt(integerString);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static String bytesToHex(byte[] data) {

        if (data == null)
            return null;

        String str = "";

        for (int i = 0; i < data.length; i++) {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
        }

        return str;

    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}

