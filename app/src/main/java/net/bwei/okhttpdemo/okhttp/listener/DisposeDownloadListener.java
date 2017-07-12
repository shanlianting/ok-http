package net.bwei.okhttpdemo.okhttp.listener;

/**
 * @author
 * @function 监听下载进度
 */
public interface DisposeDownloadListener extends DisposeDataListener {
	public void onProgress(int progrss);
}
