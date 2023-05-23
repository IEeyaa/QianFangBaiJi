package com.example.qianfangbaiji.MemoryPage;

class memory{
    private
        int date, time, q;
        private double EF;
    memory(){
        q = 0;
        date = 0;
        time = 1;
        EF = 0;
    }
    // 输入一个由用户自己确定的q，输出下一次复习需要的时间
    public int changeEF(int q)
    {
        if(q <= 3){
            this.date = 0;
            this.time = 1;
            this.EF = 2.5;
            return getdate();
        }
        double newEF;
        this.q = q;
        newEF = this.EF+(0.1-(5-q)*(0.08+(5-q)*0.02));
        if(newEF < 1.3) newEF = 1.3;
        else if(newEF > 2.5) newEF = 2.5;
        this.EF = newEF;
        return getdate();
    }
    private int getdate(){
        if(this.time == 1) this.date = 1;
        else if(this.time == 2) this.date = 2;
        else {
            this.date =(int)(this.date * this.EF);
        }
        this.time++;
        return this.time;
    }
}
