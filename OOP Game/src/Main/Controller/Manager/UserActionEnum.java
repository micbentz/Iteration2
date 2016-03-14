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
    Shift,

    //entity actions [10, 18]
    Attack,
    Spell1,
    Spell2,
    Spell3,
    Attack1,
    Attack2,
    Attack3,
    Talk,
    Trade,

    //views [18,23]
    ViewUp,
    ViewDown,
    ViewUpRight,
    ViewUpLeft,
    ViewDownLeft,
    ViewDownRight,

    //skills [24,27]
    Skill1,
    Skill2,
    Skill3,
    Skill4;
}
