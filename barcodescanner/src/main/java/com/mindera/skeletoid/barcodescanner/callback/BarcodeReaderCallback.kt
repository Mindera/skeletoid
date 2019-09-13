package com.mindera.skeletoid.barcodescanner.callback

import com.google.android.gms.vision.barcode.Barcode

/**
 * Callback that is called after the reader fragment
 * detects the correct barcode.
 */
interface BarcodeReaderCallback {
    /**
     * Returns the closest detected barcode to the center.
     */
    fun onBarcodeRead(barcode: Barcode?)
}