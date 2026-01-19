package com.example.todo_app

// TODO のデータを持つクラスを定義する
data class Todo(
    val text: String, // TODO の内容を示す文字列
    val isCompleted: Boolean // TODO が完了しているかどうかを示す真偽値
)
