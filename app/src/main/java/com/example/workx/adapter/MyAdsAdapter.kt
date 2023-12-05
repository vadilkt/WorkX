import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workx.R
import com.example.workx.fragments.MyAdsFragment
import com.example.workx.holder.AdViewHolder
import com.example.workx.model.Ad

class MyAdsAdapter(
    private val ads: List<Ad>,
    private val listener: MyAdsFragment,
    private val currentUserUid: String
) :
    RecyclerView.Adapter<AdViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ad_item, parent, false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        val ad = ads[position]
        holder.bind(ad)

        if (ad.userId == currentUserUid) {
            holder.adDeleteBtn.visibility = View.VISIBLE
            holder.adDeleteBtn.setOnClickListener {
                listener.onDeleteClick(position)
            }
        } else {
            holder.adDeleteBtn.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return ads.size
    }

    fun getItemAtPosition(position: Int): Ad {
        return ads[position]
    }
}
