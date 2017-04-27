package coolcameraruntimepermission.aminulaust.com.coolcameraruntimepermission;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ImageView pro_pic;
    Button take_pic;
    int flag =0;
    Bitmap bitphoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pro_pic=(ImageView)findViewById(R.id.propic);
        take_pic=(Button)findViewById(R.id.takepic);
        checkAndRequestPermissions();

        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0){
                    //--camera code
                    Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i,100);
                }
                else if (flag==1){
                    //---code to save sd
                    savePicToSD(bitphoto);
                    flag=0;
                    take_pic.setText("Take a Photo");
                    Toast.makeText(getApplicationContext(),"Photo Saved", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data != null){
            bitphoto = (Bitmap) data.getExtras().get("data");
            pro_pic.setImageBitmap(bitphoto);
            flag = 1;
            take_pic.setText("Save Photo");
        }
    }

    private  void savePicToSD(Bitmap bit){
      //  SimpleDateFormat date= new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
      //  String picture_name=date.format(new Date());

        android.text.format.DateFormat date = new android.text.format.DateFormat();
        String picture_name= (String) date.format("yyyy-MM-dd hh:mm:ss a", new Date());

        String root = Environment.getExternalStorageDirectory().toString();
        File create_folder=new File(root+"/CameraApp");
        create_folder.mkdirs();
        File my_file = new File(create_folder, picture_name+".png");
        try {
            FileOutputStream stream= new FileOutputStream(my_file);
            boolean compress = bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkAndRequestPermissions() {

        int sdWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int sdRedaPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (sdWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (sdRedaPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
