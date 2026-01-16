画面を作成してみよう
1 章ではプログラミング言語 Kotlin の解説や Android Studio の使い方を一緒に学んできました。ここからは実際に簡単な Android アプリを開発しながら、さまざまな技術要素を学習していきましょう！

作成するのはシンプルな TODO アプリです。自分の TODO （やること）を追加できて、追加した TODO を完了または削除できるアプリを作成していきます。

作成する TODO アプリのイメージ

今回は TODO を追加する画面の UI を作っていきます。Android アプリで UI を作成するためのライブラリ Jetpack Compose の解説を交えながら、一緒に画面を作っていきましょう。

目次
【実習】タイトルを表示しよう
【講義】Composable の作り方とプレビューの方法
【実習】入力フォームを表示しよう
【実習】見た目を整えよう
【講義】Modifier とは
【講義】dp と sp
【実習】タイトルを表示しよう
それではさっそくアプリの画面を作成していきましょう。まずはアプリのタイトルを表示してみます。
1 章で作成した My Application プロジェクトがある方はそちらを開いてください。もし My Application プロジェクトがなければ、1 章 3 講の 「Android 開発環境を整えよう」を参考しながら My Application プロジェクトを作成してください。

My Application プロジェクトが開けたら、MainActivity.kt を開いて、onCreate() の中から Greeting("Android") を呼び出している部分を削除します。

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        MyApplicationTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
            }

Greeting("Android") を削除した理由は、これから新しく画面を作成するうえで不要だったためです。前章で実装した fun Greeting(name: String) の中身の処理はもう使わないので、削除してもそのまま残しておいても大丈夫です。この教材では全体を見やすくするために fun Greeting(name: String) 自体もここで削除します。

そうしたら、新しく画面を作成するための Composable を作成します。MainActivity の下に、MyTodoApp という Composable を以下のように作成してください。

 @Composable
 fun MyTodoApp() {
     Text(text = "My TODO")
 }

MyTodoApp() を作成したら、MainActivity の onCreate() の中から呼び出しましょう。先ほど Greeting("Android") を削除したのと同じ場所に MyTodoApp() を記載してください。

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyTodoApp()
                }
            }
        }
    }

上記のように書けたら、プレビューで確認できるように DefaultPreview() の中で MyTodoApp() を参照するように修正します。

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MyTodoApp()
    }
}

これらの実装ができたら、エディタの右上から「Split」を押してプレビュー画面を開き、「Build & Refresh」を押してプレビューを表示しましょう。Android Studio のバージョンによっては、自動的にプレビューが再レンダリングされます。以下のような画面になっていれば OK です。

プレビューにタイトルを表示する

まだタイトルが表示されただけなので味気ないですが、これから他の部品も実装していくので安心してください。実装を進めていく前に、Composable の作成とプレビュー方法について解説します。

【講義】Composable の作り方とプレビューの方法
Android アプリでは Jetpack Compose というライブラリを使用して Composable という単位で UI を作成していきます。Composable を作成するときは、@Composable というアノテーションをつけて、大文字から始まる名称で関数を宣言します。

// @Composable というアノテーションを追加
@Composable
// 関数を宣言して大文字で始める
fun MyTodoApp() {
    Text(text = "My TODO")
}

このように書くことで、画面に表示できる Composable を作成できます。ちなみにアノテーションを毎回書くのが面倒な場合は、Android Studio で comp と入力して Enter を押すと自動で関数の宣言まで入力できますので、試してみてください。

Composable 作成のショートカット

ちなみに、MyTodoApp() から呼び出している Text() も文字を表示する Composable です。「文字」のように汎用的な部品は Jetpack Compose が事前に用意してくれています。ほとんどの UI については、準備されている Composable をそのまま使ったり組み合わせることで表現できるので、どんどん活用していきましょう。

UI を作成したらどんな見た目になったか確認したいですよね。その場合はプレビュー用の Composable を作成しましょう。プレビュー用の Composable は @Preview というアノテーションをつける必要があります。

// @Preview というアノテーションを追加
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MyTodoApp()
    }
}

このように宣言することで、作成した Composable をプレビューできます。@Preview の後ろについている showBackground = true という記述は、「背景つきのプレビューを表示する」という設定になります。

