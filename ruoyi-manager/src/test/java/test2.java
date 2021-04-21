import com.ruoyi.common.config.Global;

/**
 * Created by jnv on 2019/8/8.
 */
public class test2 {
    public static void main(String[] args) {
        String filename = "file.txt";// 文件名
        String[] strArray = filename.split("\\.");
        int suffixIndex = strArray.length -1;
        System.out.println("."+strArray[suffixIndex]);

    }
}
