package trieu.propertyguru.base.view.comment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import trieu.propertyguru.BuildConfig;
import trieu.propertyguru.R;
import trieu.propertyguru.base.view.InfoView;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.utils.Utils;

import static android.R.attr.color;
import static android.R.attr.level;

/**
 * Created by Apple on 4/25/17.
 */

public class CommentView extends LinearLayout {
    static public boolean FIRST_COMMENT = true;
    private final int colors[] = {Color.BLUE, Color.YELLOW, Color.GRAY, Color.GREEN, Color.RED, Color.CYAN, Color.MAGENTA};

    private float marginLeft;
    private Item item;

    @BindView(R.id.layout_comment_item_title)
    TextView titleTv;

    @BindView(R.id.layout_comment_item_text)
    TextView textTv;

    @BindView(R.id.layout_comment_item_divider)
    View dividerView;

    public CommentView(Context context) {
        super(context);
        localInit();
    }

    public CommentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        localInit();
    }

    public CommentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        localInit();
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        localInit();
    }

    private void localInit(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_comment_item, this, true);
        ButterKnife.bind(this, view);

        setOrientation(LinearLayout.VERTICAL);
        marginLeft = getResources().getDimension(R.dimen._10dp);
    }

    public void init(Item item){
        if(FIRST_COMMENT){
            FIRST_COMMENT = false;
            dividerView.setVisibility(View.GONE);
        }
        this.item = item;
        String title = Item.getCommentTitle(getContext(), item);
        titleTv.setText(title);

        Spanned text = Utils.getTextCommentHtml(item);
        textTv.setText(text);

        int level = item.getLevel();
        if(level > 1){
            LinearLayout.LayoutParams params = (LayoutParams) getLayoutParams();
            params.leftMargin = (int) (level * marginLeft);
            setLayoutParams(params);
        }
    }

    public Item getItem(){
        return item;
    }
}
