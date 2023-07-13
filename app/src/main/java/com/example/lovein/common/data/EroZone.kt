package com.example.lovein.common.data

import com.example.lovein.R

enum class EroZone(val gender: List<Gender>, val resourceId: Int, val actionList: List<Action>) {
    SCALP(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.scalp,
        listOf(Action.MASSAGE)
    ),
    EARS(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.ears,
        Action.values().toList()
    ),
    NAVEL(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.navel,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    LOWER_STOMACH(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.lower_stomach,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    SMALL_OF_THE_BACK(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.small_of_the_back,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    ARMPITS(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.armpits,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    INNER_ARMS(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.inner_arms,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    INNER_WRIST(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.inner_wrist,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    PALM_OF_HANDS_AND_FINGERTIPS(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.palm_of_hands_and_fingertips,
        listOf(Action.KISS, Action.SUCK, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    BEHIND_THE_KNEE(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.behind_the_knee,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    NIPPLES(
        listOf(Gender.MALE),
        R.string.nipples,
        Action.values().toList()
    ),
    LIPS(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.lips,
        listOf(Action.KISS, Action.SUCK, Action.MASSAGE, Action.BITE)
    ),
    NECK(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.neck,
        listOf(Action.KISS, Action.MASSAGE, Action.BITE)
    ),
    INNER_THIGH(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.inner_thigh,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK, Action.BITE)
    ),
    BOTTOM_OF_FEET_AND_TOES(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.bottom_of_feet_and_toes,
        Action.values().toList()
    ),
    ANUS(
        listOf(Gender.MALE, Gender.FEMALE),
        R.string.anus,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    AREOLA_AND_NIPPLES(
        listOf(Gender.FEMALE),
        R.string.areola_and_nipples,
        Action.values().toList()
    ),
    PUBIC_MOUND(
        listOf(Gender.FEMALE),
        R.string.pubic_mound,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    CLITORIS(
        listOf(Gender.FEMALE),
        R.string.clitoris,
        Action.values().toList()
    ),
    A_SPOT(
        listOf(Gender.FEMALE),
        R.string.a_spot,
        listOf(Action.MASSAGE)
    ),
    G_SPOT(
        listOf(Gender.FEMALE),
        R.string.g_spot,
        listOf(Action.MASSAGE)
    ),
    CERVIX(
        listOf(Gender.FEMALE),
        R.string.cervix,
        listOf(Action.MASSAGE)
    ),
    GLANS(
        listOf(Gender.MALE),
        R.string.glans,
        Action.values().toList()
    ),
    FRENULUM(
        listOf(Gender.MALE),
        R.string.frenulum,
        listOf(Action.KISS, Action.SUCK, Action.MASSAGE, Action.LICK)
    ),
    FORESKIN(
        listOf(Gender.MALE),
        R.string.foreskin,
        Action.values().toList()
    ),
    SCROTUM_AND_TESTICLES(
        listOf(Gender.MALE),
        R.string.scrotum_and_testicles,
        Action.values().toList()
    ),
    PERINEUM(
        listOf(Gender.MALE),
        R.string.perineum,
        listOf(Action.KISS, Action.MASSAGE, Action.LICK)
    ),
    PROSTATE(
        listOf(Gender.MALE),
        R.string.prostate,
        listOf(Action.MASSAGE)
    )
}
