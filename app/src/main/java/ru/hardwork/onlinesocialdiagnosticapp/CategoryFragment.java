package ru.hardwork.onlinesocialdiagnosticapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.hardwork.onlinesocialdiagnosticapp.Model.Category;
import ru.hardwork.onlinesocialdiagnosticapp.Model.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.DiagnosticConverter;
import ru.hardwork.onlinesocialdiagnosticapp.holders.DiagnosticTestViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;

/**
 *
 */
public class CategoryFragment extends Fragment {
    // views
    View mFragment;
    TextView headerText, description;
    // Адаптер для загрузки категорий в CategoryViewHolder
    FirebaseRecyclerAdapter<DiagnosticTest, DiagnosticTestViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference categories;
    DatabaseReference diagnosticTests;
    ArrayList<String> arrayList = new ArrayList<>();
    // diagnosticRecycler's
    private TabLayout tabLayout;
    private RecyclerView mRecyclerView;
    private boolean isUserScrolling = false;
    private boolean isListGoingUp = true;
    private boolean tabSelected = true;

    private DiagnosticConverter converter = new DiagnosticConverter();

    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");
        diagnosticTests = database.getReference("DiagnosticTest");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_category, container, false);

        headerText = mFragment.findViewById(R.id.headerText);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.gilroy_bold);
        headerText.setTypeface(typeface);

        description = mFragment.findViewById(R.id.description);
        mRecyclerView = mFragment.findViewById(R.id.diagnosticTestsRecycler);
        //  TabLayout Code end
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());
        // animation
        final Context context = mRecyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
        mRecyclerView.setLayoutAnimation(controller);

        tabLayout = mFragment.findViewById(R.id.categoryTab);
        // Загружаем категории
        loadCategories();
        // Код для расстояния между вкладками
        int betweenSpace = 20;
        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }
        //  Code привязки start
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isUserScrolling = false;
                int position = tab.getPosition();

                int testPosition = 0;
                for (int i = 0; i < position; i++) {
                    testPosition += Common.categoryList.get(i).getCount();
                }
                if (tabSelected) {
                    mLayoutManager.scrollToPositionWithOffset(testPosition, 0);
                    //--- код для прокрутки
                    //psyTestsRecycler.smoothScrollToPosition(testPosition);
                } else {
                    tabSelected = true;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // Загружаем диагности инициализируем адаптер
        loadDiagnosticTests();
        // Слушатель скролла
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true;
                    if (isListGoingUp) {
                        if (mLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == arrayList.size()) {
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                            }, 50);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemPosition = mLayoutManager.findFirstVisibleItemPosition();
                DiagnosticTest diagnosticTest = Common.diagnosticTests.get(itemPosition);
                if (diagnosticTest != null) {
                    description.setText(diagnosticTest.getDescription());
                }
            }

            /* @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemPosition = mLayoutManager.findFirstVisibleItemPosition();

                final DiagnosticTest diagnosticTest = Common.diagnosticTests.get(itemPosition);
                Category category = Iterables.find(Common.categoryList, new Predicate<Category>() {
                    @Override
                    public boolean apply(@Nullable Category cat) {
                        return cat != null;// && diagnosticTest.getCategoryId() == cat.getId();
                    }
                });

                int currentCatPosition = Common.categoryList.indexOf(category);
                // Если скроллит пользователь сместим таб
                if (isUserScrolling) {
                    int tabPosition = tabLayout.getSelectedTabPosition();
                    if (currentCatPosition != tabPosition) {
                        TabLayout.Tab tab = tabLayout.getTabAt(currentCatPosition);
                        tabSelected = false;
                        assert tab != null;
                        tab.select();
                    }
                }
            }*/
        });

        return mFragment;
    }

    /**
     * цэ кiт (づ ◕‿◕ )づ
     */
    private void loadCategories() {
        categories.orderByChild("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Category category = data.getValue(Category.class);
                            Common.categoryList.add(category);
                            tabLayout.addTab(tabLayout.newTab().setText(category.getName()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(error.getMessage(), error.getDetails());
                    }
                });
    }

    /**
     * load "Categories" from Firebase
     */
    private void loadDiagnosticTests() {
        // Options for recycler view by Firebase
        FirebaseRecyclerOptions<DiagnosticTest> options = new FirebaseRecyclerOptions
                .Builder<DiagnosticTest>()
                .setQuery(diagnosticTests, DiagnosticTest.class)
                .build();
        // Create adapter
        adapter = new FirebaseRecyclerAdapter<DiagnosticTest, DiagnosticTestViewHolder>(options) {
            @NonNull
            @Override
            public DiagnosticTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.item_psytest_version_2, parent, false);

                return new DiagnosticTestViewHolder(view);
            }

            @SuppressLint({"SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            protected void onBindViewHolder(@NonNull final DiagnosticTestViewHolder holder, int position, @NonNull final DiagnosticTest model) {
                final int color = position % 5;
                Activity activity = getActivity();
                // Не обрабатываема ситуация
                if (activity == null) {
                    return;
                }
                // Добавим диагностику в список
                if (!Common.diagnosticTestsIds.contains(model.getDiagnosticTestId())) {
                    Common.diagnosticTestsIds.add(model.getDiagnosticTestId());
                    Common.diagnosticTests.add(model);
                }

                holder.setId(model.getDiagnosticTestId());

                holder.layout.setBackground(activity.getDrawable(Common.colors[color]));
                //
                holder.diagnosticName.setText(model.getName());
                holder.totalQuestion.setText("0/" + model.getQuestionCount());
                holder.totalTime.setText("Займет минут: " + model.getTestDuration());

                holder.setItemClickListener((view, position1, isLongClick) -> {
                    Common.diagnosticId = holder.getId();
                    diagnosticTests.orderByChild("diagnosticTestId").equalTo(holder.getId())
                            .addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        DiagnosticTest diagnostic = converter.apply(snapshot.getValue());

                                        Intent startDiagnostic = new Intent(getActivity(), Start.class);
                                        Bundle dataSend = new Bundle();
                                        int catId = (int) diagnostic.getCategoryId() - 1;
                                        Common.descPosition = Integer.parseInt(diagnostic.getMetricId());
                                        String catName = Common.categoryList.get(catId).getName();
                                        dataSend.putString("CAT_NAME", catName);
                                        dataSend.putString("DIAGNOSTIC_NAME", diagnostic.getName());
                                        dataSend.putString("DIAGNOSTIC_DESC", diagnostic.getDescription());
                                        dataSend.putInt("COLOR_NUM", (int) (diagnostic.getDiagnosticTestId() % 5));
                                        startDiagnostic.putExtras(dataSend);

                                        startActivity(startDiagnostic);
                                    } else {
                                        Toast.makeText(getContext(), "Диагностика не найдена", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                });
            }

        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.setAdapter(adapter);
    }
}