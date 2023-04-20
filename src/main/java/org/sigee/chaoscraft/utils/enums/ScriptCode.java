package org.sigee.chaoscraft.utils.enums;

public enum ScriptCode {
    /* Console */
    ON_ENABLE,
    ON_DISABLE,
    NOT_CONSOLE_CMD,
    NOT_AVAILABLE_CMD,

    /* Admin */
    LESS_USER,
    IM_SETTER,
    IM_NOT_SETTER,

    /* Player */
    LANG_CHANGE,
    END_WORLD_SCAN_MSG,

    PLAYER_JOIN_MSG, //
    PLAYER_QUIT_MSG, //

    GAME_ALREADY_STARTED,
    PLAYER_QUIT_GAME,

    PLAYER_JOIN_PARTICIPANT,
    PLAYER_JOIN_SPECTATOR,
    PLAYER_JOIN_INACTIVE,
    PLAYER_WIN,
    PLAYER_DEFEAT,

    /* Mission */
    CLEARD,
    INTERACT_BLOCK_MISSION_TITLE,
    EAT_MISSION,

    /* Word */
    NAME,
    LANGUAGE,
    PLAYER_TYPE,
    LECTERN,
    APPLE,
    GOLDEN_APPLE,
    COOKED_PORKCHOP,

    /* END */
    EOF

}
