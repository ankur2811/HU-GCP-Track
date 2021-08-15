package com.example.appengine.springboot;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchService
{
    //Firestore Collection Name
    private static final String COLLECTION_NAME="Ankur_IN2021211_Balls";


    //Function to add ball details to the firebase
    public String addMatchEstimation(Match match) throws Exception
    {
        FileInputStream serviceAccount = new FileInputStream("./hu18-groupb-angular.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setDatabaseUrl("https://hu18-groupb-angular-default-rtdb.europe-west1.firebasedatabase.app").build();
        if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
            FirebaseApp.initializeApp(options);
        }



        Firestore db= FirestoreClient.getFirestore();

        Map<String, Object> data = new HashMap<>();
        data.put("over", match.getOver());
        data.put("run", match.getRun());
        data.put("bowler",match.getBowler());
        data.put("ball", match.getBall());
        data.put("batsman",match.getBatsman());
        data.put("TimeStamp",Timestamp.from(Instant.now()));

        ApiFuture<WriteResult> collectionsApiFuture=db.collection(COLLECTION_NAME).document().set(data);



        return collectionsApiFuture.get().getUpdateTime().toString();

    }


    //Function to get total runs scored by each team
    public HashMap<String,Long> getTotalRunsScoredByEachTeam() throws Exception
    {
        FileInputStream serviceAccount = new FileInputStream("./hu18-groupb-angular.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setDatabaseUrl("https://hu18-groupb-angular-default-rtdb.europe-west1.firebasedatabase.app").build();
        if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
            FirebaseApp.initializeApp(options);
        }

        Firestore db= FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        ApiFuture<QuerySnapshot> query2 = db.collection("Ankur_In2021211_players").get();
        QuerySnapshot querySnapshot2 = query2.get();
        List<QueryDocumentSnapshot> documentsCsv = querySnapshot2.getDocuments();
        HashMap<String,Long> map=new HashMap<>();
        for(QueryDocumentSnapshot doc1 : documentsCsv)
        {
            for(QueryDocumentSnapshot doc2 : documents)
            {
                if(doc1.getLong("id")==doc2.getLong("batsman"))
                {
                    if(map.get(doc1.get("team").toString())!=null && map.containsKey(doc1.get("team").toString()))
                    {
                        map.put(doc1.get("team").toString(),map.get(doc1.get("team").toString())+doc2.getLong("run"));
                    }
                    else
                    {
                        map.put(doc1.get("team").toString(),doc2.getLong("run"));
                    }
                }
            }
        }
        return map;
    }


    //Function to get total runs scored in each over
    public HashMap<String,Long> getTotalRunsScoredInEachOver() throws Exception
    {
        FileInputStream serviceAccount = new FileInputStream("./hu18-groupb-angular.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setDatabaseUrl("https://hu18-groupb-angular-default-rtdb.europe-west1.firebasedatabase.app").build();
        if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
            FirebaseApp.initializeApp(options);
        }

        Firestore db= FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        HashMap<String,Long> map=new HashMap<>();
        for(QueryDocumentSnapshot doc : documents)
        {
            String key="over"+doc.getLong("over");
            if( map.containsKey(key))
            {
                map.put(key,map.get(key)+doc.getLong("run"));
            }
            else
            {
                map.put(key,doc.getLong("run"));
            }
        }
        return map;
    }



    //Function to get Total runs scored by each batsman
    public Object getTotalRunsScoredByEachBatsman() throws Exception
    {
        FileInputStream serviceAccount = new FileInputStream("./hu18-groupb-angular.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setDatabaseUrl("https://hu18-groupb-angular-default-rtdb.europe-west1.firebasedatabase.app").build();
        if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
            FirebaseApp.initializeApp(options);
        }

        Firestore db= FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        ApiFuture<QuerySnapshot> query2 = db.collection("Ankur_In2021211_players").get();
        QuerySnapshot querySnapshot2 = query2.get();
        List<QueryDocumentSnapshot> documentsCsv = querySnapshot2.getDocuments();

        HashMap<Long,Long> map=new HashMap<>();
        for(QueryDocumentSnapshot doc : documents)
        {
            Long key=+doc.getLong("batsman");
            if( map.containsKey(key))
            {
                map.put(key,map.get(key)+doc.getLong("run"));
            }
            else
            {
                map.put(key,doc.getLong("run"));
            }
        }



        HashMap<Long,String> hm=new HashMap<>();
        for(QueryDocumentSnapshot doc:documentsCsv)
        {
            hm.put(doc.getLong("id"),doc.get("name")+"");
        }

        HashMap<String,Long> result=new HashMap<>();

       for(Long key:map.keySet())
        {
            if(hm.containsKey(key))
            result.put(hm.get(key),map.get(key));
        }
        return result;

    }


}
