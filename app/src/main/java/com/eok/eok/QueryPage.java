package com.eok.eok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.eok.eok.databinding.ActivityMainScreenBinding;
import com.eok.eok.databinding.ActivityQueryPageBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class QueryPage extends AppCompatActivity {

    private ActivityQueryPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueryPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        StrictMode.ThreadPolicy policy  = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        binding.btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }
    private void sendEmail() {
        final String username = "eok204140@gmail.com"; // Replace with your email
        final String password = "jlfwrykxwbszctsd"; // Replace with your email password

        String toEmail = "furkan.basibuyuk18@gmail.com"; // Replace with the recipient's email address
        String subject = "Query from " + binding.name2.getText().toString();
        String messageText = binding.longDescription.getText().toString()+"\nŞu adresten ulaşabilirsiniz: "+binding.name.getText().toString();

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your SMTP server
        properties.put("mail.smtp.port", "587"); // Replace with your SMTP port


        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);

            Toast.makeText(this, "Message is sent.", Toast.LENGTH_SHORT).show();
            // Email sent successfully
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage()+"");
            Toast.makeText(this, e.getLocalizedMessage()+"", Toast.LENGTH_SHORT).show();
            // Handle the error
        }

    }
    public void back(View b)
    {
        Intent intent = new Intent(QueryPage.this,ProfileSetting.class);
        startActivity(intent);
        finish();
    }
}