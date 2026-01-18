TODO を削除できるようにしよう
前回の学習ではアプリの見た目を整えました。今回はそれぞれの TODO に削除ボタンを作成して、TODO の削除をできるようにしましょう。

目次
【実習】TODO の見た目をプレビューで確認しよう
【実習】TODO を削除するボタンを追加しよう
【講義】アクセシビリティを理解しよう
【実習】削除ボタンの位置を整えよう
【実習】TODO を削除しよう
【実習】TODO の見た目をプレビューで確認しよう
TODO に削除ボタンを作成する前に、先に作成した TODO をプレビューで確認できるようにしましょう。今のままだと、TODO を確認するにはアプリをビルドして TODO を毎回入力する必要があります。細かい修正をするたびにアプリを操作するのは面倒ですよね。

TODO をプレビューで確認できるようにするため、まずはアプリの中身を別の Composable に分割しましょう。新しく MyTodoAppContent() という Composable を用意して、そこへ MyTodoApp() にある Scaffold() を移行してください。TODO のデータは MyTodoAppContent() の引数として受け取りましょう。

@Composable
fun MyTodoApp() {
    val todo = remember { mutableStateOf("") }
    val todoList = remember { mutableStateListOf<String>() }

    MyTodoAppContent(todo = todo, todoList = todoList)
}

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun MyTodoAppContent(
     todo: MutableState<String>,
     todoList: SnapshotStateList<String>
 ) {
     Scaffold(
         topBar = {
             TopAppBar(
                 title = { Text(text = "My TODO") }
             )
         }
     ) { paddingValue ->
         Column(
           modifier = Modifier
             .padding(paddingValue)
             .padding(16.dp)
         ) {
             Row(
                 verticalAlignment = Alignment.CenterVertically,
                 modifier = Modifier.padding(bottom = 16.dp)
             ) {
                 TextField(
                     value = todo.value,
                     onValueChange = { text -> todo.value = text },
                     modifier = Modifier
                         .padding(end = 16.dp)
                         .weight(1f)
                 )
                 Button(onClick = {
                     todoList.add(todo.value)
                     todo.value = ""
                 }) {
                     Text(text = "追加")
                 }
             }
             todoList.forEach { item ->
                 TodoItem(text = item)
             }
         }
     }
 }

Composable を分割したらアプリをビルドして、以前と同じように動作するか確認しましょう。フォームに文言を入力して「追加」ボタンを押し、TODO が一覧で表示されていたら OK です。

Composable を分割してアプリが表示できた

上記のように Composable を分割してデータを引数として受け取るようにすると、プレビューで好きなデータを入れて確認できるようになります。以前にプレビュー用の Composable として作成した DefaultPreview() を以下のように修正してください。

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MyTodoAppContent(
            todo = remember { mutableStateOf("文字を入力中...") },
            todoList = remember { mutableStateListOf("TODO 1", "TODO 2") }
        )
    }
}

作成できたらプレビューパネルを開いて「Build & Refresh」をクリックしましょう。TODO がプレビューで表示されたことを確認してください。

TODO 一覧がプレビューで表示された

これで開発が進めやすくなりましたね。それでは次に TODO を削除するためのボタンを追加しましょう。

【実習】TODO を削除するボタンを追加しよう
TODO を削除するボタンを TODO の右端に置きたいので、TodoItem() の中の Text() を Row() で囲みつつ、削除ボタンを IconButton() を使って表示しましょう。

@Composable
fun TodoItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, modifier = Modifier.padding(vertical = 4.dp))
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Delete, contentDescription ="削除ボタン")
        }
    }
}

実装できたらプレビューを更新してください。TODO の右に削除ボタンが表示できていれば OK です。

削除ボタンが表示された

追加したコードについて確認していきましょう。IconButton() というのはその名の通りアイコンをボタンとして表示するための Composable です。タップされた時の処理は引数の onClick に指定するのですが、今はまだ空にしておいてください。

また IconButton() は 48dp x 48dp の大きさを確保しており、その範囲であればアイコンの大きさに関わらずどこをタップしてもアイコンがタップされたと判定します。これはアクセシビリティを考慮したものです。アクセシビリティについては後で詳しく解説します。

