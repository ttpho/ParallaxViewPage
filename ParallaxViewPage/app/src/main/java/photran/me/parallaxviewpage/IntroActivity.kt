package photran.me.parallaxviewpage

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.util.HashMap

import photran.me.parallaxviewpage.views.PTPageView
import photran.me.parallaxviewpage.views.ParallaxPageTransformer
import kotlin.math.abs


class IntroActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    companion object {
        private val HARD_WIDTH_SCREEN = Resources.getSystem().displayMetrics.widthPixels / 2.0f
        private const val xChangeText = 1.0f
        private const val xChangeImage = 2.0f
        private const val DEFAULT_ALPHA_TEXT_VIEW = 0.01f
        private const val NUM_PAGE = 4
        private val TEXT_CONTENT_HEADER_PAGES = intArrayOf(R.string.text_content_intro_1, R.string.text_content_intro_2, R.string.text_content_intro_3, R.string.text_content_intro_4)
        private val PHOTO_CONTENT_PAGES = intArrayOf(R.drawable.intro_photo_1, R.drawable.intro_photo_2, R.drawable.intro_photo_3, R.drawable.intro_photo_4)
    }

    private var sectionsPagerAdapter: SectionsPagerAdapter? = null
    private var btnSkip: View? = null
    private var ptPageView: PTPageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btnSkip = findViewById<View>(R.id.btnSkip)?.also {
            it.setOnClickListener {
                MainActivity.callMainActivity(this@IntroActivity)
            }
        }

        sectionsPagerAdapter = SectionsPagerAdapter(this)

        val pageTransformer = ParallaxPageTransformer()
                .addViewToParallax(ParallaxPageTransformer.ParallaxTransformInformation(R.id.txtTextContent, xChangeText, xChangeText))
                .addViewToParallax(ParallaxPageTransformer.ParallaxTransformInformation(R.id.imgPhotoContent, xChangeImage, xChangeImage))

        ptPageView = (findViewById<View>(R.id.ptpageview) as? PTPageView)?.also {
            it.setSize(NUM_PAGE)
        }

        (findViewById<View>(R.id.container) as? ViewPager)?.let {
            it.adapter = sectionsPagerAdapter
            it.addOnPageChangeListener(this)
            it.setPageTransformer(true, pageTransformer)
        }
    }

    override fun onPageScrolled(position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int) {
        val posX = abs(positionOffsetPixels - HARD_WIDTH_SCREEN)
        val alpha = if (positionOffset == 0.0f) {
            1.0f
        } else {
            posX / HARD_WIDTH_SCREEN
        }
        val positionView = if (positionOffset > 0.5f) {
            position + 1
        } else {
            position
        }
        sectionsPagerAdapter?.setAlphaText(positionView, alpha)
    }

    override fun onPageSelected(position: Int) {
        ptPageView?.setState(position)
        btnSkip?.visibility = if (position == NUM_PAGE - 1) View.VISIBLE else View.INVISIBLE
    }

    override fun onPageScrollStateChanged(state: Int) = Unit

    inner class SectionsPagerAdapter(context: Context) : PagerAdapter() {

        private val inflater = LayoutInflater.from(context)
        private val positionPageTextView: HashMap<Int, TextView> = HashMap()

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val view = inflater.inflate(R.layout.layout_intro_item, collection, false)
            collection.addView(view)
            (view.findViewById<View>(R.id.imgPhotoContent) as? ImageView)
                    ?.setImageResource(getPhotoContentPage(position))
            positionPageTextView[position] = (view.findViewById<View>(R.id.txtTextContent) as? TextView)?.also {
                it.setText(getTextContentPage(position))
            } ?: return view

            return view
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount() = NUM_PAGE

        override fun isViewFromObject(view: View, objectAny: Any) = view === objectAny

        private fun getTextContentPage(position: Int) = TEXT_CONTENT_HEADER_PAGES[position]

        private fun getPhotoContentPage(position: Int) = PHOTO_CONTENT_PAGES[position]

        private fun getTextView(position: Int): TextView? = positionPageTextView[position]

        fun setAlphaText(position: Int, alpha: Float) {
            for (i in 0 until count) {
                getTextView(i)?.alpha = if (i != position) DEFAULT_ALPHA_TEXT_VIEW else alpha
            }
        }
    }
}
