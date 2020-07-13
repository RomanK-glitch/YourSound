package com.roman.yoursound.ui.register;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class PostRegister extends AsyncTask<String, Void, String> {
    String result = "";
    String userName;
    String password;
    String eMail;
    RegisterFragment registerFragment;

    public PostRegister (String userName, String password, String eMail, RegisterFragment registerFragment){
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.registerFragment = registerFragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("http://mrkoste6.beget.tech/register.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonUser = "{\"name\":" + "\"" + userName + "\"" + ",\"password\":" + "\"" + password + "\"" + ",\"email\":" + "\"" + eMail + "\"" +  "}";
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(jsonUser);
            out.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                if (line != null) {
                    result = result + line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (result.equals("Registered successfully")) {
            Toast.makeText(registerFragment.getActivity(), result,Toast.LENGTH_SHORT).show();
            registerFragment.getActivity().onBackPressed();
        } else if (result.contains("Duplicate entry")){
            Toast.makeText(registerFragment.getActivity(), "This name already taken",Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(registerFragment.getActivity(), result,Toast.LENGTH_SHORT).show();
            Toast.makeText(registerFragment.getActivity(), "Sorry, something went wrong",Toast.LENGTH_SHORT).show();
        }
    }
}