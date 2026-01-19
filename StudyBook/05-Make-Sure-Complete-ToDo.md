TODO を完了できるようにしよう
前回の学習では TODO を削除できるようにしました。今回は TODO の削除だけではなく、完了できるようにして、TODO の見た目を完了していることがわかるように変更しましょう。

目次
【実習】TODO のデータを持つクラスを定義しよう
【実習】データクラスを使って TODO 一覧を表示しよう
【実習】TODO が完了したときの見た目を作ろう
【実習】TODO を完了するボタンを用意しよう
【実習】TODO が完了したらデータを更新しよう
【実習】TODO のデータを持つクラスを定義しよう
それでは TODO を完了できるように実装していきます。そのためにはまず、TODO が完了しているのかどうかがわかるデータを定義しなければなりません。現在の TODO はただの文字列（String）なので、完了しているかどうかを判定するデータを持つことはできません。ここでは新しく TODO のデータを持つクラスを定義しましょう。

まず Todo.kt というファイルを MainActivity.kt と同じ階層に新規作成します。左の Project ツールウィンドウの中から com.example.myapplication というフォルダを右クリックして、出てきたメニューから「New > Kotlin Class/File」と選択し、「Data Class」を選択した状態で Todo と入力してファイルを作成してください。

Todo.kt を作成する

作成できたら、ファイルの中身を以下のように実装してください。

 data class Todo(
     val text: String,
     val isCompleted: Boolean
 )

以下のようにエラーが出ていなければ OK です。まだどこからも参照されていないので、Todo という文字が灰色になっています。

Todo.kt を作成する

それでは上記のコードの解説をします。まず data class というのはデータを保持しておくのに便利な Kotlin のクラスです。data class ではデータを扱うのに便利な機能（copy, equals など）が用意されているので、データを作成するときは data class を使用するのが一般的です。data class が持っている機能についてはあとで使用するので、今は説明を省略します。

また Todo の引数として text と isCompleted を用意しました。これは、

text: TODO の内容を示す文字列
isCompleted: TODO が完了しているかどうかを示す真偽値
のデータをそれぞれ持つためです。これで TODO のデータを定義できたので、次はこのデータクラスを使って TODO 一覧を表示できるようにしましょう。

【実習】データクラスを使って TODO を表示しよう
まずは TODO 一覧を保存している State の型を String から先ほど作成した Todo クラスに変更しましょう。MyTodoApp() の中の State を以下のように修正してください。

@Composable
fun MyTodoApp() {
    val todo = remember { mutableStateOf("") }
    val todoList = remember { mutableStateListOf<Todo>() }

    MyTodoAppContent(todo = todo, todoList = todoList)
}

修正すると、MyTodoAppContent() を呼び出している部分がエラーになります。MyTodoAppContent() の引数 todoList の型を修正して、その影響でエラーになる Button() と TodoItem() の実装も修正しましょう。

@Composable
fun MyTodoAppContent(
    todo: MutableState<String>,
    todoList: SnapshotStateList<Todo>
) {
  ...
        Button(onClick = {
            todoList.add(Todo(text = todo.value, isCompleted = false))
            todo.value = ""
        }) {
            Text(text = "追加")
        }
    }
    todoList.forEachIndexed { index, item ->
        TodoItem(
            todo = item,
            deleteTodo = { todoList.removeAt(index) }
        )
    }
    ...
}

そうすると今度は MyTodoAppContent() で呼び出している TodoItem() の型が合っていないためにエラーが発生します。TodoItem() の引数を修正して、修正した引数の型に合わせて実装も修正してください。

@Composable
 fun TodoItem(todo: Todo, deleteTodo: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = todo.text,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f)
        )
        IconButton(onClick = { deleteTodo() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "削除ボタン")
        }
    }
}

また、プレビューにもエラーが出ていますので、DefaultPreview() にて以下のようにテストデータを修正してください。あとで確認しやすいように、先に完了済みのデータも用意しておきましょう。

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MyTodoAppContent(
            todo = remember { mutableStateOf("文字を入力中...") },
            todoList = remember {
                mutableStateListOf(
                    Todo(text = "長い長い長い長い長い長い長い長い長い長い長い長い長い TODO", isCompleted = false),
                    Todo(text = "完了した TODO", isCompleted = true)
                )
            }
        )
    }
}

ここまでできたらアプリをビルドして、これまで通り TODO の追加と削除ができることを確認しましょう。

TODO の追加と削除ができることを確認

また、プレビューにデータが反映されていることも確認してください。

TODO の追加と削除ができることを確認

これで、データクラス Todo を使用して TODO 一覧を表示できました！

【実習】TODO が完了したときの見た目を作ろう
データクラスの適用ができたところで、次に TODO が完了していたときの見た目を作成しましょう。完了している場合は、わかりやすいように文字色を薄くして打ち消し線をつけましょう。TodoItem() を以下のように実装してください。

@Composable
fun TodoItem(todo: Todo, deleteTodo: () -> Unit) {
    val fontColor =
        if (todo.isCompleted) {
            Color.Gray
        } else {
            Color.Black
        }
    val textDecoration =
        if (todo.isCompleted) {
            TextDecoration.LineThrough
        } else {
            null
        }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = todo.text,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f),
            color = fontColor,
            textDecoration = textDecoration
        )

