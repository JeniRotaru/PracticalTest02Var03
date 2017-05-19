package ro.pub.cs.systems.eim.practicaltest02var03.network;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.practicaltest02var03.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02var03.general.Utilities;

/**
 * Created by student on 19.05.2017.
 */

public class CommunicationThread extends Thread {

    /* Referință către obiectul gestionat de server ce reține informațiile
        care au fost realizate interogări anteriore */
    private ServerThread serverThread;
    /* Canalul de citire de la client */
    private Socket socket;

    /* Constructorul clasei */
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket-ul de la client nu e valid!");
            return;
        }
        try {
            /* Se obțin obiectele prin care se vor realiza operațiile
                de citire și scriere pe canalul de comunicație */
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader/PrintWriter sunt null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Se asteapta informatiile/parametrii de la client!");
            /* Se obtin informatiile de pe socketul asociat clientului */
            /* Exemplu : String city = bufferedReader.readLine();
                         if (city == null || city.isEmpty()) {
                            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Eroare!");
                            return;
                         }
               TODO : de modificat conform cerintei
            */
            String word = bufferedReader.readLine();
            if (word == null || word.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Eroare!");
                return;
            }
            /* Se obtin obtin informatiile ce se gasesc in obiectul gestionat de server */
            //TODO : de modificat conform cerintei
            HashMap<String, String> data = serverThread.getData();
            String  definition = null;
            /* Daca informatia necesara exista stocata local in obiectul gestionat de server */
            /* Exemplu :   if (data.containsKey(city)) {
                                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Se obtine informatia din obiectul gestionat de server(local)...");
                                dataModel = data.get(city);
                            }
                TODO : de modificat conform cerintei
            */
            if (data.containsKey(word)) {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Se obtine informatia din obiectul gestionat de server(local)...");
                definition = data.get(word);
            }
                /* Altfel informatia trebuie preluata de la serverul web si parsata */
                /* Exemplu : } else { */
            else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Se obtin inforamtiile de la serverul web...");
                /** ACCESAREA resurselor prin intermediul protocolului HTTP **/
                    /* Pas 1 = instantierea unui obiect de tip HttpClient */
                HttpClient httpClient = new DefaultHttpClient();
                    /* Se va utiliza o cerere de tip POST */
                    /* Pas 2 = instantierea unui obiect de tip HttpPost
                               (va primi ca parametru adresa serverului web de la care se vor descarca datele)  */
                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS+word);
                    /* Pas 3 = definirea unei liste de perechi(<atribut, valoare>) care vor reprezenta datele
                               trimise de client si pe baza carora serverul web va genera continutul */

                    /* Pas 5 = realizarea propriu-zisa a cererii HTTP, prin apelarea metodei execute() */
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    /* Rezultatul/Continutul intors in urma cererii de tip POST */
                String pageSourceCode = httpClient.execute(httpGet, responseHandler);
                if (pageSourceCode == null) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD]Eroare in obtinerea informatiilor de la serverul web!");
                    return;
                }
                Log.d(Constants.TAG, pageSourceCode);
                definition = pageSourceCode;
                data.put(word, pageSourceCode);
        }
        if (definition == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Informatia ceruta este null!");
            return;
        }
            /* Se trimite rezultatul catre client */
            /* Exemplu : String result = null; - TODO : se va prelucra conform cerintei
                         printWriter.println(result);
                         printWriter.flush();
               TODO : de completat cu datele specifice cerintei
            */
        String result = null;
        result = definition.toString();
            printWriter.println(result);
            printWriter.flush();
    } catch (IOException ioException) {
        Log.e(Constants.TAG, "[COMMUNICATION THREAD]A aparut eroarea: " + ioException.getMessage());
        if (Constants.DEBUG) {
            ioException.printStackTrace();
        }
    } finally {
            /* Se inchide socket-ul de comunicatie cu clientul */
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD]A aparut eroarea: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}

}
