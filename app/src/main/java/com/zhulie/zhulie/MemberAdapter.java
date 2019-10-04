package com.zhulie.zhulie;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

  private Context context;
  private ArrayList<Member> memberArrayList;

  public MemberAdapter(Context context, ArrayList<Member> memberArrayList) {
    this.context = context;
    this.memberArrayList = memberArrayList;
  }

  @NonNull
  @Override
  public MemberAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.member_item, viewGroup, false);
    MemberAdapter.MemberViewHolder memberViewHolder = new MemberAdapter.MemberViewHolder(view);
    return memberViewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
    CircleImageView imageView = memberViewHolder.imageUri;
    TextView name = memberViewHolder.name;

    name.setText(memberArrayList.get(i).getName());
    Glide.with(context).load(Uri.parse(memberArrayList.get(i).getImageUri())).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(imageView);
  }

  @Override
  public int getItemCount() {
    return memberArrayList.size();
  }

  public class MemberViewHolder extends RecyclerView.ViewHolder {

    CircleImageView imageUri;
    TextView name;

    public MemberViewHolder(View view) {
      super(view);

      imageUri = view.findViewById(R.id.memberImage);
      name = view.findViewById(R.id.memberName);
    }
  }
}
