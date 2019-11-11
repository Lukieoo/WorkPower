package com.anioncode.workpower.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anioncode.workpower.MainActivity;
import com.anioncode.workpower.Model.ModelEvent;
import com.anioncode.workpower.R;
import com.anioncode.workpower.database.DatabaseHelper;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ExampleViewHolder> {
    private Context mContext;

    private ArrayList<ModelEvent> mExampleList;
    DatabaseHelper mDatabaseHelper;
    boolean checkcolor = true;
    boolean javajestpiekna = true;

    public RecyclerAdapter(Context context, ArrayList<ModelEvent> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }


    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_create, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        final ModelEvent currentItem = mExampleList.get(position);
        final String name = currentItem.getNazwa();
        //   String date = currentItem.getData();

//
        //holder.mTextData.setText(date);
        mDatabaseHelper = new DatabaseHelper(mContext);

        holder.mNumber.setText(position + 1 + ".");
        holder.mTextNazwa.setText(name);


        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(currentItem.getID());
                mExampleList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mExampleList.size());
            }
        });
        if (currentItem.getPriorytet().equals("0")) {
            holder.mMain.setBackgroundColor(Color.parseColor("#FFFFFF"));

        } else {
            holder.mMain.setBackgroundColor(Color.parseColor("#E5F2FC"));
        }
        holder.mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(10,10,10,10);
                holder.mShow.setLayoutParams(params);
                holder.mShow.setVisibility(View.VISIBLE);
                holder.mShow.setText("Brawo");
                holder.mTextNazwa.setPaintFlags(holder.mTextNazwa.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mDatabaseHelper.deleteName(currentItem.getID());
                holder.mDone.setVisibility(View.GONE);
                holder.mImportant.setVisibility(View.GONE);
                holder.mDelete.setVisibility(View.GONE);
            }
        });

        holder.mImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(currentItem.getPriorytet());


                if (currentItem.getPriorytet().equals("0")) {
                    mDatabaseHelper.updateData(currentItem.getID(), "1");

                    if (javajestpiekna) {
                        checkcolor = false;
                        javajestpiekna = false;
                    }

                } else {
                    mDatabaseHelper.updateData(currentItem.getID(), "0");
                    if (javajestpiekna) {
                        checkcolor = true;
                        javajestpiekna = false;
                    }
                }

                if (checkcolor == true) {
                    holder.mMain.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    checkcolor = false;
                } else {
                    holder.mMain.setBackgroundColor(Color.parseColor("#E5F2FC"));

                    checkcolor = true;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder {


        // public TextView mTextData;
        public TextView mTextNazwa;
        public TextView mNumber;
        public TextView mShow;

        public ImageView mDone;
        public ImageView mDelete;
        public ImageView mImportant;
        public LinearLayout mMain;

        SwipeLayout swipeLayout;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            //  mTextData = itemView.findViewById(R.id.describe);
            mTextNazwa = itemView.findViewById(R.id.describe);
            mShow = itemView.findViewById(R.id.show);

            mNumber = itemView.findViewById(R.id.number);

            mDelete = itemView.findViewById(R.id.delete);
            mImportant = itemView.findViewById(R.id.important);
            mDone = itemView.findViewById(R.id.done);

            mMain = itemView.findViewById(R.id.main);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.creator);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.bottom_wrapper));

            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });

        }
    }
}