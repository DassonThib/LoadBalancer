package Thread;

/**
 * Created by Thibault on 31/05/2016.
 */
public class Job {

    public void execute(){
        System.out.println(Thread.currentThread().getName()+ " thread is waiting");
    }
}
