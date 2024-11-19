package com.example.architecture.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.architecture.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
public class ProfileFragment extends Fragment {

    private TextView nameTextView, phoneTextView, emailTextView, passwordTextView, roleTextView;
    private ImageView profileImageView, editName, editPhone, editEmail, editPassword, editRole;
    private String currentUserEmail;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUserEmail = auth.getCurrentUser().getEmail().replace(".", ",");

        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail);

        // Initialize views
        nameTextView = view.findViewById(R.id.text_view_name);
        phoneTextView = view.findViewById(R.id.text_view_phone);
        emailTextView = view.findViewById(R.id.text_view_email);
        passwordTextView = view.findViewById(R.id.text_view_password);
        roleTextView = view.findViewById(R.id.text_view_role);
        profileImageView = view.findViewById(R.id.image_view_profile);

        // Edit buttons
        editName = view.findViewById(R.id.edit_name);
        editPhone = view.findViewById(R.id.edit_phone);
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        editRole = view.findViewById(R.id.edit_role);

        // Set edit button listeners
        setEditListeners();

        // Load profile data
        loadProfileData();

        return view;
    }

    private void loadProfileData() {
        // Fetch user data from Firebase
        userRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                setStyledText(nameTextView, "Name: ", dataSnapshot.child("name").getValue(String.class));
                setStyledText(phoneTextView, "Phone: ", dataSnapshot.child("phone").getValue(String.class)+"");
                setStyledText(emailTextView, "Email: ", dataSnapshot.child("email").getValue(String.class));
                setStyledText(passwordTextView, "Password: ", "********"); // Hide password for security
                setStyledText(roleTextView, "Role: ", dataSnapshot.child("role").getValue(String.class));

                String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                Glide.with(this).load(profileImageUrl).placeholder(R.drawable.profile).into(profileImageView);
            }
        });
    }

    private void setStyledText(TextView textView, String title, String value) {
        String fullText = title + value;
        SpannableString spannableString = new SpannableString(fullText);

        // Apply color and style to the title part
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#6200EE")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply a different color to the value part
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), title.length(), fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }
    private void setEditListeners() {
        editName.setOnClickListener(v -> showEditDialog("name", "Edit Name", nameTextView,"Name: "));
        editPhone.setOnClickListener(v -> showEditDialog("phone", "Edit Phone", phoneTextView, InputType.TYPE_CLASS_PHONE,"Phone: "));
        editEmail.setOnClickListener(v -> showEditDialog("email", "Edit Email", emailTextView, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,"Email: "));
        editPassword.setOnClickListener(v -> showEditDialog("password", "Edit Password", passwordTextView, InputType.TYPE_TEXT_VARIATION_PASSWORD, true,"Password "));
        editRole.setOnClickListener(v -> showEditDialog("role", "Edit Role", roleTextView,"Role: "));
    }

    private void showEditDialog(String field, String title, TextView textView,String t) {
        showEditDialog(field, title, textView, InputType.TYPE_CLASS_TEXT, false,t);
    }

    private void showEditDialog(String field, String title, TextView textView, int inputType,String t) {
        showEditDialog(field, title, textView, inputType, false,t);
    }

    private void showEditDialog(String field, String title, TextView textView, int inputType, boolean isPassword,String t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setInputType(inputType);
        if (isPassword) {
            input.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
        }
        String currentText = textView.getText().toString();
        String valueOnly = currentText.replace(t, "");  // Removes "Name: ", "Phone: ", etc.
        input.setText(valueOnly.trim());  // Remove any extra whitespace if needed

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newValue = input.getText().toString();
            textView.setText(newValue);

            // Update Firebase
            userRef.child(field).setValue(newValue)
                    .addOnSuccessListener(aVoid -> Snackbar.make(getView(), field + " updated successfully", Snackbar.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Snackbar.make(getView(), "Failed to update " + field, Snackbar.LENGTH_SHORT).show());
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
