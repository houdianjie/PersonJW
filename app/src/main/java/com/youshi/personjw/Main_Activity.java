package com.youshi.personjw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.youshi.personjw.Person.Person_Classroom;
import com.youshi.personjw.Person.Person_Course;
import com.youshi.personjw.Person.Person_ExamPlan;
import com.youshi.personjw.Person.Person_Get_SP;
import com.youshi.personjw.Person.Person_Info;
import com.youshi.personjw.Person.Person_Fail_Course;
import com.youshi.personjw.Person.Person_Password;
import com.youshi.personjw.Person.Person_Score;
import com.youshi.personjw.Person.Person_Score_nofinal;
import com.youshi.personjw.Person.Person_TeachPlan;
import com.youshi.personjw.Person.Search_Score;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dianjie on 2016/7/24.
 */
public class Main_Activity extends Activity {
    private ImageButton head_img_btn;
    private Bitmap bmp;
   private String COOK,HOST;
    private long mExitTime;
    //public static File f = new File(Environment.getExternalStorageDirectory()+"/QDU/head/"+"head_img.jpg");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_main);
        init();
        Intent intent = Main_Activity.this.getIntent();
        Bundle bundle = intent.getExtras();
        COOK = bundle.getString("cookieVal");
        HOST = bundle.getString("host");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int group1 = 1;
        menu.add(group1,1,1,"退出");
        menu.add(group1,2,2,"关于");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case 1:
                System.exit(0);
                break;
            case 2:
                AlertDialog.Builder a = new AlertDialog.Builder(this);
                a.setTitle("关于").setMessage("青大掌上教务，可以为你带来诸多便利，省去了登教务网站的麻烦。\n作者:侯典杰").show();
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return  true;
    }
    private void init(){
        /*main_passsword_btn = (Button) findViewById(R.id.person_main_password_btn);
        main_classroom_btn = (Button) findViewById(R.id.person_main_classroom_btn);
        main_course_btn = (Button) findViewById(R.id.person_main_course_btn);
        main_score_btn = (Button) findViewById(R.id.person_main_score_btn);*/
        mExitTime=0;
        head_img_btn = (ImageButton) findViewById(R.id.main_head_img);
            if(Get_Headimg()!=null){
                head_img_btn.setImageBitmap(Get_Headimg());
            }
        else {
                head_img_btn.setImageResource(R.drawable.head_img);
            }
        bmp = null;
        COOK = null;
    }
    private Bitmap Get_Headimg(){
        Bitmap bitmap=null;
        try
        {
            File f = new File(this.getFilesDir(),"head_img.jpg");
           // File file = new File(f);
            if(f.exists())
            {
                //System.out.println("已经存在:"+f.toString());
                bitmap = BitmapFactory.decodeFile(f.toString());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return  bitmap;
    }
    public void main_password_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Person_Password.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void main_quit_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,HomePage.class);
        startActivity(intent);
        this.finish();
    }
    public void main_info_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Person_Info.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void main_classroom_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Person_Classroom.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void main_teach_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Person_TeachPlan.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void main_score_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Search_Score.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void main_course_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Person_Course.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void main_exam_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Person_ExamPlan.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void main_study_process_click(View v){
        Intent intent = new Intent();
        intent.setClass(Main_Activity.this,Person_Get_SP.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void Fail_Course(View view){
        Intent intent = new Intent(this, Person_Fail_Course.class);
        Bundle bundle = new Bundle();
        bundle.putString("cookieVal",COOK);
        bundle.putString("host",HOST);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void Head_img_click(View v){
        CharSequence[] items = {"相册"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( i == 0 ){
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);
                        }
                    }
                })
                .create().show();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
//选择图片
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                if(bmp != null)//如果不释放的话，不断取图片，将会内存不够
                    bmp.recycle();
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            head_img_btn.setImageBitmap(bmp);
            saveBitmap();
        }else{
            Toast.makeText(Main_Activity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
        }
    }
    /** 保存方法 */
    public void saveBitmap() {
        File f = new File(this.getFilesDir(), "head_img.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.i("Saved", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
