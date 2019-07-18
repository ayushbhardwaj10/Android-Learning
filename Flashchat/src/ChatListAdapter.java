package com.ab.flashchatnewfirebase;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity mActivity;                            // We are passing all these parameters as through a adapter only the firebase database can interect with firebase
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;  //A DataSnapshot instance contains data from a Firebase Database location. Everychange leades of addition of the snapshot array. Any time you read Database data, you receive the data as a DataSnapshot(JSON type).

    // A listener objecct created to be passed to mDatabaseReference.addChildEventListener() to perform a task when Database is changed.
    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        // OnChildAdded method is involked when a new child is added to the firebase database and we recieve a data snapshot from firebase.
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
          mSnapshotList.add(dataSnapshot);  // Restrives the data from firebase in the form of JSON file
          notifyDataSetChanged();   // InOrder to tell Listview that the database is changed

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference ref, String name) {

        mActivity = activity;
        mDisplayName = name;
        // common error: typo in the db location. Needs to match what's in MainChatActivity.
        mDatabaseReference = ref.child("message"); // mDatabaseReference is pointing to the child called 'messages'
        mDatabaseReference.addChildEventListener(mListener); // Adding a listener whenever the child database 'messages' is changed, mlistiner object's method are invoked.
        mSnapshotList = new ArrayList<>();
    }

    private static class ViewHolder{  // It's a view holder to temporarily hold a view when the Listview is asking for more views again and again.
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }
    // OVERRIDING THE BASE CLASS METHODS
    @Override
    //Returns the count of elements in the list.
    public int getCount() {  // Refer section 14 video 162 for a brief theoritical working of Listview.
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot dataSnapshot= mSnapshotList.get(i); // dataSnapshot stores the JSON value now. -> To be converted to InstantMessage class type
        return dataSnapshot.getValue(InstantMessage.class); // Return JSON file in the form of InstantMessage class type.
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       // Everytime getting a view to Listview from adapter is not required. We check, if there already exist a view, we'll just replay it with the dara.
        if(view== null){
            // No view is present, so we'll create a new view
            LayoutInflater inflater= (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //LayoutInflater is used to create a new view of type Chat_Msg_row to send it to listview(Each view is passed a rowto the listview)
            view= inflater.inflate(R.layout.chat_msg_row,viewGroup,false); //created a view of type chat_msg_row layout.
            final ViewHolder  holder=  new ViewHolder();
            holder.authorName= view.findViewById(R.id.author);
            holder.body=view.findViewById(R.id.message);
            holder.params= (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            view.setTag(holder);   // To temporarily hold the view ie. AuthorName,body,params to avoid calling FindViewById again and again
                         // We have to pass a object a .setTag() thats why we created a new inner class.
        }
         // Save the authorname and the message here.
        // We are only storing and getting the messages and author from instantMessage class
        final InstantMessage instantMessage= getItem(i); // i is the new position in the listview // the function returns current InstantMessage values, thats why using functionr return call.
        final ViewHolder holder= (ViewHolder) view.getTag();  // Retriving back the temporary stored authorname and message view's
        holder.authorName.setText(instantMessage.getAuthor());
        holder.body.setText(instantMessage.getMessage());  // The msg and authorname in the " View" will be set here
        return view ;
    }         // Base adapter is a abstract class so we have to impliment it's methods in it's child class

    public void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }

}
