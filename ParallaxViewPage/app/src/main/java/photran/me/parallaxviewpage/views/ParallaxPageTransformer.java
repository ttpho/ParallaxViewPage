package photran.me.parallaxviewpage.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @NonNull
    private List<ParallaxTransformInformation> mViewsToParallax
            = new ArrayList<>();

    public ParallaxPageTransformer() {
    }

    public ParallaxPageTransformer addViewToParallax(@Nullable final List<ParallaxTransformInformation> viewsToParallax) {
        if (viewsToParallax == null || viewsToParallax.isEmpty()) return this;
        mViewsToParallax.addAll(viewsToParallax);
        return this;
    }

    public ParallaxPageTransformer addViewToParallax(ParallaxTransformInformation viewInfo) {
        if (viewInfo == null) return this;
        mViewsToParallax.add(viewInfo);
        return this;
    }

    @Override
    public void transformPage(@NonNull final View view, final float position) {
        if (!(-1 <= position && position <= 1)) return;
        final int pageWidth = view.getWidth();
        for (ParallaxTransformInformation parallaxTransformInformation : mViewsToParallax) {
            applyParallaxEffect(view, position, pageWidth, parallaxTransformInformation,
                    position > 0);
        }
    }

    private void applyParallaxEffect(@NonNull final View view,
                                     final float position,
                                     final int pageWidth,
                                     @NonNull final ParallaxTransformInformation information,
                                     final boolean isEnter) {

        if (!(information.isValid() && view.findViewById(information.resource) != null)) {
           return;
        }
        final View viewRes = view.findViewById(information.resource);
        if (viewRes == null) return;
        if (isEnter && !information.isEnterDefault()) {
            viewRes.setTranslationX(position * (pageWidth / information.parallaxEnterEffect));
        } else if (!isEnter && !information.isExitDefault()) {
            viewRes.setTranslationX(position * (pageWidth / information.parallaxExitEffect));
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

        int resource;
        float parallaxEnterEffect;
        float parallaxExitEffect;

        public ParallaxTransformInformation(final int resource,
                                            final float parallaxEnterEffect,
                                            final float parallaxExitEffect) {
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