他にもプレビューの表示方法の設定がいくつか用意されていますが、今回はこのままで良いでしょう。ちなみに showBackground = true を削除してしまうと、背景がなくなってプレビューが見づらくなってしまいますので、残しておいたほうが良いでしょう。

プレビューに背景を設定しない場合

このように、Composable で画面の UI を作成して、プレビューで確認するという方法で開発を進めていきます。

【実習】入力フォームを表示しよう
Composable の基本を理解したところで、次に TODO を入力するためのフォームを作成していきます。MyTodoApp() を以下のように実装してください。

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun MyTodoApp() {
     Column {
         Text(text = "My TODO")
         TextField(value = "", onValueChange = {})
         Button(onClick = {}) {
             Text(text = "追加")
         }
     }
}

上記のとおり書けたらプレビューを更新してみましょう。以下のように表示できれば OK です。

入力フォームを表示

: 古いバージョンの Android Studio について
2023 年 4 月にリリースされた「Android Studio Flamingo」よりも前のバージョンの Android Studio を使用している場合は、上記の見た目とやや異なるプレビューが表示されます。

これは Android Studio が参照するライブラリ（Material Design）のバージョンが更新されたためです。以前のバージョンでは以下のように表示されていると思いますが、実装する上で大きな問題は無いのでそのまま進めても大丈夫です。


今回はいくつか要素が増えました。1 つずつ説明していきます。

@OptIn(ExperimentalMaterial3Api::class) という要素を @Composable の上に追加しました。これは今回追加した TextField() という部品が試験的なもので、今後変更があるかもしれませんということを示すためのものです。@OptIn(ExperimentalMaterial3Api::class) を削除すると TextField() に警告が表示されてしまうため、付けておきましょう。

次に Column という Composable で全体を囲いました。Column はレイアウトを整える Composable で、中身の Composable を上から順番に並べてくれます。プレビューを見てみると、「My TODO というタイトル」「入力欄」「送信ボタン」が上から順番に並んでいるのがわかりますね。

もし Column で囲わなかったら、各要素が重なってしまって以下のように見づらくなってしまいます。レイアウトを整えるために Column はよく使用するので覚えておきましょう。

Column がない場合

次に、タイトルの下にテキスト入力欄とボタンを追加しました。それぞれ TextField と Button という Composable を使っています。これらも Jetpack Compose が用意してくれているものです。

TextField と Button の中に value や onValueChange 、onClick という引数が定義されていますが、これらの説明を今はしません。次回の学習でこれらの引数に値を設定するので、そのときに改めて説明します。

また、Button の {} の中には Text を追加しました。このように書くことでボタンの中に「追加」という文字を表示できます。もしボタンの中にアイコンや画像を表示したい場合は、Button の {} の中で指定すれば良いわけです。

このように、既存の Composable を組み合わせることで様々な UI を表現できるのが Jetpack Compose の良いところです。必要な部品を追加できたところで、次は画面全体の見た目を整えていきましょう。

【実習】見た目を整えよう
Composable を使って UI を作成してきましたが、今のままだと少し見づらいですね。Web ページの開発に例えるならば、HTML で要素を定義しただけで、まだ CSS が何も実装されていないような状態です。

もちろん Android アプリでも見た目を整えることはできるので、それぞれの部品を設定していきましょう。今回の場合は、アプリのタイトルをもう少し目立たせたいですね。また、タイトルと入力フォームが近すぎるので、いくらか余白を空けたいです。

そのために、MyTodoApp() を以下のように修正してください。必要なものは適宜 import してくださいね。

@Composable
fun MyTodoApp() {
    Column {
        Text(text = "My TODO", fontSize = 32.sp)
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )
        Button(onClick = {}) {
            Text(text = "追加")
        }
    }
}

実装ができたらプレビューを更新して、以下のように表示されていれば OK です。

入力フォームの見た目を調整する

アプリのタイトルの文字が大きくなって、タイトルと入力フォームの間に余白が生まれたのを確認できたでしょうか。確認できたら、今回実装したコードをそれぞれ見ていきましょう。

【講義】Modifier とは
Jetpack Compose では、見た目を整えるために Modifier というものを使います。作成した Composable を　Modify（修正）するのが Modifier と覚えてください。

