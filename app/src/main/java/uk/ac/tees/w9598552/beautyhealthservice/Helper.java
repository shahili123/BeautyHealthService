package uk.ac.tees.w9598552.beautyhealthservice;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

public class Helper
{

    private static ProgressDialog progressDialog;
    
    public static void showLoader(Context con, String msg)
    {
        progressDialog = new ProgressDialog(con);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public static void stopLoader()
    {
        if (progressDialog != null)
        {

            progressDialog.cancel();
            progressDialog = null;

        }
    }

    public static void PutData(Context context, String key, String value)
    {
        try
        {
            SharedPreferences sharedpreferences = context.getSharedPreferences("ssshhhh!!!", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String GetData(Context context, String key)
    {
        try
        {
            SharedPreferences sharedpreferences = context.getSharedPreferences("ssshhhh!!!", Context.MODE_PRIVATE);
            return sharedpreferences.getString(key, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


}

