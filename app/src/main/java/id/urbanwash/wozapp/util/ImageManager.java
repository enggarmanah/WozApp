package id.urbanwash.wozapp.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.async.HttpAsyncImageListener;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.ImageBean;

public class ImageManager implements HttpAsyncImageListener {

    private static final String IMAGE = "IMAGE";

    private Context mContext;
    private ImageBean mImageBean;
    private CircleImageView mImageView;

    private static Map<Long, Date> mImageLastUpdateMap = new HashMap<Long, Date>();

    public ImageManager(Context context) {
        mContext = context;
    }

    public void setImage(CircleImageView imageView, ImageBean imageBean) {

        if (imageBean == null) {
            return;
        }

        mImageView = imageView;
        mImageBean = getCachedImage(imageBean);

        if (mImageBean != null && mImageBean.getBytes() != null) {

            Bitmap bMap = CommonUtil.getBitmap(mImageBean.getBytes());
            imageView.setImageBitmap(bMap);

            Date lastUpdateDate = mImageLastUpdateMap.get(mImageBean);
            Date curDate = new Date();

            if (lastUpdateDate == null || (lastUpdateDate != null && (lastUpdateDate.getTime() + Constant.TIME_DAY) < curDate.getTime())) {

                HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mContext, this);
                httpAsyncManager.getImageUpdate(imageBean);

                mImageLastUpdateMap.put(mImageBean.getId(), curDate);
            }

        } else if (mImageBean != null) {

            Date lastUpdateDate = mImageLastUpdateMap.get(mImageBean);
            Date curDate = new Date();

            if (lastUpdateDate == null || (lastUpdateDate != null && (lastUpdateDate.getTime() + Constant.TIME_DAY) < curDate.getTime())) {

                HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mContext, this);
                httpAsyncManager.getImage(imageBean);

                mImageLastUpdateMap.put(mImageBean.getId(), curDate);
            }
        }
    }

    private ImageBean getCachedImage(ImageBean imageBean) {

        ObjectInputStream objectinputstream = null;

        try {

            File file = new File(mContext.getFilesDir(), IMAGE + imageBean.getId());
            FileInputStream streamIn = new FileInputStream(file);
            objectinputstream = new ObjectInputStream(streamIn);
            imageBean = (ImageBean) objectinputstream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectinputstream);
        }

        return imageBean;
    }

    public void saveImage(ImageBean imageBean) {

        if (imageBean == null) {
            return;
        }

        ObjectOutputStream objectOutputStream = null;

        try {

            File installation = new File(mContext.getFilesDir(), IMAGE + imageBean.getId());
            FileOutputStream streamOut = new FileOutputStream(installation);
            objectOutputStream = new ObjectOutputStream(streamOut);
            objectOutputStream.writeObject(imageBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectOutputStream);
        }
    }

    private void close(InputStream inputStream) {

        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close(OutputStream outputStream) {

        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAsyncTimeOut() {
    }

    public void onAsyncError(String message) {
    }

    public void onAsyncGetImage(ImageBean imageBean) {

        if (imageBean == null) {
            return;
        }

        mImageBean = imageBean;
        saveImage(mImageBean);

        if (mImageView.isShown()) {

            Bitmap bMap = CommonUtil.getBitmap(mImageBean.getBytes());
            mImageView.setImageBitmap(bMap);
        }
    }
}
