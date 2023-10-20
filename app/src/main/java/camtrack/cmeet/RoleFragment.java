package camtrack.cmeet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import camtrack.cmeet.databinding.FragmentRoleBinding;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import camtrack.cmeet.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class RoleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    Spinner Department_role_Spinner;

    Dialog dialog;

    FragmentRoleBinding binding;

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
                TextView department = dialog.findViewById(R.id.department_name);
                Button valid  = dialog.findViewById(R.id.valid);
                Button cancel  = dialog.findViewById(R.id.cancel);
                department.setText(myRoleNameRecyclerViewAdapter.getmValues().get(position).content);
                cancel.setOnClickListener(v->dialog.cancel());
                valid.setOnClickListener(v->{});
                dialog.show();
            }
        });
        return view;
    }


    public Dialog openDialog() {
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.select_role_dialog);
        Department_role_Spinner = dialog.findViewById(R.id.department_role);
        set_spinner(Department_role_Spinner, getContext(), R.array.Department_Role);
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Objects.requireNonNull(dialog.getWindow()).setLayout((width), 3 * (height) / 7);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    public void set_spinner(Spinner spinner, Context context, int array_resource_value)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                array_resource_value, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}