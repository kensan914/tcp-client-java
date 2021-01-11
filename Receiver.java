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
					// ==で文字列のアドレスを比較することで、万が一DISCONNECT_SIGNと同文字列を受信してもbreakしない。
					if (message == this.DISCONNECT_SIGN) {
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
