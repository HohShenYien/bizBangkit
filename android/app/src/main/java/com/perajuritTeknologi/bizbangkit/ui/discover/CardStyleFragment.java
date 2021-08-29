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

import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;
import com.perajuritTeknologi.bizbangkit.event.GetBusinessListEvent;
import com.perajuritTeknologi.bizbangkit.event.ImageEvent;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class CardStyleFragment extends Fragment implements CardStackListener {
    private View root;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;
    private BusinessCardAdapter businessCardAdapter;
    private ImageButton swipeLeftBtn, swipeRightBtn;
    private ArrayList<DataStructure.SimpleBusiness> businesses;
    private boolean endOfList; // set to true when arraylist returned is smaller than size expected
    private int imgInd; // to get the images for the data

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover_cardlayout, container, false);

        imgInd = 0;
        endOfList = false;
        businesses = new ArrayList<>();

        setUpComponents();
        initialize();
        setUpSwipeButtons();
        getNewContent();

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    private void setUpComponents() {
        cardStackView = root.findViewById(R.id.discover_business_card_holder);
        cardStackLayoutManager = new CardStackLayoutManager(root.getContext(), this);
        businessCardAdapter = new BusinessCardAdapter();
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
            int position = cardStackLayoutManager.getTopPosition() - 1;
            DataStructure.SimpleBusiness currentBusiness = businessCardAdapter.businesses.get(position);
            EventBus.getDefault().post(new EnterBusinessDetail(currentBusiness.businessId));
        }

        if(!endOfList && cardStackLayoutManager.getTopPosition() > businesses.size() - 6) {
            getNewContent();
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

    private void getNewContent() {
        APICaller.getBusinessList(businesses.size(), 20);
    }

    private void addContentsToAdapter(ArrayList<DataStructure.SimpleBusiness> newBusinesses) {
        businesses.addAll(newBusinesses);
        if (imgInd == businesses.size() - newBusinesses.size()) {
            getBusinessLogo();
        }
        businessCardAdapter.setItems(businesses);
        businessCardAdapter.notifyDataSetChanged();
        if (newBusinesses.size() < 20) {
            endOfList = true;
        }
    }

    @Subscribe
    public void handleNewContents(GetBusinessListEvent event) {
        addContentsToAdapter(event.businesses);
    }

    @Subscribe
    public void handleLogos(ImageEvent event) {
        if (event.event_id.compareTo("CardLogos") == 0) {
            businesses.get(imgInd).logo = event.image.image;
            businessCardAdapter.setItems(businesses);
            businessCardAdapter.notifyDataSetChanged();
            imgInd++;
            getBusinessLogo();
        }
    }

    private void getBusinessLogo() {
        while (imgInd < businesses.size()) {
            if (businesses.get(imgInd).logoPath.startsWith("./files/")) {
                APICaller.getImg(businesses.get(imgInd).logoPath, imgInd + "a", "CardLogos");
                break;
            }
            imgInd++;
        }
    }
}
