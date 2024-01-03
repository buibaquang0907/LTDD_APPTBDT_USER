package com.example.shoptbdt;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

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
                String requestBody = String.format(
                        "{\"service_id\":\"%s\",\"template_id\":\"%s\",\"user_id\":\"%s\",\"template_params\":{\"user_name\":\"%s\",\"user_email\":\"%s\",\"user_subject\":\"%s\",\"user_message\":\"%s\"}}",
                        serviceId, templateId, userId, name, email, subject, message);

                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(mediaType, requestBody))
                        .addHeader("origin", "http://localhost")
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Body: " + response.body().string());
                return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(@NonNull Boolean success) {
            if (success) {
                System.out.println("Email sent successfully");
            } else {
                System.out.println("Error sending email");
            }
        }

    }
}



