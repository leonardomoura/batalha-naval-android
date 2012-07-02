package classesExemplos;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import univali.m3.R;

import communication.Message;
import communication.bluethooth.Connection;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluethoothActivity extends Activity {
    public static final String NAME = "Crazy Soccer";
    public static final UUID MY_UUID = UUID
	    .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private class ConnectThread extends Thread {
	private final BluetoothSocket mmSocket;
	private final BluetoothDevice mmDevice;

	public ConnectThread(BluetoothDevice device) {
	    // Use a temporary object that is later assigned to mmSocket,
	    // because mmSocket is final
	    BluetoothSocket tmp = null;
	    mmDevice = device;

	    // Get a BluetoothSocket to connect with the given BluetoothDevice
	    try {
		// MY_UUID is the app's UUID string, also used by the server
		// code
		tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	    } catch (IOException e) {
	    }
	    mmSocket = tmp;
	}

	public void run() {
	    // Cancel discovery because it will slow down the connection
	    mBluetoothAdapter.cancelDiscovery();

	    try {
		// Connect the device through the socket. This will block
		// until it succeeds or throws an exception
		mmSocket.connect();
		//Exemplo de conexão
//		Connection con = new Connection(mmSocket);
//		Message m = Message.createMessage();
//		m.cod = Codes.mandouUmTiro;
//		m.data = new Tiro();
//		Message retorno = con.writeAndWait(m, m.cod, 60000);
//		Message retorno1 = con.read(2, 60000);
		
	    } catch (Exception connectException) {
		// Unable to connect; close the socket and get out
		try {
		    mmSocket.close();
		} catch (IOException closeException) {
		}
		return;
	    }

	    try {
		 
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    ;

	}

	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
	    try {
		mmSocket.close();
	    } catch (IOException e) {
	    }
	}
    }

    private class AcceptThread extends Thread {
	private final BluetoothServerSocket mmServerSocket;

	public AcceptThread() {
	    // Use a temporary object that is later assigned to mmServerSocket,
	    // because mmServerSocket is final
	    BluetoothServerSocket tmp = null;
	    try {
		// MY_UUID is the app's UUID string, also used by the client
		// code
		tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
			NAME, MY_UUID);
	    } catch (IOException e) {
	    }
	    mmServerSocket = tmp;
	}

	public void run() {
	    BluetoothSocket socket = null;

	    while (true) {
		try {
		    socket = mmServerSocket.accept();

		    // If a connection was accepted
		    if (socket != null) {
			// Do work to manage the connection (in a separate
			// thread)
			Log.d("BluetoothAdapter", "Connectado");
			try {
			    
			} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			;
			mmServerSocket.close();
			break;
		    }
		} catch (IOException e) {
		    break;
		}
	    }
	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    try {
			dialog.dismiss();
		    } catch (Throwable e) {
			// TODO: handle exception
		    }

		}
	    });
	}

	/** Will cancel the listening socket, and cause the thread to finish */
	public void cancel() {
	    try {
		mmServerSocket.close();
	    } catch (IOException e) {
	    }
	}
    }

    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<BluetoothDevice> mArrayAdapter;
    int REQUEST_ENABLE_BT = 12;
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	public void onReceive(Context context, Intent intent) {
	    String action = intent.getAction();
	    // When discovery finds a device
	    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		// Get the BluetoothDevice object from the Intent
		BluetoothDevice device = intent
			.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		// Add the name and address to an array adapter to show in a
		// ListView
		mArrayAdapter.add(device);
	    }
	}
    };
    protected ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	 
	super.onCreate(savedInstanceState);
	setContentView(R.layout.bluetooth);
	
	mArrayAdapter = new ArrayAdapter<BluetoothDevice>(
		getApplicationContext(), R.layout.row1) {
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
		    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    v = vi.inflate(R.layout.row1, null);

		}
		BluetoothDevice o = getItem(position);
		if (o != null) {

		    TextView tt = (TextView) v.findViewById(R.id.textView1);

		    if (tt != null) {
			tt.setText("Name: " + o.getName());
		    }

		}
		return v;
	    }
	};
	ListView l = ((ListView) findViewById(R.id.listView1));
	l.setAdapter(mArrayAdapter);

	l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		// AcceptThread
		BluetoothDevice device = ((BluetoothDevice) arg0
			.getItemAtPosition(arg2));

		Toast.makeText(getApplicationContext(),
			"Connecting.." + device.getName(), Toast.LENGTH_SHORT);

		new ConnectThread(device).start();

	    }
	});
	((Button) findViewById(R.id.btcreate))
		.setOnClickListener(new View.OnClickListener() {

		    @Override
		    public void onClick(View v) {
			dialog = new ProgressDialog(BluethoothActivity.this);
			dialog.setMessage("Please wait...\nWaiting to other player");
			dialog.setIndeterminate(true);
			dialog.show();
			new AcceptThread().start();
		    }
		});
	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	if (mBluetoothAdapter == null) {
	    Toast.makeText(getApplicationContext(),
		    "Sorry your mobile don't have bluetooth", Toast.LENGTH_LONG);
	    return;
	}
	if (!mBluetoothAdapter.isEnabled()) {
	    Intent enableBtIntent = new Intent(
		    BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	} else {
	    getEmparelhados();
	}
	((ImageButton) findViewById(R.id.btrefresh))
		.setOnClickListener(new View.OnClickListener() {

		    @Override
		    public void onClick(View v) {
			// Register the BroadcastReceiver
			getEmparelhados();
			mBluetoothAdapter.startDiscovery();
		    }
		});

    }

    @Override
    protected void onResume() {
	super.onResume();
	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
	super.onPause();
	unregisterReceiver(mReceiver);
    }

    public void getEmparelhados() {
	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
		.getBondedDevices();
	mArrayAdapter.clear();
	// If there are paired devices
	if (pairedDevices.size() > 0) {
	    // Loop through paired devices
	    for (BluetoothDevice device : pairedDevices) {
		// Add the name and address to an array adapter to show in a
		// ListView
		mArrayAdapter.add(device);
	    }
	}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Log.d("RETORNO", requestCode + "");

	if (requestCode == REQUEST_ENABLE_BT) {
	    getEmparelhados();
	}

    }
}