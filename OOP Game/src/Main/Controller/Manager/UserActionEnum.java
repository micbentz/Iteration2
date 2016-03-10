package Main.Controller.Manager;

/**
 * Created by mason on 3/9/16.
 */
public enum UserActionEnum {
    //entity directions [0, 7]
    Up,
    Down,
    Left,
    Right,
    UpRight,
    UpLeft,
    DownRight,
    DownLeft,

    //menu actions [8,9]
    Pause,
    Select,

    //entity actions [10, 17]
    Attack,
    Spell1,
    Spell2,
    Spell3,
    Attack1,
    Attack2,
    Attack3,
    Talk;
}
