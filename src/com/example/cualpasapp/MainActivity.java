package com.example.cualpasapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends android.support.v4.app.FragmentActivity {

	private Button autolocalizar;
	private EditText ubicacion;
	private EditText destino;
	private Button maps;
	
	private LocationManager locManager;
	private LocationListener locListener;
	
	private GoogleMap mapa = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		autolocalizar = (Button) findViewById(R.id.autolocalizar);
		ubicacion = (EditText) findViewById(R.id.ubicacion);
		maps = (Button) findViewById(R.id.maps);
		destino = (EditText) findViewById(R.id.destino);
		
		autolocalizar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				comenzarLocalizacion();
			}
		});
		
		mapa = ((SupportMapFragment) getSupportFragmentManager()
				   .findFragmentById(R.id.map)).getMap();
		
		mapa.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				Projection proj = mapa.getProjection();
				Point coord = proj.toScreenLocation(point);

//				Toast.makeText(
//				MainActivity.this, 
//				"Click Largo\n" + 
//				"Lat: " + point.latitude + "\n" +
//				"Lng: " + point.longitude + "\n" +
//				"X: " + coord.x + " - Y: " + coord.y,
//				Toast.LENGTH_SHORT).show();

			}
		});
		
		mapa.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				Projection proj = mapa.getProjection();
				Point coord = proj.toScreenLocation(point);
				destino.setText(
						"Lat: " + point.latitude + "\n" +
						"Lng: " + point.longitude + "\n" );
//						"X: " + coord.x + " - Y: " + coord.y
//				Toast.makeText(
//						MainActivity.this, 
//						"Click Largo\n" + 
//						"Lat: " + point.latitude + "\n" +
//						"Lng: " + point.longitude + "\n" +
//						"X: " + coord.x + " - Y: " + coord.y,
//						Toast.LENGTH_SHORT).show();
			}
		});
		
//		mapa.setOnCameraChangeListener(new OnCameraChangeListener() {
//			public void onCameraChange(CameraPosition position) {
//				Toast.makeText(
//						MainActivity.this, 
//						"Cambio Cámara\n" + 
//						"Lat: " + position.target.latitude + "\n" +
//						"Lng: " + position.target.longitude + "\n" +
//						"Zoom: " + position.zoom + "\n" +
//						"Orientación: " + position.bearing + "\n" +
//						"Ángulo: " + position.tilt,
//						Toast.LENGTH_SHORT).show();
//			}
//		});
		
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(
						MainActivity.this, 
						"Marcador pulsado:\n" + 
						marker.getTitle(),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
	}

	private void comenzarLocalizacion() {
		// Obtenemos una referencia al LocationManager
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Obtenemos la �ltima posici�n conocida
		Location loc = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// Mostramos la �ltima posici�n conocida
		mostrarPosicion(loc);

		// Nos registramos para recibir actualizaciones de la posici�n
		locListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				mostrarPosicion(location);
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
		};

		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,
				0, locListener);
	}

		String asd="";
	private void mostrarPosicion(Location loc) {

			asd+="Latitud: " + String.valueOf(loc.getLatitude());
			asd+=("\nLongitud: "
					+ String.valueOf(loc.getLongitude()));
			ubicacion.setText(asd);
			asd="";

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
			case R.id.menu_marcadores:
				mostrarMarcador(40.5, -3.5);
				break;
			case R.id.menu_lineas:
				mostrarLineas();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void mostrarMarcador(double lat, double lng)
	{
		mapa.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lng))
        .title("Pais: España"));
	}
	
	private void mostrarLineas()
	{
		//Dibujo con Lineas
		
		PolylineOptions lineas = new PolylineOptions()
	        .add(new LatLng(45.0, -12.0))
	        .add(new LatLng(45.0, 5.0))
	        .add(new LatLng(34.5, 5.0))
	        .add(new LatLng(34.5, -12.0))
	        .add(new LatLng(45.0, -12.0));

		lineas.width(8);
		lineas.color(Color.RED);

		mapa.addPolyline(lineas);
	}
}
