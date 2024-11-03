package mllhild.xcw;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.campaign.StarSystem;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import lunalib.lunaSettings.LunaSettings;

public class xcw_MoveInhabitedNonCoreSystems {
    private static final Logger log = Logger.getLogger(xcwModPlugin.class);

    public static void cleanup(final StarSystemAPI system) {
        final HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin)Misc.getHyperspaceTerrain().getPlugin();
        final NebulaEditor editor = new NebulaEditor((BaseTiledTerrain)plugin);
        final float minRadius = plugin.getTileSize() * 0.5f;
        final float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0f, radius + minRadius * 0.5f, 0.0f, 360.0f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0f, radius + minRadius, 0.0f, 360.0f, 0.25f);
    }

    public void LunaStart(){
        log.info("X_is_a_Core_World");
        int radiusMax = LunaSettings.getInt("xcw","radiusMax");
        log.info(radiusMax);
        int radiusMin = LunaSettings.getInt("xcw","radiusMin");
        log.info(radiusMin);
        String xcwRadio = LunaSettings.getString("xcw","xcwRadio");
        log.info(xcwRadio);

        switch (xcwRadio){
            case "Move_Inhabited_To_Core":
                log.info("Move_Inhabited_To_Core start");
                Destribution_Move_Inhabited_To_Core(radiusMin,radiusMax);
                log.info("Move_Inhabited_To_Core end");
                break;
            case "All_Random_To_Core":
                log.info("All_Random_To_Core start");
                Destribution_All_Random_To_Core(radiusMin,radiusMax);
                log.info("All_Random_To_Core end");
                break;
            case "All_Random":
                log.info("All_Random start");
                Destribution_All_Random();
                log.info("All_Random end");
                break;
            default:
                log.info("None");
                break;
        }
    }

    public void IniciateDecay(int decaySpeed){

    }

    public void Destribution_Move_Inhabited_To_Core(int radiusMin, int radiusMax){
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            boolean systemIsInhabitated = false;
            for(PlanetAPI planet: system.getPlanets()){
                MarketAPI market = planet.getMarket();
                if (market == null)
                    continue;
                int marketSize = market.getSize();
                if(marketSize > 1){
                    //log.info(marketSize);
                    //log.info(system.getBaseName());
                    int randomRange = ThreadLocalRandom.current().nextInt(radiusMin, radiusMax);
                    float angle = ThreadLocalRandom.current().nextFloat() * 360;
                    float x = (float)Math.cos(angle) * randomRange - 5000;
                    float y = (float)Math.sin(angle) * randomRange - 5000;
                    //log.info(system.getBaseName());
                    //log.info(system.getLocation());
                    system.getLocation().set(x,y);
                    cleanup(system);
                    //log.info(system.getLocation());
                    systemIsInhabitated = true;

                }
                if(systemIsInhabitated)
                    continue;
            }
        }
    }

    public void Destribution_All_Random_To_Core(int radiusMin, int radiusMax){
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            boolean systemIsInhabitated = false;
            //log.info("system: " + system.getBaseName());
            //log.info("system location old: " + system.getLocation());
            for(PlanetAPI planet: system.getPlanets()){
                //log.info("planet name: " + planet.getName());
                //log.info(planet.getFaction().toString());
                //log.info(planet.getFaction().getId());
                if(planet.getFaction() == null)
                    continue;
                if(planet.getFaction().getId().equalsIgnoreCase("neutral"))
                    continue;
                int randomRange = ThreadLocalRandom.current().nextInt(radiusMin, radiusMax);
                float angle = ThreadLocalRandom.current().nextFloat() * 360;
                float x = (float)Math.cos(angle) * randomRange;
                float y = (float)Math.sin(angle) * randomRange;
                system.getLocation().set(x,y);
                //log.info("system location new: " + system.getLocation());
                systemIsInhabitated = true;
                cleanup(system);
            }
            if(systemIsInhabitated)
                continue;
        }
    }

    public void Destribution_All_Random(){
        List<StarSystemAPI> inhabitedSystems = new java.util.ArrayList<StarSystemAPI>();
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            //log.info(system.getBaseName());
            float x = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*80000;
            float y = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*40000;
            //log.info(system.getBaseName());
            //log.info(system.getLocation());
            system.getLocation().set(x,y);
            cleanup(system);
            //log.info(system.getLocation());
        }
    }

}
