package com.mindera.skeletoid.barcodescanner.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.snackbar.Snackbar
import com.mindera.skeletoid.barcodescanner.R
import com.mindera.skeletoid.barcodescanner.barcode.BarcodeGraphic
import com.mindera.skeletoid.barcodescanner.barcode.BarcodeGraphicTracker
import com.mindera.skeletoid.barcodescanner.barcode.BarcodeTrackerFactory
import com.mindera.skeletoid.barcodescanner.callback.BarcodeScannerCallback
import com.mindera.skeletoid.barcodescanner.callback.BarcodeScannerPermissionsCallback
import com.mindera.skeletoid.barcodescanner.camera.CameraSource
import com.mindera.skeletoid.barcodescanner.camera.CameraSourcePreview
import com.mindera.skeletoid.barcodescanner.camera.GraphicOverlay
import com.mindera.skeletoid.kt.extensions.views.afterDrawn
import com.mindera.skeletoid.logs.LOG
import kotlin.math.sqrt


@Suppress("Unused")
class BarcodeScannerFragment(private val barcodeCallback: BarcodeScannerCallback,
                             private val permissionsCallback: BarcodeScannerPermissionsCallback?) : Fragment(), BarcodeGraphicTracker.BarcodeDetectorListener {
    companion object {
        const val BARCODE_FORMATS_BUNDLE_KEY = "barcodeFormats"
        const val BARCODE_STORAGE_MIN_PERCENTAGE_BUNDLE_KEY = "BARCODE_STORAGE_MIN"
        const val BARCODE_CAMERA_RESOLUTION_WIDTH_BUNDLE_KEY = "BARCODE_CAMERA_RESOLUTION_WIDTH"
        const val BARCODE_CAMERA_RESOLUTION_HEIGHT_BUNDLE_KEY = "BARCODE_CAMERA_RESOLUTION_HEIGHT"
        const val BARCODE_CAMERA_FRAMES_PER_SECOND_BUNDLE_KEY = "BARCODE_CAMERA_FRAMES_PER_SECOND"
        const val BARCODE_CAMERA_DETECTION_DELAY_BUNDLE_KEY = "BARCODE_CAMERA_DETECTION_DELAY"
        const val BARCODE_FRAGMENT_TAG = "BARCODESCANNER_FRAGMENT"
        const val RC_HANDLE_GOOGLE_MOBILE_SERVICES = 9001
        const val RC_HANDLE_CAMERA_PERMISSION = 2
    }

    //Flags that represent the barcode formats we want to read.
    private var barcodeFormats: Int = R.integer.barcodeFormats
    //Minimum percentage of available storage on the device (check initCamera commentaries)
    private var deviceStorageMinPercentage = R.integer.deviceStorageMinPercentage
    //Camera width resolution
    private var cameraResolutionWidth = R.integer.cameraResolutionWidth
    //Camera height resolution
    private var cameraResolutionHeight = R.integer.cameraResolutionHeight
    //Camera FPS
    private var cameraFramesPerSeconds = R.integer.cameraFramesPerSeconds.toFloat()
    //The amount of time the camera will detect barcodes after the first detection.
    private var cameraDetectionDelay = R.integer.cameraDetectionDelay.toLong()
    //The barcode closest to the center of the screen in portrait mode.
    private var activeBarcode : Barcode? = null

    //Camera preview screen
    private lateinit var sourcePreview: CameraSourcePreview
    //The actual camera source
    private lateinit var source: CameraSource
    //Overlay drawn over the barcodes
    private lateinit var overlay: GraphicOverlay<BarcodeGraphic>
    //Camera preview screen dimensions
    private lateinit var sourcePreviewMeasurements: Rect
    private lateinit var snackbarView: View

    //All of the barcodes we read during cameraDetectionDelay.
    private val readBarcodes = mutableListOf<Barcode>()
    //Triggered when we read the first barcode. Used to start cameraDetectionDelay countdown.
    private val hasBarcode = MutableLiveData<Boolean>() //Ensure this variable is only changed ONCE.
    //To sync barcode detections and avoid dupes.
    private val lock = Any()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_barcode_reader, container, false)

        sourcePreview = view.findViewById(R.id.fragment_barcode_scanner_camera_source_preview)
        overlay = view.findViewById(R.id.fragment_barcode_scanner_camera_source_preview_overlay)
        snackbarView = requireActivity().findViewById(android.R.id.content)

        //Gather barcode information.
        arguments?.getInt(BARCODE_FORMATS_BUNDLE_KEY)?.let { barcodeFormats = it }
        arguments?.getInt(BARCODE_CAMERA_RESOLUTION_WIDTH_BUNDLE_KEY)?.let { cameraResolutionWidth = it }
        arguments?.getInt(BARCODE_CAMERA_RESOLUTION_HEIGHT_BUNDLE_KEY)?.let { cameraResolutionHeight = it }
        arguments?.getInt(BARCODE_STORAGE_MIN_PERCENTAGE_BUNDLE_KEY)?.let { deviceStorageMinPercentage = it }
        arguments?.getLong(BARCODE_CAMERA_DETECTION_DELAY_BUNDLE_KEY)?.let { cameraDetectionDelay = it }
        arguments?.getFloat(BARCODE_CAMERA_FRAMES_PER_SECOND_BUNDLE_KEY)?.let { cameraFramesPerSeconds = it }

        //Setup the observables and their handlers.
        setupObservables()

        //Check permissions before opening the camera
        val permissionStatus = ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //If the permission is granted, we need to wait for the system to measure the camera preview screen.
            //After we have that information, we need to save it to calculate the closest barcode to the center.
            sourcePreview.afterDrawn {
                sourcePreviewMeasurements = Rect(left, top, right, bottom)
            }

            initCamera()
        } else {
            permissionsCallback?.let { permissionsCallback.requestCameraPermissions() } ?: run { requestCameraPermissions() }
        }

        return view
    }


    /**
     * Creates the required observers and handles their execution accordingly.
     *
     * hasBarcode changes to true and is observed when we detect our first barcode.
     *
     * After that, a countdown of CAMERA_DETECTION_DELAY seconds starts, allowing detection
     * of multiple barcodes during that time.
     *
     * After the time's up, the activeBarcode variable will be the closest barcode to the center.
     */
    private fun setupObservables() {
        hasBarcode.observe(this@BarcodeScannerFragment, Observer {
            Handler().postDelayed({
                barcodeCallback.onBarcodeRead(activeBarcode)
            }, cameraDetectionDelay)
        })
    }

    /**
     * Note: The first time that an app using the barcode or face API is installed on a
     * device, GMS will download a native libraries to the device in order to do detection.
     * Usually this completes before the app is run for the first time.  But if that
     * download has not yet completed, then the above call will not detect any barcodes
     * and/or faces.
     *
     * isOperational() can be used to check if the required native libraries are currently
     * available.  The detectors will automatically become operational once the library
     * downloads complete on device.
     *
     * So we check for low storage.  If there is low storage, the native library will not be
     * downloaded, so detection will not become operational.
     *
     * cacheDir returns a file representing the internal directory for the app temporary cache files.
     *
     * cachedir.usableSpace returns the available space on the device for all the applications.
     * cachedir.totalSpace returns the total space available on the device storage.
     *
     * According to some users, the depecrated ACTION_DEVICE_STORAGE_LOW checked if the device had 10%
     * of free space, and that number seems to work well, in general.
     * Either way, we define the variable deviceStorageMinPercentage as a base for the percentage of
     * free space in the drive we think is reasonable.
     */
    private fun initCamera() {
        //Create the barcode detector to detect only those we defined previously.
        val barcodeDetector = BarcodeDetector.Builder(activity?.applicationContext).setBarcodeFormats(barcodeFormats).build()
        val barcodeTrackerFactory = BarcodeTrackerFactory(overlay, this)
        barcodeDetector.setProcessor(MultiProcessor.Builder(barcodeTrackerFactory).build())

        activity?.cacheDir?.let {
            if (!barcodeDetector.isOperational && it.usableSpace * 100 / it.totalSpace <= deviceStorageMinPercentage)
                Toast.makeText(requireContext(), R.string.barcode_scanner_fragment_low_storage_warning, Toast.LENGTH_LONG).show()
        }

        //Creates the camera.
        source = CameraSource.Builder(activity?.applicationContext, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(cameraResolutionWidth, cameraResolutionHeight)
                .setRequestedFps(cameraFramesPerSeconds)
                .setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE) //Fixme: 11 Sept. 2019 - Find a workaround to this deprecated issue. Use camera2 as google recommends.
                .build()
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     *
     * MissingPermission lint is supressed because we ensured already that the user only reaches this point
     * if he has permissions.
     */
    @SuppressLint("MissingPermission")
    private fun openCamera() {
        //Checks if the device has Google Play Services enabled.
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val isGoogleApiAvailable = googleApiAvailability.isGooglePlayServicesAvailable(activity?.applicationContext)
        if (isGoogleApiAvailable != ConnectionResult.SUCCESS) googleApiAvailability.getErrorDialog(activity, isGoogleApiAvailable, RC_HANDLE_GOOGLE_MOBILE_SERVICES)

        //Guarantee that the source is actually initialized, we don't want crashes in my town.
        if (::source.isInitialized) {
            try {
                sourcePreview.start(source, overlay)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                source.release()
            }
        }
    }


    /**
     * Requests permission to use the camera.
     */
    private fun requestCameraPermissions() {
        val permissions = arrayOf(android.Manifest.permission.CAMERA)
        activity?.let { activity ->

            Snackbar.make(snackbarView, R.string.barcode_scanner_fragment_request_camera_permission, Snackbar.LENGTH_INDEFINITE).setAction(R.string.barcode_scanner_fragment_snackbar_confirm) {
                ActivityCompat.requestPermissions(activity, permissions, RC_HANDLE_CAMERA_PERMISSION)
            }.show()

        } ?: run { LOG.w(BARCODE_FRAGMENT_TAG, "Activity is null on requestCameraPermission")}
    }

    /**
     * If our source preview screen isn't measured yet, we can't know its measurements and, therefore,
     * we can't check which rect drawn over the barcode is closest to the center.
     *
     * On the other hand, if we do have the measurements, we can calculate the closest rect drawn over the
     * barcode by using pythagoras theorem.
     */
    private fun getClosestBarcode(sourcePreviewMeasurements: Rect) {
        readBarcodes.forEach { next ->
            //If the barcode center is within our camera preview
            if (sourcePreviewMeasurements.contains(next.boundingBox.centerX(), next.boundingBox.centerY())) {

                //Then we now calculate the distance of its center to the camera preview's center.
                //If the center we have now calculated is less than the previous one or the list is empty,
                //replace the active barcode on the live data with the new barcode.
                if (activeBarcode == null) {
                    //There's no active barcode currently, so there's no need to calculate the closest to the center yet,
                    //as this is the only one available. Yay for performance!
                    activeBarcode = next
                } else {
                    //If we do have one active, however, then we need to check if the one we caught is the closest
                    //to the center than the previously active one.
                    activeBarcode?.let { previous ->

                        //Guarantee we are not calculating things unnecessarily, hip hip hurrah for performance!
                        if (next != previous) {
                            //Find the closest barcode to the center of the screen preview.
                            val nextBarcodeDistanceToCenter = distanceToCenter(sourcePreviewMeasurements, barcodeToRect(next))
                            val previousBarcodeDistanceToCenter = distanceToCenter(sourcePreviewMeasurements, barcodeToRect(previous))
                            if (nextBarcodeDistanceToCenter < previousBarcodeDistanceToCenter) activeBarcode = next
                        }
                    }
                }
            }
        }
    }

    /**
     * Converts a barcode bounding box to a Rect object.
     * a^2 + b^2 = c^2
     *
     */
    private fun barcodeToRect(barcode: Barcode): Rect {
        return Rect(barcode.boundingBox.left, barcode.boundingBox.top, barcode.boundingBox.left + barcode.boundingBox.right, barcode.boundingBox.top + barcode.boundingBox.bottom)
    }

    /**
     * Distance to center using pythagoras theorem.
     *
     * We want the hypotenuse which is c^2, so, c = sqrt(a^2 + b^2)
     *
     */
    private fun distanceToCenter(a: Rect, b: Rect) : Double {
        return sqrt(Math.pow((a.centerX() - b.centerX()).toDouble(), 2.0) + Math.pow((a.centerY() - b.centerY()).toDouble(), 2.0))
    }

    /**
     * Callback that is triggered when a new object is detected by the camera.
     *
     * @param data the barcode parsed object, in this case, our QRCode.
     */
    override fun onObjectDetected(data: Barcode?) {
        //The callback can be triggered multiple times for the same object,
        //So we might be looking at cases where, without synchronization,
        //we are trying to find an item that does not yet exist in the list, but is already
        //ready to be added, and our comparison will not return false, adding duplicates.
        //To avoid that, RACE CONDITIONS!
        synchronized(lock) {
            //Avoid duplicates & null data..
            if (data != null && readBarcodes.find { it.rawValue == data.rawValue } == null) {
                //All good, add the new bar code.
                readBarcodes.add(data)
                //Get the closest barcode and save it on our active barcode live data.
                if (::sourcePreviewMeasurements.isInitialized) getClosestBarcode(sourcePreviewMeasurements)
                //Send message that we have a barcode so we can start the count down. ONLY one time.
                if (hasBarcode.value == null) hasBarcode.postValue(true)
            }
        }
    }

    /**
     * Receives the requestCameraPermission function result of success or lack of and handles it.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RC_HANDLE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) initCamera()
            else Snackbar.make(snackbarView, R.string.barcode_scanner_fragment_no_camera_permission, Snackbar.LENGTH_SHORT).show()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onResume() {
        LOG.w(BARCODE_FRAGMENT_TAG, "Re-opening the camera onResume.")
        super.onResume()
        openCamera()
    }

    override fun onPause() {
        LOG.w(BARCODE_FRAGMENT_TAG, "Stopping the camera onPause.")
        super.onPause()
        sourcePreview.stop()
    }

    override fun onDestroy() {
        LOG.w(BARCODE_FRAGMENT_TAG, "Releasing the camera onDestroy.")
        super.onDestroy()
        sourcePreview.release()
    }

}