package com.example.todo_app

import android.R.attr.name
import android.R.attr.text
import android.R.attr.top
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.experimental.Experimental
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.ui.theme.ToDoAppTheme
import kotlinx.coroutines.NonCancellable.isCompleted

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                //     Greeting(
                //         name = "Android",
                //         modifier = Modifier.padding(innerPadding)
                //     )
                    MyTodoApp()
                }
            }
        }
    }
}

// @Composable というアノテーションを追加
@OptIn(ExperimentalMaterial3Api::class) // MyTodoApp() の TextField() が試験的なもの今後変更があるかもしれませんということを示す
@Composable
// fun Greeting(name: String, modifier: Modifier = Modifier) {
//     Text(
//         text = "Hello $name!",
//         modifier = modifier
//     )
// }

// Modifier(修正するという意)の設定一覧
// clickable: クリックできるようにする
// background: 背景を設定する
// width: 横幅を指定する
// border: 境界線を設定する
// rotate: 回転を設定する

// 関数を宣言して大文字で始める(compと入力すれば候補として出てくる)
fun MyTodoApp() {
    val todo = remember { mutableStateOf("") } // remember{} でComposable が更新されても {} 内のデータを保持できるようにする(宣言的 UI),  mutableStateOf("") は状態（State）であることを宣言する、初期文字として空文字を設定している、State）をもとにして見た目が更新されます。この State を入力フォームに渡すことで、フォームに入力された文言を保存してそのまま表示できるようになる
    val todoList = remember { mutableStateListOf<Todo>() } //  mutableStateListOf(): List という型で State を作成する, <String>で文字列のみ受け入れる, <Todo> に変更

    MyTodoAppContent(todo = todo, todoList = todoList)
}

// Scaffold( ...の部分を別の Composable に移動させて分割, 前に作成した TODO をプレビューで確認できるようにする
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTodoAppContent(
    todo: MutableState<String>,
    todoList: SnapshotStateList<Todo>
) {
    Scaffold( // 画面全体のレイアウトを組むための Composable で、トップバーやボトムバー、スナックバーなどを表示する領域を用意してくれている
        topBar = { // トップバー
            TopAppBar(
                title = { Text(
                    text = "My TODO",
                    // color = Color.Blue
                    // ここにトップバーの背景色や文字の色を変えるコードを加える
                )}, // タイトルの文字色を変えたり、書体を変えたりできるようにするために、Text(text = "My TODO" ) としている
            )
        }
    ) { paddingValues ->
        Column( // レイアウトを整える(中身の Composable を上から順番に並べる),
            modifier = Modifier
                // Text(text = "My TODO", fontSize = 32.sp) // sp: Scale-independent Pixels（スケールに依存しないピクセル）、ユーザの文字サイズ指定に基づいて大きさが変わる単位
                .padding(paddingValues)// TopAppBar のスペース確保のために必要である
                .padding(16.dp) // 画面全体を囲っている Column に対して余白をつけて、フォームに設定している上部の余白を削除する
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) { // Row()で横に並べる、追加ボタンを横に並べる, verticalAlignment = Alignment.CenterVerticallyでRowの中の要素を上下中央揃えにする,  Alignment.Top を設定すれば各要素が上揃えになり、Alignment.Bottom を設定すれば下揃えになる, ユーザの視点で考えると、フォームに入力すると視線が左から右に移動していくので、フォームの右にボタンがあると視線の流れから自然とボタンへ辿り着ける
                TextField(
                    // value = "",
                    value = todo.value,
                    // onValueChange = {},
                    onValueChange = { text ->
                        todo.value = text
                    }, // フォームに入力された内容を todo.value へ保存するようにする, フォームに入力された文言が todo.value に保存される → 保存された todo.value がフォームに表示される
                    // modifier = Modifier.padding(top = 24.dp, bottom = 16.dp) // 上に 24.dp, 下に 16.dp の余白をつける, dp: Density-independent Pixels（密度に依存しないピクセル）の略称で、1dp は中密度（160dpi）の画面における 1 ピクセルと同じ大きさを示します。そのため、どのような解像度の端末でも同じ大きさを表現できる
                    // modifier = Modifier.padding(bottom = 16.dp)
                    modifier = Modifier
                        .padding(end = 16.dp) // 面白い、(end = 16.dp)の部分のendで、アラビア語は左右反転になるため、left, rightじゃ対応できないので、日本語や英語のような左から右に読む言語においては、start は左側、end は右側の指定、アラビア語のような右から左に読む言語においては、start は右側、end は左側の指定になる
                        .weight(1f) // Row() の中に配置された要素の横幅比を設定する,
                    // ここでは入力フォームだけに weight() を設定したので、入力フォームの横幅は「追加」ボタンを除いた横幅いっぱいに広がる、逆に言えば、入力フォームは「追加」ボタンを除いた横幅以上には広がらなくなるので、「追加」ボタンを押し出さなくなる
                    // 文字数を増やしていったときに「追加」ボタンが見切れてしまう問題を修正
                    // 「追加」ボタンに Modifier.weight(0.5f) を指定すると、フォームと「追加」ボタンの横幅比が 2 : 1（1f : 0.5f）になる
                )
                Button(onClick = {
                    // todoList.add(todo.value)
                    todoList.add(Todo(text = todo.value, isCompleted = false))
                    todo.value = "" // 「追加」ボタンを押したときにフォームの中身がリセットされるようにする
                }) { // 「追加」ボタンを押したときにフォームの文言を保存する
                    Text(text = "追加")
                }
            }
            // todoList に入っている値を一覧で表示
            todoList.forEachIndexed { index, item -> // forEachIndexed(): リストを各要素ごとに処理を実行するとき、その要素がリストの何番目かを教えてくれる
                // Text(text = item)
                TodoItem( // TodoItem() に TODO を削除する関数を渡す
                    // text = item,
                    todo = item,
                    deleteTodo = { todoList.removeAt(index) },
                    completeTodo = { todoList[index] = item.copy(isCompleted = it) } // 、index 番目の TODO にデータを再度入れることで要素を更新
                )
            }
        }
    }
}