IconButton() の中には Icon() を入れて、表示したいアイコンを指定します。自分で追加したアイコンを表示することもできますが、ライブラリが用意してくれているアイコンを使用します。Icon() の引数 imageVector に Icons.Default.Delete を指定することで削除アイコンを表示しました。

このアイコンは Material Design というデザインシステムが提供しているものです。Icons のインポート先が androidx.compose.material.icons.Icons となっており、Material Design が Android のために用意しているライブラリを使っていることがわかります。

Material Design ではユーザの体験を良くするために、デザイン原則の定義や使いやすい UI 部品の提供を行っています。今回使用したアイコンもその一部です。Material Design が用意しているアイコンは他にもあるので、気になる方は Google Fonts のアイコン を参照しながら Icons.Default の中を探してみてください。

Google Fonts が提供するアイコン例

また、Icon() の引数 contentDescription には 削除ボタン と記載しました。contentDescription はこのアイコンが何を示しているのかを説明するためのもので、アクセシビリティを考慮しています。アクセシビリティに関する実装をしたところで、アクセシビリティについて解説していきます。

【講義】アクセシビリティを理解しよう
アプリのアクセシビリティとは、ユーザにとってのアプリの使いやすさを示すものです。ここで言う「ユーザ」には、視力や聴力が十分にない人やスマホを操作するのに十分な運動能力がない人、そして普段は能力があっても以下のような状況によって操作が制限される人を含みます。

スマホに日差しが反射してディスプレイを見るのが難しい状況にいる人
大音量の会場でスマホの音が聞こえない状況にいる人
手が濡れていたり手袋をしていたりして画面をタップできない状況にいる人
現在は多くの人がスマホを利用しており、様々な状況でアプリが利用されているでしょう。そのようなあらゆる人々があらゆる状況でアプリを使えるようにすること、それがアクセシビリティを考慮する目的です。

そして Android 端末ではアクセシビリティを保つために以下のようなユーザ補助機能が用意されています。

画面を読み上げてくれる TalkBack
端末に表示する文字サイズを変更したり色を調整する
再生される音声に対して自動で字幕を生成して表示する
その他のユーザ補助機能については以下をご覧ください。
Android ユーザ補助機能の概要 - Google ヘルプ
Android 端末のユーザ補助設定は、設定アプリから「ユーザ補助」メニューを開いて確認できます。

ユーザ補助機能

ユーザが上記のような機能を使ってアプリを開いたときにも、アプリは問題なく動作する必要があります。例えば、先ほど TODO アプリで Icon() を実装したとき、 contentDescription に 削除ボタン と指定しました。これによって、TalkBack でアプリの読み上げを実行したときにこのアイコンが「削除ボタン」と読み上げられます。contentDescription を設定することで、アイコンを見なくてもどのような役割のボタンなのかわかるようになるのです。

また、アイコンを表示するときに使用している IconButton() ではタップできる領域が 48dp x 48dp に設定されているため、アイコンが小さすぎて押せないという事態を防いでいます。Android アプリでは Material Design によって、タップできる UI は最低でも 48dp x 48dp は確保することを推奨されていますので、覚えておくと良いでしょう。

Material Design では他にもアクセシビリティのある UI を作成するための観点が記載されていますので、よろしければご覧ください。

Accessibility - Material Design

【実習】削除ボタンの位置を整えよう
アクセシビリティへの理解を深められたところで、TODO アプリの開発に戻りましょう。まずは TODO の削除ボタンの見た目を整えます。現在の実装では TODO の長さに応じて削除ボタンの位置がずれてしまい、長い文言の場合は削除ボタンが見えなくなってしまいます。

削除ボタンの位置がバラバラで見づらい

上記の問題を修正するために、削除ボタンを右端に配置して TODO が適切な位置で改行されるようにしましょう。TodoItem() を以下のように修正してください。

@Composable
fun TodoItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f)
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "削除ボタン")
        }
    }
}

