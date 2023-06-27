package com.example.lovein.common.data

enum class EroZone(val gender: List<Gender>, val label: String, val actionList: List<Action>) {
    SCALP(
        listOf(Gender.MALE, Gender.FEMALE),
        "Scalp",
        listOf(Action.MASSAGE)
    ),
    EARS(
        listOf(Gender.MALE, Gender.FEMALE),
        "Ears",
        Action.values().toList()
    ),
    NAVEL(
        listOf(Gender.MALE, Gender.FEMALE),
        "Navel",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    LOWER_STOMACH(
        listOf(Gender.MALE, Gender.FEMALE),
        "Lower stomach",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    SMALL_OF_THE_BACK(
        listOf(Gender.MALE, Gender.FEMALE),
        "Small of the back",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    ARMPITS(
        listOf(Gender.MALE, Gender.FEMALE),
        "Armpits",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    INNER_ARMS(
        listOf(Gender.MALE, Gender.FEMALE),
        "Inner arms",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    INNER_WRIST(
        listOf(Gender.MALE, Gender.FEMALE),
        "Inner wrist",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    PALM_OF_HANDS_AND_FINGERTIPS(
        listOf(Gender.MALE, Gender.FEMALE),
        "Palm of hands and fingertips",
        listOf(Action.KISS, Action.SUCK, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    BEHIND_THE_KNEE(
        listOf(Gender.MALE, Gender.FEMALE),
        "Behind the knee",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    NIPPLES(
        listOf(Gender.MALE),
        "Nipples",
        Action.values().toList()
    ),
    LIPS(
        listOf(Gender.MALE, Gender.FEMALE),
        "Lips",
        listOf(Action.KISS, Action.SUCK, Action.MASSAGE, Action.BITE)
    ),
    NECK(
        listOf(Gender.MALE, Gender.FEMALE),
        "Neck",
        listOf(Action.KISS, Action.MASSAGE, Action.BITE)
    ),
    INNER_THIGH(
        listOf(Gender.MALE, Gender.FEMALE),
        "Inner thigh",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    BOTTOM_OF_FEET_AND_TOES(
        listOf(Gender.MALE, Gender.FEMALE),
        "Bottom of feet and toes",
        Action.values().toList()
    ),
    AREOLA_AND_NIPPLES(
        listOf(Gender.FEMALE),
        "Areola and nipples",
        Action.values().toList()
    ),
    PUBLIC_MOUND(
        listOf(Gender.FEMALE),
        "Pubic mound",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    CLITORIS(
        listOf(Gender.FEMALE),
        "Clitoris",
        Action.values().toList()
    ),
    A_SPOT(
        listOf(Gender.FEMALE),
        "A-spot",
        listOf(Action.MASSAGE)
    ),
    G_SPOT(
        listOf(Gender.FEMALE),
        "G-spot",
        listOf(Action.MASSAGE)
    ),
    CERVIX(
        listOf(Gender.FEMALE),
        "Cervix",
        listOf(Action.MASSAGE)
    ),
    GLANS(
        listOf(Gender.MALE),
        "Glans",
        Action.values().toList()
    ),
    FRENULUM(
        listOf(Gender.MALE),
        "Frenulum",
        listOf(Action.KISS, Action.SUCK, Action.MASSAGE, Action.LICK)
    ),
    FORESKIN(
        listOf(Gender.MALE),
        "Foreskin",
        Action.values().toList()
    ),
    SCROTUM_AND_TESTICLES(
        listOf(Gender.MALE),
        "Scrotum and testicles",
        Action.values().toList()
    ),
    PERINEUM(
        listOf(Gender.MALE),
        "Perineum",
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    PROSTATE(
        listOf(Gender.MALE),
        "Prostate",
        listOf(Action.MASSAGE)
    )
}
