package me.t4b.tktllmetronome;

import android.content.Context;

/**
 * Class containing the tempo names, separated because maybe I could support other (or no) names in the future
 */
class tempoName {
    int[][] tempoNames;

    public tempoName() {
        tempoNames = new int[8][2];
        tempoNames[0] = new int[]{0, R.string.largo};
        tempoNames[1] = new int[]{60, R.string.larghetto};
        tempoNames[2] = new int[]{66, R.string.adagio};
        tempoNames[3] = new int[]{76, R.string.andante};
        tempoNames[4] = new int[]{108, R.string.moderato};
        tempoNames[5] = new int[]{120, R.string.allegro};
        tempoNames[6] = new int[]{168, R.string.presto};
        tempoNames[7] = new int[]{200, R.string.prestissimo};
    }

    /**
     * Returns the matching tempo name
     *
     * @param c   Context
     * @param bpm beats per minute
     * @return tempo name, empty string when none matches (would be strange)
     */
    public String getName(Context c, int bpm) {
        for (int i = tempoNames.length - 1; i >= 0; i--) {
            if (bpm >= tempoNames[i][0]) {
                return (c.getString(tempoNames[i][1]));
            }
        }
        return ("");
    }
}
