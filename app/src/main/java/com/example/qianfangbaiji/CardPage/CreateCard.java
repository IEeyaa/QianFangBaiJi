package com.example.qianfangbaiji.CardPage;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

//这一段程序用于：创建一张卡片
public class CreateCard{
    private
    //opcode是操作模式，1插入，2删除，3修改
    int number, opcode;
    String title, hint1, hint2, hint3, hint4;
    SQLiteDatabase db1;
    //构造函数，传入参数
    public
    CreateCard(int Number, int Opcode, String Title, String Hint1, String Hint2, String Hint3, String Hint4, SQLiteDatabase db1){
        number = Number;
        opcode = Opcode;
        title = Title;
        hint1 = Hint1;
        hint2 = Hint2;
        hint3 = Hint3;
        hint4 = Hint4;
        this.db1 = db1;
        if(Hint1.equals("")) hint1 = "无";
        if(Hint2.equals("")) hint2 = "无";
        if(Hint3.equals("")) hint3 = "无";
        if(Hint4.equals("")) hint4 = "无";
    }
    CreateCard(int Number, SQLiteDatabase db1){
        number = Number;
        opcode = 1;
        this.db1 = db1;
    }
    //进行一些数据库的操作;0代表新建一个card，这里的问题是这个card要不要让用户一次性填完，1代表删除一个card, 2代表修改一个card;
    void execute(){
        String sql = "";
        if(opcode == 0) {
            sql = "insert into card values('" + title + "', " + number + " , '" + hint1 + "', '" + hint2 + "', '" + hint3 + "', '" + hint4 + "')";
        }
        else if(opcode == 1){
            sql = "delete from card where number == "+ number;
        }
        else if(opcode == 2){
            sql = "update card set title = '" + title + "', hint1 = '"+hint1+ "', hint2 = '"+hint2+ "', hint3 = '"+hint3+ "', hint4 = '"+hint4+ "' where number == "+ number;
        }
        db1.execSQL(sql);
    }
}