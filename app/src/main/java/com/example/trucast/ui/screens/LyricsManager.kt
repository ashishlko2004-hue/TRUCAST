package com.example.trucast.ui.screens

data class LyricLine(
    val timeMs: Int,
    val text: String
)

object LyricsManager {
    fun getLyricsForSong(title: String): List<LyricLine> {
        return when {
            title.contains("Dulhe", true) -> listOf(
                LyricLine(0, "🎵 [Intro Music]"),
                LyricLine(4000, "Dulhe ki behen brigade saj ke aayi hai!"),
                LyricLine(10000, "Nachne ka mood ban gaya hai bhai!"),
                LyricLine(16000, "DJ wale baf pe dhol bajega jor se..."),
                LyricLine(23000, "Shaadi ki khushiyan chaayi charo or se!"),
                LyricLine(30000, "Bhaang bhangra aur thumke lagao re..."),
                LyricLine(37000, "Aaj mere yaar ki shaadi hai nacho gaao re!"),
                LyricLine(45000, "🎵 [Beat Drop & Dance Interlude]"),
                LyricLine(55000, "Phoolon ki barsaat ho, shehnai ki goonj ho..."),
                LyricLine(62000, "Sabke labon par khushi ki dhoond ho!"),
                LyricLine(70000, "🎵 [Outro]")
            )
            title.contains("Sanson", true) -> listOf(
                LyricLine(0, "🎵 [Instrumental Alap]"),
                LyricLine(5000, "Sanson ki mala pe simroon main pi ka naam..."),
                LyricLine(12000, "Sanson ki mala pe simroon main pi ka naam!"),
                LyricLine(19000, "Duniya se main kya janu, mera toh bas yehi kaam..."),
                LyricLine(28000, "Sanson ki mala pe simroon main pi ka naam!"),
                LyricLine(36000, "🎵 [Flute Interlude]"),
                LyricLine(44000, "Apne hi rang me rang loon mujhko..."),
                LyricLine(52000, "Balam pardesiya, man ki sudh budh khoyi..."),
                LyricLine(60000, "Sanson ki mala pe simroon main pi ka naam!"),
                LyricLine(70000, "🎵 [Ending Melodies]")
            )
            title.contains("Sajanva", true) -> listOf(
                LyricLine(0, "🎵 [Intro Beats]"),
                LyricLine(4000, "Sajanva re... tumse milna..."),
                LyricLine(10000, "Dil ka ye haal kehna hai..."),
                LyricLine(16000, "Raaton me jagna aur tera intezaar karna..."),
                LyricLine(23000, "Ab toh yahi mera jeene ka dhang hai!"),
                LyricLine(30000, "🎵 [Heavy Bass Drop]"),
                LyricLine(38000, "Sajanva re... paas aaja..."),
                LyricLine(46000, "Dooriyan ab sahin jaye na..."),
                LyricLine(55000, "🎵 [Outro]")
            )
            else -> listOf(
                LyricLine(0, "🎵 [TRUCAST Intro]"),
                LyricLine(6000, "Streaming live on TRUCAST Music Hub..."),
                LyricLine(13000, "Feel the rhythm, feel the absolute beat..."),
                LyricLine(20000, "Music connects souls across the universe..."),
                LyricLine(28000, "🎵 [Instrumental Groove]"),
                LyricLine(36000, "Keep listening, keep vibing..."),
                LyricLine(45000, "🎵 [Fade Out]")
            )
        }
    }
}