// 編集ボタンや完了ボタン、削除ボタンを追加
@Composable
// fun TodoItem(text: String) {
// fun TodoItem(text: String, deleteTodo: () -> Unit) {
fun TodoItem(
    todo: Todo,
    deleteTodo: () -> Unit,
    completeTodo: (Boolean) -> Unit // チェックボックスを判別
) { // 関数が引数を必要としない場合は、() の中身を空にしておく, Unit: 返り値がないときに返ってくる型, () -> Unit: 引数がなく返り値もない関数を示している
    // Todoが完了している場合に文字の色を薄くする
    val fontColor = // val fontColor では文字色を保持
        if (todo.isCompleted) {
            Color.Gray // 引数の todo のプロパティである isCompleted を見て true だったら Gray を,
        } else {
            Color.Black // false だったら Black を入れるようにする
        }
    // Todoが完了している場合に打ち消し線をつける
    val textDecoration =
        if (todo.isCompleted) {
            TextDecoration.LineThrough // 打ち消し線(文字の中央に線を引く)
        } else {
            null
        }
    // 削除ボタン
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            // text = text,
            text = todo.text,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f), // 削除ボタンを必ず右端に配置されるようにして、TODO が適切な位置で改行されるようにする, weight() を使って伸ばして削除ボタンを右端に固定する
            color = fontColor,
            textDecoration = textDecoration
        )
        // チェックボックス
        Checkbox(
            checked = todo.isCompleted, // 。チェックされているかどうかは checked に渡された値で判断して、
            onCheckedChange = completeTodo, // チェックボックスがタップされたら onCheckedChange に指定された処理を実行する, onCheckedChange は、(Boolean) -> Unit という型の関数を受け取れる、関数の引数に設定されている Boolean は、チェックボックスがタップされたあとの状態を返す、つまり、チェックボックスをタップしてチェックがついたら true、チェックが外れたら false が返ってくる
            modifier = Modifier.padding(start = 4.dp)
        )
        // IconButton(onClick = {}) {
        IconButton(onClick = { deleteTodo() }) { // deleteTodo()で削除ボタンにする
            Icon(imageVector = Icons.Default.Delete, contentDescription = "削除ボタン") // 、TalkBack でアプリの読み上げを実行したときに contentDescription に指定した言葉が読み上げられる(アクセシビリティ)
        }
    }
}

// @Preview というアノテーションを追加, アプリ(app)を起動しなくても、SplitやDesignで見れるようにする
@Preview(showBackground = true) // showBackground = true で背景つきのプレビューを表示する
@Composable
fun GreetingPreview() {
    ToDoAppTheme {
        // Greeting("Android")
        MyTodoAppContent(
            todo = remember { mutableStateOf("文字を入力中...") },
            // todoList = remember { mutableStateListOf("TODO 1", "TODO 2") }
            todoList = remember {
                mutableStateListOf(
                    Todo(text = "長い長い長い長い長い長い長い長い長い長い長い長い長い TODO", isCompleted = false),
                    Todo(text = "完了したTODO", isCompleted = true)
                )
            }
        )
    }
}