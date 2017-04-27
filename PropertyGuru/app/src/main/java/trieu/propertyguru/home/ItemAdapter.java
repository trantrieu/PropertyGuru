package trieu.propertyguru.home;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import rx.Subscriber;
import trieu.propertyguru.R;
import trieu.propertyguru.base.view.InfoView;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.data.webservices.WebServicesUtils;

/**
 * Created by Apple on 4/25/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Item[] arrayItemList;
    private String [] arrayItem;
    private Context context;
    public ItemAdapter(Context contexts, String [] arrayItem){
        this.arrayItem = arrayItem;
        arrayItemList = new Item[arrayItem.length];
        context = contexts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        MyViewHolder holder = new MyViewHolder(context, v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Item item = arrayItemList[position];
        final MyViewHolder myViewHolder = ((MyViewHolder)holder);
        myViewHolder.setIndex((position+1)+"");
        if(item == null){
            myViewHolder.showLoading();
            String itemId = arrayItem[position];
            WebServicesUtils.getDetailItem(itemId, new Subscriber<Item>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    myViewHolder.showError(e.getMessage());
                }

                @Override
                public void onNext(Item item) {
                    myViewHolder.showMain();
                    arrayItemList[position] = item;
                    myViewHolder.updateView(item);
                }
            }, holder.itemView);
        }else{
            myViewHolder.showMain();
            myViewHolder.updateView(item);
        }
    }

    @Override
    public int getItemCount() {
        return arrayItem.length;
    }

    static private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTv, urlTv, descriptionTv, indexTv;

        private LinearLayout mainView;
        private InfoView infoView;
        private Context context;
        public MyViewHolder(Context context,View itemView) {
            super(itemView);
            this.context = context;
            titleTv = (TextView) itemView.findViewById(R.id.layout_item_title);
            urlTv = (TextView) itemView.findViewById(R.id.layout_item_url);
            descriptionTv = (TextView) itemView.findViewById(R.id.layout_item_description);
            indexTv = (TextView) itemView.findViewById(R.id.layout_item_index);
            mainView = (LinearLayout) itemView.findViewById(R.id.layout_item_main);
            infoView = (InfoView) itemView.findViewById(R.id.layout_item_info_view);
            infoView.setMainLayout(mainView);

            urlTv.setPaintFlags(urlTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        public void updateView(Item item){
            String title = item.getTitle();
            String url = item.getUrl();
            String description = Item.getDescription(context, item);
            titleTv.setText(title);
            urlTv.setText(url);
            descriptionTv.setText(description);
        }

        public void setIndex(String index){
            indexTv.setText(index);
        }

        public void showLoading(){
            infoView.showLoading();
        }

        public void showMain(){
            infoView.showMainView();
        }

        public void showError(String error){
            infoView.showMessage(error);
        }
    }

    public void destroy(){
        WebServicesUtils.clearAll();
    }

    public Item getItemAt(int position){
        return arrayItemList[position];
    }

    public interface OnItemClickListener{
        void onItemClick(Item item);
    }
}
