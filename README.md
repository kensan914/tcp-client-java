# tcp-client-java
Javaによるソケット通信クライアントです。**Sender**と**Receiver**の2種類に分かれ、Senderから送信されたバイナリデータをReceiverが受け取り表示します。SenderとReceiverは一対多の関係を想定しています。サーバは[relay_server](https://github.com/hiro4669/relay_server/)のrelay_server.pyを前提としています。


# DEMO
Sender ×1(左)とReceiver ×2(中央・右)によるソケット通信の様子です。

![demo](https://user-images.githubusercontent.com/52157596/104133039-6bab0e80-53c4-11eb-8b99-6abc4ff7d79a.gif)

# クラス図
![class](https://user-images.githubusercontent.com/52157596/104135959-766e9f00-53d6-11eb-9b2c-e0386e94c472.png)

