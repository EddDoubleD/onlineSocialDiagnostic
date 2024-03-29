package ru.hardwork.onlinesocialdiagnosticapp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.Splitter;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.holders.InviteSwipeViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.firebase.Invite;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.SpeedyLinearLayoutManager;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.ViewAnimation;
import ru.hardwork.onlinesocialdiagnosticapp.ui.activity.DoneActivity;
import ru.hardwork.onlinesocialdiagnosticapp.ui.activity.StartActivity;
import ru.hardwork.onlinesocialdiagnosticapp.ui.dialogs.InviteDialog;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static ru.hardwork.onlinesocialdiagnosticapp.common.Common.categoryList;

/**
 * This snippet is for displaying and creating invitations
 */
public class GroupFragment extends Fragment {
    private static final String INVITE = "invite";
    boolean isRotate = false;
    // ui
    private RecyclerView mRecyclerView;
    private FloatingActionButton addButton, fabJoin, fabInvite;
    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference invites;
    private FirebaseRecyclerOptions<Invite> options;
    private FirebaseRecyclerAdapter<Invite, InviteSwipeViewHolder> adapter;

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

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connection initialization
        database = FirebaseDatabase.getInstance();
        invites = database.getReference(INVITE);
        String uid = Common.firebaseUser != null ? Common.firebaseUser.getUid() : "";
        // Options for recycler view by Firebase
        options = new FirebaseRecyclerOptions
                .Builder<Invite>()
                .setQuery(database.getReference(INVITE).orderByChild("auth").equalTo(uid), Invite.class)
                .build();
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

            @SuppressLint("InflateParams")
            View addDialog = inflater.inflate(R.layout.create_invite_layout, null);
            final InviteDialog dialog = new InviteDialog(addDialog, null, null);
            dialog.setPositiveButtonListener(null, (dialogInterface, i) -> {
                if (dialog.validate()) {
                    DatabaseReference inviteRef = database.getReference().child(INVITE);
                    DatabaseReference pushedInviteRef = inviteRef.push();
                    // Get the unique ID generated by a push()
                    String id = pushedInviteRef.getKey();
                    Invite invite = new Invite(id, Common.firebaseUser.getUid(), dialog.spinner.getSelectedItemPosition(), dialog.aliasText.getText().toString(), new Date());
                    pushedInviteRef.setValue(invite, (databaseError, databaseReference) -> {
                        if (databaseError != null) {
                            Toast.makeText(getActivity(), format("Ошибка сохранения %s, попробуйте повторить попытку", databaseError.getMessage()), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), format("Приглашение %s успешно создано", dialog.aliasText.getText()), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Необходимо задать псевдоним", Toast.LENGTH_SHORT).show();
                }
            }).show();
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

        adapter = new FirebaseRecyclerAdapter<Invite, InviteSwipeViewHolder>(options) {

            @NonNull
            @Override
            public InviteSwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.item_swipe_invite, parent, false);

                return new InviteSwipeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull InviteSwipeViewHolder holder, int position, @NonNull Invite model) {
                Activity activity = getActivity();
                // Не обрабатываема ситуация
                if (activity == null) {
                    return;
                }
                DiagnosticTest diagnostic = Common.diagnosticTests.get(model.getDiagnosticId());
                holder.aliasText.setText(model.getAlias());
                holder.diagnosticNameText.setText(diagnostic.getName());
                final int color = model.getDiagnosticId() % 5;
                @SuppressLint("UseCompatLoadingForDrawables")
                Drawable drawable = activity.getDrawable(Common.shapes[color]);
                holder.mainLayout.setBackground(drawable);

                holder.aliasText.setOnClickListener(v -> {
                    if (CollectionUtils.isEmpty(model.getResults())) {
                        Toast.makeText(getActivity(), "Нет результатов", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String answers = model.getResults().get(0).getAnswers();
                    ArrayList<Integer> result = new ArrayList<>();
                    for (String s : Splitter.on(",").trimResults().splitToList(answers.replace("[", "").replace("]", ""))) {
                        result.add(Integer.parseInt(s));
                    }
                    Intent done = new Intent(getContext(), DoneActivity.class);
                    Bundle dataSend = new Bundle();
                    dataSend.putIntegerArrayList("RESULT", result);
                    dataSend.putSerializable("DIAGNOSTIC", diagnostic);
                    done.putExtras(dataSend);
                    startActivity(done);
                });

                holder.sharedButton.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Your Subject");
                    i.putExtra(Intent.EXTRA_TEXT, model.getId());
                    startActivity(Intent.createChooser(i, "choose one"));
                });

                holder.editButton.setOnClickListener(v -> {
                    @SuppressLint("InflateParams")
                    View addDialog = inflater.inflate(R.layout.create_invite_layout, null);

                    final InviteDialog dialog = new InviteDialog(addDialog, null, null);
                    dialog.aliasText.setText(model.getAlias());
                    dialog.spinner.setSelection(model.getDiagnosticId());
                    dialog.setPositiveButtonListener("Редактировать", (dialogInterface, i) -> {
                        if (dialog.validate()) {
                            ObservableSnapshotArray<Invite> invites = adapter.getSnapshots();
                            int inv = 0;
                            for (int j = 0; j < adapter.getItemCount(); j++) {
                                Invite invite = invites.get(j);
                                if (model.getId().equals(invite.getId())) {
                                    inv = j;
                                    break;
                                }
                            }

                            DatabaseReference inviteRef = adapter.getRef(inv);
                            Map<String, Object> upd = new HashMap<>();
                            upd.put("diagnosticId", dialog.spinner.getSelectedItemPosition());
                            upd.put("alias", dialog.aliasText.getText().toString());
                            inviteRef.updateChildren(upd, (error, ref) -> {
                                if (error == null || error.getCode() == 0) {
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Запись отредактирована", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Не удалось обновить", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).show();

                });

                holder.deleteButton.setOnClickListener(v -> {
                    ObservableSnapshotArray<Invite> invites = adapter.getSnapshots();
                    int inv = 0;
                    for (int j = 0; j < adapter.getItemCount(); j++) {
                        Invite invite = invites.get(j);
                        if (model.getId().equals(invite.getId())) {
                            inv = j;
                            break;
                        }
                    }

                    DatabaseReference inviteRef = adapter.getRef(inv);
                    inviteRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(getActivity(), "Запись успешно удалена", Toast.LENGTH_SHORT).show();
                });
            }
        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.setAdapter(adapter);

        return view;
    }
}