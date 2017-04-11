import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.Texture;

/**
 *  Created when program is launched; 
 *  manages the screens that appear during the game.
 *  Can store objects required by multiple screens.
 */
public class BaseGame extends Game
{
    /**
     *  Reference to game; used when calling setScreen method.
     */
    public static BaseGame game;
    
    /**
     *  Stores objects required by multiple screens, such as user interface styles
     *  (BitmapFont, LabelStyle, TextButtonStyle).
     */
    public static Skin skin;
    
    /**
     *  Called when game is initialized; 
     *  should initialize assets stored by Skin object 
     *  or required by multiple screens.
     */
    public void create() 
    {        
        game = this;
        skin = new Skin();
        
        /*
        // Label style
        
        BitmapFont uiFont = new BitmapFont(Gdx.files.internal("assets/spacey.fnt"));
        uiFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        LabelStyle uiLabelStyle = new LabelStyle(uiFont, Color.WHITE);
        
        skin.add("uiLabelStyle", uiLabelStyle);
        
        // TextButton style
        
        TextButtonStyle uiTextButtonStyle = new TextButtonStyle();
        
        Texture buttonTex = new Texture(Gdx.files.internal("assets/button.png"));
        skin.add("button", new NinePatch(buttonTex, 26,26,16,20));
        uiTextButtonStyle.up = skin.getDrawable("button");
        uiTextButtonStyle.font      = uiFont;
        uiTextButtonStyle.fontColor = new Color(0, 0.25f, 0, 1);
        
        skin.add("uiTextButtonStyle", uiTextButtonStyle);
            */
        
        BaseScreen s = new ChessScreen();
        setScreen(s);
    }
}