package id.urbanwash.wozapp.async;

import id.urbanwash.wozapp.model.ImageBean;

public interface HttpAsyncImageListener {
	
	public void onAsyncTimeOut();
	
	public void onAsyncError(String message);

    public void onAsyncGetImage(ImageBean image);
}
