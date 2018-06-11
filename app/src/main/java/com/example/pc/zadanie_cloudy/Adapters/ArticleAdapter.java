package com.example.pc.zadanie_cloudy.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.zadanie_cloudy.Pojo.Article;
import com.example.pc.zadanie_cloudy.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by pc on 28.11.2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{


    private List<Article> articleList;
    private Context context;

    public ArticleAdapter(List<Article> articleList) {
        Collections.reverse(articleList);
        this.articleList = articleList;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        String hypertext = "<a href='"+article.getLink()+"'>"+article.getTitle()+"</a>";
        holder.article_name.setText(Html.fromHtml(hypertext));
        holder.article_name.setMovementMethod(LinkMovementMethod.getInstance());

        try{
            if (getLinkFromContent(article.getContent()) != null)
                Picasso.with(context).load(getLinkFromContent(article.getContent())).into(holder.article_img);
            else
                holder.article_img.setImageResource(R.drawable.news);
        }catch (StringIndexOutOfBoundsException e){
            holder.article_img.setImageResource(R.drawable.news);
        }

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView article_name;
        ImageView article_img;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            article_img =  (ImageView) itemView.findViewById(R.id.article_img);
            article_name = (TextView) itemView.findViewById(R.id.article_name);
        }
    }

    private String getLinkFromContent(String content){
        String link = content.substring(content.indexOf("h"), content.indexOf('"', content.indexOf('"')+1));
        if (link.contains("http")) return link;
        else return null;
    }

}
