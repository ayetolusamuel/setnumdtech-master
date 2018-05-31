package technologies.setnumd.com.setnumdtech;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import technologies.setnumd.com.setnumdtech.R;
import technologies.setnumd.com.setnumdtech.model.Products;
import technologies.setnumd.com.setnumdtech.utilities.NetworkUtils;
import technologies.setnumd.com.setnumdtech.utils.Helper;

public class MainActivityAsyncTaskLoader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    //private static final String BASE_URL = "https://gist.githubusercontent.com/ayetolusamuel/61af1c180257f400c04d69df190083bc/raw/74864dec4c1f5fea6147d8518a18e21467b70e2e/products.json";
    private static final int GITHUB_SEARCH_LOADER = 22;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    ProgressBar progressBar;
    private Context context;
    private Helper helper;
    private ArrayList<Products> productsArrayList = new ArrayList<>();

    private TextView resultTextView,errorTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_async_task_loader);
        resultTextView = findViewById(R.id.resultTextView);
        errorTextView = findViewById(R.id.errorMessageText);
        progressBar = findViewById(R.id.progressBar);


        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);

            resultTextView.setText(queryUrl);
        }

        helper = new Helper(this);


//        try {
//            String result = helper.readJsonDataFromFile();
//            System.out.println("RESULT "+result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


         getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        final String jsonResult = null;
       // String BASE_URL;


        return new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {

                String BASE_URL = null;
                try {
                    BASE_URL = helper.readJsonDataFromFile();
                    System.out.println("BASE URL "+BASE_URL);
                } catch (IOException e) {
                    e.printStackTrace();
                }



                /* If the user didn't enter anything, there's nothing to search for */
                if (BASE_URL == null || TextUtils.isEmpty(BASE_URL)) {
                    return null;
                }
                String githubSearchResults;
                // COMPLETED (12) Copy the try / catch block from the AsyncTask's doInBackground method
                /* Parse the URL from the passed in String and perform the search */
                //     URL githubUrl = new URL(BASE_URL);
                //     githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubUrl);


                return BASE_URL;
            }
            @Override
            protected void onStartLoading() {

//            if (args == null){
//                return;
//        }


                progressBar.setVisibility(View.VISIBLE);
                if (jsonResult != null){
                    deliverResult(jsonResult);
                }
                else{
                    forceLoad();
                }

            }

            @Override
            public void deliverResult(@Nullable String data) {
                super.deliverResult(data);
            }


        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        System.out.println("RESULT   "+ data);
        progressBar.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")){
            try {
                readJsonData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
        else {
            showErrorMessage();
        }
    }

    public void readJsonData(String jsonSting) throws JSONException {


        JSONObject json = new JSONObject(jsonSting);
        JSONArray jsonArray = json.getJSONArray("products");

        int length = jsonArray.length();
        productsArrayList = new ArrayList<>();
        Products products = null;
        for (int i = 0; i < length; i++) {

            JSONObject menuItemObject = jsonArray.getJSONObject(i);

            String id, instock,imagePath, name,description,price,category,image;

            id = menuItemObject.getString("id");
            name = menuItemObject.getString("name");
            description = menuItemObject.getString("description");
            price = menuItemObject.getString("price");
            instock = menuItemObject.getString("instock");
            category = menuItemObject.getString("category");
            imagePath = menuItemObject.getString("photo");

            resultTextView.append("Name :"+name + "\n\nDescription :"+description+"\n\nPrice "+price+ "\n\nCategory :"+category+ "\n\nImage "+imagePath+"\n\n\n");


            products = new Products(id,name,description,price,instock,category,imagePath);


        }

        productsArrayList.add(products);


        showProductView();

    }



    private void showProductView() {
        /* First, make sure the error is invisible */
        errorTextView.setVisibility(View.INVISIBLE);
        /* Then, make sure the JSON data is visible */
        resultTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        resultTextView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        errorTextView.setVisibility(View.VISIBLE);
    }



    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        String queryUrl = resultTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);
    }
}
