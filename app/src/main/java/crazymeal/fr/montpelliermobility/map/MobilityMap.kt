package crazymeal.fr.montpelliermobility.map

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import org.osmdroid.views.MapView

class MobilityMap(context: Context?, attrs: AttributeSet?) : MapView(context, attrs) {
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        this.parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(event)
    }
}