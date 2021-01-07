import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Sender {
	public static void main(String[] args) {
		SenderClient senderClient = new SenderClient();
		senderClient.run();
	}

	static class SenderClient extends TCPClient {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		@Override
		void initClientId() {
			this.clientId = ClientId.SENDER;
		}

		@Override
		void disconnect() throws Exception {
			this.input.close();
			super.disconnect();
		}

		@Override
		void run() {
			try {
				while (true) {
					System.out.print("> ");
					String line = input.readLine();
					byte[] messageBytes = line.getBytes(this.CHAR_CODE);
					this.send(messageBytes);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
