package ro.pub.cs.systems.eim.practicaltest02var03.network;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02var03.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02var03.general.Utilities;

/**
 * Created by student on 19.05.2017.
 */

public class ClientThread extends Thread{

    /* Obiectele corespunzatoare elementelor grafice */
    private int clientPort;
    //private String clientAddress;
    private TextView showResponse;
    //TODO : de completat cu alte date specifice cerintei
    private String word;
    /* Socket-ul folosit pentru comunicatie */
    private Socket socket;

    public ClientThread(int clientPort, TextView showResponse, String word) {
        this.clientPort = clientPort;
        this.showResponse = showResponse;
        //TODO : de completat cu alte date specifice cerintei
        this.word = word;
    }

    @Override
    public void run() {
        try {
            /* Deschiderea unui canal de comunicație folosind parametrii de conexiune la server (adresa Internet, port) */
            socket = new Socket("127.0.0.1", clientPort);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Socket-ul nu a putut fi creat!");
                return;
            }
            /* Obținerea unor obiecte prin care se vor realiza operațiile de citire și scriere pe canalul de comunicație */
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader/PrintWriter nu au putut fi instantiate!");
                return;
            }
            /* Trimiterea informatiilor la server */
            /* TODO : de completat cu datele specifice cerintei
            Exemplu : printWriter.println(city);
                      printWriter.flush(); */
            printWriter.println(word);
            printWriter.flush();
            /* Primirea/Citirea datelor de la server */
            String infoServer;
            while ( (infoServer = bufferedReader.readLine()) != null ) {
                final String infoServerTextView = infoServer;
                /* Vizualizarea datelor intr-un obiect de tip TextView */
                showResponse.post(new Runnable() {
                    @Override
                    public void run() {
                        showResponse.append(infoServerTextView+"\n");
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] S-a produs exceptia: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            /* Inchiderea canalului de comunicatie */
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] S-a produs exceptia: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
