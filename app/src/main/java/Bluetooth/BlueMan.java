package Bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pi314.friendonator.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class BlueMan {
	private static BluetoothHandler mHandler = null;
    private static BlueMan mMan = null;
    private static DeviceValidator mValidator= null;
    private static Activity mAct = null;
    /**
     * Se inicializan variables y se construye un nombre para el dispositivo, el nombre
     * de solo numeros , con una longitud que va entre los 3 y los 5 digitos.
     * Ademas tambien se encarga de:
     *      preder el bluetooth
     *      poner UNLIMITED a la duracion de descubrimiento
     */
    private BlueMan() {
        mHandler = new BluetoothHandler(mAct);
        mValidator = new DeviceValidator();
        this.setNuevoNombre();
        this.setUnlimitedVisibility();
    }

    /**
     * Se inicializan variables y se construye un nombre para el dispositivo, el nombre
     * de solo numeros , con una longitud que va entre los 3 y los 5 digitos.
     * Ademas tambien se encarga de:
     *      preder el bluetooth
     *      poner UNLIMITED a la duracion de descubrimiento
     */
    public static BlueMan getInstance(Activity act){
        mAct = act;
        if(mMan == null){
            mMan = new BlueMan();
        }
        return mMan;
    }

    /**
     * Para poner la visibilidad del bluetooth como ilimitada
     *
     */
    private void setUnlimitedVisibility(){
        mHandler.SetUnlimitedVisibility();
    }

    /**
     * Calcula un nombre numerico Random, lo encripta y se lo asigna como
     * nuevo nombre Bluetooth al telefono
     */
    public void setNuevoNombre(){
        String nuevoNombre = "";
        int cont = 0;
        while (cont <= this.getRandomLenght()){
            cont ++;
            nuevoNombre += this.getRandomNumber();
        }
        nuevoNombre = mValidator.encrypt(nuevoNombre);
        mHandler.setLocalBluetoothName(nuevoNombre);
    }

    private int getRandomLenght(){
        Random ran = new Random();
        int[] opciones = {
          3,4,5
        };
        return opciones[ran.nextInt(3)];
    }

    /**
     * Retorna un numero random del 1 al 9
     * @return
     */
    private int getRandomNumber(){
        Random ran = new Random();
        int[] opciones = {
                1,2,3,4,5,6,7,8,9
        };
        return opciones[ran.nextInt(8)];
    }

    public List<BluetoothDevice> getDeviceList(){
        return mHandler.getListVisibleDevices();
    }

    public void startScan(){
        mHandler.Scan();
    }



}