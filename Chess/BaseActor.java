import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Intersector.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Extends functionality of the LibGDX <code>Actor</code> class by adding support for textures/animation, 
 * collision polygons, movement, world boundaries, and camera scrolling. 
 * Most game objects should extend this class.
 * Instances of new objects automatically stored in lists, accessible by class name from <code>getList(className)</code> method.
 * @see #Actor
 * @author Lee Stemkoski
 */
public class BaseActor extends Group
{
    /**
     *  Animation-related data.
     */
    protected Animation<TextureRegion> animation;
    protected float elapsedTime;
    private boolean animationPause;
    
    /** 
     *  Collision polygon data.
     *  Initialized by setSize() or setCollisionPolygon().
     *  Used by overlaps() and preventOverlap().
     */
    private Polygon boundingPolygon;
    
    /**
     *  Stores ArrayLists of instances of BaseActor extensions.
     */
    private static HashMap<String, ArrayList> listMap;
    
    /**
     * Stores size of game world. 
     * Initialized by
     * Used for boundToWorld(), scrollTo(), and wrap().
     */
    private static Rectangle worldBounds;
    
    // values used for motion/physics
    
    /**
     * Amount to accelerate (pixels/second)/second.
     */
    public float acceleration;
    
    /**
     * Maximum movement speed (pixels/second).
     */
    public float maxSpeed;
    
    /**
     * Amount to decelerate (pixels/second)/second. Only applies when actor is not accelerating.
     */
    public float deceleration;
    
    // used in applyPhysics method
    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    
    /**
     *  Initializes position and stage of object, and adds instance to corresponding list.
     *  @param startX Initial X coordinate of actor.
     *  @param startY Initial Y coordinate of actor.
     *  @param theStage Stage to which this actor should be added. 
     *  If set to null, object will be destroyed when lists are updated, unless added to a stage/table later.
     */ 
    public BaseActor(float startX, float startY, Stage theStage)
    {
        // call Actor constructor
        super();
        
        setPosition(startX, startY);
        
        // Stage could be null to (1) initialize a list, or (2) for UI elements placed by Table
        if(theStage != null)
        {
            theStage.addActor(this);
        }
        
        if(listMap == null)
        {
            listMap = new HashMap<String, ArrayList>();
        }
        
        String className = this.getClass().getName();
        
        if(!listMap.containsKey(className))
        {
            listMap.put(className, new ArrayList<BaseActor>());
        }
        
        getList(className).add(this);
        
        animation = null;
        elapsedTime = 0;
        animationPause = false;
        
        velocityVec = new Vector2(0,0);
        accelerationVec = new Vector2(0,0);
    }
    
    // ----------------------------------------------
    // Instance list methods
    // ----------------------------------------------
    
    /**
     *  Retrieves a list of all instances of the object with the given class name.
     *  If no instances exist, returns an empty class.
     *  Useful when coding interactions between different types of game objects in update method.
     *  @param className name of a class that extends the BaseActor class
     *  @return list of instances of the object with the given class name 
     */
    public static ArrayList<BaseActor> getList(String className)
    {
        // in case the list does not exist...
        // avoid Null Pointer Exception errors...
        if(!listMap.containsKey(className))
        {
            return new ArrayList<BaseActor>();
        }
        else
        {
            return listMap.get(className);
        }
    }
    
    /**
     *  Keeps the Actor references in Stage and BaseActor lists in sync:
     *  if an Actor has been removed from its Stage, 
     *  it will be removed from the corresponding BaseActor list as well.
     */
    public static void updateAllLists()
    {
        for(String s : listMap.keySet())
        {
            updateList(s);
        }
    }
    
    /**
     *  Internal method for updating individual BaseActor lists.
     *  @param className name of a class that extends the BaseActor class
     *  @see #updateAllLists
     */
    private static void updateList(String className)
    {
        ArrayList<BaseActor> list = getList(className);
        
        for(int n = list.size() - 1; n >= 0; n--)
        {
            BaseActor ba = list.get(n); 
            
            if(ba.getStage() == null)
            {
                list.remove(ba);
            }
        }
    }

    /**
     *  Returns number of instances of a given class (that extends BaseActor).
     *  @param className name of a class that extends the BaseActor class
     *  @return number of instances of the class
     */
    public static int count(String className)
    {
        return getList(className).size();
    }
    
