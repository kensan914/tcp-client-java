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
					// By comparing the address of the message variable in ==, even if the same
					// string as DISCONNECT_SIGN is received, it will not break.
					if (message == this.DISCONNECT_SIGN) {
						break;
					}
					System.out.println("- " + message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
