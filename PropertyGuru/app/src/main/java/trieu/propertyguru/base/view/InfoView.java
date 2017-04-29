package trieu.propertyguru.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import trieu.propertyguru.R;


/**
 * Created by Trieu on 7/28/2016.
 */
public class InfoView extends FrameLayout {

    private LayoutInflater layoutInflater;
    private View mainView;
    private ProgressBar loadingView;
    private TextView messageView;
    private View errorView;

    public InfoView(Context context) {
        super(context);
        init();
    }

    public InfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.view_information, this, true);

        //find children
        if(!isInEditMode()) {
            loadingView = (ProgressBar) view.findViewById(R.id.information_layout_loading_bar);
            messageView = (TextView) view.findViewById(R.id.information_layout_tv_msg);
            errorView = view.findViewById(R.id.information_layout_error_layout);
        }
        showLoading();
    }

    public void setMainLayout(View view){
        this.mainView = view;
    }

    public void showLoading(){
        setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        messageView.setVisibility(View.GONE);
        if(mainView != null){
            mainView.setVisibility(View.GONE);
        }
    }

    public void showMessage(String message){
        setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        messageView.setVisibility(View.VISIBLE);
        if(mainView != null){
            mainView.setVisibility(View.GONE);
        }

        messageView.setText(message);
    }

    public void showError(String error, Throwable e, OnClickListener onClickListener){
        setVisibility(View.VISIBLE);
        if(mainView != null){
            mainView.setVisibility(View.GONE);
        }
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        messageView.setVisibility(View.GONE);

        String txtError = (e != null ? e.getMessage() : "");
        if(error != null){
            txtError = error;
        }
        TextView.class.cast(errorView.findViewById(R.id.information_layout_tv_error)).setVisibility(View.VISIBLE);
        TextView.class.cast(errorView.findViewById(R.id.information_layout_button_retry)).setVisibility(View.VISIBLE);
        TextView.class.cast(errorView.findViewById(R.id.information_layout_tv_error)).setText(txtError);
        Button.class.cast(errorView.findViewById(R.id.information_layout_button_retry)).setOnClickListener(onClickListener);
    }

    public void showMainView(){
        setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        messageView.setVisibility(View.GONE);

        if(mainView != null){
            mainView.setVisibility(View.VISIBLE);
        }
    }

    public void showOnlyButton(String btnTitle, OnClickListener onClickListener){
        setVisibility(View.VISIBLE);
        if(mainView != null){
            mainView.setVisibility(View.GONE);
        }
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        messageView.setVisibility(View.GONE);

        TextView.class.cast(errorView.findViewById(R.id.information_layout_tv_error)).setVisibility(View.GONE);
        Button button = Button.class.cast(errorView.findViewById(R.id.information_layout_button_retry));
        button.setOnClickListener(onClickListener);
        button.setText(btnTitle);
    }
}
