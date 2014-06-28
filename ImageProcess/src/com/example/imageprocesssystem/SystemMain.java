package com.example.imageprocesssystem;
import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SystemMain extends Activity {
    private Button selectAlgBtn;
    private OnClickListener seleAlgBtnListener=null;

    private Bitmap myBitmap;
    private ImageView myImageView, originalImageView;
    private ImageProcess myImageProcess=new ImageProcess();
    private static final String DYNAMICACTION_Broadcast = "Broadcast.selectAlg";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("ImageProcessing");
        setContentView(R.layout.activity_system_main);
        /*seleImgBtnListener= new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        };*/
        seleAlgBtnListener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SystemMain.this,SelectAlgActivity.class);
				startActivity(intent);
			}
		};

		selectAlgBtn=(Button)findViewById(R.id.processBtn);
    	//selectImgBtn=(Button)findViewById(R.id.SelectBtn);
    	
    	selectAlgBtn.setOnClickListener(seleAlgBtnListener);

    	originalImageView=(ImageView)findViewById(R.id.originalimageshow);
    	myImageView=(ImageView)findViewById(R.id.imageshow);
    	
    	Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.lenna);
		originalImageView.setImageBitmap(bitmap);
	
		myImageView.setImageBitmap(bitmap);
		myBitmap = bitmap;
		
		IntentFilter filter_dynamic = new IntentFilter();
		filter_dynamic.addAction(DYNAMICACTION_Broadcast);
		registerReceiver(dynamicReceiver, filter_dynamic);
		
		
    }
 

    //2 自定义动态广播接收器，内部类,接收选择的算法
  	private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {
  		@Override
  		public void onReceive(Context context, Intent intent) {
  			if(intent.getAction().equals(DYNAMICACTION_Broadcast)){
  				Toast.makeText(SystemMain.this, "Please wait ...", Toast.LENGTH_SHORT).show();
  				String seleFlag = intent.getStringExtra("selectFlag");
                int ch=Integer.parseInt(seleFlag);
                switch(ch){
                case 0:
                	ShowImage(myImageProcess.addSaltAndPepperNoise(0.25f, myBitmap));
                	break;
                case 1:
                	ShowImage(myImageProcess.averageFilter(3,3,myBitmap));
                	break;
                case 2:
                	ShowImage(myImageProcess.averageFilter(3,3,myBitmap));
                	break;
                default:
                Toast.makeText(SystemMain.this, "Wrong!", Toast.LENGTH_SHORT).show();
                		break;
                }
                Toast.makeText(SystemMain.this, "Processing finished!", Toast.LENGTH_SHORT).show();
  			}
  		}
  	};
    public void ShowImage(Bitmap bitmap){
    	  if (bitmap!=null) {  
              myImageView.setImageBitmap(bitmap);  
          }  
          else {  
             bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.lenna);  
             myImageView.setImageBitmap(bitmap);  
             myBitmap=bitmap;  
          }  
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       
    	  Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.lenna);
		   myImageView.setImageBitmap(bitmap);
		   myBitmap=bitmap;
            ShowImage(myBitmap);
        //}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_system_main, menu);
        return true;
    }
}
