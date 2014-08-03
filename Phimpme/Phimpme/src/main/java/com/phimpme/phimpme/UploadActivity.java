package com.phimpme.phimpme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;

public class UploadActivity extends ActionBarActivity {
    private AdView adView;
    private Button AndroidSharingListButton;
    private Button bluetoothButton;
    private ImageView preview;
    private TextView descriptionEditText;
    private TextView nfcTextView;
    private Uri imageUri;
    private String imageDescription;
    private Switch locationSwitch;
    private Button locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        find_views();

        // Automatically update imageDescription with descriptionEditText
        descriptionEditText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                imageDescription = descriptionEditText.getText().toString();
            }
        });

        enable_functions_or_hide_views();
    }

    private void find_views() {
        bluetoothButton = (Button) findViewById(R.id.bluetoothButton);
        AndroidSharingListButton = (Button) findViewById(R.id.otherButton);
        preview = (ImageView) findViewById(R.id.imageView);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        nfcTextView = (TextView) findViewById(R.id.nfcTextView);
        imageUri = (Uri) getIntent().getExtras().get("imageUri");
        locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        locationButton = (Button) findViewById(R.id.locationButton);
    }

    private void enable_functions_or_hide_views() {
        if (Configuration.ENABLE_NFC) {
            if (!enable_nfc()) {
                nfcTextView.setText("NFC not supported");
            }
        } else {
            nfcTextView.setVisibility(View.GONE);
        }

        if (Configuration.ENABLE_PHOTO_LOCATION_MODIFICATION) {
            enable_location_modification();
        } else {
            locationSwitch.setVisibility(View.GONE);
            locationButton.setVisibility(View.GONE);
        }

        // TODO: Init locationSwitch (visible if GPS data is available)

        try {
            preview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Configuration.ENABLE_ANDROID_SHARING) {
            enable_android_sharing();
        } else {
            AndroidSharingListButton.setVisibility(View.GONE);
        }

        if (Configuration.ENABLE_BLUETOOTH) {
            enable_bluetooth();
        } else {
            bluetoothButton.setVisibility(View.GONE);
        }

        if (Configuration.ENABLE_ADVERTISEMENT) {
            enable_adView();
        }
    }

    private boolean enable_nfc() {
        nfcTextView.setVisibility(View.VISIBLE);
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            // NFC is available on this device
            nfcAdapter.setBeamPushUris(null, this);
            nfcAdapter.setBeamPushUrisCallback(new NfcAdapter.CreateBeamUrisCallback() {
                @Override
                public Uri[] createBeamUris(NfcEvent event) {
                    Uri[] nfcPushUris = new Uri[1];
                    nfcPushUris[0] = imageUri;
                    return nfcPushUris;
                }
            }, this);
            return true;
        } else {
            return false;
        }
    }

    private void enable_android_sharing() {
        AndroidSharingListButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent uploadPhotoIntent = new Intent(Intent.ACTION_SEND);
                uploadPhotoIntent.setType("image/*");
                uploadPhotoIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                uploadPhotoIntent.putExtra(Intent.EXTRA_TEXT, descriptionEditText.getText().toString());
                startActivity(Intent.createChooser(uploadPhotoIntent, "Share Image To:"));
            }
        });
    }

    private void enable_bluetooth() {
        bluetoothButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadPhotoIntent = new Intent(Intent.ACTION_SEND);
                uploadPhotoIntent.setType("image/*");
                uploadPhotoIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                uploadPhotoIntent.putExtra(Intent.EXTRA_TEXT, descriptionEditText.getText().toString());
                uploadPhotoIntent.setPackage("com.android.bluetooth");
                startActivity(Intent.createChooser(uploadPhotoIntent, "Share Image To:"));
            }
        });
    }

    private void enable_adView() {
        // 以查询资源的方式查询AdView并加载请求。
        adView = (AdView) findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void enable_location_modification() {
        locationSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                locationButton.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        locationButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(UploadActivity.this, GeographicalLocationActivity.class);
                intent.putExtra("imageUri", imageUri);
                startActivity(intent);
            }
        });
    }

    private void creatUploadDialog() {
        final ArrayList<String> arrayList = new ArrayList<String>();
        if (Configuration.ENABLE_SHARING_TO_WORDPRESS) arrayList.add("WordPress");
        if (Configuration.ENABLE_SHARING_TO_DRUPAL) arrayList.add("Drupal");
        if (Configuration.ENABLE_SHARING_TO_JOOMLA) arrayList.add("Joomla");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrayList);

        new AlertDialog.Builder(this).setTitle("Upload image to:")
                .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String item = arrayList.get(which);
                        if (imageUri != null) {
                            AccountInfo accountInfo = AccountInfo.getSavedAccountInfo(UploadActivity.this, item);
                            if (accountInfo.getAccountCategory() == null) {
                                AccountInfo.createAndSaveAccountInfo(UploadActivity.this, item);
                            } else {
                                if(item.equals("WordPress")) new ShareToWordPress(UploadActivity.this, accountInfo, imageUri.getPath()).uploadPhoto();
                                if(item.equals("Drupal")) new ShareToDrupal(UploadActivity.this, accountInfo, imageUri.getPath(), imageDescription).uploadPhoto();
                                if(item.equals("Joomla")) new ShareToJoomla(UploadActivity.this, accountInfo, imageUri.getPath()).uploadPhoto(descriptionEditText.getText().toString());
                            }
                        }
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }})
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.UploadActivityAccountManger) {
            Intent intent = new Intent(UploadActivity.this, AccountEditor.class);
            intent.putExtra("accountCategory", "null");
            startActivity(intent);
            return true;
        } else if (id == R.id.UploadActivityUpload) {
            creatUploadDialog();
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
