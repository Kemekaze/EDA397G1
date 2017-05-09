package chalmers.eda397g1.interfaces;

import android.view.View;
import android.widget.ViewFlipper;

/**
 * Created by fredrikhansson on 25/04/17.
 */

public interface RecyclerViewFlipperClickListener {
    public void recycleViewFlipperListClicked(View v, int position, ViewFlipper viewFlip);
}
