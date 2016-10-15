package id.urbanwash.wozapp.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.model.PlaceBean;

public class PlaceManager {

    private static final String PLACE = "PLACE";

    private Context mContext;

    private List<PlaceBean> mPlaceBeans;

    public PlaceManager(Context context) {
        mContext = context;
    }

    public List<PlaceBean> getPlaces() {

        mPlaceBeans = new ArrayList<PlaceBean>();

        ObjectInputStream objectinputstream = null;

        try {

            File file = new File(mContext.getFilesDir(), PLACE);
            FileInputStream streamIn = new FileInputStream(file);
            objectinputstream = new ObjectInputStream(streamIn);
            mPlaceBeans = (List<PlaceBean>) objectinputstream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectinputstream);
        }

        return mPlaceBeans;
    }

    public void savePlaces(List<PlaceBean> placeBeans) {

        ObjectOutputStream objectOutputStream = null;

        try {

            File installation = new File(mContext.getFilesDir(), PLACE);
            FileOutputStream streamOut = new FileOutputStream(installation);
            objectOutputStream = new ObjectOutputStream(streamOut);
            objectOutputStream.writeObject(placeBeans);

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
