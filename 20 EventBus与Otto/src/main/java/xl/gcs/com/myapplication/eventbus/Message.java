package xl.gcs.com.myapplication.eventbus;

/**
 * Created by xianglei on 2018/5/20.
 */

public class Message {
    private String name;
    private String sex;
    private int age;

    public Message(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
