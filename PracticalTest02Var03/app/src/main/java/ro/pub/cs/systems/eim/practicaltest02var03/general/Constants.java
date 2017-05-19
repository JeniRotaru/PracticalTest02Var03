package ro.pub.cs.systems.eim.practicaltest02var03.general;

/**
 * Created by student on 19.05.2017.
 */

public interface Constants {

    //Folosit pentru DEBUG
    final public static String TAG = "[PracticalTest02]";

    final public static boolean DEBUG = true;

    //Adresa serviciului web accesat de server pentru obtinerea informatiilor necesare
    final public static String WEB_SERVICE_ADDRESS = "http://services.aonaware.com/DictService/DictService.asmx/Define?word=";

    //TODO : de completat cu datele specifice cerintei
    /* final public static String TEMPERATURE = "temperature";
    final public static String WIND_SPEED = "wind_speed";
    final public static String CONDITION = "condition";
    final public static String PRESSURE = "pressure";
    final public static String HUMIDITY = "humidity";
    final public static String ALL = "all"; */

    final public static String EMPTY_STRING = "";

    final public static String QUERY_ATTRIBUTE = "query";

    final public static String SCRIPT_TAG = "script";
    final public static String SEARCH_KEY = "wui.api_data =\n";

    final public static String CURRENT_OBSERVATION = "current_observation";


}
