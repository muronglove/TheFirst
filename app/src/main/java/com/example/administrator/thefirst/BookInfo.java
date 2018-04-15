package com.example.administrator.thefirst;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZZh on 2018/3/31.
 */

public class BookInfo implements Parcelable{
    private Long id;
    private String mTitle;
    private long mISBN;
    private String mAuthor;
    private Bitmap mThumbnail;
    private String mThumbnailFilePath;
    private String mPublisher;
    private String mPublishDate;
    private String mPrice;
    private String mSummary;
    private String mRating;
    private String mTranslator;
    private int mPages;
    private String mIllustrate;

    public BookInfo(){

    }

    protected BookInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        mTitle = in.readString();
        mISBN = in.readLong();
        mAuthor = in.readString();
        mThumbnail = in.readParcelable(Bitmap.class.getClassLoader());
        mThumbnailFilePath = in.readString();
        mPublisher = in.readString();
        mPublishDate = in.readString();
        mPrice = in.readString();
        mSummary = in.readString();
        mRating = in.readString();
        mTranslator = in.readString();
        mPages = in.readInt();
        mIllustrate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(mTitle);
        dest.writeLong(mISBN);
        dest.writeString(mAuthor);
        dest.writeParcelable(mThumbnail, flags);
        dest.writeString(mThumbnailFilePath);
        dest.writeString(mPublisher);
        dest.writeString(mPublishDate);
        dest.writeString(mPrice);
        dest.writeString(mSummary);
        dest.writeString(mRating);
        dest.writeString(mTranslator);
        dest.writeInt(mPages);
        dest.writeString(mIllustrate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel in) {
            return new BookInfo(in);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public long getISBN() {
        return mISBN;
    }

    public void setISBN(long mISBN) {
        this.mISBN = mISBN;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getThumbnailFilePath() {
        return mThumbnailFilePath;
    }

    public void setThumbnailFilePath(String mThumbnailFilePath) {
        this.mThumbnailFilePath = mThumbnailFilePath;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public void setPublishDate(String mPublishDate) {
        this.mPublishDate = mPublishDate;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String mRating) {
        this.mRating = mRating;
    }

    public String getTranslator() {
        return mTranslator;
    }

    public void setTranslator(String mTranslator) {
        this.mTranslator = mTranslator;
    }

    public int getPages() {
        return mPages;
    }

    public void setPages(int mPages) {
        this.mPages = mPages;
    }

    public String getIllustrate() {
        return mIllustrate;
    }

    public void setIllustrate(String mIllustrate) {
        this.mIllustrate = mIllustrate;
    }
}
