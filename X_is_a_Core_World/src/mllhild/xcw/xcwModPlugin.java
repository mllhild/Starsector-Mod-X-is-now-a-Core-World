package mllhild.xcw;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Logger;
import java.util.concurrent.ThreadLocalRandom;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

public class xcwModPlugin extends BaseModPlugin {
    private static final Logger log = Logger.getLogger(xcwModPlugin.class);

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        // Test that the .jar is loaded and working, using the most obnoxious way possible.
        //throw new RuntimeException("Template mod loaded!\nRemove this crash in TemplateModPlugin.");
    }

    @Override
    public void onNewGame() {
        super.onNewGame();
    }

    @Override
    public void onGameLoad(boolean newGame) {
    }

    @Override
    public void onNewGameAfterProcGen(){
//        log.info("onNewGameAfterProcGen");
//        new xcw_MoveInhabitedNonCoreSystems().Destribution_All_Random_To_Core();
    }

    @Override
    public void onNewGameAfterTimePass() {
//        log.info("onNewGameAfterTimePass");
//        new xcw_MoveInhabitedNonCoreSystems().Destribution_All_Random_To_Core();
//        String xcwRadio = LunaSettings.getString("xcw","xcwRadio");
//        if(xcwRadio == "All_Random_To_Core")
//            new xcw_MoveInhabitedNonCoreSystems().Destribution_All_Random_To_Core();
    }

    @Override
    public void onNewGameAfterEconomyLoad(){
        log.info("onNewGameAfterEconomyLoad");
        new xcw_MoveInhabitedNonCoreSystems().LunaStart();
        //new xcw_SectorDecay().AddLevelOfDecayToAllMarkets();
    }

}
