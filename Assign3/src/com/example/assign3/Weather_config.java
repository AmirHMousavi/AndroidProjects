package com.example.assign3;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign3.weatherWidget.Weather_widgetProvider;

public class Weather_config extends Activity implements OnItemClickListener {

	static final String TAG = "Weather_config";
	private static final String PREFS_NAME = "com.example.assign3.weatherWidget.WeatherProvider";
	int WidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	ObjectItem[] citiesWithLink = new ObjectItem[129];
	private ArrayAdapter<ObjectItem> listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_config);
		setResult(RESULT_CANCELED);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			WidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		// if no widget then ignore!
		if (WidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		// Generate a list of cities in Sweden
		initList();

		ListView citiesList = (ListView) findViewById(R.id.weatherCityList);
		listAdapter = new myListAdabter();
		citiesList.setAdapter(listAdapter);
		citiesList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		final Context context = view.getContext();
		TextView textViewItem = ((TextView) view
				.findViewById(R.id.simple_textview));
		String city = textViewItem.getText().toString();
		String cityLink = textViewItem.getTag().toString();
		String[] pref_city_link = new String[] { city, cityLink };
		savePrefs(context, WidgetId, pref_city_link);

		Toast.makeText(context, "city: " + city + ", Link: " + cityLink,
				Toast.LENGTH_SHORT).show();
		letsGoForTheWidget();

	}

	private void letsGoForTheWidget() {
		// Push widget update to surface with newly set widgetID
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		
		// ******
		RemoteViews views = new RemoteViews(this.getPackageName(),
				R.layout.weather_widget);
		
		// *******
		Intent intent = new Intent(this, Weather_widgetProvider.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int ids[] = AppWidgetManager.getInstance(getApplication())
				.getAppWidgetIds(
						new ComponentName(getApplication(),
								Weather_widgetProvider.class));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//		sendBroadcast(intent);
		// ********
		appWidgetManager.updateAppWidget(WidgetId, views);
		
		// *****
		// Make sure we pass back the original appWidgetId
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}

	// **********Preferences part*********************************************

	// Write the prefix to the SharedPreferences object for this widget
	static void savePrefs(Context context, int appWidgetId,
			String[] pref_city_link) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putString("city" + appWidgetId, pref_city_link[0]);
		prefs.putString("link" + appWidgetId, pref_city_link[1]);
		prefs.commit();
	}

	//
	public static String[] loadPrefs(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		String[] prefix = new String[2];
		prefix[0] = prefs.getString("city" + appWidgetId, null);
		prefix[1] = prefs.getString("link" + appWidgetId, null);
		return prefix;
	}

	static void deletePrefs(Context context, int appWidgetId) {
	}



	// *****Object Item ******************************************************
	private class ObjectItem {
		public String cityName;
		public String cityLink;

		// constructor
		public ObjectItem(String cityname, String citylink) {
			this.cityName = cityname;
			this.cityLink = citylink;
		}
	}

	// **********my Adapter****************************************************

	private class myListAdabter extends ArrayAdapter<ObjectItem> {

		public myListAdabter() {
			super(Weather_config.this, R.layout.simple_textview_item,
					citiesWithLink);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.simple_textview_item, parent, false);
			}
			ObjectItem currentObject = citiesWithLink[position];
			TextView tv = (TextView) itemView
					.findViewById(R.id.simple_textview);
			tv.setText(currentObject.cityName);
			tv.setTag(currentObject.cityLink);

			return itemView;
		}
	}

	private void initList() {
		citiesWithLink[0] = new ObjectItem("Alings�s",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Alings�s/forecast.xml");
		citiesWithLink[1] = new ObjectItem("Alvesta",
				"http://www.yr.no/place/Sweden/Kronoberg/Alvesta/forecast.xml");
		citiesWithLink[2] = new ObjectItem("Arboga",
				"http://www.yr.no/place/Sweden/V�stmanland/Arboga/forecast.xml");
		citiesWithLink[3] = new ObjectItem("Arvika",
				"http://www.yr.no/place/Sweden/V�rmland/Arvika/forecast.xml");
		citiesWithLink[4] = new ObjectItem("Avesta",
				"http://www.yr.no/place/Sweden/Dalarna/Avesta/forecast.xml");
		citiesWithLink[5] = new ObjectItem("Boden",
				"http://www.yr.no/place/Sweden/Norrbotten/Boden/forecast.xml");
		citiesWithLink[6] = new ObjectItem("Bolln�s",
				"http://www.yr.no/place/Sweden/G�vleborg/Bolln�s/forecast.xml");
		citiesWithLink[7] = new ObjectItem("Borl�nge",
				"http://www.yr.no/place/Sweden/Dalarna/Borl�nge/forecast.xml");
		citiesWithLink[8] = new ObjectItem("Bor�s",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Bor�s/forecast.xml");
		citiesWithLink[9] = new ObjectItem("Brunflo",
				"http://www.yr.no/place/Sweden/J�mtland/Brunflo~2718991/forecast.xml");
		citiesWithLink[10] = new ObjectItem("B�lsta",
				"http://www.yr.no/place/Sweden/Uppsala/B�lsta/forecast.xml");
		citiesWithLink[11] = new ObjectItem("Degerfors",
				"http://www.yr.no/place/Sweden/�rebro/Degerfors/forecast.xml");
		citiesWithLink[12] = new ObjectItem("Enk�ping",
				"http://www.yr.no/place/Sweden/Uppsala/Enk�ping/forecast.xml");
		citiesWithLink[13] = new ObjectItem("Eskilstuna",
				"http://www.yr.no/place/Sweden/S�dermanland/Eskilstuna/forecast.xml");
		citiesWithLink[14] = new ObjectItem("Esl�v",
				"http://www.yr.no/place/Sweden/Scania/Esl�v/forecast.xml");
		citiesWithLink[15] = new ObjectItem("Fagersta",
				"http://www.yr.no/place/Sweden/V�stmanland/Fagersta/forecast.xml");
		citiesWithLink[16] = new ObjectItem("Falkenberg",
				"http://www.yr.no/place/Sweden/Halland/Falkenberg/forecast.xml");
		citiesWithLink[17] = new ObjectItem("Falk�ping",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Falk�ping/forecast.xml");
		citiesWithLink[18] = new ObjectItem("Falun",
				"http://www.yr.no/place/Sweden/Dalarna/Falun/forecast.xml");
		citiesWithLink[19] = new ObjectItem("Finsp�ng",
				"http://www.yr.no/place/Sweden/�sterg�tland/Finsp�ng/forecast.xml");
		citiesWithLink[20] = new ObjectItem("F�r�sund",
				"http://www.yr.no/place/Sweden/Gotland/F�r�sund/forecast.xml");
		citiesWithLink[21] = new ObjectItem("Gothenburg",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Gothenburg/forecast.xml");
		citiesWithLink[22] = new ObjectItem("G�llivare",
				"http://www.yr.no/place/Sweden/Norrbotten/G�llivare/forecast.xml");
		citiesWithLink[23] = new ObjectItem("G�vle",
				"http://www.yr.no/place/Sweden/G�vleborg/G�vle/forecast.xml");
		citiesWithLink[24] = new ObjectItem("Halmstad",
				"http://www.yr.no/place/Sweden/Halland/Halmstad/forecast.xml");
		citiesWithLink[25] = new ObjectItem("Helsingborg",
				"http://www.yr.no/place/Sweden/Scania/Helsingborg/forecast.xml");
		citiesWithLink[26] = new ObjectItem("Hemse",
				"http://www.yr.no/place/Sweden/Gotland/Hemse/forecast.xml");
		citiesWithLink[27] = new ObjectItem("Holmsund",
				"http://www.yr.no/place/Sweden/V�sterbotten/Holmsund~605676/forecast.xml");
		citiesWithLink[28] = new ObjectItem("Hudiksvall",
				"http://www.yr.no/place/Sweden/G�vleborg/Hudiksvall/forecast.xml");
		citiesWithLink[29] = new ObjectItem("H�rn�sand",
				"http://www.yr.no/place/Sweden/V�sternorrland/H�rn�sand/forecast.xml");
		citiesWithLink[30] = new ObjectItem("H�ssleholm",
				"http://www.yr.no/place/Sweden/Scania/H�ssleholm/forecast.xml");
		citiesWithLink[31] = new ObjectItem("H�gan�s",
				"http://www.yr.no/place/Sweden/Scania/H�gan�s/forecast.xml");
		citiesWithLink[32] = new ObjectItem("J�nk�ping",
				"http://www.yr.no/place/Sweden/J�nk�ping/J�nk�ping/forecast.xml");
		citiesWithLink[33] = new ObjectItem("Kalmar",
				"http://www.yr.no/place/Sweden/Kalmar/Kalmar/forecast.xml");
		citiesWithLink[34] = new ObjectItem("Karlshamn",
				"http://www.yr.no/place/Sweden/Blekinge/Karlshamn/forecast.xml");
		citiesWithLink[35] = new ObjectItem("Karlskoga",
				"http://www.yr.no/place/Sweden/�rebro/Karlskoga/forecast.xml");
		citiesWithLink[36] = new ObjectItem("Karlskrona",
				"http://www.yr.no/place/Sweden/Blekinge/Karlskrona/forecast.xml");
		citiesWithLink[37] = new ObjectItem("Karlstad",
				"http://www.yr.no/place/Sweden/V�rmland/Karlstad/forecast.xml");
		citiesWithLink[38] = new ObjectItem("Katrineholm",
				"http://www.yr.no/place/Sweden/S�dermanland/Katrineholm/forecast.xml");
		citiesWithLink[39] = new ObjectItem("Kinna",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Kinna/forecast.xml");
		citiesWithLink[40] = new ObjectItem("Kiruna",
				"http://www.yr.no/place/Sweden/Norrbotten/Kiruna/forecast.xml");
		citiesWithLink[41] = new ObjectItem("Klintehamn",
				"http://www.yr.no/place/Sweden/Gotland/Klintehamn/forecast.xml");
		citiesWithLink[42] = new ObjectItem("Kristianstad",
				"http://www.yr.no/place/Sweden/Scania/Kristianstad/forecast.xml");
		citiesWithLink[43] = new ObjectItem("Kristinehamn",
				"http://www.yr.no/place/Sweden/V�rmland/Kristinehamn/forecast.xml");
		citiesWithLink[44] = new ObjectItem("Krokom",
				"http://www.yr.no/place/Sweden/J�mtland/Krokom/forecast.xml");
		citiesWithLink[45] = new ObjectItem("Kumla",
				"http://www.yr.no/place/Sweden/�rebro/Kumla/forecast.xml");
		citiesWithLink[46] = new ObjectItem("Kungsbacka",
				"http://www.yr.no/place/Sweden/Halland/Kungsbacka/forecast.xml");
		citiesWithLink[47] = new ObjectItem("Kung�lv",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Kung�lv/forecast.xml");
		citiesWithLink[48] = new ObjectItem("K�vlinge",
				"http://www.yr.no/place/Sweden/Scania/K�vlinge/forecast.xml");
		citiesWithLink[49] = new ObjectItem("K�ping",
				"http://www.yr.no/place/Sweden/V�stmanland/K�ping/forecast.xml");
		citiesWithLink[50] = new ObjectItem("Laholm",
				"http://www.yr.no/place/Sweden/Halland/Laholm/forecast.xml");
		citiesWithLink[51] = new ObjectItem("Landskrona",
				"http://www.yr.no/place/Sweden/Scania/Landskrona/forecast.xml");
		citiesWithLink[52] = new ObjectItem("Lerum",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Lerum/forecast.xml");
		citiesWithLink[53] = new ObjectItem("Lidk�ping",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Lidk�ping/forecast.xml");
		citiesWithLink[54] = new ObjectItem("Lindesberg",
				"http://www.yr.no/place/Sweden/�rebro/Lindesberg/forecast.xml");
		citiesWithLink[55] = new ObjectItem("Lindshammar",
				"http://www.yr.no/place/Sweden/Kronoberg/Lindshammar/forecast.xml");
		citiesWithLink[56] = new ObjectItem("Link�ping",
				"http://www.yr.no/place/Sweden/�sterg�tland/Link�ping/forecast.xml");
		citiesWithLink[57] = new ObjectItem("Ljungby",
				"http://www.yr.no/place/Sweden/Kronoberg/Ljungby/forecast.xml");
		citiesWithLink[58] = new ObjectItem("Ludvika",
				"http://www.yr.no/place/Sweden/Dalarna/Ludvika/forecast.xml");
		citiesWithLink[59] = new ObjectItem("Lule�",
				"http://www.yr.no/place/Sweden/Norrbotten/Lule�/forecast.xml");
		citiesWithLink[60] = new ObjectItem("Lund",
				"http://www.yr.no/place/Sweden/Scania/Lund/forecast.xml");
		citiesWithLink[61] = new ObjectItem("Lycksele",
				"http://www.yr.no/place/Sweden/V�sterbotten/Lycksele/forecast.xml");
		citiesWithLink[62] = new ObjectItem("Majorna",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Majorna/forecast.xml");
		citiesWithLink[63] = new ObjectItem("Malm�",
				"http://www.yr.no/place/Sweden/Scania/Malm�/forecast.xml");
		citiesWithLink[64] = new ObjectItem("Mariestad",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Mariestad/forecast.xml");
		citiesWithLink[65] = new ObjectItem("Markaryd",
				"http://www.yr.no/place/Sweden/Kronoberg/Markaryd/forecast.xml");
		citiesWithLink[66] = new ObjectItem("Mj�lby",
				"http://www.yr.no/place/Sweden/�sterg�tland/Mj�lby/forecast.xml");
		citiesWithLink[67] = new ObjectItem("Mora",
				"http://www.yr.no/place/Sweden/Dalarna/Mora/forecast.xml");
		citiesWithLink[68] = new ObjectItem("Motala",
				"http://www.yr.no/place/Sweden/�sterg�tland/Motala/forecast.xml");
		citiesWithLink[69] = new ObjectItem("M�lndal",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/M�lndal/forecast.xml");
		citiesWithLink[70] = new ObjectItem("M�lnlycke",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/M�lnlycke/forecast.xml");
		citiesWithLink[71] = new ObjectItem("Norrk�ping",
				"http://www.yr.no/place/Sweden/�sterg�tland/Norrk�ping/forecast.xml");
		citiesWithLink[72] = new ObjectItem("Nybro",
				"http://www.yr.no/place/Sweden/Kalmar/Nybro/forecast.xml");
		citiesWithLink[73] = new ObjectItem("Nyk�ping",
				"http://www.yr.no/place/Sweden/S�dermanland/Nyk�ping/forecast.xml");
		citiesWithLink[74] = new ObjectItem("N�ssj�",
				"http://www.yr.no/place/Sweden/J�nk�ping/N�ssj�/forecast.xml");
		citiesWithLink[75] = new ObjectItem("Olofstr�m",
				"http://www.yr.no/place/Sweden/Blekinge/Olofstr�m/forecast.xml");
		citiesWithLink[76] = new ObjectItem("Oskarshamn",
				"http://www.yr.no/place/Sweden/Kalmar/Oskarshamn/forecast.xml");
		citiesWithLink[77] = new ObjectItem("Oxel�sund",
				"http://www.yr.no/place/Sweden/S�dermanland/Oxel�sund/forecast.xml");
		citiesWithLink[78] = new ObjectItem("Partille",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Partille/forecast.xml");
		citiesWithLink[79] = new ObjectItem("Pite�",
				"http://www.yr.no/place/Sweden/Norrbotten/Pite�/forecast.xml");
		citiesWithLink[80] = new ObjectItem("Ronneby",
				"http://www.yr.no/place/Sweden/Blekinge/Ronneby/forecast.xml");
		citiesWithLink[81] = new ObjectItem("R�rsby",
				"http://www.yr.no/place/Sweden/Uppsala/R�rsby/forecast.xml");
		citiesWithLink[82] = new ObjectItem("Sala",
				"http://www.yr.no/place/Sweden/V�stmanland/Sala/forecast.xml");
		citiesWithLink[83] = new ObjectItem("Sandviken",
				"http://www.yr.no/place/Sweden/G�vleborg/Sandviken/forecast.xml");
		citiesWithLink[84] = new ObjectItem("Skellefte�",
				"http://www.yr.no/place/Sweden/V�sterbotten/Skellefte�/forecast.xml");
		citiesWithLink[85] = new ObjectItem("Skoghall",
				"http://www.yr.no/place/Sweden/V�rmland/Skoghall/forecast.xml");
		citiesWithLink[86] = new ObjectItem("Sk�vde",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Sk�vde/forecast.xml");
		citiesWithLink[87] = new ObjectItem("Slite",
				"http://www.yr.no/place/Sweden/Gotland/Slite/forecast.xml");
		citiesWithLink[88] = new ObjectItem("Sollefte�",
				"http://www.yr.no/place/Sweden/V�sternorrland/Sollefte�/forecast.xml");
		citiesWithLink[89] = new ObjectItem("Sollentuna",
				"http://www.yr.no/place/Sweden/Stockholm/Sollentuna/forecast.xml");
		citiesWithLink[90] = new ObjectItem("Staffanstorp",
				"http://www.yr.no/place/Sweden/Scania/Staffanstorp/forecast.xml");
		citiesWithLink[91] = new ObjectItem("Stavasj�",
				"http://www.yr.no/place/Sweden/V�sterbotten/Stavasj�/forecast.xml");
		citiesWithLink[92] = new ObjectItem("Stockholm",
				"http://www.yr.no/place/Sweden/Stockholm/Stockholm/forecast.xml");
		citiesWithLink[93] = new ObjectItem("Str�ngn�s",
				"http://www.yr.no/place/Sweden/S�dermanland/Str�ngn�s/forecast.xml");
		citiesWithLink[94] = new ObjectItem("Str�msund",
				"http://www.yr.no/place/Sweden/J�mtland/Str�msund/forecast.xml");
		citiesWithLink[95] = new ObjectItem("Sundsvall",
				"http://www.yr.no/place/Sweden/V�sternorrland/Sundsvall/forecast.xml");
		citiesWithLink[96] = new ObjectItem("Sveg",
				"http://www.yr.no/place/Sweden/J�mtland/Sveg/forecast.xml");
		citiesWithLink[97] = new ObjectItem("S�ffle",
				"http://www.yr.no/place/Sweden/V�rmland/S�ffle/forecast.xml");
		citiesWithLink[98] = new ObjectItem("S�derhamn",
				"http://www.yr.no/place/Sweden/G�vleborg/S�derhamn/forecast.xml");
		citiesWithLink[99] = new ObjectItem("S�dert�lje",
				"http://www.yr.no/place/Sweden/Stockholm/S�dert�lje/forecast.xml");
		citiesWithLink[100] = new ObjectItem("S�lvesborg",
				"http://www.yr.no/place/Sweden/Blekinge/S�lvesborg/forecast.xml");
		citiesWithLink[101] = new ObjectItem("Timr�",
				"http://www.yr.no/place/Sweden/V�sternorrland/Timr�/forecast.xml");
		citiesWithLink[102] = new ObjectItem("Tran�s",
				"http://www.yr.no/place/Sweden/J�nk�ping/Tran�s/forecast.xml");
		citiesWithLink[103] = new ObjectItem("Trelleborg",
				"http://www.yr.no/place/Sweden/Scania/Trelleborg/forecast.xml");
		citiesWithLink[104] = new ObjectItem("Trollh�ttan",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Trollh�ttan/forecast.xml");
		citiesWithLink[105] = new ObjectItem("T�by",
				"http://www.yr.no/place/Sweden/Stockholm/T�by/forecast.xml");
		citiesWithLink[106] = new ObjectItem("Uddevalla",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/Uddevalla/forecast.xml");
		citiesWithLink[107] = new ObjectItem("Ume�",
				"http://www.yr.no/place/Sweden/V�sterbotten/Ume�/forecast.xml");
		citiesWithLink[108] = new ObjectItem("Upplands V�sby",
				"http://www.yr.no/place/Sweden/Stockholm/Upplands_V�sby/forecast.xml");
		citiesWithLink[109] = new ObjectItem("Uppsala",
				"http://www.yr.no/place/Sweden/Uppsala/Uppsala/forecast.xml");
		citiesWithLink[110] = new ObjectItem("Valje",
				"http://www.yr.no/place/Sweden/Scania/Valje/forecast.xml");
		citiesWithLink[111] = new ObjectItem("Varberg",
				"http://www.yr.no/place/Sweden/Halland/Varberg/forecast.xml");
		citiesWithLink[112] = new ObjectItem("Vetlanda",
				"http://www.yr.no/place/Sweden/J�nk�ping/Vetlanda/forecast.xml");
		citiesWithLink[113] = new ObjectItem("Vimmerby",
				"http://www.yr.no/place/Sweden/Kalmar/Vimmerby/forecast.xml");
		citiesWithLink[114] = new ObjectItem("Visby",
				"http://www.yr.no/place/Sweden/Gotland/Visby/forecast.xml");
		citiesWithLink[115] = new ObjectItem("V�nersborg",
				"http://www.yr.no/place/Sweden/V�stra_G�taland/V�nersborg/forecast.xml");
		citiesWithLink[116] = new ObjectItem("V�nn�s",
				"http://www.yr.no/place/Sweden/V�sterbotten/V�nn�s/forecast.xml");
		citiesWithLink[117] = new ObjectItem("V�rnamo",
				"http://www.yr.no/place/Sweden/J�nk�ping/V�rnamo/forecast.xml");
		citiesWithLink[118] = new ObjectItem("V�stervik",
				"http://www.yr.no/place/Sweden/Kalmar/V�stervik/forecast.xml");
		citiesWithLink[119] = new ObjectItem("V�ster�s",
				"http://www.yr.no/place/Sweden/V�stmanland/V�ster�s/forecast.xml");
		citiesWithLink[120] = new ObjectItem("V�xj�",
				"http://www.yr.no/place/Sweden/Kronoberg/V�xj�/forecast.xml");
		citiesWithLink[121] = new ObjectItem("Ystad",
				"http://www.yr.no/place/Sweden/Scania/Ystad/forecast.xml");
		citiesWithLink[122] = new ObjectItem("�lmhult",
				"http://www.yr.no/place/Sweden/Kronoberg/�lmhult/forecast.xml");
		citiesWithLink[123] = new ObjectItem("�lvkarleby",
				"http://www.yr.no/place/Sweden/Uppsala/�lvkarleby/forecast.xml");
		citiesWithLink[124] = new ObjectItem("�ngelholm",
				"http://www.yr.no/place/Sweden/Scania/�ngelholm/forecast.xml");
		citiesWithLink[125] = new ObjectItem("�rebro",
				"http://www.yr.no/place/Sweden/�rebro/�rebro/forecast.xml");
		citiesWithLink[126] = new ObjectItem("�rnsk�ldsvik",
				"http://www.yr.no/place/Sweden/V�sternorrland/�rnsk�ldsvik/forecast.xml");
		citiesWithLink[127] = new ObjectItem("�stersund",
				"http://www.yr.no/place/Sweden/J�mtland/�stersund/forecast.xml");
		citiesWithLink[128] = new ObjectItem("�sthammar",
				"http://www.yr.no/place/Sweden/Uppsala/�sthammar/forecast.xml");

	}
}
