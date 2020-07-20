package com.roman.yoursound.ui.EditProfile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;
import com.roman.yoursound.MainActivity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostUserImage extends AsyncTask<String, Void, String> {

    String result = "";
    int serverResponseCode = 0;
    EditProfileActivity editProfileActivity;
    String imagePath;

    public PostUserImage (String imagePath, EditProfileActivity editProfileActivity) {
        this.imagePath = imagePath;
        this.editProfileActivity = editProfileActivity;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            String sourceFileUri = imagePath;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (sourceFile.isFile()) {

                try {

                    //Compress image file
                    Bitmap b = BitmapFactory.decodeFile(sourceFile.getPath());
                    b.compress(Bitmap.CompressFormat.JPEG, 25, new FileOutputStream(sourceFile));
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);

                    // open a URL connection to the Servlet
                    URL url = new URL("http://mrkoste6.beget.tech/upload_image.php");

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("bill", sourceFileUri);

                    dos = new DataOutputStream(conn.getOutputStream());

                    //change file name to users id
                    String newFileName = String.valueOf(MainActivity.userLocalStore.getLoggedInUser().id) + sourceFileUri.substring(sourceFileUri.indexOf("."));

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\"" + newFileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                    InputStream inputStream = conn.getInputStream();
                    BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null){
                        line = input.readLine();
                        if (line != null){
                            result = result + line;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    result = e.getMessage();
                }
            } else {
                // End else block
                result = sourceFileUri;
            }
        } catch (Exception ex) {
            result = ex.getMessage();
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!result.contains("Success")){
            Toast.makeText(editProfileActivity, result, Toast.LENGTH_SHORT).show();
        }
    }
}