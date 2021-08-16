package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;
import com.perajuritTeknologi.bizbangkit.event.ProfileScrolled;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CardStyleFragment extends Fragment implements CardStackListener {
    private View root;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;
    private BusinessCardAdapter businessCardAdapter;
    private ImageButton swipeLeftBtn, swipeRightBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover_cardlayout, container, false);
        Log.d("ShenYien", "entered cards");
        setUpComponents();
        initialize();
        setUpSwipeButtons();
        return root;
    }

    private void setUpComponents() {
        cardStackView = root.findViewById(R.id.discover_business_card_holder);
        cardStackLayoutManager = new CardStackLayoutManager(root.getContext(), this);
        businessCardAdapter = new BusinessCardAdapter(tmp());
        swipeLeftBtn = root.findViewById(R.id.swipe_left_btn);
        swipeRightBtn = root.findViewById(R.id.swipe_right_btn);
    }

    private void initialize() {
        cardStackLayoutManager.setStackFrom(StackFrom.None);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setTranslationInterval(8.0f);
        cardStackLayoutManager.setScaleInterval(0.95f);
        cardStackLayoutManager.setSwipeThreshold(0.3f);
        cardStackLayoutManager.setMaxDegree(20.0f);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollHorizontal(true);
        cardStackLayoutManager.setCanScrollVertical(true);
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        cardStackView.setLayoutManager(cardStackLayoutManager);
        cardStackView.setAdapter(businessCardAdapter);
    }

    private ArrayList<DataStructure.SimpleBusiness> tmp() {
        ArrayList<DataStructure.SimpleBusiness> list = new ArrayList();
        DataStructure.SimpleBusiness temp = new DataStructure.SimpleBusiness();
        temp.businessId = 9;
        for (int i = 0; i < 50; i++) {
            list.add(temp);
        }
        return list;
    }

    private void setUpSwipeButtons() {
        TooltipCompat.setTooltipText(swipeLeftBtn, "Dismiss");
        TooltipCompat.setTooltipText(swipeRightBtn, "View");
        swipeLeftBtn.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder().
                    setDirection(Direction.Left).
                    setDuration(Duration.Slow.duration).build();
            cardStackLayoutManager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        swipeRightBtn.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder().
                    setDirection(Direction.Right).
                    setDuration(Duration.Slow.duration).build();
            cardStackLayoutManager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (direction.equals(Direction.Right)) {
            int position = cardStackLayoutManager.getTopPosition();
            DataStructure.SimpleBusiness currentBusiness = businessCardAdapter.businesses.get(position);
            EventBus.getDefault().post(new EnterBusinessDetail(currentBusiness.businessId));
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
