/**
 * VaxjoWeather.java
 * Created: May 9, 2010
 * Jonas Lundberg, LnU
 */

package com.example.assign1;

import java.io.IOException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This is a first prototype for a weather app. It is currently only downloading
 * weather data for Växjö.
 * 
 * This activity downloads weather data and constructs a WeatherReport, a data
 * structure containing weather data for a number of periods ahead.
 * 
 * The WeatherHandler is a SAX parser for the weather reports (forecast.xml)
 * produced by www.yr.no. The handler constructs a WeatherReport containing meta
 * data for a given location (e.g. city, country, last updated, next update) and
 * a sequence of WeatherForecasts. Each WeatherForecast represents a forecast
 * (weather, rain, wind, etc) for a given time period.
 * 
 * The next task is to construct a list based GUI where each row displays the
 * weather data for a single period.
 * 
 * 
 * @author jlnmsi
 * 
 */

public class VaxjoWeather extends Activity {
	private WeatherReport report = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {
			URL url = new URL(
					"http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml");
			new WeatherRetriever().execute(url);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	private void populateListView() {
		if (this.report != null) {
			ArrayAdapter<WeatherForecast> adabter = new myListAdabter();
			ListView list = (ListView) findViewById(R.id.forcast_list);
			list.setAdapter(adabter);

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Weather report has not been loaded.\ncheck your internet connection")
					.setCancelable(true)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	private class myListAdabter extends ArrayAdapter<WeatherForecast> {

		public myListAdabter() {
			super(VaxjoWeather.this, R.layout.item_view, report
					.getForcastList());
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.item_view,
						parent, false);
			}
			// get forcasts contained in the report one by one
			// and fill the view for each one
			WeatherForecast forecast = report.getForcastList().get(position);

			// ****set the image
			ImageView _imageView = (ImageView) itemView
					.findViewById(R.id.icon_image);
			int weatherCode = forecast.getWeatherCode();

			if (weatherCode == 1)
				_imageView.setImageResource(R.drawable.wi1);
			else if (weatherCode == 2)
				_imageView.setImageResource(R.drawable.wi2);
			else if (weatherCode == 3)
				_imageView.setImageResource(R.drawable.wi3);
			else if (weatherCode == 4)
				_imageView.setImageResource(R.drawable.wi4);
			else if (weatherCode == 5)
				_imageView.setImageResource(R.drawable.wi5);
			else if (weatherCode == 6)
				_imageView.setImageResource(R.drawable.wi6);
			else if (weatherCode == 7)
				_imageView.setImageResource(R.drawable.wi7);
			else if (weatherCode == 8)
				_imageView.setImageResource(R.drawable.wi8);
			else if (weatherCode == 9)
				_imageView.setImageResource(R.drawable.wi9);
			else if (weatherCode == 10)
				_imageView.setImageResource(R.drawable.wi10);
			else if (weatherCode == 11)
				_imageView.setImageResource(R.drawable.wi11);
			else if (weatherCode == 12)
				_imageView.setImageResource(R.drawable.wi12);
			else if (weatherCode == 13)
				_imageView.setImageResource(R.drawable.wi13);
			else if (weatherCode == 14)
				_imageView.setImageResource(R.drawable.wi14);
			else if (weatherCode == 15)
				_imageView.setImageResource(R.drawable.wi15);
			// ****set the temperature
			TextView _temperature = (TextView) itemView
					.findViewById(R.id.iem_temp);
			_temperature.setText(""+forecast.getTemp()+"�");
			//****set the period 
			TextView _timePeriod=(TextView) itemView.findViewById(R.id.item_period);
			String period=forecast.getStartHHMM()+" to "+forecast.getEndHHMM();
			_timePeriod.setText(period);
			//****set the Date
			TextView _date=(TextView) itemView.findViewById(R.id.item_date);
			_date.setText(forecast.getStartYYMMDD());
			//*****set the wind speed
			TextView _windSpeed=(TextView) itemView.findViewById(R.id.item_windSpeed);
			_windSpeed.setText(""+forecast.getWindSpeed()+"m/s");
			//****set the wind direction
			TextView _windDirection=(TextView) itemView.findViewById(R.id.item_windDirection);
			_windDirection.setText(forecast.getWindDirection());
			//****set the rain volume
			TextView _rainVolume=(TextView) itemView.findViewById(R.id.item_rain);
			_rainVolume.setText(""+forecast.getRain()+" mm");
			return itemView;

		}
	}

	private void PrintReportToConsole() {
		if (this.report != null) {
			/* Print location meta data */
			System.out.println(report);
			

			/* Print forecasts */
			int count = 0;
			for (WeatherForecast forecast : report) {
				count++;
				System.out.println("Forecast " + count);
				
				System.out.println(forecast.toString());
				
			}
		} else {
			System.out.println("Weather report has not been loaded.");
		}
	}

	private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
		protected WeatherReport doInBackground(URL... urls) {
			try {
				return WeatherHandler.getWeatherReport(urls[0]);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		protected void onProgressUpdate(Void... progress) {

		}

		protected void onPostExecute(WeatherReport result) {
			report = result;
	//		PrintReportToConsole();
			populateListView();
		}

	}
}