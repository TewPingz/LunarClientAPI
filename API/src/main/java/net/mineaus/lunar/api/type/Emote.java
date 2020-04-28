package net.mineaus.lunar.api.type;

import lombok.Getter;

@Getter
public enum Emote {

    WAVE(0),
    HANDS_UP(1),
    FLOSS(2),
    DAB(3),
    T_POSE(4),
    SHRUG(5),
    FACEPALM(6),
    FRESH(7),
    HYPE(8),
    SQUAT_KICK(9),
    L_DANCE(10),
    TIDY(11),
    FREE_FLOW(12),
    SHIMMER(13),
    GET_FUNKY(14),
    GUN_LEAN(15),
    GANGAM_STYLE(16),
    SALUTE(17),
    BITCHSLAP(18),
    BONGO_CAT(19),
    BREATHTAKING(20),
    DISGUSTED(21),
    EXHAUSTED(22),
    PUNCH(23),
    SNEEZE(24),
    THREATENING(25),
    WOAH(26),
    BONELESS(27),
    BEST_MATES(28),
    DEFAULT(29),
    DISCO_FEVER(30),
    ELECTRO_SHUFFLE(31),
    FLOSS_2(32),
    INFINITE_DAB(33),
    ORANGE_JUSITCE(34),
    SKIBIDI(35),
    BOY(36),
    BOW(37),
    CALCULATED(38),
    CHICKEN(39),
    CLAPPING(40),
    CLUB(41),
    CONFUSED(42),
    CRYING(43),
    DAB_2(44),
    FACEPALM_2(45),
    FIST(46),
    LAUGHING(47),
    NO(48),
    POINTING(49),
    POPCORN(50),
    PURE_SALT(51),
    SHRUG_2(52),
    T_POSE_2(53),
    THINKING(54),
    TWERK(55),
    WAVE_2(56),
    YES(57);

    private int emoteId;

    Emote(int emoteId) {
        this.emoteId = emoteId;
    }

    public static Emote getById(int emoteId) {
        for (Emote emote : values()) {
            if (emote.getEmoteId() == emoteId) {
                return emote;
            }
        }
        return null;
    }

    public static Emote getByName(String input) {
        for (Emote emote : values()) {
            if (emote.name().equalsIgnoreCase(input)) {
                return emote;
            }
        }
        return null;
    }

}
