package ru.hardwork.onlinesocialdiagnosticapp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.holders.InviteViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.firebase.Invite;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.SpeedyLinearLayoutManager;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.ViewAnimation;
import ru.hardwork.onlinesocialdiagnosticapp.ui.activity.StartActivity;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static ru.hardwork.onlinesocialdiagnosticapp.common.Common.categoryList;

/**
 * This snippet is for displaying and creating invitations
 */
public class GroupFragment extends Fragment {
    private static final String INVITE = "invite";

    private DatabaseReference invites;
    private DatabaseReference ref;

    private FirebaseRecyclerAdapter<Invite, InviteViewHolder> adapter;
    private RecyclerView mRecyclerView;
    FloatingActionButton addButton, fabJoin, fabInvite;
    boolean isRotate = false;

    /**
     * Required empty public constructor
     */
    public GroupFragment() {

    }


    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connection initialization
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        invites = database.getReference(INVITE);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        fabInvite = view.findViewById(R.id.fabInvite);
        ViewAnimation.init(fabInvite);
        fabInvite.setOnClickListener(v -> {
            // this function is available only to authorized users
            if (Common.firebaseUser == null) {
                Toast.makeText(getActivity(), "Необходимо авторизоваться для создания приглашений", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> data = new ArrayList<>();
            for (DiagnosticTest diagnostic : Common.diagnosticTests) {
                data.add(diagnostic.getName());
            }

            @SuppressLint("InflateParams")
            View addDialog = inflater.inflate(R.layout.create_invite_layout, null);
            MaterialEditText aliasText = addDialog.findViewById(R.id.aliasText);
            Spinner diagnosticSpinner = addDialog.findViewById(R.id.diagnosticSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            diagnosticSpinner.setAdapter(adapter);
            diagnosticSpinner.setSelection(0);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
            builder.setNegativeButton("ОТМЕНА", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setPositiveButton("СОЗДАТЬ", (dialogInterface, i) -> {
                if (isNotEmpty(aliasText.getText())) {
                    DatabaseReference inviteRef = ref.child(INVITE);
                    DatabaseReference pushedInviteRef = inviteRef.push();
                    // Get the unique ID generated by a push()
                    String id = pushedInviteRef.getKey();
                    Invite invite = new Invite(id, Common.firebaseUser.getUid(), diagnosticSpinner.getSelectedItemPosition(), aliasText.getText().toString(), new Date());
                    pushedInviteRef.setValue(invite, (databaseError, databaseReference) -> {
                        if (databaseError != null) {
                            Toast.makeText(getActivity(), format("Ошибка сохранения %s, попробуйте повторить попытку", databaseError.getMessage()), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), format("Приглашение %s успешно создано", aliasText.getText()), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Необходимо задать псевдоним", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setTitle("Создание приглашения");
            builder.setView(addDialog);
            builder.show();
        });

        fabJoin = view.findViewById(R.id.fabJoin);
        ViewAnimation.init(fabJoin);
        fabJoin.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
            @SuppressLint("InflateParams")
            View addDialog = inflater.inflate(R.layout.join_invite_layout, null);
            MaterialEditText joinIdText = addDialog.findViewById(R.id.joinIdText);
            builder.setPositiveButton("ВОЙТИ", (dialogInterface, i) -> {
                if (joinIdText.getText() == null || isEmpty(joinIdText.getText().toString())) {
                    Toast.makeText(getActivity(), "Необходимо задать идентификатор", Toast.LENGTH_SHORT).show();
                    return;
                }

                Query query = invites.orderByKey().equalTo(joinIdText.getText().toString());
                MutableBoolean key = new MutableBoolean();
                key.setFalse();
                query.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (key.getValue()) {
                            return;
                        } else {
                            key.setTrue();
                        }

                        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                        if (!iterator.hasNext()) {
                            Toast.makeText(getActivity(), format("Не найдено приглашение с идентификатором %s", joinIdText.getText().toString()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DataSnapshot firstChild = iterator.next();
                        Invite joinInvite = firstChild.getValue(Invite.class);
                        if (joinInvite == null) {
                            Toast.makeText(getActivity(), format("Ошибка получения приглашения %s", joinIdText.getText().toString()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DiagnosticTest diagnostic = Common.diagnosticTests.get(joinInvite.getDiagnosticId());
                        Intent startIntent = new Intent(getActivity(), StartActivity.class);
                        Bundle dataSend = new Bundle();
                        int catId = (int) diagnostic.getCategoryId() - 1;
                        String catName = categoryList.get(catId).getName();
                        dataSend.putSerializable("DIAGNOSTIC", diagnostic);
                        dataSend.putString("CAT_NAME", catName);
                        dataSend.putString("INVITE", joinInvite.getId());
                        startIntent.putExtras(dataSend);
                        startActivity(startIntent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (error.getCode() != 0) {
                            Toast.makeText(getActivity(), format("Ошибка сохранения %s, попробуйте повторить попытку", error.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });
            builder.setTitle("Введите идентификатор приглашения");
            builder.setView(addDialog);
            builder.show();
        });

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            isRotate = ViewAnimation.rotateFab(v, !isRotate);
            if (isRotate) {
                ViewAnimation.showIn(fabJoin);
                ViewAnimation.showIn(fabInvite);
            } else {
                ViewAnimation.showOut(fabJoin);
                ViewAnimation.showOut(fabInvite);
            }
        });

        mRecyclerView = view.findViewById(R.id.inviteRV);
        final SpeedyLinearLayoutManager mLayoutManager = new SpeedyLinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());

        String uid = Common.firebaseUser != null ? Common.firebaseUser.getUid() : "";
        // Options for recycler view by Firebase
        FirebaseRecyclerOptions<Invite> options = new FirebaseRecyclerOptions
                .Builder<Invite>()
                .setQuery(invites.orderByChild("auth").equalTo(uid), Invite.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Invite, InviteViewHolder>(options) {

            @NonNull
            @Override
            public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.item_invite, parent, false);

                return new InviteViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull InviteViewHolder holder, int position, @NonNull Invite model) {

            }
        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.setAdapter(adapter);


        return view;
    }
}