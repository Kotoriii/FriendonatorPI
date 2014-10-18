package Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.List;
import java.util.Random;

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
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mAct.registerReceiver(mReceiver, filter);
        mHandler = new BluetoothHandler(mAct);
        mValidator = new DeviceValidator();
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

    public BluetoothHandler getHandler(){return mHandler;}


    /**
     * Recibe la notificacion cuando el bluetooth cambia de estado
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        //
                        setNuevoNombre();
                        setUnlimitedVisibility();
                        //Se unregister el receiver xq ya no lo vamos a necesitar
                        mAct.unregisterReceiver(mReceiver);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };
}