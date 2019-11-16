package photran.me.parallaxviewpage.views;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Parallax transformer for ViewPagers that let you set different parallax
 * effects for each view in your Fragments.
 * <p/>
 * Created by Marcos Trujillo (#^.^#) on 1/10/14.
 * Recreate by Guihgo :{) on 20/02/16.
 */
public class ParallaxPageTransformer implements ViewPager.PageTransformer {

    private List<ParallaxTransformInformation> mViewsToParallax
            = new ArrayList<ParallaxTransformInformation>();

    public ParallaxPageTransformer() {
    }

    public ParallaxPageTransformer(List<ParallaxTransformInformation> viewsToParallax) {
        mViewsToParallax = viewsToParallax;
    }

    public ParallaxPageTransformer addViewToParallax(ParallaxTransformInformation viewInfo) {
        if (mViewsToParallax != null) {
            mViewsToParallax.add(viewInfo);
        }
        return this;
    }

    public void transformPageTest(View view, float position) {
        int pageWidth = view.getWidth();
        for (ParallaxTransformInformation parallaxTransformInformation : mViewsToParallax) {
            applyParallaxEffect(view, 0.0f, pageWidth, parallaxTransformInformation,
                    position > 0);
        }
    }

    @Override
    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        if (position < -1) {
            // This page is way off-screen to the left.
//            ViewHelper.setAlpha(view, 1);
//
        } else if (position <= 1 && mViewsToParallax != null) { // [-1,1]
            for (ParallaxTransformInformation parallaxTransformInformation : mViewsToParallax) {
                applyParallaxEffect(view, position, pageWidth, parallaxTransformInformation,
                        position > 0);
            }

        } else {
            // This page is way off-screen to the right.
            //  ViewHelper.setAlpha(view, 1);
        }
    }

    private void applyParallaxEffect(View view, float position, int pageWidth, ParallaxTransformInformation information, boolean isEnter) {

        if (information.isValid() && view.findViewById(information.resource) != null) {
            View viewRes = view.findViewById(information.resource);
            if (isEnter && !information.isEnterDefault()) {
                if (viewRes != null)
                    viewRes.setTranslationX(position * (pageWidth / information.parallaxEnterEffect));
            } else if (!isEnter && !information.isExitDefault()) {
                if (viewRes != null)
                    viewRes.setTranslationX(position * (pageWidth / information.parallaxExitEffect));
            }
        }
    }

    /**
     * Information to make the parallax effect in a concrete view.
     * <p/>
     * parallaxEffect positive values reduces the speed of the view in the translation
     * ParallaxEffect negative values increase the speed of the view in the translation
     * Try values to see the different effects. I recommend 2, 0.75 and 0.5
     */
    public static class ParallaxTransformInformation {

        public static final float PARALLAX_EFFECT_DEFAULT = -101.1986f;

        int resource = -1;
        float parallaxEnterEffect = 1f;
        float parallaxExitEffect = 1f;

        public ParallaxTransformInformation(int resource, float parallaxEnterEffect,
                                            float parallaxExitEffect) {
            this.resource = resource;
            this.parallaxEnterEffect = parallaxEnterEffect;
            this.parallaxExitEffect = parallaxExitEffect;
        }

        public boolean isValid() {
            return parallaxEnterEffect != 0 && parallaxExitEffect != 0 && resource != -1;
        }

        public boolean isEnterDefault() {
            return parallaxEnterEffect == PARALLAX_EFFECT_DEFAULT;
        }

        public boolean isExitDefault() {
            return parallaxExitEffect == PARALLAX_EFFECT_DEFAULT;
        }
    }
}