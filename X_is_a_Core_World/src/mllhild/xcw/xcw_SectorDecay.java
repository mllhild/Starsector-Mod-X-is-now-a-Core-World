package mllhild.xcw;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import org.apache.log4j.Logger;
import com.fs.starfarer.api.campaign.listeners.ListenerManagerAPI;

public class xcw_SectorDecay {
    private static final Logger log = Logger.getLogger(xcwModPlugin.class);

    boolean sectorWillDecay = false;
    int sectorDecaySpeed = 50;
    int sectorLastDecayDate = 500;
    float lv1timer = 1;
    float lv2timer = 2;
    float lv3timer = 3;

    public void AddLevelOfDecayToAllMarkets() {
        log.info("AddLevelOfDecayToAllMarkets Start");
        CheckDecayTimer();
        for (StarSystemAPI system : Global.getSector().getStarSystems()) {
            for (PlanetAPI planet : system.getPlanets()) {
                MarketAPI market = planet.getMarket();
                if (market == null)
                    continue;
                market.addCondition("xcw_sector_decay_lv1");
                market.removeCondition("xcw_sector_decay_lv1");
                market.addCondition("xcw_sector_decay_lv2");
                market.removeCondition("xcw_sector_decay_lv2");
                market.addCondition("xcw_sector_decay_lv3");
            }
        }
        log.info("AddLevelOfDecayToAllMarkets End");

    }


    // add this to a listener that gets called at the end of every month
    // add this to listener of when a market is founded.
    public void CheckDecayTimer(){
        // get date
        log.info("CheckDecayTimer Start");
        int day = Global.getSector().getClock().getDay();
        log.info("Day: " + day);

        // check if date is further than next level decay date
        // swap current level of decay to next
        log.info("CheckDecayTimer End");
    }

}
