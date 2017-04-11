import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
public class Launcher
{
    public static void main ()
    {
        BaseGame myProgram = new BaseGame();
        LwjglApplication launcher = new LwjglApplication(myProgram);
    }
}