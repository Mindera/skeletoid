package com.mindera.skeletoid.barcodescanner.barcode;

/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.mindera.skeletoid.barcodescanner.camera.GraphicOverlay;

import java.util.Map;

/**
 * Generic tracker which is used for tracking or reading a barcode (and can really be used for
 * any type of item).  This is used to receive newly detected items, add a graphical representation
 * to an overlay, update the graphics as the item changes, and remove the graphics when the item
 * goes away.
 */
public class BarcodeGraphicTracker extends Tracker<Barcode> {

    private GraphicOverlay<BarcodeGraphic> mOverlay;
    private BarcodeGraphic mGraphic;
    private BarcodeDetectorListener mBarcodeDetectorListener;

    BarcodeGraphicTracker(GraphicOverlay<BarcodeGraphic> overlay, BarcodeGraphic graphic) {
        mOverlay = overlay;
        mGraphic = graphic;
    }

    public void setDetectorListener(BarcodeDetectorListener mBarcodeDetectorListener) {
        this.mBarcodeDetectorListener = mBarcodeDetectorListener;
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    @Override
    public void onNewItem(int id, Barcode item) {
        mGraphic.setId(id);
        if(mBarcodeDetectorListener == null) return;
        mBarcodeDetectorListener.onObjectDetected(item);
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {
        mOverlay.add(mGraphic);
        mGraphic.updateItem(item);
    }

    /**
     * Hide the graphic when the corresponding object was not detected.  This can happen for
     * intermediate frames temporarily, for example if the object was momentarily blocked from
     * view.
     */
    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {
        mOverlay.remove(mGraphic);
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
    }

    public interface BarcodeDetectorListener{
        /**
         * Multiple events can be fired depending the number of barcodes identified,
         * So you may want to build a Map<K,V> to add the detected objects and finish the
         * activity when the user is satisfied. @see {@link Map}
         * <br/>
         *
         * @param data Barcode parsed object will contain different kinds of data depending
         *             on the scanned barcode content.
         */
        void onObjectDetected(Barcode data);
    }
}
