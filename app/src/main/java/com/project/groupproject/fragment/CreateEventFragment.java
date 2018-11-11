package com.project.groupproject.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.groupproject.R;
import com.project.groupproject.lib.Helper;
import com.project.groupproject.models.Event;
import com.project.groupproject.viewmodals.EventViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import io.opencensus.internal.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateEventFragmentListener} interface
 * to handle interaction events.
 */
public class CreateEventFragment extends Fragment {

    private CreateEventFragmentListener mListener;

    // components
    private Button mButton;
    private EditText fromDate;
    private EditText toDate;
    private EditText fromTime;
    private EditText toTime;
    private EditText inputName;
    private EditText inputDesc;
    private EditText inputLocation;
    private ImageView imgEvent;
    private Uri selectedImage;

    private Calendar fromDateValue;
    private Calendar toDateValue;

    private DatePickerDialog datepicker;
    private DatePickerDialog.OnDateSetListener fromListener, toListener;
    private TimePickerDialog timepicker;


    static final int PICK_IMAGE = 1;

    public CreateEventFragment() {
        // Required empty public constructor
        fromDateValue = Calendar.getInstance();
        toDateValue = Calendar.getInstance();
    }

    public static CreateEventFragment getInstance() {
        return new CreateEventFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        //
        fromDate = view.findViewById(R.id.input_event_start_date);
        toDate = view.findViewById(R.id.input_event_end_date);
        fromTime = view.findViewById(R.id.input_event_start_time);
        toTime = view.findViewById(R.id.input_event_end_time);
        mButton = view.findViewById(R.id.btn_create_event);
        inputName = view.findViewById(R.id.input_event_name);
        inputDesc = view.findViewById(R.id.input_event_desc);
        inputLocation = view.findViewById(R.id.input_event_location);
        imgEvent = view.findViewById(R.id.event_photo);

        // reset fields
        fromDate.setText("");
        toDate.setText("");
        inputName.setText("");
        inputDesc.setText("");
        inputLocation.setText("");
        imgEvent.setImageResource(R.drawable.event1);

        // datepicker
        datepicker = new DatePickerDialog(view.getContext());


        // set listener
        fromListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromDate.setText(year + "/" + month + "/" + dayOfMonth);
                fromDateValue.set(year, month, dayOfMonth);
            }
        };
        toListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toDate.setText(year + "/" + month + "/" + dayOfMonth);
                toDateValue.set(year, month, dayOfMonth);
            }
        };
        fromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(view.getContext(), "test", Toast.LENGTH_SHORT);
                    datepicker.setOnDateSetListener(fromListener);
                    datepicker.show();
                }
            }
        });
        toDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datepicker.setOnDateSetListener(toListener);
                    datepicker.show();
                }
            }
        });

        fromTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar currentTime = Calendar.getInstance();
                    int hour = 9; //currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = 0; //currentTime.get(Calendar.MINUTE);
                    timepicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hourString = Strings.padStart(String.valueOf(hourOfDay), 2, '0');
                            String minString = Strings.padStart(String.valueOf(minute), 2, '0');
                            fromTime.setText( hourString + ":" + minString);
                            fromDateValue.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            fromDateValue.set(Calendar.MINUTE, minute);
                        }
                    }, hour, minute, true);
                    timepicker.show();
                }
            }
        });
        toTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar currentTime = Calendar.getInstance();
                    int hour = 18;//currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = 0; //currentTime.get(Calendar.MINUTE);
                    timepicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hourString = Strings.padStart(String.valueOf(hourOfDay), 2, '0');
                            String minString = Strings.padStart(String.valueOf(minute), 2, '0');
                            toTime.setText( hourString + ":" + minString);
                            toDateValue.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            toDateValue.set(Calendar.MINUTE, minute);
                        }
                    }, hour, minute, true);
                    timepicker.show();
                }
            }
        });

        imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, PICK_IMAGE);
            }
        });

        //
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Event e = getEvent();

                // get coordinate first
                try{
                    e.generateCoordinate(getContext());
                } catch (IOException ex) {
                    Log.d("Event", "Get coordinate error");
                    ex.printStackTrace();
                }

                final String eventId = FirebaseFirestore.getInstance().collection("events").document().getId();
                e.id = eventId;
                if (selectedImage != null){
                    // upload image after get id
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageRef = storage.getReference("events");
                    storageRef.child(eventId).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("project_group", "upload success");
                            storageRef.child(eventId).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    e.setImage(task.getResult());
                                    createEvent(e);
                                }
                            });
                        }
                    });
                } else {
                    createEvent(e);
                }

                Log.d("Event", "Get coordinate error");

                // create event
                /*
                EventViewModel.createEvent(e).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        final String id = documentReference.getId();

                        // upload image after get id
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        final StorageReference storageRef = storage.getReference("events");

                        if (selectedImage != null){
                            storageRef.child(id).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // trigger listener to open single event
                                    mListener.onCreated(id);

                                    storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            FirebaseFirestore.getInstance().collection("events").document(id).set();
                                        }
                                    });

                                    Log.d("project_group", "upload success");
                                }
                            });
                        }
                    }
                });
                */
            }
        });

        return view;
    }

    public void createEvent(Event event){
        final Event e = event;
        EventViewModel.createEvent(e.id, event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // trigger listener to open single event
                mListener.onCreated(e.id);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateEventFragmentListener) {
            mListener = (CreateEventFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreateEventFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CreateEventFragmentListener {
        // TODO: Update argument type and name
        void onCreated(String id);
    }

    /**
     *
     * @return
     */
    private Event getEvent() {
        Event e = new Event();
        e.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        e.name = inputName.getText().toString();
        e.description = inputDesc.getText().toString();
        e.location = inputLocation.getText().toString();
        e.lat = -1.0;
        e.lng = 23.0012;
        e.num_follow = 0;
        e.num_like = 0;
        e.start_date = fromDateValue.getTimeInMillis();
        e.end_date = toDateValue.getTimeInMillis();
        e.extractTags();
        return e;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE){
            Log.d("PICK_IMAGE", "pick image success");
            selectedImage = data.getData();
            imgEvent.setImageURI(selectedImage);
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public String getImagePath(Uri uri){
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
