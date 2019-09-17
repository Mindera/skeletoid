# Barcode Scanner

Uses google api vision to create an easy way to scan all types of barcodes. If there are more than one barcode in the screen,
it tries to pick the one closest to the center of the screen.

## Dependency

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:barcodescanner:1.0.0'
}
```

## Using the barcode scanner

1. Implement the `BarcodeReaderCallback` interface on your view. The `onBarcodeRead`callback will return the closest barcode to the center of the screen.
2. Instantiate the `BarcodeScannerFragment` and pass the view with the callback to the object.
3. Configure the bundle according to your needs. We can set the following variables:

    - `BARCODE_FORMATS_BUNDLE_KEY` Accepted barcode formats
      - Default: All
      - Type: Int
      
    - `BARCODE_STORAGE_MIN_PERCENTAGE_BUNDLE_KEY` Min storage percentage on the device
      - Default: 10% of free space
      - Type: Int
      
    - `BARCODE_STORAGE_MIN_PERCENTAGE_BUNDLE_KEY` Camera width resolution
      - Default: 1920
      - Type: Int
      
    - `BARCODE_CAMERA_RESOLUTION_WIDTH_BUNDLE_KEY` Camera height resolution
      - Default: 1080 
      - Type: Int
      
    - `BARCODE_CAMERA_RESOLUTION_HEIGHT_BUNDLE_KEY` Camera detection delay
      - Default: 2000L 
      - Type: Long
      
    - `BARCODE_CAMERA_FRAMES_PER_SECOND_BUNDLE_KEY` Camera frames per second
      - Default: 30.0f 
      - Type: Float
      
4. Start reading barcodes!

Optional: Implement the `BarcodeScannerPermissionsCallback` interface to override the permissions request behaviour as you can see below on the code samples.

## Sample code

```
class MainActivity : AppCompatActivity(), BarcodeReaderCallback {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        button.setOnClickListener {

            val fragment = BarcodeScannerFragment(this, null)
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val bundle = Bundle()

            bundle.putInt(BARCODE_FORMATS_BUNDLE_KEY, Barcode.CODE_128 or Barcode.QR_CODE)
            bundle.putInt(BARCODE_STORAGE_MIN_PERCENTAGE_BUNDLE_KEY, 10)
            bundle.putInt(BARCODE_CAMERA_RESOLUTION_WIDTH_BUNDLE_KEY, 1920)
            bundle.putInt(BARCODE_CAMERA_RESOLUTION_HEIGHT_BUNDLE_KEY, 1080)
            bundle.putLong(BARCODE_CAMERA_DETECTION_DELAY_BUNDLE_KEY, 1500L)
            bundle.putFloat(BARCODE_CAMERA_FRAMES_PER_SECOND_BUNDLE_KEY, 30f)

            fragment.arguments = bundle
            transaction.add(R.id.container, fragment, BarcodeReaderFragment.BARCODE_FRAGMENT_TAG)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onBarcodeRead(barcode: Barcode?) {
        Log.d(BarcodeReaderFragment.BARCODE_FRAGMENT_TAG, "Got the barcode on the callback: $barcode")
    }
}
```

However, you can also override the request permissions behaviour real easy, by implementing the `BarcodeScannerPermissionsCallback`interface on your parent activity/fragment.

Usage is as such:

```
class MainActivity : AppCompatActivity(), BarcodeReaderCallback, BarcodeScannerPermissionsCallback {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        button.setOnClickListener {

            val fragment = BarcodeReaderFragment(this, this)
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val bundle = Bundle()

            bundle.putInt(BARCODE_FORMATS_BUNDLE_KEY, Barcode.CODE_128 or Barcode.QR_CODE)
            bundle.putInt(BARCODE_STORAGE_MIN_PERCENTAGE_BUNDLE_KEY, 10)
            bundle.putInt(BARCODE_CAMERA_RESOLUTION_WIDTH_BUNDLE_KEY, 1920)
            bundle.putInt(BARCODE_CAMERA_RESOLUTION_HEIGHT_BUNDLE_KEY, 1080)
            bundle.putLong(BARCODE_CAMERA_DETECTION_DELAY_BUNDLE_KEY, 2000L)
            bundle.putFloat(BARCODE_CAMERA_FRAMES_PER_SECOND_BUNDLE_KEY, 30f)

            fragment.arguments = bundle
            transaction.add(R.id.container, fragment, BarcodeReaderFragment.BARCODE_FRAGMENT_TAG)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun requestCameraPermissions() {
       //Do my request camera permissions logic here..
    }

    override fun onBarcodeRead(barcode: Barcode?) {
        Log.d(BarcodeReaderFragment.BARCODE_FRAGMENT_TAG, "Got the barcode on the callback: $barcode")
    }
}
```


