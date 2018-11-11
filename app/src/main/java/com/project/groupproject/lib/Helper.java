package com.project.groupproject.lib;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;

public class Helper {

    static public void seed() {
        String email = "Mavis_Goyette@hotmail.com";
        String password = "123456";
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String uid = task.getResult().getUser().getUid();

                // add user info
                User user = new User();
                user.setId(uid);
                user.firstname = "Mavis";
                user.lastname = "Goyette";
                user.email = "email";
                FirebaseFirestore.getInstance().collection("users").document(uid).set(user);

                // events
                String[] eventsName = new String[] {
                        "Playland, 2901 E Hastings St, Vancouver, BC V5K 5J1",
                        "Canada Place, 999 Canada Pl, Vancouver, BC V6C 3T4",
                        "Stanley Park, Vancouver, BC V6G 1Z4",
                        "Metrotown, 4700 Kingsway, Burnaby, BC V5H 4N2",
                        "Douglas College, 700 Royal Ave, New Westminster, BC V3M 5Z5"
                };
//                String[] desc = new String[] {"Playland is an amusement park in Vancouver, British Columbia, Canada. It is located in Hastings Park and is the oldest amusement park in Canada. The regular season lasts from May to the end of September every year. The park reopens for Fright Nights from mid October to the Halloween weekend."}
//
//                Event event1 = new Event();
//                event1.uid = uid;
//                event1.name = "Christmas Night Markket";
//                event1.location = "Canada Place, 999 Canada Pl, Vancouver, BC V6C 3T4";
//                event1.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer id suscipit lacus. Integer ac risus id dolor venenatis venenatis. Suspendisse accumsan nibh quis nibh sodales, non pretium arcu rhoncus. Nam lobortis nunc nulla, id aliquam lorem pulvinar vitae. Praesent nec urna vitae nisi semper congue ut sit amet erat.";
            }
        });
    }

}
