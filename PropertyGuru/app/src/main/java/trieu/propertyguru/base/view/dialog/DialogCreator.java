package trieu.propertyguru.base.view.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import trieu.propertyguru.R;


/**
 * Created by Apple on 2/6/17.
 */

public class DialogCreator {
    static private ProgressDialog progressDialog;
    static public void showDialogMessage(final Context context, final String title, final String message, final DialogInterface.OnClickListener onOkClickListener){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onOkClickListener != null){
                            onOkClickListener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    static public void showDialogMessage(final Context context, final String title, final String message, final DialogInterface.OnClickListener onOkClickListener, final DialogInterface.OnClickListener onCancelClickListener){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onOkClickListener != null){
                            onOkClickListener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(onCancelClickListener != null){
                            onCancelClickListener.onClick(dialogInterface, i);
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    static public void showDialogMessage(final Context context, final int title, final int message, final DialogInterface.OnClickListener onOkClickListener){
        String titleStr = context.getString(title);
        String messageStr = context.getString(message);
        showDialogMessage(context, titleStr, messageStr, onOkClickListener);
    }

    static public void showDialogMessage(final Context context, final String message, final DialogInterface.OnClickListener onOkClickListener){
        showDialogMessage(context, null, message, onOkClickListener);
    }

    static public void showToast(final Context context, final String message){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    static public void showToast(Context context, int message){
        showToast(context, context.getString(message));
    }

    static public void showUniqueProgressDialog(final Context context, final String title, final String message){
        hideUniqueProgressDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    static public void hideUniqueProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

}
