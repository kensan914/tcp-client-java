public class Receiver {
	public static void main(String[] args) {
		ReceiverClient receiverClient = new ReceiverClient();
		receiverClient.run();
	}

	static class ReceiverClient extends TCPClient {
		@Override
		void initClientType() {
			this.clientType = ClientType.RECEIVER;
		}

		@Override
		void run() {
			try {
				while (true) {
					String message = this.receive();
					if (message == this.DISCONNECT_SIGN) { // 文字列のアドレスを比較
						break;
					}
					System.out.println("・" + message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
