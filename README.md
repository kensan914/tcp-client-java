# tcp-client-java
Javaによるソケット通信クライアントです。**Sender**と**Receiver**の2種類に分かれ、Senderから送信されたバイナリデータをReceiverが受け取り表示します。SenderとReceiverは一対多の関係を想定しています。サーバは[relay_server](https://github.com/hiro4669/relay_server/)/relay_server.pyを前提としています。


# DEMO
Sender ×1(左)とReceiver ×2(中央・右)によるソケット通信の様子です。実行前に[relay_server](https://github.com/hiro4669/relay_server/)/relay_server.pyでローカルサーバを立ち上げています。

![demo](https://user-images.githubusercontent.com/52157596/104133039-6bab0e80-53c4-11eb-8b99-6abc4ff7d79a.gif)

# ソケット通信フロー
本プログラムは、[relay_server](https://github.com/hiro4669/relay_server/)/relay_server.pyの仕様に準じて、TCPによる通信を行います。

## アクティビティ図
Sender・Receiverそれぞれのソケット通信のフローを[アクティビティ図](https://qiita.com/devopsCoordinator/items/e4910ace92e66bece10f)で表します。

![activity](https://user-images.githubusercontent.com/52157596/104137792-cc494400-53e2-11eb-85e6-0c6467b13704.png)

## ヘッダー
[relay_server](https://github.com/hiro4669/relay_server/)/relay_server.pyでは、コネクション確立直後にヘッダー受信待ち状態になります。サーバは、クライアントからこのヘッダーを受信し、クライアントタイプ(SenderかReceiverか)を識別し、チャネルを決定します。ヘッダー情報の詳細は以下の通りになります。
|  |型|例|
|-----------|------------|------------|
|クライアントタイプ|int|0x1(Sender)｜0x2(Receiver)|
|チャネル名の長さ|int|3|
|チャネル名|String|"abc"|


実際には、クライアントがこれらをバイト配列に変換し、表の順で結合したものを送信します。

# クラス設計
抽象クラスTCPClient、それを継承した2つの具象クラスSenderClient・ReceiverClientで構成されています。

## クラス図
[クラス図](http://objectclub.jp/technicaldoc/uml/java2uml#8)

![class](https://user-images.githubusercontent.com/52157596/104190985-3eac3980-5460-11eb-9c7b-51717357f0e4.png)

## フィールド
|フィールド名|初期値|説明|
|-----------|------------|------------|
|HOST|"127.0.0.1"|ローカルサーバのIPアドレス|
|PORT|8888|ローカルサーバのポート|
|CHANNEL|"abc"|チャネル名。チャネル名が同じクライアント同士しかデータのやり取りは行われない。|
|OK_MESSAGE|"OK"|ヘッダーの認証判定に使用される。|
|DISCONNECT_SIGN|"DISCONNECT"|後述のreceive()内でサーバ側からの接続解除が検知された場合の戻り値として使用される。|
|CHAR_CODE|StandardCharsets.US_ASCII|文字コード(ASCII)|
|BUFFER_SIZE|1024|送受信可能な最大バイト数|
|clientType|-|enum列挙型であるClientTypeのオブジェクト。ヘッダーに送信する用として、フィールドidを持つ。|
|socket|-|JavaによるTCP通信を実現する[java.net.Socket](https://docs.oracle.com/javase/jp/8/docs/api/java/net/Socket.html)クラスのインスタンス。|
|in|-|socketのフィールドである入力ストリーム。受信時にソケットからバイトを読み込むために使用。|
|out|-|socketのフィールドである出力ストリーム。送信時にソケットにバイトを書き込むために使用。|

## メソッド
|メソッド|説明|
|-----------|------------|
|createHeader(): byte[]|前述したヘッダーを作成し、バイト配列に変換したものを返す。|
|connect(): void|ソケットの作成・コネクションの確立・ヘッダーをcreateHeader()で作成し、送信。|
|disconnect(): void|接続解除。socketをclose。|
|send(buffer: byte[]): void|バイト配列bufferを送信。|
|receive(): String|実行と同時に受信待ち状態に。サーバから受信が開始されると1バイトずつ読み取り、バイト配列を作成し、ASCIIでデコードした文字列を返す。サーバからの接続解除が検知された時、フィールドDISCONNECT_SIGNを返す。|
|initClientType(): void|SenderClient・ReceiverClientでオーバーライド。フィールドclientTypeの初期化を行う。|
|run(): void|SenderClient・ReceiverClientでオーバーライド。ソケットのコネクション確立後に実行される。前述のアクティビティ図の最後2つのアクションノードが実行される。|