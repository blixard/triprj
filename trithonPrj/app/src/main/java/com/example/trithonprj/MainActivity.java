package com.example.trithonprj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ImageReader;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trithonprj.ml.FlowerModel;
import com.example.trithonprj.ml.PlantClassificationModel;
import com.example.trithonprj.ml.PlantDiseaseModel;
import com.example.trithonprj.ml.SampleModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static Bitmap imageBitmap;
    static List<Category> probability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("", "hello ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_CapImg = (Button) findViewById(R.id.btn_captureImage);
        btn_CapImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent_CapImg = new Intent(getApplicationContext() , Act2CapImg.class);
//                startActivity(intent_CapImg);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 44);

                } catch (ActivityNotFoundException e) {
                    System.out.print(e);
                    // display error state to the user
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView iv_showImg = findViewById(R.id.iv_showImg);
        if (requestCode == 44 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            Bitmap iBitmap =  Bitmap.createScaledBitmap(imageBitmap, 64,64,true );
            iv_showImg.setImageBitmap(iBitmap);
            RecognitionListener listener = null;
//            ImageAnalyzer obj = new ImageAnalyzer(this, listener);

            try {
                PlantClassificationModel model = PlantClassificationModel.newInstance(this);
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{ 1,64, 64, 3}, DataType.FLOAT32);
                ByteBuffer b = ByteBuffer.allocate(4*64*64*3);
                iBitmap.copyPixelsToBuffer(b);
                inputFeature0.loadBuffer(b);

                // Runs model inference and gets result.
                PlantClassificationModel.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                int [] ar =  outputFeature0.getIntArray();
                String fs = "";
                for(int i=0 ; i<ar.length; i++){

                    fs = fs + i +" : " + String.valueOf(ar[i]) + " " ;
                }
                // Releases model resources if no longer used.
                model.close();

                TextView tv_label1 = findViewById(R.id.tv_label1);
                tv_label1.setText(fs );
            } catch (IOException e) {
                // TODO Handle the exception
            }
        }
    }

//
//    private class ImageAnalyzer {
//        ImageAnalyzer(Context ctx, RecognitionListener listener) {
//            try {
//                PlantClassificationModel model = PlantClassificationModel.newInstance(ctx);
//                // Creates inputs for reference.
//                TensorImage image = TensorImage.fromBitmap(imageBitmap);
//                TensorBuffer tb = image.getTensorBuffer();
//                ByteBuffer b = ByteBuffer.allocate(64*64*3);
//                Bitmap iBitmap = Bitmap.createBitmap(imageBitmap , 0,0,128,128);
//                TensorImage img = TensorImage.fromBitmap(iBitmap);
//                TensorBuffer f = img.getTensorBuffer();
////                iBitmap.copyPixelsToBuffer(b);
////                int [] arr = {64,64,3};
//////                bb =  image.getBuffer();
////                TensorBuffer tb1 = TensorBuffer.createFixedSize(arr, DataType.FLOAT32 );
////                TensorBuffer tb2 = TensorBuffer.createFrom(tb1 , DataType.FLOAT32);
////                tb1.loadBuffer(b);
//                // Runs model inference and gets result.
//                 PlantClassificationModel.Outputs outputs = model.process(f);
//                TensorBuffer probability = outputs.getOutputFeature0AsTensorBuffer();
//                // Releases model resources if no longer used.
//                model.close();
//                int [] ar =  probability.getIntArray();
//                String fs = "";
//                for(int i=0 ; i<ar.length; i++){
//
//                    fs = fs + " " + String.valueOf(ar[i]) ;
//                }
//
//                float large = 0;
////                String shape = probability.getShape().toString();
//                String s = probability.getBuffer().getClass().getCanonicalName();
//
//            TextView tv_label1 = findViewById(R.id.tv_label1);
//            TextView tv_label2 = findViewById(R.id.tv_label2);
//            TextView tv_label3 = findViewById(R.id.tv_label3);
//            TextView tv_label4 = findViewById(R.id.tv_label4);
//                tv_label1.setText(fs );
//                int [] arr = probability.getShape().clone();
//                String s2 =  String.valueOf(arr[1]) ;
//                int [] abc = tb.getShape();
//                tv_label2.setText(String.valueOf("") );
////                tv_label3.setText(s1 );
//                tv_label4.setText(String.valueOf(abc[2]));
//
//            } catch (IOException e) {
//                // TODO Handle the exception
//            }
//            try {
//                PlantClassificationModel model = PlantClassificationModel.newInstance(ctx);
//
//                Bitmap iBitmap =  Bitmap.createScaledBitmap(imageBitmap, 64,64,true );
////                TensorImage img = TensorImage.fromBitmap(iBitmap);
////                TensorBuffer f = img.getTensorBuffer();
//                // Creates inputs for reference.
//                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{ 1,64, 64, 3}, DataType.FLOAT32);
//                ByteBuffer b = ByteBuffer.allocate(4*64*64*3);
//                iBitmap.copyPixelsToBuffer(b);
//                inputFeature0.loadBuffer(b);
//
//                // Runs model inference and gets result.
//                PlantClassificationModel.Outputs outputs = model.process(inputFeature0);
//                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//                float [] ar =  outputFeature0.getFloatArray();
//                String fs = "";
//                for(int i=0 ; i<ar.length; i++){
//
//                    fs = fs + " " + String.valueOf(ar[i]) ;
//                }
//                // Releases model resources if no longer used.
//                model.close();
//
//                tv_label1.setText(fs );
//            } catch (IOException e) {
//                // TODO Handle the exception
//            }


//            try {
//                PlantDiseaseModel model = PlantDiseaseModel.newInstance(ctx);
//
////                Bitmap iBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, 224, 224);
//                // Creates inputs for reference.
//                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
//                Bitmap iBitmap =  Bitmap.createScaledBitmap(imageBitmap, 224,224,true );
//                ByteBuffer b = ByteBuffer.allocate(4* 224 * 224 * 3);
//                iBitmap.copyPixelsToBuffer(b);
//                inputFeature0.loadBuffer(b);
//
//                // Runs model inference and gets result.
//                PlantDiseaseModel.Outputs outputs = model.process(inputFeature0);
//                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//                float[] ar = outputFeature0.getFloatArray();
////                Arrays.sort(ar);
//                String fs = "";
//                for (int i = 0; i < ar.length; i++) {
//
//                    fs = fs + i + " : " + String.valueOf(ar[i] + " ");
//                }
//                tv_label1.setText(fs );
//
//                // Releases model resources if no longer used.
//                model.close();
//            } catch (IOException e) {
//
//
//            }
//        }
//    }
}


