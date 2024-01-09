package com.example.shoptbdt;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class EmailSender {

    public static void sendEmail(String name, String email, String subject, String message) {
        new SendEmailTask().execute(name, email, subject, message);
    }

    private static class SendEmailTask extends AsyncTask<String, Void, Boolean> {

        @NonNull
        @Override
        protected Boolean doInBackground(@NonNull String... params) {
            String name = params[0];
            String email = params[1];
            String subject = params[2];
            String message = params[3];

            String serviceId = "service_7giqvj6";
            String templateId = "template_9vdqusx";
            String userId = "0Ts1Uaskm14Kbgj-i";
            String url = "https://api.emailjs.com/api/v1.0/email/send";

            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("service_id", serviceId);
                jsonBody.put("template_id", templateId);
                jsonBody.put("user_id", userId);

                JSONObject templateParams = new JSONObject();
                templateParams.put("user_name", name);
                templateParams.put("user_email", email);
                templateParams.put("user_subject", subject);
                templateParams.put("user_message", message);

                jsonBody.put("template_params", templateParams);

                RequestBody requestBody = RequestBody.create(mediaType, jsonBody.toString());

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("origin", "http://localhost")
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                int responseCode = response.code();
                String responseBody = response.body().string();

                Log.d("EmailSender", "Response Code: " + responseCode);
                Log.d("EmailSender", "Response Body: " + responseBody);

                return response.isSuccessful();

            } catch (IOException | JSONException e) {
                Log.e("EmailSender", "Error sending email", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(@NonNull Boolean success) {
            if (success) {
                Log.d("EmailSender", "Email sent successfully");
            } else {
                Log.e("EmailSender", "Error sending email");
            }
        }
    }
}



