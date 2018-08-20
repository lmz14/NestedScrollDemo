package com.lmz.viewdemo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.lmz.viewdemo.adapter.SearchNestedScrollingFragmentAdapter;
import com.lmz.viewdemo.fragment.SearchNestedScrollingDemoFragment;
import com.lmz.viewdemo.iface.AppBarLayoutObserved;
import com.lmz.viewdemo.view.MyCoordinatorLayout;
import com.lmz.viewdemo.view.XRefreshLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author lmz14
 * @date 2018.8.20
 */
public class ScaleSearchNestedViewPagerActivity extends AppCompatActivity implements AppBarLayoutObserved {

    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;

    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;

    @BindView(R.id.rlSearchBarCover)
    RelativeLayout rlSearchBarCover;
    @BindView(R.id.rlSearchBar)
    RelativeLayout rlSearchBar;
    @BindView(R.id.tvSearchContentCover)
    TextView tvSearchContentCover;
    @BindView(R.id.tvSearchBtnCover)
    TextView tvSearchBtnCover;
    @BindView(R.id.rlSearchBarCoverLayout)
    RelativeLayout rlSearchBarCoverLayout;
    @BindView(R.id.ivHeadCoverBg)
    ImageView ivSearchBarCoverBg;

    @BindView(R.id.refreshview)
    XRefreshView refreshview;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.coordinatorLayout)
    MyCoordinatorLayout coordinatorLayout;

    private XRefreshLayout xRefreshLayout;
    private int headHeight;
    private int maxHeadTopHeight,minHeadTopHeight,maxLimit,minLimit,maxMarginBottom,marginLeft;
    private int searchBarHeight,textSize;
    private int searchBarScale,textScale,marginBottomScale;
    private int maxOffset;
    private int mStatus = STATUS_EXPANDED;//默认展开状态

    private List<SearchNestedScrollingDemoFragment> fragments;
    private SearchNestedScrollingFragmentAdapter fragmentAdapter;
    private List<String> tabs;
    private int tabCount = 4;
    private int lastVerticalOffset=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nested_viewpager);
        ButterKnife.bind(this);
        preData();
        initView();
        initData();
        initListener();
    }

    private void preData(){
        tabs = new ArrayList<>();
        tabs.add("精选");
        tabs.add("女装");
        tabs.add("男装");
        tabs.add("鞋包");
        fragments = new ArrayList<>();
        for(int i=0;i<tabCount;i++){
            SearchNestedScrollingDemoFragment fragment = new SearchNestedScrollingDemoFragment();
            fragment.setIndex(i);
            fragments.add(fragment);
        }
    }

    private void initView() {
        xRefreshLayout = new XRefreshLayout(this);
        refreshview.setCustomHeaderView(xRefreshLayout);
        refreshview.setPinnedContent(true);
        refreshview.setIsFixHeaderView(true, (int) this.getResources().getDimension(R.dimen.search_home_pull_refresh_largest_height));

        fragmentAdapter = new SearchNestedScrollingFragmentAdapter(getSupportFragmentManager(),fragments);
        viewpager.setAdapter(fragmentAdapter);
        coordinatorLayout.setCurrentScrollableContainer(fragments.get(0));
        smartTabLayout.setCustomTabView(new MyTabProvider());
        smartTabLayout.setViewPager(viewpager);
    }

    private void initData(){
        headHeight = (int) this.getResources().getDimension(R.dimen.headHeight);
        minHeadTopHeight = (int) this.getResources().getDimension(R.dimen.minHeadTopHeight);
        maxHeadTopHeight = (int) this.getResources().getDimension(R.dimen.maxHeadTopHeight);
        maxOffset = maxHeadTopHeight - minHeadTopHeight;
        searchBarHeight = (int) this.getResources().getDimension(R.dimen.searchBarHiight);
        searchBarScale = (int) this.getResources().getDimension(R.dimen.searchBarScale);
        textSize = (int) this.getResources().getDimension(R.dimen.textSize);
        textScale = (int) this.getResources().getDimension(R.dimen.textScale);
        maxMarginBottom = (int) this.getResources().getDimension(R.dimen.maxMarginBottom);
        marginBottomScale = (int) this.getResources().getDimension(R.dimen.marginBottomScale);
        marginLeft = (int) this.getResources().getDimension(R.dimen.marginLeft);
        maxLimit = (int) this.getResources().getDimension(R.dimen.maxLimit);
        minLimit = (int) this.getResources().getDimension(R.dimen.minLimit);
    }

    private void initListener(){
        refreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh() {
                super.onRefresh();
                refreshview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshview.stopRefresh();
                    }
                },1000);
            }

            @Override
            public void onHeaderMove(double offset, int offsetY) {
                super.onHeaderMove(offset, offsetY);
                //offset:移动距离和headerview高度的比例。范围是0~1，0：headerview全然没显示 1：headerview全然显示
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
            }
        });

        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset 向上滑动得到的值是负的，初始值为0 就是展开状态
                int h = headHeight + verticalOffset ;//剩下未滑出屏幕的高度
                if(verticalOffset == 0){
                    //展开状态
                    mStatus = STATUS_EXPANDED;
                }else if(h == minHeadTopHeight){
                    mStatus = STATUS_COLLAPSED;
                }else{
                    mStatus = 0;
                }

                if(lastVerticalOffset!=verticalOffset){
                    if(h<=maxHeadTopHeight){
                        rlSearchBarCoverLayout.setVisibility(View.VISIBLE);
                        rlSearchBar.setVisibility(View.GONE);
                        int offset = maxHeadTopHeight - h;//当前滑出屏幕的距离
                        float rate = (1.0f * offset)/maxOffset;//maxOffset需要滑出屏幕的最大距离
                        //背景图片透明度
                        ivSearchBarCoverBg.setAlpha(1 * rate);
                        //搜索布局容器高度缩放
                        rlSearchBarCoverLayout.getLayoutParams().height = h;
                        //搜索框内容部分缩放
                        if(h<=maxLimit){
                            int mMaxOffSet = maxLimit - minLimit;
                            int mOffset = maxLimit - h;
                            float mRate = (1.0f * mOffset)/mMaxOffSet;
                            //搜索框高度缩放
                            int rlSearchBoxCoverH = (int) (searchBarHeight - searchBarScale * mRate);
                            rlSearchBarCover.getLayoutParams().height = rlSearchBoxCoverH;
                            //字体缩放
                            int mTextSize = (int) (textSize - textScale * mRate);
                            tvSearchContentCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                            tvSearchBtnCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                            //底部marginBottom距离缩放
                            mMaxOffSet = maxLimit - minHeadTopHeight;
                            mRate = (1.0f * mOffset)/mMaxOffSet;
                            int marginBottom = (int) (maxMarginBottom - marginBottomScale * mRate);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlSearchBarCover.getLayoutParams();
                            params.setMargins(marginLeft,0,marginLeft,marginBottom);
                        }else{
                            //下滑时，由于比例不会等于0，进行复位
                            rlSearchBarCover.getLayoutParams().height = searchBarHeight;
                            tvSearchContentCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                            tvSearchBtnCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlSearchBarCover.getLayoutParams();
                            params.setMargins(marginLeft,0,marginLeft,maxMarginBottom);
                        }
                        //重新布局
                        rlSearchBarCoverLayout.requestLayout();
                    }else{
                        rlSearchBar.setVisibility(View.VISIBLE);
                        rlSearchBarCoverLayout.setVisibility(View.GONE);
                        //下滑时，由于比例不会等于0，所以隐藏搜索框时，进行复位
                        rlSearchBarCoverLayout.getLayoutParams().height = maxHeadTopHeight;
                        ivSearchBarCoverBg.setAlpha(1f);
                        rlSearchBarCoverLayout.requestLayout();
                    }
                    lastVerticalOffset = verticalOffset;
                }
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                coordinatorLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });

        coordinatorLayout.setAppBarLayoutObserved(this);
        coordinatorLayout.setxRefreshView(refreshview);
        coordinatorLayout.setNestedScrollView(nestedScrollView);
    }

    public List<String> getListData(int count){
        List<String> listData = new ArrayList<>();
        for(int i=0;i<count;i++){
            listData.add("Item-"+i);
        }
        return listData;
    }


    @Override
    public int getAppBarLayoutStatus() {
        return mStatus;
    }


    private class MyTabProvider implements SmartTabLayout.TabProvider{

        private LayoutInflater inflater;

        public MyTabProvider(){
            inflater = LayoutInflater.from(ScaleSearchNestedViewPagerActivity.this);
        }

        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            View view = inflater.inflate(R.layout.category_tab,container,false);
            TextView textView = view.findViewById(R.id.tvTabText);
            textView.setText(tabs.get(position));
            return view;
        }
    }
}
