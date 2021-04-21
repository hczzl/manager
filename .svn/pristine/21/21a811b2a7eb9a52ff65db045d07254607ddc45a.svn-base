import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jnv on 2019/8/6.
 */
public class test {
    public static void main(String[] args) {
       /* long oneDay = 1;//时间间隔

        long initDelay = DateUtils.getTimeMillis("15:27:00") - System.currentTimeMillis();//
        initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;//初始化延时数
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("run "+ System.currentTimeMillis());
            }
        }, 0, oneDay, TimeUnit.MILLISECONDS);*/
        /*getBetweenDate("2019-05-26","2019-06-01");
        System.out.print(getBetweenDate("2019-05-26","2019-06-01"));*/






}


    public static List<String> getBetweenDate(String startTime, String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime()<=endDate.getTime()){
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.DATE, 1);
                // 获取增加后的日期
                startDate=calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getFirstDoubleRepetitiveItem(int[] items) {
        int b=0;
        for(int i=0,length=items.length;i<length;i++){
            int a=0;
            for(int p=0;p<length;p++){
                if(items[i]==items[p]){
                    a++;
                }
            }
            if(a==2){
                b= items[i];
                break;
            }
        }
        return b;
    }
}
