package com.example;

import com.example.Example.GCSEvent;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Example implements BackgroundFunction<GCSEvent> {
    private static final Logger logger = Logger.getLogger(Example.class.getName());

    private static String projID = "hu18-groupb-angular";
    private static String bucketName = "ankur_in2021211_firstbucket";
    private static String filePattern = "players.csv";

    public static void triggerFunction() throws Exception {

        Storage storage = StorageOptions.newBuilder().setProjectId(projID).build().getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, filePattern)).build();
        int duration = 7;
        HttpMethod httpMethod = HttpMethod.GET;
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", "text/csv");
        FileInputStream inputStream = new FileInputStream(new File("./hu18-groupb.json"));
        ServiceAccountCredentials serviceAccountCredentials = ServiceAccountCredentials.fromStream(inputStream);

        URL url = storage.signUrl(
                blobInfo, duration,
                TimeUnit.DAYS,
                Storage.SignUrlOption.httpMethod(httpMethod),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.signWith(serviceAccountCredentials));

        logger.info("Successfully deployed with Signed URL: \n");
        logger.info(url.toString());

        URL defURL = new URL(url.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) defURL.openConnection();
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Content-type", "text/csv");
        InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(isr);

        InputStream serviceAccount = new FileInputStream("./hu18-groupb.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(credentials).build();
        FirebaseApp.initializeApp(firebaseOptions);

        Firestore firestore = FirestoreClient.getFirestore();

        int idx = 1;
        String line;
        String[] arr;
        bufferedReader.readLine();
        while ((line = bufferedReader.readLine()) != null) {
            arr = line.split(",");
            System.out.println(Integer.parseInt(arr[0]) + "  " + arr[1] + "  " + arr[0] + "  " + arr[3]);

            Timestamp instant= Timestamp.from(Instant.now());
            HashMap<String, Object> json = new HashMap<>();
            json.put("id", Integer.parseInt(arr[0]));
            json.put("name", arr[1]);
            json.put("age", Integer.parseInt(arr[2]));
            json.put("team", arr[3]);
            json.put("timestamp", instant);

            DocumentReference documentReference = firestore.collection("Ankur_In2021211_players").document("record" + idx);
            ApiFuture<WriteResult> result = documentReference.set(json);
            idx++;

            logger.info("Updated time : " + result.get().getUpdateTime());
        }
    }

    @Override
    public void accept(GCSEvent event, Context context) {
        logger.info("Trigger Starts " + event.name);
        try {
            triggerFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static class GCSEvent {
        String bucket;
        String name;
        String metageneration;
    }
}
