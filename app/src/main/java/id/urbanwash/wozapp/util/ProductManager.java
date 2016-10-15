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
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.async.HttpAsyncImageListener;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.model.ProductBean;

public class ProductManager {

    private static final String PRODUCT = "PRODUCT";

    private Context mContext;
    private List<ProductBean> mProductBeans;

    public ProductManager(Context context) {
        mContext = context;
    }

    public List<ProductBean> getProducts() {

        ObjectInputStream objectinputstream = null;

        try {

            File file = new File(mContext.getFilesDir(), PRODUCT);
            FileInputStream streamIn = new FileInputStream(file);
            objectinputstream = new ObjectInputStream(streamIn);
            mProductBeans = (List<ProductBean>) objectinputstream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectinputstream);
        }

        return mProductBeans;
    }

    public void saveProducts(List<ProductBean> productBeans) {

        ObjectOutputStream objectOutputStream = null;

        try {

            File installation = new File(mContext.getFilesDir(), PRODUCT);
            FileOutputStream streamOut = new FileOutputStream(installation);
            objectOutputStream = new ObjectOutputStream(streamOut);
            objectOutputStream.writeObject(productBeans);

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
}
