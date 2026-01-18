package com.example.todo_app

import android.R.attr.name
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.ui.theme.ToDoAppTheme

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
    val todoList = remember { mutableStateListOf<String>() } //  mutableStateListOf(): List という型で State を作成する, <String>で文字列のみ受け入れる

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
                        todoList.add(todo.value)
                        todo.value = "" // 「追加」ボタンを押したときにフォームの中身がリセットされるようにする
                    }) { // 「追加」ボタンを押したときにフォームの文言を保存する
                        Text(text = "追加")
                    }
                }
                // todoList に入っている値を一覧で表示
                todoList.forEach { item ->
                    // Text(text = item)
                    TodoItem(text = item)
                }
            }
        }
    }

// 編集ボタンや完了ボタン、削除ボタンを追加
@Composable
fun TodoItem(text: String) {
    Text(text = text, modifier = Modifier.padding(vertical = 4.dp))
}

// @Preview というアノテーションを追加, アプリ(app)を起動しなくても、SplitやDesignで見れるようにする
@Preview(showBackground = true) // showBackground = true で背景つきのプレビューを表示する
@Composable
fun GreetingPreview() {
    ToDoAppTheme {
        // Greeting("Android")
        MyTodoApp()
    }
}