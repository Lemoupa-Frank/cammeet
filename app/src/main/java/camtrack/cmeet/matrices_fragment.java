package camtrack.cmeet;

import static camtrack.cmeet.activities.DatePickerFragment.startdatetemp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import camtrack.cmeet.activities.DatePickerFragment;
import camtrack.cmeet.activities.Events.MainActivityEventFragment;
import camtrack.cmeet.activities.Events.TableFragment;
import camtrack.cmeet.activities.Events.event_model;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityMatricesEventsBinding;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class matrices_fragment extends Fragment {

    String depart_name;
    DateTime Estartdate;
    LocalDate Lstartdate;
    DateTime Eenddate;

    LocalDate LEnddate;
    ActivityMatricesEventsBinding binding;
    Retrofit retrofit = Retrofit_Base_Class.getClient_String();

    public matrices_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatricesEventsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.departmentTextView.setText(depart_name);
        binding.attendeeDetailsShimmer.hideShimmer();
        binding.getMeetingShimmer.hideShimmer();
        binding.getMeetingsShimmer.hideShimmer();
        binding.meetingCountShimmer.hideShimmer();
        binding.absentshimmer.hideShimmer();
        binding.userDetailsShimmer.hideShimmer();
        TextView get_meetings;
        get_meetings = binding.getMeetings;

        binding.attendeeDetailsForMeeting.setOnClickListener(v->{
            Dialog dialog = dialog();
            Button startdate = dialog.findViewById(R.id.buttonstartdate);
            Button enddate = dialog.findViewById(R.id.buttonenddate);
            EditText meeting_id = dialog.findViewById(R.id.dialog_edit_text);
            Button cancel = dialog.findViewById(R.id.cancel);
            Button validate = dialog.findViewById(R.id.valid);
            startdate.setVisibility(View.GONE);
            enddate.setVisibility(View.GONE);
            meeting_id.setVisibility(View.VISIBLE);
            dialog.show();
            cancel.setOnClickListener(can -> {
                dialog.cancel();
            });
            validate.setOnClickListener(val -> {
                Retrofit rtb = Retrofit_Base_Class.getClient_JacksonConverterFactory();
                Request_Route RR = rtb.create(Request_Route.class);
                Call<List<UserMeetings>> attendance = RR.get_meeting_attendance(meeting_id.getText().toString());
                get_attendance(attendance);
                dialog.cancel();
            });
        });

        binding.Absentees.setOnClickListener(v ->
        {
            Dialog dialog = dialog();
            Button startdate = dialog.findViewById(R.id.buttonstartdate);
            Button enddate = dialog.findViewById(R.id.buttonenddate);
            EditText meeting_id = dialog.findViewById(R.id.dialog_edit_text);
            Button cancel = dialog.findViewById(R.id.cancel);
            Button validate = dialog.findViewById(R.id.valid);
            startdate.setVisibility(View.GONE);
            enddate.setVisibility(View.GONE);
            meeting_id.setVisibility(View.VISIBLE);
            dialog.show();
            cancel.setOnClickListener(can -> {
                dialog.cancel();
            });
            validate.setOnClickListener(val -> {
                Retrofit rtb = Retrofit_Base_Class.getClient();
                Request_Route RR = rtb.create(Request_Route.class);
                Call<List<User>> get_meeting_absentee = RR.get_meeting_absentee(meeting_id.getText().toString());
                get_meeting_absentee(get_meeting_absentee);
                dialog.cancel();
            });
        });

        get_meetings.setOnClickListener(v ->
        {
            Dialog dialog = dialog();
            dialog.show();
            Button startdate = dialog.findViewById(R.id.buttonstartdate);
            Button enddate = dialog.findViewById(R.id.buttonenddate);
            Button cancel = dialog.findViewById(R.id.cancel);
            Button validate = dialog.findViewById(R.id.valid);
            startdate.setOnClickListener(start -> {
                DatePickerFragment newFragment = new DatePickerFragment();
                TextView starttext = dialog.findViewById(R.id.textstartdate);
                newFragment.setListener((year, month, dayOfMonth) -> {
                    LocalDateTime dateTimes = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0);
                    Lstartdate = LocalDate.from(dateTimes);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String formattedDate = dateTimes.format(formatter);
                    setStartdatetemp(DateTime.parseRfc3339(formattedDate));
                    starttext.setText(Estartdate.toString());
                });
                newFragment.show(getChildFragmentManager(), "StartDate");
            });

            enddate.setOnClickListener(end -> {
                DatePickerFragment newFragment = new DatePickerFragment();
                TextView endtext = dialog.findViewById(R.id.textendtdate);
                newFragment.setListener((year, month, dayOfMonth) -> {

                    LocalDateTime dateTimes = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0);
                    LEnddate = LocalDate.from(dateTimes);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String formattedDate = dateTimes.format(formatter);
                    setEnddatetemp(DateTime.parseRfc3339(formattedDate));
                    endtext.setText(Eenddate.toString());
                });
                newFragment.show(getChildFragmentManager(), "EndDate");
            });

            cancel.setOnClickListener(can -> {
                dialog.cancel();
            });
            validate.setOnClickListener(val ->
            {
                Retrofit rtb = Retrofit_Base_Class.getClient();
                Request_Route RR = rtb.create(Request_Route.class);
                Call<List<event_model>> get_department_meets = RR.get_department_meets(depart_name, Lstartdate, LEnddate);
                get_department_meets(get_department_meets);
                dialog.cancel();
            });
        });

        binding.meetingCount.setOnClickListener(v -> {
            Dialog dialog = dialog();
            dialog.show();
            Button startdate = dialog.findViewById(R.id.buttonstartdate);
            Button enddate = dialog.findViewById(R.id.buttonenddate);
            Button cancel = dialog.findViewById(R.id.cancel);
            Button validate = dialog.findViewById(R.id.valid);
            startdate.setOnClickListener(start -> {
                DatePickerFragment newFragment = new DatePickerFragment();
                TextView starttext = dialog.findViewById(R.id.textstartdate);
                newFragment.setListener((year, month, dayOfMonth) -> {
                    LocalDateTime dateTimes = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0);
                    Lstartdate = LocalDate.from(dateTimes);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String formattedDate = dateTimes.format(formatter);
                    setStartdatetemp(DateTime.parseRfc3339(formattedDate));
                    starttext.setText(Estartdate.toString());
                });
                newFragment.show(getChildFragmentManager(), "StartDate");
            });

            enddate.setOnClickListener(end -> {
                DatePickerFragment newFragment = new DatePickerFragment();
                TextView endtext = dialog.findViewById(R.id.textendtdate);
                newFragment.setListener((year, month, dayOfMonth) -> {

                    LocalDateTime dateTimes = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0);
                    LEnddate = LocalDate.from(dateTimes);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String formattedDate = dateTimes.format(formatter);
                    setEnddatetemp(DateTime.parseRfc3339(formattedDate));
                    endtext.setText(Eenddate.toString());
                });
                newFragment.show(getChildFragmentManager(), "EndDate");
            });

            cancel.setOnClickListener(can -> {
                dialog.cancel();
            });

            validate.setOnClickListener(val -> {
                Request_Route RR = retrofit.create(Request_Route.class);
                Call<String> get_count = RR.number_of_meets(depart_name, Lstartdate, LEnddate);
                get_count(get_count);
                dialog.cancel();
            });
        });

        binding.getMeeting.setOnClickListener(v->{
            Dialog dialog = dialog();
            Button startdate = dialog.findViewById(R.id.buttonstartdate);
            Button enddate = dialog.findViewById(R.id.buttonenddate);
            EditText meeting_id = dialog.findViewById(R.id.dialog_edit_text);
            Button cancel = dialog.findViewById(R.id.cancel);
            Button validate = dialog.findViewById(R.id.valid);
            startdate.setVisibility(View.GONE);
            enddate.setVisibility(View.GONE);
            meeting_id.setVisibility(View.VISIBLE);
            dialog.show();

            cancel.setOnClickListener(can -> {
                dialog.cancel();
            });
            validate.setOnClickListener(val ->
            {
                Retrofit rtb = Retrofit_Base_Class.getClient();
                Request_Route RR = rtb.create(Request_Route.class);
                Call<event_model> get_meeting = RR.get_meeting(meeting_id.getText().toString());
                get_meeting(get_meeting);
                dialog.cancel();
            });
        });


        return view;
    }

    public void setDepart_name(String depart_name) {
        this.depart_name = depart_name;
    }

    public Dialog dialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog);
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Objects.requireNonNull(dialog.getWindow()).setLayout(5 * (width) / 7, 3 * (height) / 4);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void get_count(Call<String> get_count) {
        binding.meetingCountShimmer.showShimmer(true);
        get_count.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<String> call, @androidx.annotation.NonNull Response<String> response) {
                binding.meetingCountShimmer.hideShimmer();
                if (response.isSuccessful()) {
                    String answer = response.body();
                    if (response.body() != null) {
                        TextView count = binding.numberOfMeetings;
                        count.setVisibility(View.VISIBLE);
                        count.setText(answer);
                    }
                } else {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    System.out.println(response);
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t, Toast.LENGTH_LONG).show();
                binding.meetingCountShimmer.hideShimmer();
            }
        });
    }

    public void get_department_meets(Call<List<event_model>> get_department_meets) {
        binding.getMeetingsShimmer.showShimmer(true);
        get_department_meets.enqueue(new Callback<List<event_model>>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<List<event_model>> call, @androidx.annotation.NonNull Response<List<event_model>> response) {
                binding.getMeetingsShimmer.hideShimmer();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        FrameLayout frame = binding.allMeetingsForPeriod;
                        frame.setVisibility(View.VISIBLE);
                        MainActivityEventFragment mainActivityEventFragment = new MainActivityEventFragment();
                        mainActivityEventFragment.cmeet_list = response.body();
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.all_meetings_for_period, mainActivityEventFragment)
                                .commit();
                    }
                } else {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    System.out.println(response);
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<List<event_model>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t, Toast.LENGTH_LONG).show();
                binding.getMeetingsShimmer.hideShimmer();
            }
        });
    }

    public void get_meeting_absentee(Call<List<User>> get_meeting_absentee) {
        binding.absentshimmer.showShimmer(true);
        get_meeting_absentee.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<List<User>> call, @androidx.annotation.NonNull Response<List<User>> response) {
                binding.absentshimmer.hideShimmer();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<String> user_name = new ArrayList<>();
                        for (User us : response.body()
                        ) {
                            user_name.add(us.getUserId());
                        }
                        if (response.body().size() > 0) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), R.layout.text_layout, user_name);
                            ListView listView = binding.absenteeList;
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapter);
                        }
                        else {
                            Toast.makeText(getContext(), "pas de absent pour cette reunion", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    System.out.println(response);
                                    }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t, Toast.LENGTH_LONG).show();
                binding.absentshimmer.hideShimmer();
            }
        });
    }

    public void get_meeting(Call<event_model> get_meeting_absentee) {
        binding.getMeetingShimmer.showShimmer(true);
        get_meeting_absentee.enqueue(new Callback<event_model>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<event_model> call, @androidx.annotation.NonNull Response<event_model> response) {
                binding.getMeetingShimmer.hideShimmer();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        FrameLayout frame = binding.obtainAMeeting;
                        List<event_model> em = new ArrayList<>();
                        em.add(response.body());
                        frame.setVisibility(View.VISIBLE);
                        MainActivityEventFragment mainActivityEventFragment = new MainActivityEventFragment();
                        mainActivityEventFragment.cmeet_list = em;
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.obtain_a_meeting, mainActivityEventFragment)
                                .commit();
                    }
                } else {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    System.out.println(response);
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<event_model> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t, Toast.LENGTH_LONG).show();
                binding.getMeetingShimmer.hideShimmer();
            }
        });
    }

    public void get_attendance(Call<List<UserMeetings>> get_attendance) {
        binding.attendeeDetailsShimmer.showShimmer(true);
        get_attendance.enqueue(new Callback<List<UserMeetings>>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<List<UserMeetings>> call, @androidx.annotation.NonNull Response<List<UserMeetings>> response) {
                binding.attendeeDetailsShimmer.hideShimmer();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                       String[] attendee_name = new String[response.body().size()];
                       String[] attendee_email = new String[response.body().size()];
                       String[] role =  new String[response.body().size()];
                       List<byte[]> signature = new ArrayList<>();
                       int i = 0;
                       for(UserMeetings um : response.body())
                       {
                           attendee_name[i] = TableFragment.extractNameFromEmail(um.getUserMeetingsPK().getUserId());
                           attendee_email[i] = um.getUserMeetingsPK().getUserId();
                           role[i] = um.getRole();
                           signature.add(um.getSignature_data());
                           i = i+1;

                       }
                        attendance_table_fragment tableFragment = new attendance_table_fragment();
                        tableFragment.setAttendee_email(attendee_email); tableFragment.setAttendee_names(attendee_name);
                        tableFragment.setNumberofaatendees(response.body().size()); tableFragment.setRole(role);
                        tableFragment.setAttendee_signature(signature);
                                getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.attendance_details_frame, tableFragment)
                                .commit();
                        binding.attendanceDetailsFrame.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    System.out.println(response);
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<List<UserMeetings>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t, Toast.LENGTH_LONG).show();
                binding.attendeeDetailsShimmer.hideShimmer();
            }
        });
    }

    public DateTime getStartdatetemp() {
        return startdatetemp;
    }

    public void setStartdatetemp(DateTime startdatetemp) {
        this.Estartdate = startdatetemp;
    }

    public DateTime getEnddatetemp() {
        return Eenddate;
    }

    public void setEnddatetemp(DateTime enddatetemp) {
        this.Eenddate = enddatetemp;
    }

}