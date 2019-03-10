package com.project.groupproject.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.groupproject.R;
import com.project.groupproject.adapters.LocationAdapter;
import com.project.groupproject.models.Event;
import com.project.groupproject.viewmodals.EventViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateEventFragmentListener} interface
 * to handle interaction events.
 */
public class CreateEventFragment extends Fragment
    implements LocationAdapter.OnSelectLocation {

    private CreateEventFragmentListener mListener;
    private Context context;

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
    private ProgressBar loadingBar;

    private Calendar fromDateValue;
    private Calendar toDateValue;

    private DatePickerDialog datepicker;
    private DatePickerDialog.OnDateSetListener fromListener, toListener;
    private TimePickerDialog timepicker;
    private FragmentActivity activity;

    private RecyclerView locationAutocompleteView;
    private LocationAdapter locationAdapter;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = getActivity();
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

        loadingBar = activity.findViewById(R.id.loading_bar);

        // location
        locationAutocompleteView = view.findViewById(R.id.location_autocomplete);
        locationAdapter = new LocationAdapter();
        locationAutocompleteView.setAdapter(locationAdapter);
        locationAutocompleteView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationAdapter.setOnSelectListener(this);

        // reset fields
        fromDate.setText("");
        toDate.setText("");
        inputName.setText("");
        inputDesc.setText("");
        inputLocation.setText("");
        imgEvent.setImageResource(R.drawable.logo222);

        // datepicker
        datepicker = new DatePickerDialog(view.getContext());

        // set listener
        fromListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromDate.setText(year + "/" + (month+1) + "/" + dayOfMonth);
                fromDateValue.set(year, month, dayOfMonth);
                datepicker.getDatePicker().setMinDate(0);
                datepicker.getDatePicker().setMinDate(fromDateValue.getTime().getTime());
            }
        };
        toListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toDate.setText(year + "/" + (month+1) + "/" + dayOfMonth);
                toDateValue.set(year, month, dayOfMonth);
            }
        };
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker = new DatePickerDialog(view.getContext());
                datepicker.setOnDateSetListener(fromListener);
                datepicker.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datepicker.show();
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker.setOnDateSetListener(toListener);
                datepicker.show();
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Calendar currentTime = Calendar.getInstance();
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
        });
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        toTime.clearFocus();
                    }
                }, hour, minute, true);
                timepicker.show();
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
            }
        });


        // Auto place
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // init places
        Places.initialize(view.getContext().getApplicationContext(), getString(R.string.google_api_key));
        final PlacesClient placesClient = Places.createClient(view.getContext());

        inputLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {String query = inputLocation.getText().toString();
                // Use the builder to create a FindAutocompletePredictionsRequest.
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        //.setLocationRestriction(bounds)
                        .setCountry("ca")
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setSessionToken(token)
                        .setQuery(query)
                        .build();

                placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onSuccess(FindAutocompletePredictionsResponse response) {

                        ArrayList<String> list = LocationAdapter.convertToArray(response);
                        locationAdapter.updateData(list);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e("DEBUGZZZ", "Place not found: " + apiException.getStatusCode());
                        }
                    }
                });
            }
        });

        return view;
    }

    public void createEvent(Event event){
        final Event e = event;

        if (validate()) {

            //reset fields
            fromDate.setText("");
            toDate.setText("");
            fromTime.setText("");
            toTime.setText("");
            inputName.setText("");
            inputDesc.setText("");
            inputLocation.setText("");

            if (loadingBar != null) {
                loadingBar.setVisibility(ProgressBar.VISIBLE);;
            }
            EventViewModel.createEvent(e.id, event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    loadingBar.setVisibility(ProgressBar.GONE);;
                    // trigger listener to open single event
                    mListener.onCreated(e.id);
                }
            });
        }
    }

    private boolean validate() {
        //
        if (inputName.getText().toString().trim().equals("")){
            inputName.requestFocus();
            inputName.setError(context.getString(R.string.error_event_name_blank));
            return false;
        }

        if (fromDate.getText().toString().trim().equals("")){
            fromDate.requestFocus();
            fromDate.setError(context.getString(R.string.error_event_start_date_blank));
            return false;
        }

        if (fromTime.getText().toString().trim().equals("")){
            fromTime.requestFocus();
            fromTime.setError(context.getString(R.string.error_event_start_time_blank));
            return false;
        }

        if (toDate.getText().toString().trim().equals("")){
            toDate.requestFocus();
            toDate.setError(context.getString(R.string.error_event_end_date_blank));
            return false;
        }

        if (toTime.getText().toString().trim().equals("")){
            toTime.requestFocus();
            toTime.setError(context.getString(R.string.error_event_end_time_blank));
            return false;
        }

        if (fromDateValue.getTimeInMillis() > toDateValue.getTimeInMillis()) {
            toTime.requestFocus();
            toTime.setError(context.getString(R.string.error_event_time_conflict));
            return false;
        }

        if (inputLocation.getText().toString().trim().equals("")){
            inputLocation.requestFocus();
            inputLocation.setError(context.getString(R.string.error_event_loc_blank));
            return false;
        }

        return true;
    }

    @Override
    public void onSelect(String item) {
        inputLocation.setText(item);
        locationAdapter.clearData();
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
        if (requestCode == PICK_IMAGE && data != null){
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
