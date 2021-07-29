package com.example.tplab5_appderecetas.querys;

import android.os.Handler;
import android.os.Message;

public class HiloImagen extends Thread {
    Handler handler;
    Message msg;
    String query;
    int position;

    public HiloImagen(int position, String query, Handler handler){
        this.position = position;
        this.handler = handler;
        this.query = query;
    }

    @Override
    public void run() {
        HttpConexion connHttp = new HttpConexion();
        msg = new Message();
        byte[] res = connHttp.ObtenerRespuesta(null, query,true);
        msg.arg1 = this.position;
        msg.obj = res;
        handler.sendMessage(msg);
    }
}
