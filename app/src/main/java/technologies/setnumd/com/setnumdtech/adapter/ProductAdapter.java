package technologies.setnumd.com.setnumdtech.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import technologies.setnumd.com.setnumdtech.DetailsActivity;
import technologies.setnumd.com.setnumdtech.R;
import technologies.setnumd.com.setnumdtech.model.Products;
import technologies.setnumd.com.setnumdtech.utils.ColorUtils;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by User on 4/8/2018.
 */




public class ProductAdapter   extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context context;
    ArrayList<Products> productsArrayList = new ArrayList<>();
    private final static String ID_CONSTANT = "SET";
    private int viewHolderCount = 0;


    public ProductAdapter(Context context, ArrayList<Products> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View productView = layoutInflater.inflate(R.layout.activity_product_main,parent,false);
        ViewHolder viewHolder = new ViewHolder(productView);


      //  viewHolder.viewHolderItemView.setText("ViewHolder index: " +viewHolderCount);
       int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context,viewHolderCount);




       // viewHolder.productIDTextView.setBackgroundColor(backgroundColorForViewHolder);

        viewHolderCount++;
  Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "          + viewHolderCount);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Products products = productsArrayList.get(position);

        holder.productNameTextView.setText(products.getName());
        holder.productCategoryTextView.setText(products.getCategory());
       holder.productPriceTextView.setText("₦"+products.getPrice());
        holder.productImageView.setImageBitmap(getImageFromAssestFile(context,products.getImagePath()));



    }



    @Override
    public int getItemCount() {
        if(productsArrayList != null){

            return productsArrayList.size();
        }
        return 0;

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView productNameTextView,productCategoryTextView,productPriceTextView;
        ImageView productImageView;


        public ViewHolder(View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productCategoryTextView = itemView.findViewById(R.id.productCategoryTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productImageView = itemView.findViewById(R.id.productImageView);

            itemView.setOnClickListener(this);

        }




        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Products products = productsArrayList.get(itemPosition);

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("id",products.getId());
            intent.putExtra("name",products.getName());
            intent.putExtra("category",products.getCategory());
            intent.putExtra("price",products.getPrice());
            intent.putExtra("instock",products.getInStock());
            intent.putExtra("description",products.getDescription());
            intent.putExtra("image",products.getImagePath());
            context.startActivity(intent);


        }
    }
    public  static Bitmap getImageFromAssestFile(Context context, String fileName){
        Bitmap image = null;
        AssetManager am =context.getResources().getAssets();

        try {
            InputStream inputStream =am.open(fileName);
            image = BitmapFactory.decodeStream(inputStream);
            inputStream.close();


        }
        catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

}
