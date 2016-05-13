package photran.me.parallaxviewpage;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import photran.me.parallaxviewpage.views.PTPageView;
import photran.me.parallaxviewpage.views.ParallaxPageTransformer;


public class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private float HARD_WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels / 2.0f;
    private final float xChangeText = 1.0f;//-10.0f;
    private final float xChangeImage = 2.0f;//4.0f;
    private float DEFAULT_ALPHA_TEXT_VIEW = 0.01f;
    private final int NUM_PAGE = 4;
    private final int[] TEXT_CONTENT_HEADER_PAGES = {
            R.string.text_content_intro_1,
            R.string.text_content_intro_2,
            R.string.text_content_intro_3,
            R.string.text_content_intro_4,
    };
    private final int[] PHOTO_CONTENT_PAGES = {
            R.drawable.intro_photo_1,
            R.drawable.intro_photo_2,
            R.drawable.intro_photo_3,
            R.drawable.intro_photo_4
    };

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private PTPageView mPtPageView;
    private Button btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.callMainActivity(IntroActivity.this);
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(this);

        mPtPageView = (PTPageView) findViewById(R.id.ptpageview);
        mPtPageView.setSize(NUM_PAGE);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);


        ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.txtTextContent, xChangeText, xChangeText));
        pageTransformer.addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.imgPhotoContent, xChangeImage, xChangeImage));

        mViewPager.setPageTransformer(true, pageTransformer);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int positionView = position;

        float posX = Math.abs(positionOffsetPixels - HARD_WIDTH_SCREEN);
        float alpha = posX / HARD_WIDTH_SCREEN;

        if (positionOffset > 0.5f) {
            positionView = position + 1;
        }

        if (positionOffset == 0.0f) {
            alpha = 1.0f;
        }

        mSectionsPagerAdapter.setAlphaText(positionView, alpha);
    }

    @Override
    public void onPageSelected(int position) {
        mPtPageView.setState(position);
        btnSkip.setVisibility(
                (position == (NUM_PAGE - 1))
                        ? View.VISIBLE
                        : View.INVISIBLE
        );
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class SectionsPagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private Context mContext;
        private HashMap<Integer, TextView> positionPageTextView;

        public SectionsPagerAdapter(Context context) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);
            positionPageTextView = new HashMap<>();
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            ViewGroup mLayout = (ViewGroup) inflater.inflate(getLayoutId(), collection, false);
            collection.addView(mLayout);


            TextView txtTextContent = (TextView) mLayout.findViewById(R.id.txtTextContent);
            txtTextContent.setText(getTextContentPage(position));

            ImageView imgPhotoContent = (ImageView) mLayout.findViewById(R.id.imgPhotoContent);
            imgPhotoContent.setImageResource(getPhotoContentPage(position));

            positionPageTextView.put(position, txtTextContent);

            return mLayout;
        }

        private int getLayoutId() {
            return R.layout.layout_intro_item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return NUM_PAGE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getTextContentPage(int position) {
            return TEXT_CONTENT_HEADER_PAGES[position];
        }

        public int getPhotoContentPage(int position) {
            return PHOTO_CONTENT_PAGES[position];
        }

        private TextView getTextView(int position) {
            return positionPageTextView.get(position);
        }

        public void setAlphaText(int position, float alpha) {
            for (int i = 0; i < getCount(); i++) {
                if (getTextView(i) != null) {
                    getTextView(i).setAlpha(
                            (i != position) ?
                                    DEFAULT_ALPHA_TEXT_VIEW :
                                    alpha);
                }
            }
        }
    }
}