先ほど入力フォームに対して実装した modifier = Modifier.padding(top = 24.dp, bottom = 16.dp) というのは、上に 24.dp、下に 16.dp の余白をつけるという設定です。

dp という単位については後で説明するので一旦置いておきます。ここでは Web ページの開発で使用した px のようなものだと理解してください。

Modifier には様々な設定が用意されていて、今回のように余白をつける padding 以外にも様々なものがあります。例を挙げると...

clickable: クリックできるようにする
background: 背景を設定する
width: 横幅を指定する
border: 境界線を設定する
rotate: 回転を設定する
などがあります。

Modifier はいくつもつなげて書くことができるので、例えばこんなこともできます。

TextField(
    value = "",
    onValueChange = {},
    modifier = Modifier
        .padding(top = 24.dp, bottom = 16.dp)
        .rotate(15f)
        .border(width = 12.dp, color = Color.Green)
)

複数の Modifier をつけてみる

上記の例は極端ですが、Modifier を使いこなせるようになれば、UI で表現できる幅も大きく広がります。ぜひ色々な設定を試してみてください。

一方で Modifier では設定できないものもあります。今回で言えば、タイトルの「文字サイズ」のようなものは Modifier では指定できません。そのため、タイトルを大きくするために Text() が用意してくれている fontSize という引数に 32.sp を指定しました。

Text(text = "My TODO", fontSize = 32.sp)

見た目を修正するときに、Modifier を使うのか、用意された Composable が持っている引数を使うのか迷うことがあるかもしれません。基本的には、どんな部品にも共通の設定（今回なら余白）は Modifier、部品に特有の設定（今回なら文字サイズ）は引数で設定すると考えておけば良いでしょう。様々な Composable を使っていくうちに自然と身についてくるものなので、少しずつ慣れていきましょう。

また、文字サイズの指定として sp という単位を使用しました。先ほどの dp と混同してしまいがちなので、合わせて解説します。

【講義】dp と sp
まず前提として、Android には様々な端末があります。スマートフォンだけでも大きいものから小さいもの、細長いものがあり、他にも Android を搭載したタブレットや TV まであります。そしてそれぞれの端末では解像度も様々です。

さて、そんな多種多様な端末に対して同じレイアウトを表示するためにはどうすれば良いでしょうか。端末によって横幅のピクセル数も縦幅のピクセル数も異なります。作成した UI に直接ピクセル数を指定したら、解像度が高いスマートフォンでは小さく表示されて、解像度が低いスマートフォンでは大きく表示されてしまいます。

ここで使われるのが dp という解像度に依存しない単位です。dp というのは Density-independent Pixels（密度に依存しないピクセル）の略称で、1dp は中密度（160dpi）の画面における 1 ピクセルと同じ大きさを示します。そのため、どのような解像度の端末でも同じ大きさを表現できるのです。

一方で sp というのは Scale-independent Pixels（スケールに依存しないピクセル）の略称で、ユーザの文字サイズ指定に基づいて大きさが変わる単位です。文字の大きさを sp で指定することで、ユーザが設定した文字サイズを UI にも反映できます。

Android 端末の文字サイズは、設定画面の「ディスプレイ」→「フォントサイズ」から設定できます（端末やバージョンによっては設定方法が異なるかもしれません）。

文字サイズ設定画面

このように、様々な解像度の端末やユーザの文字サイズ設定においてアプリを正常に表示するために dp と sp という単位を使っています。もしもっと詳しく知りたい場合は、Android Developer の以下の記事を参照してみてください。

各種のピクセル密度をサポートする

以上で入力フォームの作成は完了です。お疲れ様でした！

次回は実際にフォームを使ってデータを入力できるようにして、それを一覧で表示できるようにしていきます。

まとめ
Composable を作成するためには @Composable というアノテーションをつける
プレビュー用の Composable を用意することで、Android Studio でデザインの確認ができる
Modifier を使って見た目を整えることができる
Composable のサイズ指定には dp 、文字サイズの指定には sp を使う

---
## リンク
[Android Developer: 各種のピクセル密度をサポートする](https://developer.android.com/training/multiscreen/screendensities?hl=ja)