また、修正内容を確認するために、プレビューに以下のように長めの TODO を入力してみましょう。

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MyTodoAppContent(
            todo = remember { mutableStateOf("文字を入力中...") },
            todoList = remember { mutableStateListOf("長い長い長い長い長い長い長い長い長い長い長い長い長い TODO", "TODO 2") }
        )
    }
}

修正できたらプレビューを更新してください。長い TODO が入ってきても適切に改行されること、TODO の長さに関係なく削除ボタンが右端に固定されていることを確認できたら OK です。

長い TODO と削除ボタンが適切に表示された

前回の学習で入力フォームの大きさを weight() を使って空白いっぱいに伸ばしたのと同じように、TODO も weight() を使って伸ばして削除ボタンを右端に固定しました。プレビューにデータを入れれば、実際にアプリを動かして文言を入力しなくても見た目の確認ができるので便利ですね。文字が短い場合、長い場合などさまざまなパターンのデータを用意しておくと不具合を未然に防ぐことができます。

プレビューで表示の確認ができたら、次は削除ボタンを押した時の挙動を実装しましょう。

【実習】TODO を削除しよう
まずは TodoItem() の引数に削除ボタンを押した時の処理を受け取れるようにします。TodoItem() に deleteTodo という引数を追加して、IconButton() の onClick で deleteTodo を呼び出すようにしましょう。

@Composable
 fun TodoItem(text: String, deleteTodo: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f)
        )
        IconButton(onClick = { deleteTodo() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "削除ボタン")
        }
    }
}

deleteTodo には  () -> Unit という型を指定しました。これは Kotlin における関数の型で、矢印の前にはその関数が必要とする引数の型を () の中に定義して、矢印の後にはその関数が返す型を定義します。関数が引数を必要としない場合は、() の中身を空にしておきます。また Unit とは返り値がないときに返ってくる型になります。

つまり  () -> Unit という定義は、引数がなく返り値もない関数を示しています。参考までに、他のパターンの関数型を紹介します。

(Int) -> Int: 引数に Int 型の値を取って、Int 型の値を返す関数
(String, Boolean) -> Unit: 引数に String と Boolean 型の値を取って、何も値を返さない関数
また、 IconButton() の onClick で deleteTodo を呼び出すようにしました。これで削除ボタンをタップしたら deleteTodo が実行されるようになります。

ここまで実装できたら、TodoItem() に TODO を削除する関数を渡しましょう。MyTodoAppContent() で TodoItem() を呼び出している部分を以下のように修正してください。

@Composable
fun MyTodoAppContent(
    todo: MutableState<String>,
    todoList: SnapshotStateList<String>
) {
    ...
        todoList.forEachIndexed { index, item ->
            TodoItem(
                text = item,
                deleteTodo = { todoList.removeAt(index) }
            )
        }

ここまで実装できたら、動作を確認するためにアプリをビルドしてみましょう。アプリが開いたらフォームから TODO を追加して、その TODO の削除ボタンをタップしたときに TODO が消えることを確認してください。

TODO が削除できることを確認する

実装したコードについて解説しますと、まず TODO 一覧を表示するために使用する処理を forEach() から forEachIndexed() に変更しました。forEachIndexed() は、リストを各要素ごとに処理を実行するとき、その要素がリストの何番目かを教えてくれます。forEachIndexed() で返ってくる 1 つ目の値の index が要素の順番を、2 つ目の値の item が要素自体を示しています。

またそのあと、TodoItem() の deleteTodo に、TODO を削除する処理として { todoList.removeAt(index) } を渡しました。これは removeAt() の引数に削除したい要素の順番を渡すことで、リストからその位置にある要素を削除できます。

つまり forEachIndexed() でリストにある各 TODO の順番を取得して、削除ボタンをタップしたら removeAt() を実行して TODO を削除できるようにした、ということです。

ここまでで今回の学習は完了です。次の講義では、TODO の削除に加えて「完了」ができるようにしていきましょう。

以上で今回の学習は終了です。お疲れ様でした！

まとめ
Composable に必要なデータを引数で受け取るようにするとプレビューで確認しやすい
Material Design が用意しているアイコンを Icons.Default で使うことができる
アクセシビリティを考慮したアプリを開発することが大事
関数の型は () -> Unit などで定義できる
