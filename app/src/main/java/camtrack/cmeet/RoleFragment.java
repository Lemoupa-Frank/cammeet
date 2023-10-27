package camtrack.cmeet;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.databinding.FragmentRoleBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.api.client.json.Json;

import java.util.Objects;

import camtrack.cmeet.placeholder.PlaceholderContent;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A fragment representing a list of Items.
 */
public class RoleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    Dialog dialog;

    FragmentRoleBinding binding;

    TextView department_role;

    Retrofit retrofit = Retrofit_Base_Class.getClient_String();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RoleFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RoleFragment newInstance(int columnCount) {
        RoleFragment fragment = new RoleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_role_list, container, false);
        binding = FragmentRoleBinding.inflate(inflater,container, false);
        MyRoleNameRecyclerViewAdapter myRoleNameRecyclerViewAdapter = new MyRoleNameRecyclerViewAdapter(PlaceholderContent.ITEMS);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(myRoleNameRecyclerViewAdapter);

        }
        MyRoleNameRecyclerViewAdapter.ViewHolder viewHolder = myRoleNameRecyclerViewAdapter.new ViewHolder(binding);
        myRoleNameRecyclerViewAdapter.setOnItemClickListener(new MyRoleNameRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                dialog  = openDialog();
                department_role = dialog.findViewById(R.id.department_name);
                EditText password = dialog.findViewById(R.id.department_password);
                Button valid  = dialog.findViewById(R.id.valid);
                Button cancel  = dialog.findViewById(R.id.cancel);
                department_role.setText(myRoleNameRecyclerViewAdapter.getmValues().get(position).content);
                cancel.setOnClickListener(v->dialog.cancel());
                valid.setOnClickListener(v->{
                    update_role(MainActivity.userid, department_role.getText().toString() ,password.getText().toString(),retrofit);
                });
                dialog.show();
            }
        });
        return view;
    }


    public Dialog openDialog() {
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.select_role_dialog);
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Objects.requireNonNull(dialog.getWindow()).setLayout((width),  (height) / 7);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void update_role(String userid, String role, String password, Retrofit retrofitObject)
    {
        Dialog delaydialog = cmeet_delay.delaydialogCircular(getContext());
        delaydialog.show();
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<String> CreateUserCall = RR.update_user_role(userid,"Human Resource Head",password);
        CreateUserCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<String> call, @androidx.annotation.NonNull Response<String> response) {
                if(response.isSuccessful())
                {
                    delaydialog.cancel();
                    SharedPreferences sharedPreferences; SharedPreferences.Editor editor;
                    sharedPreferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("Role", department_role.getText().toString());
                    String answer = response.body();
                    Toast.makeText(getContext(), "success" +  answer, Toast.LENGTH_LONG).show();
                    editor.apply();
                    Toast.makeText(getContext(), sharedPreferences.getString("Role",""), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "response.code()", Toast.LENGTH_LONG).show();
                    System.out.println(response);
                    delaydialog.cancel();
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<String> call, @NonNull Throwable t) {
                //Toast.makeText(getContext(), R.string.Server_down, Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Failure: " + t, Toast.LENGTH_LONG).show();
                delaydialog.cancel();
            }
        });
    }


}