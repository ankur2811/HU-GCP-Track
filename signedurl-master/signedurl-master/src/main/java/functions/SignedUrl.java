package functions;

//import com.google.api.client.util.Lists;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignedUrl implements HttpFunction {
    public static final String projectId ="hu18-groupb-angular";
    public static final String objectName="players.csv";
    public static final String filePath="src/resources/players.csv";
    public static final String bucketName="ankur_in2021211_firstbucket";
    @Override
    public void service(HttpRequest request, HttpResponse response) throws StorageException, IOException {
        BufferedWriter writer = response.getWriter();
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("./service.json")) .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName,objectName)).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "text/plain");

       /* URL url =storage.signUrl(
                        blobInfo,
                        15,
                        TimeUnit.MINUTES,
                        Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                        Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                        Storage.SignUrlOption.withV4Signature());*/
        URL url=storage.signUrl(blobInfo,7, TimeUnit.DAYS,Storage.SignUrlOption.httpMethod(HttpMethod.PUT),Storage.SignUrlOption.withExtHeaders(extensionHeaders),Storage.SignUrlOption.withV4Signature(),Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(new FileInputStream(new File("./service.json")))));
        System.out.println(url);
    }

}
