package com.example.total_test


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.total_test.network.MyApiService
import com.example.total_test.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.UUID
import com.example.total_test.data.BluetoothData;


class MainActivity_my : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_my)
        settingButtonok()
        settingButtonok1()
        settingButtonok2()
        val dlg = CustomPopupDialog(this)
        dlg.setOnOKClickedListener(object : CustomPopupDialog.OnClickedListener {
            override fun onOKClicked(msg: String) {
                Toast.makeText(applicationContext, "$msg", Toast.LENGTH_SHORT).show()
                val cancle_n = findViewById<TextView>(R.id.cancel_emergency_num)
                val detect = findViewById<TextView>(R.id.detect_fall_num)
                val emer = findViewById<TextView>(R.id.call_emergency_num)
                var number = 0;
                var number_3 = 1;


                number++
                number_3++

                cancle_n.setText(number.toString())
                detect.setText(number_3.toString())
                emer.setText(number.toString())

            }

            override fun onOKClicked() {
                Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT).show()
                val emer = findViewById<TextView>(R.id.call_emergency_num)
                val detect = findViewById<TextView>(R.id.detect_fall_num)
                var number_2 = 0;


                number_2++
                emer.setText(number_2.toString())
                detect.setText(number_2.toString())

            }
        })
        dlg.show()
        plus()
        plus2()





        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val bigrectangle4 = findViewById<View>(R.id.bigrectangle_4)
        bigrectangle4.setOnClickListener { listPairedDevices() }


    }


    fun settingButtonok() {
        val button = findViewById<View>(R.id.bigrectangle_1)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity_userinfo::class.java)
            startActivity(intent)
        }
    }

    fun settingButtonok1() {
        val button = findViewById<View>(R.id.bigrectangle_3)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity_log1::class.java)
            startActivity(intent)
        }
    }

    fun settingButtonok2() {
        val button = findViewById<View>(R.id.bigrectangle_2)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity_signup3::class.java)
            startActivity(intent)
        }
    }

    var number = 0;
    fun plus() {
        var cancle_v = findViewById<View>(R.id.butt1)
        val cancle_n = findViewById<TextView>(R.id.cancel_emergency_num)
        val detect = findViewById<TextView>(R.id.detect_fall_num)

        cancle_v.setOnClickListener {
            number++
            cancle_n.setText(number.toString())
            detect.setText((number + number_2).toString())
        }
    }

    var number_2 = 0;
    fun plus2() {
        var cancle_v = findViewById<View>(R.id.butt2)
        val emer = findViewById<TextView>(R.id.call_emergency_num)
        val detect = findViewById<TextView>(R.id.detect_fall_num)
        cancle_v.setOnClickListener {
            number_2++
            emer.setText(number_2.toString())
            detect.setText((number + number_2).toString())
        }
    }


    //블루투스 코드
    companion object {
        const val BT_REQUEST_ENABLE = 1
        const val BT_MESSAGE_READ = 2
        const val BT_CONNECTING_STATUS = 3
        val BT_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    // Bluetooth 관련 변수
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mListPairedDevices: MutableList<String>? = null
    //private var mBluetoothHandler: Handler? = null
    private var mBluetoothDevice: BluetoothDevice? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    private lateinit var mPairedDevices: Set<BluetoothDevice>
    private var mThreadConnectedBluetooth: ConnectedBluetoothThread? = null

    private fun listPairedDevices() {
        if (mBluetoothAdapter!!.isEnabled) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mPairedDevices = mBluetoothAdapter!!.bondedDevices
            if (mPairedDevices.size > 0) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("장치 선택")
                mListPairedDevices = ArrayList()
                for (device in mPairedDevices) {
                    mListPairedDevices!!.add(device.name)
                }
                val items = mListPairedDevices!!.toTypedArray<CharSequence>()
                builder.setItems(
                    items
                ) { dialog: DialogInterface?, item: Int ->
                    connectSelectedDevice(
                        items[item].toString()
                    )
                }
                val alert = builder.create()
                alert.show()
            } else {
                Toast.makeText(applicationContext, "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun connectSelectedDevice(selectedDeviceName: String) { //받은 것은 선택된 블루투스 기기의 이름이므로 주소 값을 찾음
        for (bleDevice in mPairedDevices) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            if (selectedDeviceName == bleDevice.name) {
                mBluetoothDevice = bleDevice
                break
            }
        }
        try { //찾은 주소값을 이용하여 연결
            mBluetoothSocket =
                mBluetoothDevice?.createRfcommSocketToServiceRecord(BT_UUID)
            mBluetoothSocket?.connect()
            mThreadConnectedBluetooth = mBluetoothSocket?.let { ConnectedBluetoothThread(it) }
            mThreadConnectedBluetooth!!.start()
            Toast.makeText(applicationContext, "블루투스가 연결되었습니다.", Toast.LENGTH_LONG).show()

        } catch (e: IOException) {
            Toast.makeText(applicationContext, "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show()
            Log.e("Error Reason", e.toString())
        }
    }


    inner class ConnectedBluetoothThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream
        private val mmOutStream: OutputStream




        // Retrofit2 인터페이스 생성
        private val apiService = RetrofitClient.client!!.create(MyApiService::class.java)

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show()
            }
            mmInStream = tmpIn!!
            mmOutStream = tmpOut!!
        }

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int

            while (true) {
                try {
                    bytes = mmInStream.available()
                    if (bytes != 0) {
                        SystemClock.sleep(100)
                        mmInStream.read(buffer, 0, bytes)

                        val bluetooth_data = BluetoothData(String(buffer, Charset.forName("UTF-8")))

                        // Bluetooth 데이터를 Retrofit2를 사용하여 서버로 전송
                        sendDataToServer(bluetooth_data)
                    }
                } catch (e: IOException) {
                    break
                }
            }
        }

        // Retrofit2를 사용하여 데이터를 서버로 전송하는 함수
        private fun sendDataToServer(bluetooth_data:BluetoothData) {
            val call = apiService.sendDataToServer(bluetooth_data)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 데이터가 성공적으로 저장되었을 때 처리
                        Toast.makeText(
                            applicationContext,
                            "데이터가 MySQL에 저장되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // 데이터 저장 중 오류 발생 시 처리
                        Toast.makeText(applicationContext, "데이터 저장 오류", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 오류 시 처리
                    Toast.makeText(applicationContext, "네트워크 오류: " + t.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }
}