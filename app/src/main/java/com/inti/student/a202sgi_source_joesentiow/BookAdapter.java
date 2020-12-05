package com.inti.student.a202sgi_source_joesentiow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private ArrayList<Book> mBookData;
    private Context mContext;

    /**
     * Constructor that passes in the book data and the context
     * @param bookData ArrayList containing the book data
     * @param context Context of the application
     */
    BookAdapter(Context context, ArrayList<Book> bookData) {
        this.mBookData = bookData;
        this.mContext = context;
    }

    /**
     * Required method for creating the ViewHolder objects.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    /**
     * Required method that binds the data to the ViewHolder.
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, final int position) {
        // Get current book
        final Book currentBook = mBookData.get(position);
        // Populate the TextViews with data
        holder.bindTo(currentBook);

        Glide.with(mContext).load(currentBook.getImageResource()).into(holder.mBookImage);

        holder.mBtnDetails.setOnClickListener(new MyButtonOnClickListener(
                currentBook.getGenre(),
                currentBook.getTitle(),
                currentBook.getAuthor(),
                currentBook.getCode(),
                currentBook.getDescription(),
                currentBook.getPrice(),
                currentBook.getImageResource()) {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);

                intent.putExtra("genre", genre);
                intent.putExtra("title", title);
                intent.putExtra("author", author);
                intent.putExtra("code", code);
                intent.putExtra("description", description);
                intent.putExtra("price", price);
                intent.putExtra("image", imageResource);
                mContext.startActivity(intent);
            }
        });

        holder.mBtnAddToCart.setOnClickListener(new MyButtonOnClickListener(
                null,
                currentBook.getTitle(),
                null,
                null,
                null,
                currentBook.getPrice(),
                0) {

            @Override
            public void onClick(View v) {
                SharedPreferences preferences = mContext.getSharedPreferences("preferences", MODE_PRIVATE);
                boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                ArrayPreferences arrayPreferences = new ArrayPreferences(mContext);
                final ArrayList<String> bookArray = arrayPreferences.getListString("bookArray");

                if (!isLoggedIn) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }
                else {
                    String bookList = preferences.getString("bookList", "");
                    float totalPrice = preferences.getFloat("totalPrice", 0);

                    String title = currentBook.getTitle();
                    String price = "RM" + String.format("%.2f", currentBook.getPrice());

                    String books = title + "\n" + String.format("%50s", price);

                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("bookList", bookList + "\n" + books);
                    editor.putFloat("totalPrice", totalPrice + (float)currentBook.getPrice());
                    editor.apply();

                    bookArray.add(title);
                    arrayPreferences.putListString("bookArray", bookArray);

                    Toast.makeText(mContext, "Item added to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Returns size of data set
    @Override
    public int getItemCount() {
        return mBookData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;
        private TextView mTvPrice;
        private ImageView mBookImage;

        Button mBtnDetails;
        Button mBtnAddToCart;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         * @param itemView The root view of the list_item.xml layout file
         */
        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views
            mTvTitle    = itemView.findViewById(R.id.tvTitleDetail);
            mTvPrice    = itemView.findViewById(R.id.tvPriceDetail);
            mBookImage  = itemView.findViewById(R.id.ivBookDetail);

            mBtnDetails     = itemView.findViewById(R.id.btnDetails);
            mBtnAddToCart   = itemView.findViewById(R.id.btnAddToCart);
        }

        void bindTo(Book currentBook) {
            // Populate the TextViews with data
            mTvTitle.setText(currentBook.getTitle());
            mTvPrice.setText("RM" + String.valueOf(String.format("%.2f", currentBook.getPrice())));
        }
    }
}
