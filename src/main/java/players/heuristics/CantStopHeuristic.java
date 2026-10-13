package players.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;
import games.cantstop.CantStopGameState;
import utilities.Utils;

public class CantStopHeuristic implements IStateHeuristic {

    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        // 1. Cast the state to access Can't Stop specific methods
        CantStopGameState csgs = (CantStopGameState) gs;

        // 2. Evaluate definitive terminal states first
        CoreConstants.GameResult playerResult = csgs.getPlayerResults()[playerId];
        if (playerResult == CoreConstants.GameResult.LOSE_GAME) return -1;
        if (playerResult == CoreConstants.GameResult.WIN_GAME) return 10;

        double score = 0.0;

        // 3. Heavily weight overall game score (secured tracks)
        score += csgs.getGameScore(playerId) * 100.0;

        // 4. Iterate through all possible dice columns (2 through 12)
        for (int col = 2; col <= 12; col++) {

            // Add points for permanent, banked markers
            int permanentPos = csgs.getMarkerPosition(col, playerId);
            score += permanentPos * 10.0;

            // Add points for unbanked progress in the current turn
            int temporaryPos = csgs.getTemporaryMarkerPosition(col);
            score += temporaryPos * 5.0;

            // Bonus for columns that are completely locked down
            if (csgs.isTrackComplete(col)) {
                score += 50.0;
            }
        }

        return score;
    }
}