実装できたらプレビューを更新して、完了した TODO の見た目が変わることを確認してください。

TODO が完了したときの見た目を確認

実装したコードについて解説しますと、最初の val fontColor では文字色を保持します。fontColor には if を使った条件分岐で、引数の todo のプロパティである isCompleted を見て true だったら Gray を、false だったら Black を入れるようにしました。

同じように val textDecoration にも、todo.isCompleted が true なら TextDecoration.LineThrough を、false なら null を指定しました。TextDecoration.LineThrough は文字の中央に線を引く装飾です。ちなみに TextDecoration には他にも TextDecoration.Underline があり、こちらは文字の下に線を引く装飾になります。

TextDecoration.Underline で下線を引く

そして fontColor と textDecoration に入れた文字色と文字装飾を TODO の内容を表示する Text() に適用しました。文字色を変えるために color、文字装飾をつけるために textDecoration という引数にそれぞれの値を代入しました。

これで完了時の見た目が作れたので、次にアプリから TODO を完了できるようにボタンを作成しましょう。

【実習】TODO を完了するボタンを用意しよう
TODO を完了するボタンとして、今回はチェックボックスを使用します。Material Design が Checkbox という Composable を用意してくれているので、それを TodoItem() に追加しましょう。

@Composable
fun TodoItem(todo: Todo, deleteTodo: () -> Unit) {
    ...
    Row(verticalAlignment = Alignment.CenterVertically) {
    Text(
        text = todo.text,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .weight(1f),
        color = fontColor,
        textDecoration = textDecoration
    )
    Checkbox(
        checked = todo.isCompleted,
        onCheckedChange = {},
        modifier = Modifier.padding(start = 4.dp)
    )
    IconButton(onClick = { deleteTodo() }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "削除ボタン")
    }
}

チェックボックスが追加できたらプレビューを更新して確認してみましょう。

チェックボックスが追加された

Checkbox() はその名の通りチェックボックスを表示するための Composable です。チェックされているかどうかは checked に渡された値で判断して、チェックボックスがタップされたら onCheckedChange に指定された処理を実行します。

まだ onCheckedChange の中身は実装していないので、チェックボックスをタップしてもボックス自体の表示が変わるだけです。次に、チェックボックスをタップしたら TODO を完了する処理を実装していきましょう。

【実習】TODO が完了したらデータを更新しよう
チェックボックスがタップされたときの処理を実装するため、TodoItem() でチェックボックスをタップしたときの処理を受け取って、チェックボックスに渡しましょう。

@Composable
 fun TodoItem(
    todo: Todo,
    deleteTodo: () -> Unit,
    completeTodo: (Boolean) -> Unit
 ) {
    ...
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = completeTodo,
            modifier = Modifier.padding(start = 4.dp)
        )

そうしたら TodoItem() を呼び出している MyTodoAppContent() にエラーが発生するため、MyTodoAppContent() を以下のように修正してください。

@Composable
fun MyTodoAppContent(
    todo: MutableState<String>,
    todoList: SnapshotStateList<Todo>
) {
    ...
        todoList.forEachIndexed { index, item ->
            TodoItem(
                todo = item,
                deleteTodo = { todoList.removeAt(index) },
                completeTodo = { todoList[index] = item.copy(isCompleted = it) }
            )
        }

ここまで実装できたらアプリをビルドして、チェックボックスをタップしたときに完了状態が切り替わることを確認しましょう。

チェックボックスをタップすると完了状態が切り替わる

これで TODO を完了にして、見た目が変わるように実装できました。それでは実装したコードを確認していきましょう。

Checkbox() の onCheckedChange は、(Boolean) -> Unit という型の関数を受け取れます。関数の引数に設定されている Boolean は、チェックボックスがタップされたあとの状態を返します。つまり、チェックボックスをタップしてチェックがついたら true、チェックが外れたら false が返ってきます。そのため、TodoItem() の引数には completeTodo という名称で (Boolean) -> Unit 型の関数を受け取るようにしました。

そして、MyTodoAppContent() から TodoItem() に対して completeTodo = { todoList[index] = item.copy(isCompleted = it) } を渡すようにしました。これは、index 番目の TODO にデータを再度入れることで要素を更新しています。

また、copy() というのは data class が持っている処理で、その名前の通り data class のコピーを作成できます。そして、コピーするときに要素を指定して値を入れると、その値が反映されたコピーが生成されます。つまり copy(isCompleted = it) と書くことで、isCompleted は受け取った it の値になり、text は以前のままの値になっている Todo を生成しています。

生成した Todo を todoList[index] に代入することで、リストのアイテムが更新されて、チェックボックスの状態も更新されました。これで、チェックボックスをタップして TODO を更新する、という一連の流れを実現できました。

以上で今回の学習は終了です。お疲れ様でした！

まとめ
data class を使ってデータを定義する
TextDecoration で文字に装飾を追加できる
Checkbox() という Composable を使うことでチェックボックスを表示できる
リストを更新するには、更新したいアイテムに新しいアイテムを代入する
