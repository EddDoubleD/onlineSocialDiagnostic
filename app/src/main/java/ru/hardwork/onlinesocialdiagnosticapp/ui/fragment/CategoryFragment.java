package ru.hardwork.onlinesocialdiagnosticapp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.Iterables;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.DiagnosticConverter;
import ru.hardwork.onlinesocialdiagnosticapp.common.UIDataUtils;
import ru.hardwork.onlinesocialdiagnosticapp.holders.DiagnosticTestViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Category;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.SpeedyLinearLayoutManager;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;
import ru.hardwork.onlinesocialdiagnosticapp.ui.activity.Start;

import static ru.hardwork.onlinesocialdiagnosticapp.common.Common.categoryList;

/**
 *
 */
public class CategoryFragment extends Fragment {
    // viewseditor
    View mFragment;
    TextView headerText, description;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_category, container, false);

        headerText = mFragment.findViewById(R.id.headerText);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.gilroy_bold);
        headerText.setTypeface(typeface);

        description = mFragment.findViewById(R.id.description);
        //
        if (CollectionUtils.isEmpty(Common.diagnosticTests)) {
            UIDataUtils.init(getResources());
        }

        description.setText(Common.diagnosticTests.get(0).getDescription());

        mRecyclerView = mFragment.findViewById(R.id.diagnosticTestsRecycler);
        //  TabLayout Code end
        final SpeedyLinearLayoutManager mLayoutManager = new SpeedyLinearLayoutManager(
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
        loadCat();
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
                    testPosition += categoryList.get(i).getCount();
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
                        if (mLayoutManager.findLastCompletelyVisibleItemPosition() == -1) {
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

                if (itemPosition == mLayoutManager.findFirstVisibleItemPosition()) {
                    return;
                }

                itemPosition = mLayoutManager.findFirstVisibleItemPosition();

                final DiagnosticTest diagnosticTest = Common.diagnosticTests.get(itemPosition);
                if (diagnosticTest != null) {
                    description.setText(diagnosticTest.getDescription());
                }

                Category category = Iterables.find(Common.categoryList, cat -> cat != null && (diagnosticTest != null ? diagnosticTest.getCategoryId() : 0) == cat.getCategoryId());

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
            }


        });

        return mFragment;
    }

    private int itemPosition;

    /**
     * цэ кiт (づ ◕‿◕ )づ
     */
    private void loadCat() {
        for (Category category : categoryList) {
            tabLayout.addTab(tabLayout.newTab().setText(category.getName()));
        }
    }

    /**
     * load "Categories" from Firebase
     */
    private void loadDiagnosticTests() {
        DiagnosticTestAdapter adapter = new DiagnosticTestAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.scheduleLayoutAnimation();
    }

    public class DiagnosticTestAdapter extends RecyclerView.Adapter<DiagnosticTestViewHolder> {

        @NonNull
        @Override
        public DiagnosticTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_psytest_version_2, parent, false);

            return new DiagnosticTestViewHolder(view);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull DiagnosticTestViewHolder holder, int position) {
            final int color = position % 5;
            Activity activity = getActivity();
            // Не обрабатываема ситуация
            if (activity == null) {
                return;
            }
            // Достаем модельку теста
            DiagnosticTest model = Common.diagnosticTests.get(position);
            holder.setId(model.getId());
            // Раскрашиваем форму теста
            @SuppressLint("UseCompatLoadingForDrawables")
            Drawable drawable = activity.getDrawable(Common.shapes[color]);
            holder.layout.setBackground(drawable);
            //
            holder.diagnosticName.setText(model.getName());
            holder.totalQuestion.setText("0/" + model.getQuestionCount());
            holder.totalTime.setText("Займет минут: " + model.getTestDuration());
            //
            holder.setItemClickListener((v, p, longClick) -> {
                DiagnosticTest diagnostic = Common.diagnosticTests.get(p);
                Intent startDiagnostic = new Intent(getActivity(), Start.class);
                Bundle dataSend = new Bundle();
                int catId = (int) diagnostic.getCategoryId() - 1;
                Common.descPosition = diagnostic.getMetricId();
                String catName = categoryList.get(catId).getName();
                dataSend.putInt("DIAGNOSTIC_ID", diagnostic.getId());
                dataSend.putString("CAT_NAME", catName);
                dataSend.putString("DIAGNOSTIC_NAME", diagnostic.getName());
                dataSend.putString("DIAGNOSTIC_DESC", diagnostic.getFullDescription());
                dataSend.putInt("COLOR_NUM", diagnostic.getId() % 5);
                startDiagnostic.putExtras(dataSend);

                startActivity(startDiagnostic);
            });

        }

        @Override
        public int getItemCount() {
            return Common.diagnosticTests.size();
        }
    }
}