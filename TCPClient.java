import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;

abstract class TCPClient {
	final String HOST = "127.0.0.1";
	final int PORT = 8888;
	final String CHANNEL = "abc";
	final Charset CHAR_CODE = StandardCharsets.US_ASCII;
	final String OK_MESSAGE = "OK";
	final String DISCONNECT_SIGN = "DISCONNECT";
	final int BUFFER_SIZE = 1024;

	protected enum ClientId {
		SENDER((byte) 0x1), RECEIVER((byte) 0x2),;

		public final byte id;

		private ClientId(final byte _id) {
			this.id = _id;
		}
	}

	protected ClientId clientId;

	Socket socket;
	InputStream in;
	OutputStream out;

	void connect() {
		try {
			System.out.println("connecting...");
			this.socket = new Socket(this.HOST, this.PORT);
			this.in = this.socket.getInputStream();
			this.out = this.socket.getOutputStream();

			byte[] headerBytes = createHeader();
			this.send(headerBytes);
			String connectResult = this.receive();

			if (connectResult.equals(OK_MESSAGE)) {
				System.out.println("connected!");
			} else {
				this.disconnect();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void disconnect() throws Exception {
		this.socket.close();
		System.out.println("disconnected.");
	}

	byte[] createHeader() {
		String headerChannel = this.CHANNEL.length() + this.CHANNEL;
		byte[] headerChannelBytes = headerChannel.getBytes(this.CHAR_CODE);

		byte[] headerBytes = new byte[headerChannelBytes.length + 1];
		System.arraycopy(headerChannelBytes, 0, headerBytes, 1, headerChannelBytes.length);
		headerBytes[0] = this.clientId.id;
		return (headerBytes);
	}

	void send(byte[] buffer) throws Exception {
		this.out.write(buffer);
	}

	String receive() throws Exception {
		byte[] buffer = new byte[this.BUFFER_SIZE];
		int dataByteSize = 0;
		while (dataByteSize < this.BUFFER_SIZE) {
			if (dataByteSize > 0 && this.in.available() == 0) {
				break;
			}
			int result = this.in.read();
			if (result == -1) {
				this.disconnect();
				return this.DISCONNECT_SIGN;
			}
			buffer[dataByteSize++] = (byte) result;
		}

		return (new String(buffer, 0, dataByteSize, this.CHAR_CODE));
	}

	public TCPClient() {
		this.initClientId();
		this.connect();
	}

	abstract void initClientId();

	abstract void run();
}