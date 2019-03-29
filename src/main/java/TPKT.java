import java.util.Arrays;

public class TPKT {

	private int contentLength;

	private byte[] content;

	public TPKT(int contentLength, byte[] content) {
		this.contentLength = contentLength;
		this.content = content;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "TPKT Protocol [contentLength=" + contentLength + ", content=" + Arrays.toString(content) + "]";
	}

}