    /**  
     *  This method removes all lists.
     *  It is useful when re-loading a screen, 
     *  to avoid storing references to previously created objects.
     */
    public static void clearAllLists()
    {
        if(listMap != null)
        {
            listMap.clear();
        }
    }
    
    // ----------------------------------------------
    // Texture / Animation methods
    // ----------------------------------------------
    
    /**
     * Sets the animation used when rendering this actor; 
     * also sets actor size (if size is currently not set).
     * @param anim animation that will be drawn when actor is rendered
     */
    public void setAnimation(Animation anim)
    {
        animation = anim;
        
        if(getWidth() == 0 || getHeight() == 0)
        {
            Texture t = animation.getKeyFrame(0).getTexture();
            setSize(t.getWidth(), t.getHeight());
        }
    }
    
    /**
     * Creates an animation from images stored in separate files.
     * @param frameDuration how long each frame should be displayed
     * @param loop should the animation loop
     * @param fileNames names of files containing animation images
     * @return animation created (useful for storing multiple animations)
     */
    public Animation loadAnimationFromFiles(float frameDuration, boolean loop, String... fileNames)
    { 
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        for(int n = 0; n < fileCount; n++)
        {   
            String fileName = fileNames[n];
            Texture texture = new Texture( Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }
        
        Animation anim = new Animation(frameDuration, textureArray);
        
        if(loop)
        {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        if(animation == null)
        {
            setAnimation(anim);
        }
            
        return anim;
    }
    
    /**
     * Creates an animation from a spritesheet: a rectangular grid of images stored in a single file.
     * @param frameDuration how long each frame should be displayed
     * @param loop should the animation loop
     * @param fileName name of file containing spritesheet
     * @param rows number of rows of images in spritesheet
     * @param cols number of columns of images in spritesheet
     * @return animation created (useful for storing multiple animations)
     */
    public Animation loadAnimationFromSheet(float frameDuration, boolean loop, String fileName, int rows, int cols)
    { 
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                textureArray.add(temp[r][c]);
            }
        }
        
        Animation anim = new Animation(frameDuration, textureArray);
        
        if(loop)
        {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        if(animation == null)
        {
            setAnimation(anim);
        }
        return anim;
    }
    
    /**
     *  Convenience method for creating a 1-frame animation from a single texture.
     *  @param fileName names of image file
     *  @return animation created (useful for storing multiple animations)
     */
    public Animation loadTexture(String fileName)
    {
        return loadAnimationFromFiles(1, false, fileName);
    }
    
    /**
     *  Set the pause state of the animation.
     *  @param pause true to pause animation, false to resume animation
     */
    public void setAnimationPause(boolean pause)
    {
        animationPause = pause;
    }
    
    // ----------------------------------------------
    // Collision polygon methods
    // ----------------------------------------------
    
    /**
     *  Set size of Actor; values also used to set origin to center 
     *  and initialize bounding polygon to rectangle. <br>
     *  Automatically called when initial animation is set.
     *  @param w width 
     *  @param h height
     */
    public void setSize(float w, float h)
    {
        setWidth(w);
        setHeight(h);
        setOrigin(w/2, h/2);
        
        // initialize Rectangular bounding polygon by default
        float[] vertices = {0,0, w,0, w,h, 0,h};
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(w/2, h/2);
    }
    
    /**
     *  Replace default (rectangle) collision polygon with an n-sided polygon. <br>
     *  Vertices of polygon lie on the ellipse contained within bounding rectangle.
     *  Note: one vertex will be located at point (0,width);
     *  a 4-sided polygon will appear in the orientation of a diamond.
     *  @param numSides number of sides of the collision polygon
     */
    // Recommended for n >= 6.
    //   Note: n = 4 will create a diamond, not a rectangle.
    public void setCollisionPolygon(int numSides)
    {
        float w = getWidth();
        float h = getHeight();
        
        if(w == 0 || h == 0)
        {
            System.err.println("Warning: size of BaseActor not set; cannot initialize polygon" );
            return;
        }
        
        float[] vertices = new float[2*numSides];
        for(int i = 0; i < numSides; i++)
        {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
            // y-coordinate
            vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
        }
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(getOriginX(), getOriginY());
    }
    
    
    /**
     *  Returns bounding polygon for this BaseActor, adjusted by Actor's current position and rotation.
     *  @return bounding polygon for this BaseActor
     */
    public Polygon getBoundingPolygon()
    {
        boundingPolygon.setPosition(getX(), getY());
        boundingPolygon.setRotation(getRotation());
        return boundingPolygon;
    }
    
    /**
     *  Determine if this BaseActor overlaps other BaseActor (according to collision polygons).
     *  @param other BaseActor to check for overlap
     *  @return true if collision polygons of this and other BaseActor overlap
     *  @see #setSize
     *  @see #setCollisionPolygon
     */
    public boolean overlaps(BaseActor other)
    {
        return Intersector.overlapConvexPolygons(this.getBoundingPolygon(), other.getBoundingPolygon());
    }
    
    /**
     *  Implement a "solid"-like behavior:
     *  when there is overlap, move this BaseActor away from other BaseActor
     *  along minimum translation vector until there is no overlap.
     *  @param other BaseActor to check for overlap
     */
    public void preventOverlap(BaseActor other)
    {
        Polygon poly1 = this.getBoundingPolygon();
        Polygon poly2 = other.getBoundingPolygon();
        
        // initial test to improve performance
        if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
        {
            return;
        }
        
        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        if(polygonOverlap)
        {
            this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        }
    }
    
    // ----------------------------------------------
    // Boundary and camera scrolling methods
    // ----------------------------------------------
    
    /**
     *  Set world dimensions for use by methods:
     *  boundToWorld(), scrollTo(), wrap(), isOutsideWorld(), alignToWorldCenter()  methods.
     *  @param width width of world
     *  @param height height of world
     */
    public static void setWorldBounds(float width, float height)
    {
        worldBounds = new Rectangle(0,0, width, height);
    }
    
    /**
     *  Set world dimensions for use by methods:
     *  boundToWorld(), scrollTo(), wrap(), isOutsideWorld(), alignToWorldCenter()  methods.
     *  @param BaseActor whose size determines the world bounds (typically a background image)
     */
    public static void setWorldBounds(BaseActor ba)
    {
        if(ba.getWidth() == 0 || ba.getHeight() == 0)
        {
            System.err.println("Warning: no image set; cannot determine world bounds.");
        }
        else
        {
            setWorldBounds(ba.getWidth(), ba.getHeight());
        }
    }   
    
    /**
     * If an edge of an object moves past the world bounds, 
     *   adjust its position to keep it completely on screen.
     */
    public void boundToWorld()
    {
        if(worldBounds == null)
        {
            System.err.println("Warning: no world bounds set.");
            return;
        }
        
        if(getX() < 0)
        {
            setX(0);
        }
        if(getX() + getWidth() > worldBounds.width)  
        {
            setX(worldBounds.width - getWidth());
        }
        if(getY() < 0)
        {
            setY(0);
        }
        if(getY() + getHeight() > worldBounds.height)
        {
            setY(worldBounds.height - getHeight());
        }
    }

    /**
     *  Center camera on this object, while keeping camera's range of view 
     *  (determined by screen size) completely within world bounds.
     */
    public void scrollTo()
    {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();
        
        // center camera on actor
        cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);
        
        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, v.getScreenWidth()/2,  worldBounds.width -  v.getScreenWidth()/2);
        cam.position.y = MathUtils.clamp(cam.position.y, v.getScreenHeight()/2, worldBounds.height - v.getScreenHeight()/2);
        cam.update();
    }
    
    /** 
     *  If this object moves completely past the world bounds,
     *  adjust its position to the opposite side of the world.
     */
    public void wrap()
    {
        if(getX() + getWidth() < 0)
        {
            setX(worldBounds.width);
        }
        if(getX() > worldBounds.width)
        {
            setX(-getWidth());
        }
        if(getY() + getHeight() < 0)
        {
            setY(worldBounds.height);
        }
        if(getY() > worldBounds.height)
        {
            setY(-getHeight());
        }
    }
    
    /**
     *  Determine if this object is completely outside the world boundaries.
     *  Useful for identifying objects that can be removed from the game, to improve performance.
     *  @return true if bounding rectangle is completely outside world bounds
     */
    public boolean isOutsideWorld()
    {
        return (getX() + getWidth() < 0) || (getX() > worldBounds.width) 
            || (getY() + getHeight() < 0) || (getY() > worldBounds.height);
    }
    
    /**
     *  Center BaseActor within world boundaries.
     */
    public void alignToWorldCenter()
    {
        this.setX(worldBounds.width/2 - this.getWidth()/2);
        this.setY(worldBounds.height/2 - this.getHeight()/2);
    }
    
    /** 
     *  Repositions this BaseActor so its center is aligned
     *  with center of other BaseActor. Useful when one BaseActor spawns another.
     *  @param other BaseActor to align this BaseActor with
     */
    public void alignToActorCenter(BaseActor other)
    {
        this.setX(other.getX() + other.getWidth()/2 - this.getWidth()/2);
        this.setY(other.getY() + other.getHeight()/2 - this.getHeight()/2);
    }
    
    // ----------------------------------------------
    // motion/physics methods
    // ----------------------------------------------
    
    /**
     *  Calculates the speed of movement (in pixels/second).
     *  @return speed of movement (pixels/second)
     */
    public float getSpeed()
    {
        return velocityVec.len();
    }
    
    /**
     *  Calculates the angle of motion (in degrees), calculated from the velocity vector.
     *  <br>
     *  To align actor image angle with motion angle, use <code>setRotation( getMotionAngle() )</code>.
     *  @return angle of motion (degrees)
     */
    public float getMotionAngle()
    {
        return MathUtils.atan2(velocityVec.y, velocityVec.x) * MathUtils.radiansToDegrees;
    }
    
    /**
     *  Update accelerate vector by angle and value stored in acceleration field.
     *  Acceleration is applied by <code>applyPhysics</code> method.
     *  @param angle Angle (degrees) in which to accelerate.
     *  @see #acceleration
     *  @see #applyPhysics
     */
    public void accelerate(float angle)
    {
        accelerationVec.add(
            acceleration * MathUtils.cosDeg(angle), 
            acceleration * MathUtils.sinDeg(angle));
    }
    
    /**
     *  Update accelerate vector by current rotation angle and value stored in acceleration field.
     *  Acceleration is applied by <code>applyPhysics</code> method.
     *  @see #acceleration
     *  @see #applyPhysics
     */
    public void accelerate()
    {
        accelerate(getRotation());
    }
    
    /**
     *  Calculate new velocity vector based on acceleration vector, then adjust position based on velocity vector. <br>
     *  If not accelerating, deceleration value is applied. <br>
     *  Speed is limited by maxSpeed value. <br>
     *  Acceleration vector reset to (0,0) at end of method. <br>
     *  @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method.
     *  @see #acceleration
     *  @see #deceleration
     *  @see #maxSpeed
     */
    public void applyPhysics(float dt)
    {
        // apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);
        
        float speed = velocityVec.len();
        
        // decrease velocity (decelerate) when not accelerating
        if(accelerationVec.len() == 0)
        {
            speed -= deceleration * dt;
        }
        
        // keep velocity in bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);
        
        // update velocity
        velocityVec.setLength(speed);
        
        // apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt);
        
        // reset acceleration
        accelerationVec.set(0,0);
    }
    
    // ----------------------------------------------
    // Actor methods: act and draw
    // ----------------------------------------------
    
    /**
     *  Processes all Actions and related code for this object; 
     *  automatically called by act method in Stage class.
     *  @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    public void act(float dt)
    {
        super.act(dt);
        
        if(!animationPause)
        {
            elapsedTime += dt;
        }
    }
    
    /**
     *  Draws current frame of animation; automatically called by draw method in Stage class. <br>
     *  If color has been set, image will be tinted by that color. <br>
     *  If no animation has been set or object is invisible, nothing will be drawn.
     *  @param batch (supplied by Stage draw method)
     *  @param parentAlpha (supplied by Stage draw method)
     *  @see #setColor
     *  @see #setVisible
     *  
     */
    public void draw(Batch batch, float parentAlpha) 
    {
        super.draw(batch, parentAlpha);
        
        Color c = getColor(); // used to apply tint color effect
        
        batch.setColor(c.r, c.g, c.b, c.a);
        
        if (animation != null && isVisible())
        {
            batch.draw(animation.getKeyFrame(elapsedTime), 
                getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }
}