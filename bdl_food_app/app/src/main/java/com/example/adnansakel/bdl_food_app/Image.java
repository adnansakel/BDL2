package com.example.adnansakel.bdl_food_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.app.Dialog;
import android.content.DialogInterface;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Image extends Activity {

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        Firebase.setAndroidContext(this);

        selectImage();
    }

    public void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Image.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
                String encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("encoded_image",encodeImage);
                setResult(RESULT_OK, intent);
                finish();

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
                String encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                Intent intent = new Intent();
                intent.putExtra("encoded_image",encodeImage);
                setResult(RESULT_OK, intent);
                finish();

            }
        }
    }
}
